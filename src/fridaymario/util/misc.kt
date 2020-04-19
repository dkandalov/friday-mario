package fridaymario.util

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.text.StringUtilRt
import com.intellij.util.messages.MessageBus

fun show(message: String) {
    if (StringUtilRt.isEmptyOrSpaces(message)) return
    val noTitle = ""
    val notification = Notification("Friday Mario", noTitle, message, NotificationType.INFORMATION)
    appMessageBus.syncPublisher(Notifications.TOPIC).notify(notification)
}

val appMessageBus: MessageBus get() = ApplicationManager.getApplication().messageBus