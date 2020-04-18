package fridaymario.listeners;

import com.intellij.execution.testframework.TestsUIUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class UnitTests implements Restartable {
	private final MessageBusConnection busConnection;
	private final Listener listener;

	public UnitTests(Project project, Listener listener) {
		this.listener = listener;
		this.busConnection = project.getMessageBus().connect();
	}

	@Override public void start(Disposable disposable) {
		busConnection.subscribe(Notifications.TOPIC, new Notifications() {
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

	@Override public void stop() {
		busConnection.disconnect();
	}

	public interface Listener {
		void onUnitTestSucceeded();

		void onUnitTestFailed();
	}
}
