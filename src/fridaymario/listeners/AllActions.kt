package fridaymario.listeners;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

public class AllActions implements Restartable {
	private final Listener listener;

	public AllActions(final Listener listener) {
		this.listener = listener;
	}

	@Override public void start(Disposable disposable) {
		ApplicationManager.getApplication().getMessageBus().connect(disposable)
				.subscribe(AnActionListener.TOPIC, new AnActionListener() {
					@Override public void beforeActionPerformed(@NotNull AnAction action,
					                                            @NotNull DataContext dataContext,
					                                            @NotNull AnActionEvent event) {
						listener.onAction(ActionManager.getInstance().getId(action));
					}
				});
	}

	public interface Listener {
		void onAction(String actionId);
	}
}
