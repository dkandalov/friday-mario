package fridaymario;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.util.SystemInfo;
import fridaymario.listeners.*;
import fridaymario.sounds.SilentSound;
import fridaymario.sounds.Sounds;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.openapi.util.text.StringUtilRt.isEmptyOrSpaces;

public class IntelliJAppComponent {
	private final Map<Project, Refactoring> refactoringByProject = new HashMap<>();
	private final Map<Project, VcsActions> vcsActionsByProject = new HashMap<>();
	private final Map<Project, Compilation> compilationByProject = new HashMap<>();
	private final Map<Project, UnitTests> unitTestsByProject = new HashMap<>();

	private ActionListeningSoundPlayer soundPlayer;
	private AllActions allActions;

	private ProjectManagerListener projectManagerListener;
	private boolean silentMode;
	private boolean logUnmappedActions;

	public IntelliJAppComponent() {
		Application application = ApplicationManager.getApplication();
		application.getMessageBus().connect().subscribe(AppLifecycleListener.TOPIC, new AppLifecycleListener() {
			@Override public void appFrameCreated(@NotNull List<String> commandLineArgs) {
				if (!Settings.getInstance().isPluginEnabled()) return;
				init();
			}

			@Override public void appWillBeClosed(boolean isRestart) {
				if (!Settings.getInstance().isPluginEnabled()) return;
				soundPlayer.stopAndPlayGameOver();
				dispose(true);
			}
		});
	}

	private void init() {
		soundPlayer = new ActionListeningSoundPlayer(createSounds(), createLoggingListener()).init();
		initApplicationListeners();
		initProjectListeners();

		// see https://github.com/dkandalov/friday-mario/issues/3#issuecomment-160421286
		// and http://keithp.com/blogs/Java-Sound-on-Linux/
		String clipProperty = System.getProperty("javax.sound.sampled.Clip");
		if (SystemInfo.isLinux && clipProperty != null && clipProperty.equals("org.classpath.icedtea.pulseaudio.PulseAudioMixerProvider")) {
			show("JDK used by your IDE can lock up or fail to play sounds.<br/>" +
				 "Please see <a href=\"http://keithp.com/blogs/Java-Sound-on-Linux/\">http://keithp.com/blogs/Java-Sound-on-Linux</a> to fix it.");
		}
	}

	private void dispose(boolean isIdeShutdown) {
		if (soundPlayer == null) return;

		disposeProjectListeners();
		disposeApplicationListeners();
		soundPlayer.stop(isIdeShutdown);

		soundPlayer = null;
	}

	private void initApplicationListeners() {
		allActions = new AllActions(soundPlayer);
		allActions.start(ApplicationManager.getApplication());
	}

	private void disposeApplicationListeners() {
		allActions.stop();
	}

	private void initProjectListeners() {
		projectManagerListener = new ProjectManagerListener() {
			@Override public void projectOpened(@NotNull Project project) {
				if (!refactoringByProject.containsKey(project)) {
					Refactoring refactoring = new Refactoring(project, soundPlayer);
					refactoring.start(project);
					refactoringByProject.put(project, refactoring);
				}

				if (!vcsActionsByProject.containsKey(project)) {
					VcsActions vcsActions = new VcsActions(project, soundPlayer);
					vcsActions.start(project);
					vcsActionsByProject.put(project, vcsActions);
				}

				if (!compilationByProject.containsKey(project) && isIdeWithCompilation()) {
					Compilation compilation = Compilation.factory.create(project, soundPlayer);
					compilation.start(project);
					compilationByProject.put(project, compilation);
				}

				if (!unitTestsByProject.containsKey(project)) {
					UnitTests unitTests = new UnitTests(project, soundPlayer);
					unitTests.start(project);
					unitTestsByProject.put(project, unitTests);
				}
			}

			@Override public void projectClosed(@NotNull Project project) {
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
		ProjectManager projectManager = ProjectManager.getInstance();
		for (Project project : projectManager.getOpenProjects()) {
			projectManagerListener.projectClosed(project);
		}
		projectManager.removeProjectManagerListener(projectManagerListener);
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
		if (silentMode) {
			return Sounds.createSilent(new SilentSound.Listener() {
				@Override public void playing(String soundName) { show(soundName); }
				@Override public void stopped(String soundName) { show("stopped: " + soundName); }
			});
		} else {
			Settings settings = Settings.getInstance();
			return Sounds.create(settings.actionSoundsEnabled, settings.backgroundMusicEnabled);
		}
	}

	private ActionListeningSoundPlayer.Listener createLoggingListener() {
		return new ActionListeningSoundPlayer.Listener() {
			@Override public void unmappedAction(String actionId) {
				if (logUnmappedActions) show(actionId);
			}

			@Override public void unmappedRefactoring(String refactoringId) {
				if (logUnmappedActions) show(refactoringId);
			}
		};
	}

	private static void show(String message) {
		if (isEmptyOrSpaces(message)) return;
		String noTitle = "";
		Notification notification = new Notification("Friday Mario", noTitle, message, NotificationType.INFORMATION);
		ApplicationManager.getApplication().getMessageBus().syncPublisher(Notifications.TOPIC).notify(notification);
	}

	static IntelliJAppComponent instance() {
		return ApplicationManager.getApplication().getComponent(IntelliJAppComponent.class);
	}

	private static boolean isIdeWithCompilation() {
		try {
			Class.forName("com.intellij.openapi.compiler.CompilationStatusAdapter");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public void setBackgroundMusicEnabled(boolean value) {
		Settings.getInstance().setBackgroundMusicEnabled(value);
		update();
	}

	public void setActionSoundsEnabled(boolean value) {
		Settings.getInstance().setActionSoundsEnabled(value);
		update();
	}

	private void update() {
		Settings settings = Settings.getInstance();
		if (!settings.actionSoundsEnabled && !settings.backgroundMusicEnabled) {
			dispose(false);
		} else {
			dispose(false);
			init();
		}
	}
}
