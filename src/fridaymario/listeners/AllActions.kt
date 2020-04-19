package fridaymario.listeners

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import fridaymario.util.appMessageBus

class AllActions(private val listener: Listener): Restartable {
    override fun start(disposable: Disposable) {
        appMessageBus.connect(disposable).subscribe(AnActionListener.TOPIC, object: AnActionListener {
            override fun beforeActionPerformed(action: AnAction, dataContext: DataContext, event: AnActionEvent) {
                listener.onAction(ActionManager.getInstance().getId(action))
            }
        })
    }

    interface Listener {
        fun onAction(actionId: String?)
    }
}