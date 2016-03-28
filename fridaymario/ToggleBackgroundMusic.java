package fridaymario;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;

public class ToggleBackgroundMusic extends ToggleAction {
	@Override public boolean isSelected(AnActionEvent anActionEvent) {
		return Settings.getInstance().backgroundMusicEnabled;
	}

	@Override public void setSelected(AnActionEvent anActionEvent, boolean value) {
		IntelliJAppComponent.instance().setBackgroundMusicEnabled(value);
	}
}
