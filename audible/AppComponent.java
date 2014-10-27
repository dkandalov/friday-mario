package audible;

import audible.idelisteners.*;
import audible.wav.Sounds;
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

public class AppComponent implements ApplicationComponent {
    private final Map<Project, Refactoring> refactoringByProject = new HashMap<Project, Refactoring>();
    private final Map<Project, VcsActions> vcsActionsByProject = new HashMap<Project, VcsActions>();
    private final Map<Project, Compilation> compilationByProject = new HashMap<Project, Compilation>();
    private final Map<Project, UnitTests> unitTestsByProject = new HashMap<Project, UnitTests>();

    private Navigation navigation;
    private EditorModification editorModification;
    private ProjectManagerListener projectManagerListener;

    private NotificationListener listener;
    private ApplicationAdapter applicationListener;

    @Override public void initComponent() {
        Sounds sounds = new Sounds();
        listener = new NotificationListener(sounds).init();

        projectManagerListener = new ProjectManagerAdapter() {
            @Override
            public void projectOpened(Project project) {
                Refactoring refactoring = new Refactoring(project, listener);
                refactoring.start();
                refactoringByProject.put(project, refactoring);

                VcsActions vcsActions = new VcsActions(project, listener);
                vcsActions.start();
                vcsActionsByProject.put(project, vcsActions);

                Compilation compilation = new Compilation(project, listener);
                compilation.start();
                compilationByProject.put(project, compilation);

                UnitTests unitTests = new UnitTests(project, listener);
                unitTests.start();
                unitTestsByProject.put(project, unitTests);
            }

            @Override
            public void projectClosed(Project project) {
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

        navigation = new Navigation(listener);
        navigation.start();
        editorModification = new EditorModification(listener);
        editorModification.start();

        applicationListener = new ApplicationAdapter() {
            @Override
            public void applicationExiting() {
                listener.stop();
            }
        };
        ApplicationManager.getApplication().addApplicationListener(applicationListener);
    }

    @Override
    public void disposeComponent() {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            projectManagerListener.projectClosed(project);
        }
        ApplicationManager.getApplication().removeApplicationListener(applicationListener);
        navigation.stop();
        editorModification.stop();
        listener.stop();
    }

    @SuppressWarnings("ConstantConditions")
    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getCanonicalName();
    }
}
