package fridaymario.listeners;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.refactoring.listeners.RefactoringEventData;
import com.intellij.refactoring.listeners.RefactoringEventListener;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Refactoring implements Restartable {
	private final Listener listener;
	private final MessageBus messageBus;

	public Refactoring(Project project, Listener listener) {
		this.listener = listener;
		this.messageBus = project.getMessageBus();
	}

	@Override public void start(Disposable disposable) {
		messageBus.connect(disposable).subscribe(RefactoringEventListener.REFACTORING_EVENT_TOPIC, new RefactoringEventListener() {
			@Override public void refactoringDone(@NotNull String refactoringId, @Nullable RefactoringEventData afterData) {
				listener.onRefactoring(refactoringId);
			}

			@Override public void refactoringStarted(@NotNull String refactoringId, @Nullable RefactoringEventData beforeData) {}

			@Override public void conflictsDetected(@NotNull String refactoringId, @NotNull RefactoringEventData conflictsData) {}

			@Override public void undoRefactoring(@NotNull String refactoringId) {}
		});
	}

	public interface Listener {
		void onRefactoring(String refactoringId);
	}
}

