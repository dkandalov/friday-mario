import com.intellij.openapi.components.ApplicationComponent
import fridaymario.IntelliJAppComponent

import static liveplugin.PluginUtil.changeGlobalVar
import static liveplugin.PluginUtil.show
// add-to-classpath $PLUGIN_PATH/out/artifacts/friday_mario/friday-mario.jar

changeGlobalVar("AppComponent"){ ApplicationComponent oldInstance ->
	if (oldInstance != null) oldInstance.disposeComponent()
	def component = new IntelliJAppComponent().silentMode().logUnmappedActionsMode()
	component.initComponent()
	component
}

if (!isIdeStartup) show("Reloaded FridayMario plugin")