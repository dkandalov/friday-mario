package fridaymario

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.SystemInfo
import fridaymario.listeners.*
import fridaymario.sounds.SilentSound
import fridaymario.sounds.Sounds
import fridaymario.util.newDisposable
import fridaymario.util.registerParent
import fridaymario.util.show
import fridaymario.util.whenDisposed

class IntelliJAppComponent: AppLifecycleListener {
    private var soundPlayer: ActionListeningSoundPlayer? = null
    private var disposable: Disposable? = null
    private var silentMode = false
    private var logUnmappedActions = false

    init {
        instance = this
    }

    override fun appFrameCreated(commandLineArgs: List<String>) {
        if (!Settings.getInstance().isPluginEnabled) return
        disposable = init()
    }

    override fun appWillBeClosed(isRestart: Boolean) {
        if (!Settings.getInstance().isPluginEnabled) return
        if (soundPlayer != null && Settings.getInstance().backgroundMusicEnabled) {
            soundPlayer!!.stopAndPlayGameOver()
        }
    }

    private fun init(): Disposable {
        val disposable = newDisposable("FridayMario").registerParent(ApplicationManager.getApplication())

        soundPlayer = ActionListeningSoundPlayer(createSounds(), createLoggingListener()).init()
        disposable.whenDisposed {
            if (soundPlayer != null) {
                soundPlayer!!.stop()
                soundPlayer = null
            }
        }
        initApplicationListeners(soundPlayer!!, disposable)
        initProjectListeners(soundPlayer!!, disposable)

        // see https://github.com/dkandalov/friday-mario/issues/3#issuecomment-160421286
        // and http://keithp.com/blogs/Java-Sound-on-Linux/
        val clipProperty = System.getProperty("javax.sound.sampled.Clip")
        if (SystemInfo.isLinux && clipProperty != null && clipProperty == "org.classpath.icedtea.pulseaudio.PulseAudioMixerProvider") {
            show("JDK used by your IDE can lock up or fail to play sounds.<br/>" +
                     "Please see <a href=\"http://keithp.com/blogs/Java-Sound-on-Linux/\">http://keithp.com/blogs/Java-Sound-on-Linux</a> to fix it.")
        }
        return disposable
    }

    private fun initApplicationListeners(soundPlayer: ActionListeningSoundPlayer, disposable: Disposable) {
        AllActions(soundPlayer).start(disposable)
    }

    private fun initProjectListeners(soundPlayer: ActionListeningSoundPlayer, parentDisposable: Disposable) {
        val projectManagerListener = object: ProjectManagerListener {
            override fun projectOpened(project: Project) {
                val disposable = newDisposable().registerParent(parentDisposable, project)
                Refactoring(project, soundPlayer).start(disposable)
                VcsActions(project, soundPlayer).start(disposable)
                Compilation.factory.create(project, soundPlayer).start(disposable)
                UnitTests(project, soundPlayer).start(disposable)
            }
        }

        for (project in ProjectManager.getInstance().openProjects) {
            projectManagerListener.projectOpened(project)
        }
        ApplicationManager.getApplication().messageBus.connect(parentDisposable)
            .subscribe(ProjectManager.TOPIC, projectManagerListener)
    }

    fun silentMode(): IntelliJAppComponent {
        silentMode = true
        return this
    }

    fun logUnmappedActionsMode(): IntelliJAppComponent {
        logUnmappedActions = true
        return this
    }

    private fun createSounds(): Sounds {
        return if (silentMode) {
            Sounds.createSilent(object: SilentSound.Listener {
                override fun playing(soundName: String) = show(soundName)
                override fun stopped(soundName: String) = show("stopped: $soundName")
            })
        } else {
            val settings = Settings.getInstance()
            Sounds.create(settings.actionSoundsEnabled, settings.backgroundMusicEnabled)
        }
    }

    private fun createLoggingListener() =
        object: ActionListeningSoundPlayer.Listener {
            override fun unmappedAction(actionId: String) {
                if (logUnmappedActions) show(actionId)
            }

            override fun unmappedRefactoring(refactoringId: String) {
                if (logUnmappedActions) show(refactoringId)
            }
        }

    fun setBackgroundMusicEnabled(value: Boolean) {
        Settings.getInstance().setBackgroundMusicEnabled(value)
        update()
    }

    fun setActionSoundsEnabled(value: Boolean) {
        Settings.getInstance().setActionSoundsEnabled(value)
        update()
    }

    private fun update() {
        if (disposable != null) Disposer.dispose(disposable!!)
        if (Settings.getInstance().isPluginEnabled) disposable = init()
    }

    companion object {
        var instance: IntelliJAppComponent? = null
    }
}