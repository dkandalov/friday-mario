package fridaymario;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class ToggleActionSounds extends ToggleAction implements DumbAware {
	@Override public boolean isSelected(@NotNull AnActionEvent anActionEvent) {
		return Settings.getInstance().actionSoundsEnabled;
	}

	@Override public void setSelected(@NotNull AnActionEvent anActionEvent, boolean value) {
		IntelliJAppComponent.instance.setActionSoundsEnabled(value);
	}
}
