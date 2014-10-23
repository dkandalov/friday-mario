package audible;

import audible.notifications.*;
import audible.wav.Sound;
import audible.wav.Sounds;

import java.util.HashMap;
import java.util.Map;

import static liveplugin.PluginUtil.show;

public class NotificationListener implements
        Compilation.Listener, EditorNavigation.Listener, Refactoring.Listener, UnitTests.Listener, VcsActions.Listener {

    private final Sounds sounds;
    private final Map<String, Sound> soundsByNavigationAction;
    private final Map<String, Sound> soundsByRefactoring;

    public NotificationListener(Sounds sounds) {
        this.sounds = sounds;
        this.soundsByNavigationAction = createNavigationSounds(sounds);
        this.soundsByRefactoring = createRefactoringSounds(sounds);
    }

    @Override
    public void compilationSucceeded() {
        show("compilationSucceeded"); // TODO
    }

    @Override
    public void compilationFailed() {
        sounds.coin.play();
    }

    @Override
    public void onNavigationAction(String actionId) {
        Sound sound = soundsByNavigationAction.get(actionId);
        if (sound != null) {
            sound.play();
        }
    }

    @Override
    public void onRefactoring(String refactoringId) {
        Sound sound = soundsByRefactoring.get(refactoringId);
        if (sound != null) {
            sound.play();
        }
    }

    private static Map<String, Sound> createRefactoringSounds(Sounds sounds) {
        Map<String, Sound> result = new HashMap<String, Sound>();
        result.put("refactoring.rename", sounds.coin);
        result.put("refactoring.extractVariable", sounds.coin);
        result.put("refactoring.extract.method", sounds.coin);
        result.put("refactoring.inline.local.variable", sounds.coin);
        result.put("refactoring.safeDelete", sounds.coin);
        result.put("refactoring.introduceParameter", sounds.coin);
        return result;
    }

    private static Map<String, Sound> createNavigationSounds(Sounds sounds) {
        Map<String, Sound> result = new HashMap<String, Sound>();
        result.put("EditorUp", sounds.coin);
        result.put("EditorDown", sounds.coin);
        result.put("EditorLeft", sounds.coin);
        result.put("EditorRight", sounds.coin);
        result.put("EditorUpWithSelection", sounds.coin);
        result.put("EditorDownWithSelection", sounds.coin);
        result.put("EditorLeftWithSelection", sounds.coin);
        result.put("EditorRightWithSelection", sounds.coin);

        result.put("EditorPreviousWord", sounds.coin);
        result.put("EditorNextWord", sounds.coin);
        result.put("EditorPreviousWordWithSelection", sounds.coin);
        result.put("EditorNextWordWithSelection", sounds.coin);

        result.put("EditorLineStart", sounds.coin);
        result.put("EditorLineEnd", sounds.coin);
        result.put("EditorLineStartWithSelection", sounds.coin);
        result.put("EditorLineEndWithSelection", sounds.coin);

        result.put("EditorPageUp", sounds.coin);
        result.put("EditorPageDown", sounds.coin);

        // TODO complete statement action

        return result;
    }

    @Override
    public void onUnitTestSucceeded() {
        sounds.coin.play();
    }

    @Override
    public void onUnitTestFailed() {
        sounds.coin.play();
    }

    @Override
    public void onVcsCommit() {
        sounds.coin.play();
    }

    @Override
    public void onVcsUpdate() {
        sounds.coin.play();
    }
}
