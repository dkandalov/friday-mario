package fridaymario;

import fridaymario.listeners.*;
import fridaymario.sounds.Sounds;
import com.intellij.openapi.application.ApplicationAdapter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IntelliJAppComponent implements ApplicationComponent {
	private final Map<Project, Refactoring> refactoringByProject = new HashMap<Project, Refactoring>();
	private final Map<Project, VcsActions> vcsActionsByProject = new HashMap<Project, VcsActions>();
	private final Map<Project, Compilation> compilationByProject = new HashMap<Project, Compilation>();
	private final Map<Project, UnitTests> unitTestsByProject = new HashMap<Project, UnitTests>();

	private SoundPlayer soundPlayer;
	private AllActions allActions;

	private ProjectManagerListener projectManagerListener;
	private ApplicationAdapter applicationListener;
	private boolean silentMode;
	private boolean logUnmappedActions;

	@Override public void initComponent() {
		soundPlayer = new SoundPlayer(createSounds(), logUnmappedActions).init();
		initApplicationListeners();
		initProjectListeners();
	}

	@Override public void disposeComponent() {
		disposeProjectListeners();
		disposeApplicationListeners();
		soundPlayer.stop();
	}

	private void initApplicationListeners() {
		allActions = new AllActions(soundPlayer);
		allActions.start();

		applicationListener = new ApplicationAdapter() {
			@Override
			public void applicationExiting() {
				soundPlayer.stop();
			}
		};
		ApplicationManager.getApplication().addApplicationListener(applicationListener);
	}

	private void disposeApplicationListeners() {
		ApplicationManager.getApplication().removeApplicationListener(applicationListener);
		allActions.stop();
	}

	private void initProjectListeners() {
		projectManagerListener = new ProjectManagerAdapter() {
			@Override public void projectOpened(Project project) {
				Refactoring refactoring = new Refactoring(project, soundPlayer);
				refactoring.start();
				refactoringByProject.put(project, refactoring);

				VcsActions vcsActions = new VcsActions(project, soundPlayer);
				vcsActions.start();
				vcsActionsByProject.put(project, vcsActions);

				Compilation compilation = new Compilation(project, soundPlayer);
				compilation.start();
				compilationByProject.put(project, compilation);

				UnitTests unitTests = new UnitTests(project, soundPlayer);
				unitTests.start();
				unitTestsByProject.put(project, unitTests);
			}

			@Override public void projectClosed(Project project) {
				if (refactoringByProject.containsKey(project)) {
					refactoringByProject.get(project).stop();
					refactoringByProject.remove(project);
				}
				if (vcsActionsByProject.containsKey(project)) {
					vcsActionsByProject.get(project).stop();
					vcsActionsByProject.remove(project);
				}
				if (compilationByProject.containsKey(project)) {
					compilationByProject.get(project).stop();
					compilationByProject.remove(project);
				}
				if (unitTestsByProject.containsKey(project)) {
					unitTestsByProject.get(project).stop();
					unitTestsByProject.remove(project);
				}
			}
		};

		for (Project project : ProjectManager.getInstance().getOpenProjects()) {
			projectManagerListener.projectOpened(project);
		}
		ProjectManager.getInstance().addProjectManagerListener(projectManagerListener);
	}

	private void disposeProjectListeners() {
		for (Project project : ProjectManager.getInstance().getOpenProjects()) {
			projectManagerListener.projectClosed(project);
		}
	}

	@SuppressWarnings("ConstantConditions")
	@NotNull @Override public String getComponentName() {
		return this.getClass().getCanonicalName();
	}

	public IntelliJAppComponent silentMode() {
		silentMode = true;
		return this;
	}

	public IntelliJAppComponent logUnmappedActionsMode() {
		logUnmappedActions = true;
		return this;
	}

	private Sounds createSounds() {
		return (silentMode ? Sounds.createSilent() : Sounds.create());
	}
}
