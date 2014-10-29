Building plugin
===============
You can build it as a standard IntelliJ plugin:
 - git clone
 - open project in IntelliJ IDEA
 - in project settings configure project SDK, it must be "IntelliJ Platform plugin SDK" for IJ13 or 14
 (see https://www.jetbrains.com/idea/webhelp/configuring-intellij-idea-plugin-sdk.html)
 - remove groovy and liveplugin libraries from module dependencies
 - run "Plugin" configuration


Reloading plugin at runtime
===========================
Because it is annoying to restart IDE after each code change
there is alternative way to restart plugin using [liveplugin](https://github.com/dkandalov/live-plugin)
and make development more iterative and fun.

 - install [liveplugin](https://github.com/dkandalov/live-plugin), restart
 - move this project to liveplugins folder, reopen project in new location
 (e.g. in Plugins toolwindow ctrl+shift+c on existing plugin to see liveplugins path)
 - in "Plugins" toolwindow click "Settings -> Add LivePlugin Jar to Project"
 - add groovy jar to module dependencies (you can grab it from IntelliJ installation lib folder)
 - compile project, make sure output jar was created "out/artifacts/friday_mario/friday-mario.jar"
 - in "Plugins" toolwindow "Run Plugin" (also "ctrl+c, e" anywhere in project),
 this will basically run code from plugin.groovy and will reload plugin

Note that IntelliJAppComponent has silentMode() and logUnmappedActionsMode() methods.


Folder structure
================
fridaymario/listeners - classes which listen to IDE events hiding details of IntelliJ API
fridaymario/sounds - code to play sounds and wav files
fridaymario/IntelliJAppComponent - plugin entry point
META-INF/plugin.xml - plugin meta info
plugin.groovy - entry point for liveplugin