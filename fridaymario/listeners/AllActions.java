package fridaymario.listeners;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;

public class AllActions implements Restartable {
	private final AnActionListener.Adapter actionListener;

	public AllActions(final Listener listener) {
		actionListener = new AnActionListener.Adapter() {
			@Override public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
				listener.onAction(ActionManager.getInstance().getId(action));
			}
		};
	}

	@Override public void start() {
		ActionManager.getInstance().addAnActionListener(actionListener);
	}

	@Override public void stop() {
		ActionManager.getInstance().removeAnActionListener(actionListener);
	}

	public interface Listener {
		void onAction(String actionId);
	}
}
