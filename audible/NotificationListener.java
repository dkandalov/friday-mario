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

    @Override
    public void compilationSucceeded() {
        compilationIsFailing = false;
        sounds.coin.play();
    }

    @Override
    public void compilationFailed() {
        compilationIsFailing = true;
        sounds.coin.play();
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
        sounds.coin.play();
    }

    @Override
    public void onUnitTestFailed() {
        testsAreFailing = true;
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

        result.put("EditorCompleteStatement", sounds.coin);

        return result;
    }
}
