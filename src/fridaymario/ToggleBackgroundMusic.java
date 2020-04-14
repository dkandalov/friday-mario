package fridaymario;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class ToggleBackgroundMusic extends ToggleAction implements DumbAware {
	@Override public boolean isSelected(@NotNull AnActionEvent anActionEvent) {
		return Settings.getInstance().backgroundMusicEnabled;
	}

	@Override public void setSelected(@NotNull AnActionEvent anActionEvent, boolean value) {
		IntelliJAppComponent.instance().setBackgroundMusicEnabled(value);
	}
}
