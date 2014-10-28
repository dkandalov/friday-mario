package fridaymario.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.refactoring.listeners.RefactoringEventData;
import com.intellij.refactoring.listeners.RefactoringEventListener;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Refactoring implements Restartable {
	private final MessageBusConnection busConnection;
	private final Listener listener;

	public Refactoring(Project project, Listener listener) {
		this.listener = listener;
		this.busConnection = project.getMessageBus().connect();
	}

	@Override public void start() {
		busConnection.subscribe(RefactoringEventListener.REFACTORING_EVENT_TOPIC, new RefactoringEventListener() {
			@Override public void refactoringDone(@NotNull String refactoringId, @Nullable RefactoringEventData afterData) {
				listener.onRefactoring(refactoringId);
			}

			@Override public void refactoringStarted(@NotNull String refactoringId, @Nullable RefactoringEventData beforeData) {
			}

			@Override public void conflictsDetected(@NotNull String refactoringId, @NotNull RefactoringEventData conflictsData) {
			}

			@Override public void undoRefactoring(@NotNull String refactoringId) {
			}
		});
	}

	@Override public void stop() {
		busConnection.disconnect();
	}

	public static interface Listener {
		void onRefactoring(String refactoringId);
	}
}

