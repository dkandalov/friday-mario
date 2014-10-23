package audible;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;
import audible.idelisteners.*;
import audible.wav.Sounds;

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
                refactoringByProject.get(project).stop();
                refactoringByProject.remove(project);
                vcsActionsByProject.get(project).stop();
                vcsActionsByProject.remove(project);
                compilationByProject.get(project).stop();
                compilationByProject.remove(project);
                unitTestsByProject.get(project).stop();
                unitTestsByProject.remove(project);
            }
        };

        ProjectManager.getInstance().addProjectManagerListener(projectManagerListener);
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            projectManagerListener.projectOpened(project);
        }

        navigation = new Navigation(listener);
        navigation.start();
        editorModification = new EditorModification(listener);
        editorModification.start();
    }

    @Override
    public void disposeComponent() {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            projectManagerListener.projectClosed(project);
        }
        navigation.stop();
        editorModification.stop();
        listener.dispose();
    }

    @SuppressWarnings("ConstantConditions")
    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getCanonicalName();
    }
}
