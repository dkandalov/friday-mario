package fridaymario.listeners;

import com.intellij.execution.testframework.TestsUIUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

public class UnitTests implements Restartable {
	private final Listener listener;
	private final MessageBus messageBus;

	public UnitTests(Project project, Listener listener) {
		this.listener = listener;
		this.messageBus = project.getMessageBus();
	}

	@Override public void start(Disposable disposable) {
		messageBus.connect(disposable).subscribe(Notifications.TOPIC, new Notifications() {
			@Override public void notify(@NotNull Notification notification) {
				if (notification.getGroupId().equals(TestsUIUtil.NOTIFICATION_GROUP.getDisplayId())) {
					boolean testsFailed = (notification.getType() == NotificationType.ERROR);
					if (testsFailed) {
						listener.onUnitTestFailed();
					} else {
						listener.onUnitTestSucceeded();
					}
				}
			}
		});
	}

	public interface Listener {
		void onUnitTestSucceeded();

		void onUnitTestFailed();
	}
}
