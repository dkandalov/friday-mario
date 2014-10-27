import fridaymario.IntelliJAppComponent
import com.intellij.openapi.components.ApplicationComponent

import static liveplugin.PluginUtil.changeGlobalVar
import static liveplugin.PluginUtil.show

// add-to-classpath $PLUGIN_PATH/out/artifacts/audible_actions/audible-actions.jar

changeGlobalVar("AppComponent"){ ApplicationComponent oldInstance ->
	if (oldInstance != null) oldInstance.disposeComponent()
	def component = new IntelliJAppComponent()
	component.initComponent()
	component
//    null
}

/*
// TODO use it and remove
changeGlobalVar("actionsListener") { previousListener ->
    if (previousListener != null) {
        ActionManager.instance.removeAnActionListener(previousListener)
    }
    def listener = new AnActionListener.Adapter() {
        @Override void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
//            show("!!" + ActionManager.instance.getId(action))
            show(action.hashCode())
        }
    }
    ActionManager.instance.addAnActionListener(listener)
    listener
}
*/

//unwrapAction("EditorCompleteStatement")
//wrapAction("EditorCompleteStatement") { AnActionEvent event, AnAction originalAction ->
//    show("aaa!!!")
//    originalAction.actionPerformed(event)
//}

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