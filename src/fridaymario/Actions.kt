package fridaymario

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware

class ToggleActionSounds: ToggleAction(), DumbAware {
    override fun isSelected(anActionEvent: AnActionEvent) = Settings.getInstance().actionSoundsEnabled
    override fun setSelected(anActionEvent: AnActionEvent, value: Boolean) =
        IntelliJAppComponent.instance!!.setActionSoundsEnabled(value)
}

class ToggleBackgroundMusic: ToggleAction(), DumbAware {
    override fun isSelected(anActionEvent: AnActionEvent) = Settings.getInstance().backgroundMusicEnabled
    override fun setSelected(anActionEvent: AnActionEvent, value: Boolean) =
        IntelliJAppComponent.instance!!.setBackgroundMusicEnabled(value)
}