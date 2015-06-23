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
	private final CheckinHandlerFactory checkinListener;
	private final NotificationsAdapter pushListener;

	public VcsActions(Project project, final Listener listener) {
		this.busConnection = project.getMessageBus().connect();

		updatedListener = new UpdatedFilesListener() {
			@Override public void consume(Set<String> files) {
				listener.onVcsUpdate();
			}
		};
		checkinListener = new CheckinHandlerFactory() {
			@NotNull @Override
			public CheckinHandler createHandler(@NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
				return new CheckinHandler() {
					@Override public void checkinSuccessful() {
						listener.onVcsCommit();
					}
				};
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
	}

	@Override public void start() {
		// using bus to listen to vcs updates because normal listener calls it twice
		// (see also https://gist.github.com/dkandalov/8840509)
		busConnection.subscribe(UpdatedFilesListener.UPDATED_FILES, updatedListener);
		busConnection.subscribe(Notifications.TOPIC, pushListener);
		CheckinHandlersManager.getInstance().registerCheckinHandlerFactory(checkinListener);
	}

	@Override public void stop() {
		busConnection.disconnect();
		CheckinHandlersManager.getInstance().unregisterCheckinHandlerFactory(checkinListener);
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

	public interface Listener {
		void onVcsCommit();

		void onVcsUpdate();

		void onVcsPush();

		void onVcsPushFailed();
	}
}
