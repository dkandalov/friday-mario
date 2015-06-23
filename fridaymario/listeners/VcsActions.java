package fridaymario.listeners;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.notification.NotificationsAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.openapi.vcs.impl.CheckinHandlersManager;
import com.intellij.openapi.vcs.update.UpdatedFilesListener;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class VcsActions implements Restartable {
	private final MessageBusConnection busConnection;
	private final UpdatedFilesListener updatedListener;
	private final NotificationsAdapter pushListener;
	private final Listener listener;

	public VcsActions(Project project, final Listener listener) {
		this.busConnection = project.getMessageBus().connect();

		updatedListener = new UpdatedFilesListener() {
			@Override public void consume(Set<String> files) {
				listener.onVcsUpdate();
			}
		};

		// see git4idea.push.GitPushResultNotification#create
		// see org.zmlx.hg4idea.push.HgPusher#push
		pushListener = new NotificationsAdapter() {
			@Override public void notify(@NotNull Notification notification) {
				if (!isVcsNotification(notification)) return;

				if (matchTitleOf(notification, "Push successful")) {
					listener.onVcsPush();
				} else if (matchTitleOf(notification, "Push failed", "Push partially failed", "Push rejected", "Push partially rejected")) {
					listener.onVcsPushFailed();
				}
			}
		};
		this.listener = listener;
	}

	@Override public void start() {
		// using bus to listen to vcs updates because normal listener calls it twice
		// (see also https://gist.github.com/dkandalov/8840509)
		busConnection.subscribe(UpdatedFilesListener.UPDATED_FILES, updatedListener);
		busConnection.subscribe(Notifications.TOPIC, pushListener);
		MyCheckinHandlerFactory.listener = listener;
	}

	@Override public void stop() {
		busConnection.disconnect();
		MyCheckinHandlerFactory.listener = null;
	}

	private static boolean isVcsNotification(Notification notification) {
		return notification.getGroupId().equals("Vcs Messages") ||
				notification.getGroupId().equals("Vcs Important Messages") ||
				notification.getGroupId().equals("Vcs Minor Notifications") ||
				notification.getGroupId().equals("Vcs Silent Notifications");
	}

	private static boolean matchTitleOf(Notification notification, String... expectedTitles) {
		for (String title : expectedTitles) {
			if (notification.getTitle().startsWith(title)) return true;
		}
		return false;
	}

	/**
	 * Using listener registration through extension point even though it's not reloadable.
	 * This is because deprecated part of {@link CheckinHandlersManager} API was deleted in IJ15.
	 */
	public static class MyCheckinHandlerFactory extends CheckinHandlerFactory {
		public static Listener listener;

		@NotNull @Override
		public CheckinHandler createHandler(CheckinProjectPanel checkinProjectPanel, CommitContext commitContext) {
			return new CheckinHandler() {
				@Override public void checkinSuccessful() {
					if (listener != null) {
						listener.onVcsCommit();
					}
				}
			};
		}
	}

	public interface Listener {
		void onVcsCommit();

		void onVcsUpdate();

		void onVcsPush();

		void onVcsPushFailed();
	}
}
