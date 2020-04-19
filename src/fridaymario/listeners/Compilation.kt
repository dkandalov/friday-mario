package fridaymario.listeners

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

abstract class Compilation: Restartable {
    override fun start(disposable: Disposable) {}

    interface Factory {
        fun create(project: Project, listener: Listener): Compilation
    }

    interface Listener {
        fun compilationSucceeded()
        fun compilationFailed()
    }

    companion object {
        var factory: Factory = object: Factory {
            override fun create(project: Project, listener: Listener) = object: Compilation() {}
        }
    }
}