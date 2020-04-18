package fridaymario.listeners;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import org.jetbrains.annotations.NotNull;

public class AllActions implements Restartable {
	private final AnActionListener actionListener;

	public AllActions(final Listener listener) {
		actionListener = new AnActionListener() {
			@Override public void beforeActionPerformed(@NotNull AnAction action,
			                                            @NotNull DataContext dataContext,
			                                            @NotNull AnActionEvent event) {
				listener.onAction(ActionManager.getInstance().getId(action));
			}
		};
	}

	@Override public void start(Disposable disposable) {
		ActionManager.getInstance().addAnActionListener(actionListener);
	}

	@Override public void stop() {
		ActionManager.getInstance().removeAnActionListener(actionListener);
	}

	public interface Listener {
		void onAction(String actionId);
	}
}
