import fridaymario.IntelliJAppComponent
import com.intellij.openapi.components.ApplicationComponent

import static liveplugin.PluginUtil.changeGlobalVar
import static liveplugin.PluginUtil.show

// add-to-classpath $PLUGIN_PATH/out/artifacts/friday_mario/friday-mario.jar

changeGlobalVar("AppComponent"){ ApplicationComponent oldInstance ->
	if (oldInstance != null) oldInstance.disposeComponent()
	def component = new IntelliJAppComponent().silentMode().logUnmappedActionsMode()
	component.initComponent()
	component
//    null
}

// TODO triggers before actual complete; popup window on typing "."
//unwrapAction("CodeCompletion")
//wrapAction("CodeCompletion") { AnActionEvent event, AnAction action ->
//    show("CodeCompletion")
//    action.actionPerformed(event)
//}
// TODO wrap doesn't work
//unwrapAction("EditorChooseLookupItem")
//wrapAction("EditorChooseLookupItem") { AnActionEvent event, AnAction action ->
//    action.actionPerformed(event)
//}
//unwrapAction("EditorChooseLookupItemReplace")
//wrapAction("EditorChooseLookupItemReplace") { AnActionEvent event, AnAction action ->
//    action.actionPerformed(event)
//}


if (!isIdeStartup) show("Reloaded audible-actions plugin")