import audible.AppComponent
import com.intellij.openapi.components.ApplicationComponent

import static liveplugin.PluginUtil.*
// add-to-classpath $PLUGIN_PATH/out/artifacts/audible_actions/audible-actions.jar

changeGlobalVar("AppComponent") { ApplicationComponent oldInstance ->
    if (oldInstance != null) oldInstance.disposeComponent()
    def component = new AppComponent()
    component.initComponent()
    component
}

//changeGlobalVar("sounds") { oldSound ->
//    if (oldSound != null) {
//        oldSound.stop()
//    }
//    def sounds = new Sounds()
//    sounds.background.play()
//
////    sounds.backgroundSad.play()
//}

//changeGlobalVar("clip") { Clip oldClip ->
//    if (oldClip != null) {
//        oldClip.stop()
//    }
////    Clip clip = AudioSystem.getClip();
////    InputStream stream = new ByteArrayInputStream(new Sounds().coin.bytes);
////    if (!stream.markSupported()) stream = new BufferedInputStream(stream);
////    AudioInputStream inputStream = AudioSystem.getAudioInputStream(stream);
////    clip.open(inputStream);
//////    clip.start();
////    clip.loop(0)
////    clip
//    null
//}

unwrapAction("HippieCompletion")
//wrapAction("HippieCompletion") { AnActionEvent event, AnAction action ->
//    show("HippieCompletion")
//    action.actionPerformed(event)
//}
// TODO triggers before actual complete; popup window on typing "."
unwrapAction("CodeCompletion")
//wrapAction("CodeCompletion") { AnActionEvent event, AnAction action ->
//    show("CodeCompletion")
//    action.actionPerformed(event)
//}
// TODO wrap doesn't work
unwrapAction("EditorChooseLookupItem")
//wrapAction("EditorChooseLookupItem") { AnActionEvent event, AnAction action ->
//    action.actionPerformed(event)
//}
unwrapAction("EditorChooseLookupItemReplace")
//wrapAction("EditorChooseLookupItemReplace") { AnActionEvent event, AnAction action ->
//    action.actionPerformed(event)
//}


if (!isIdeStartup) show("Reloaded audible-actions plugin")