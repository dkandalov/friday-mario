package fridaymario;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;

public class ToggleActionSounds extends ToggleAction {
	@Override public boolean isSelected(AnActionEvent anActionEvent) {
		return Settings.getInstance().actionSoundsEnabled;
	}

	@Override public void setSelected(AnActionEvent anActionEvent, boolean value) {
		IntelliJAppComponent.instance().setActionSoundsEnabled(value);
	}
}
