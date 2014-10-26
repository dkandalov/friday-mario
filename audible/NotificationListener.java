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
    private boolean stopped;

    public NotificationListener(Sounds sounds) {
        this.sounds = sounds;
        this.soundsByAction = createEditorSounds(sounds);
        this.soundsByRefactoring = createRefactoringSounds(sounds);
    }

    public NotificationListener init() {
        sounds.background.playInBackground();
        return this;
    }

    public void stop() {
        if (stopped) return;
        stopped = true;
        sounds.background.stop();
        sounds.backgroundSad.stop();
        sounds.gameover.playAndWait();
    }

    @Override
    public void compilationSucceeded() {
        sounds.oneUp.play();
        if (compilationIsFailing) {
            compilationIsFailing = false;
            sounds.background.playInBackground();
            sounds.backgroundSad.stop();
        }
    }

    @Override
    public void compilationFailed() {
        sounds.oneDown.play();
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
        } else {
            sounds.coin.play();
        }
    }

    @Override
    public void onUnitTestSucceeded() {
        sounds.oneUp.play();
    }

    @Override
    public void onUnitTestFailed() {
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
        result.put("EditorUp", sounds.kick);
        result.put("EditorDown", sounds.kick);
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
        result.put("EditorStartNewLine", sounds.kick);
        result.put("EditorDeleteLine", sounds.breakblock);
        result.put("EditorDeleteToWordStart", sounds.breakblock);
        result.put("EditorDeleteToWordEnd", sounds.breakblock);
        result.put("CommentByLineComment", sounds.breakblock);
        result.put("CommentByBlockComment", sounds.breakblock);

        result.put("NextTab", sounds.jumpSuper);
        result.put("PreviousTab", sounds.jumpSuper);
        result.put("CloseActiveTab", sounds.fireworks);
        result.put("$Undo", sounds.fireworks);
        result.put("$Redo", sounds.fireworks);
        result.put("ExpandAllRegions", sounds.stomp);
        result.put("CollapseAllRegions", sounds.stomp);

        return result;
    }
}
