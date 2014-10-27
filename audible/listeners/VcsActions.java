package audible.listeners;

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

	public VcsActions(Project project, final Listener listener) {
		this.busConnection = project.getMessageBus().connect();

		updatedListener = new UpdatedFilesListener() {
			@Override
			public void consume(Set<String> files) {
				listener.onVcsUpdate();
			}
		};
		checkinListener = new CheckinHandlerFactory() {
			@NotNull
			@Override
			public CheckinHandler createHandler(@NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
				return new CheckinHandler() {
					@Override
					public void checkinSuccessful() {
						listener.onVcsCommit();
					}
				};
			}
		};
	}

	@Override public void start() {
		busConnection.subscribe(UpdatedFilesListener.UPDATED_FILES, updatedListener);
		CheckinHandlersManager.getInstance().registerCheckinHandlerFactory(checkinListener);
	}

	@Override public void stop() {
		busConnection.disconnect();
		CheckinHandlersManager.getInstance().unregisterCheckinHandlerFactory(checkinListener);
	}

	public static interface Listener {
		void onVcsCommit();

		void onVcsUpdate();
		// TODO git push
	}
}
