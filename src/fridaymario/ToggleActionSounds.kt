package fridaymario

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware

class ToggleActionSounds: ToggleAction(), DumbAware {
    override fun isSelected(anActionEvent: AnActionEvent): Boolean {
        return Settings.getInstance().actionSoundsEnabled
    }

    override fun setSelected(anActionEvent: AnActionEvent, value: Boolean) {
        IntelliJAppComponent.instance!!.setActionSoundsEnabled(value)
    }
}