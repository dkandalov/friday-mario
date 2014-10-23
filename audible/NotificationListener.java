package audible;

import audible.idelisteners.*;
import audible.wav.Sound;
import audible.wav.Sounds;

import java.util.HashMap;
import java.util.Map;

public class NotificationListener implements
        Compilation.Listener, Navigation.Listener, EditorModification.Listener,
        Refactoring.Listener, UnitTests.Listener, VcsActions.Listener {

    private final Sounds sounds;
    private final Map<String, Sound> soundsByAction;
    private final Map<String, Sound> soundsByRefactoring;
    private boolean compilationIsFailing;
    private boolean testsAreFailing;

    public NotificationListener(Sounds sounds) {
        this.sounds = sounds;
        this.soundsByAction = createEditorSounds(sounds);
        this.soundsByRefactoring = createRefactoringSounds(sounds);
    }

    public NotificationListener init() {
        sounds.background.playInBackground();
        return this;
    }

    public void dispose() {
        sounds.background.stop();
        sounds.backgroundSad.stop();
    }

    @Override
    public void compilationSucceeded() {
        if (compilationIsFailing) {
            compilationIsFailing = false;
            sounds.background.playInBackground();
            sounds.backgroundSad.stop();
        }
    }

    @Override
    public void compilationFailed() {
        if (!compilationIsFailing) {
            compilationIsFailing = true;
            sounds.backgroundSad.playInBackground();
            sounds.background.stop();
        }
    }

    @Override
    public void onEditorNavigation(String actionId) {
        Sound sound = soundsByAction.get(actionId);
        if (sound != null) {
            sound.play();
        }
    }

    @Override
    public void onEditorModification(String actionId) {
        Sound sound = soundsByAction.get(actionId);
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

    @Override
    public void onUnitTestSucceeded() {
        testsAreFailing = false;
        sounds.oneUp.play();
    }

    @Override
    public void onUnitTestFailed() {
        testsAreFailing = true;
        sounds.oneDown.play();
    }

    @Override
    public void onVcsCommit() {
        sounds.powerupAppears.play();
    }

    @Override
    public void onVcsUpdate() {
        sounds.powerup.play();
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

    private static Map<String, Sound> createEditorSounds(Sounds sounds) {
        Map<String, Sound> result = new HashMap<String, Sound>();
//        result.put("EditorUp", sounds.coin);
//        result.put("EditorDown", sounds.coin);
//        result.put("EditorLeft", sounds.coin);
//        result.put("EditorRight", sounds.coin);
//        result.put("EditorUpWithSelection", sounds.coin);
//        result.put("EditorDownWithSelection", sounds.coin);
//        result.put("EditorLeftWithSelection", sounds.coin);
//        result.put("EditorRightWithSelection", sounds.coin);
        result.put("EditorPreviousWord", sounds.kick);
        result.put("EditorNextWord", sounds.kick);
        result.put("EditorPreviousWordWithSelection", sounds.kick);
        result.put("EditorNextWordWithSelection", sounds.kick);
        result.put("EditorLineStart", sounds.jumpSmall);
        result.put("EditorLineEnd", sounds.jumpSmall);
        result.put("EditorLineStartWithSelection", sounds.jumpSmall);
        result.put("EditorLineEndWithSelection", sounds.jumpSmall);
        result.put("EditorPageUp", sounds.jumpSuper);
        result.put("EditorPageDown", sounds.jumpSuper);

        result.put("EditorCompleteStatement", sounds.fireball);
        result.put("HippieCompletion", sounds.fireball);
        result.put("HippieBackwardCompletion", sounds.fireball);
        result.put("EditorDeleteLine", sounds.breakblock);
        result.put("$Paste", sounds.bowserfalls);

        return result;
    }
}
