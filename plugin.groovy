import com.intellij.openapi.components.ApplicationComponent
import fridaymario.IntelliJAppComponent
import fridaymario.Settings

import static liveplugin.PluginUtil.changeGlobalVar
import static liveplugin.PluginUtil.show
// add-to-classpath $PLUGIN_PATH/out/artifacts/friday_mario/friday-mario.jar

Settings.instance.pluginEnabled = true

changeGlobalVar("AppComponent"){ ApplicationComponent oldInstance ->
	if (oldInstance != null) oldInstance.dispose()
	def component = new IntelliJAppComponent()./*silentMode().*/logUnmappedActionsMode()
	component.init()
	component
}

if (!isIdeStartup) show("Reloaded FridayMario plugin")