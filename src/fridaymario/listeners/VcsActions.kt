package fridaymario.listeners

import com.intellij.notification.Notification
import com.intellij.notification.Notifications
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import com.intellij.openapi.vcs.update.UpdatedFilesListener

class VcsActions(private val project: Project, private val listener: Listener): Restartable {
    private val updatedListener = UpdatedFilesListener { listener.onVcsUpdate() }

    // see git4idea.push.GitPushResultNotification#create
    // see org.zmlx.hg4idea.push.HgPusher#push
    private val pushListener: Notifications = object: Notifications {
        override fun notify(notification: Notification) {
            if (!isVcsNotification(notification)) return
            if (matchTitleOf(notification, "Push successful")) {
                listener.onVcsPush()
            } else if (matchTitleOf(notification, "Push failed", "Push partially failed", "Push rejected", "Push partially rejected")) {
                listener.onVcsPushFailed()
            }
        }
    }

    override fun start(disposable: Disposable) {
        // using bus to listen to vcs updates because normal listener calls it twice
        // (see also https://gist.github.com/dkandalov/8840509)
        val messageBus = project.messageBus
        messageBus.connect(disposable).subscribe(UpdatedFilesListener.UPDATED_FILES, updatedListener)
        messageBus.connect(disposable).subscribe(Notifications.TOPIC, pushListener)
        MyCheckinHandlerFactory.listener = listener
        Disposer.register(disposable, Disposable { MyCheckinHandlerFactory.listener = null })
    }

    /**
     * Using listener registration through extension point even though it's not reloadable.
     * This is because deprecated part of CheckinHandlersManager API was deleted in IJ15.
     */
    class MyCheckinHandlerFactory: CheckinHandlerFactory() {
        override fun createHandler(checkinProjectPanel: CheckinProjectPanel, commitContext: CommitContext) =
            object: CheckinHandler() {
                override fun checkinSuccessful() {
                    if (listener != null) listener!!.onVcsCommit()
                }
            }

        companion object {
            var listener: Listener? = null
        }
    }

    interface Listener {
        fun onVcsCommit()
        fun onVcsUpdate()
        fun onVcsPush()
        fun onVcsPushFailed()
    }

    companion object {
        private fun isVcsNotification(notification: Notification) =
            notification.groupId == "Vcs Messages"
                || notification.groupId == "Vcs Important Messages"
                || notification.groupId == "Vcs Minor Notifications"
                || notification.groupId == "Vcs Silent Notifications"

        private fun matchTitleOf(notification: Notification, vararg expectedTitles: String): Boolean {
            return expectedTitles.any { notification.title.startsWith(it) }
        }
    }
}