package fridaymario

import fridaymario.listeners.*
import fridaymario.sounds.Sound
import fridaymario.sounds.Sounds
import java.util.*

class ActionListeningSoundPlayer(private val sounds: Sounds, private val listener: Listener):
    Compilation.Listener, Refactoring.Listener, UnitTests.Listener, VcsActions.Listener, AllActions.Listener {

    private val soundsByAction: Map<String?, Sound>
    private val soundsByRefactoring: Map<String?, Sound>
    private var compilationFailed = false
    private var stopped = false

    fun init(): ActionListeningSoundPlayer {
        sounds.marioSong.playInBackground()
        return this
    }

    fun stop() {
        if (stopped) return
        stopped = true
        sounds.marioSong.stop()
        sounds.zeldaSong.stop()
    }

    fun stopAndPlayGameOver() {
        stop()
        sounds.gameover.playAndWait()
    }

    override fun onAction(actionId: String) {
        val sound = soundsByAction[actionId]
        if (sound != null) {
            sound.play()
        } else {
            listener.unmappedAction(actionId)
        }
    }

    override fun onRefactoring(refactoringId: String) {
        val sound = soundsByRefactoring[refactoringId]
        if (sound != null) {
            sound.play()
        } else {
            sounds.coin.play()
            listener.unmappedRefactoring(refactoringId)
        }
    }

    override fun compilationSucceeded() {
        sounds.oneUp.play()
        if (compilationFailed) {
            compilationFailed = false
            sounds.marioSong.playInBackground()
            sounds.zeldaSong.stop()
        }
    }

    override fun compilationFailed() {
        sounds.oneDown.play()
        if (!compilationFailed) {
            compilationFailed = true
            sounds.zeldaSong.playInBackground()
            sounds.marioSong.stop()
        }
    }

    override fun onUnitTestSucceeded() {
        sounds.oneUp.play()
    }

    override fun onUnitTestFailed() {
        sounds.oneDown.play()
    }

    override fun onVcsCommit() {
        sounds.powerupAppears.play()
    }

    override fun onVcsUpdate() {
        sounds.powerup.play()
    }

    override fun onVcsPush() {
        sounds.powerup.play()
    }

    override fun onVcsPushFailed() {
        sounds.oneDown.play()
    }

    interface Listener {
        fun unmappedAction(actionId: String)
        fun unmappedRefactoring(refactoringId: String)
    }

    companion object {
        private fun refactoringSounds(sounds: Sounds): Map<String?, Sound> {
            val result: MutableMap<String?, Sound> = HashMap()
            result["refactoring.rename"] = sounds.coin
            result["refactoring.extractVariable"] = sounds.coin
            result["refactoring.extract.method"] = sounds.coin
            result["refactoring.inline.local.variable"] = sounds.coin
            result["refactoring.safeDelete"] = sounds.coin
            result["refactoring.introduceParameter"] = sounds.coin
            return result
        }

        private fun editorSounds(sounds: Sounds): Map<String?, Sound> {
            val result: MutableMap<String?, Sound> = HashMap()
            result["EditorUp"] = sounds.kick
            result["EditorDown"] = sounds.kick
            result["EditorUpWithSelection"] = sounds.kick
            result["EditorDownWithSelection"] = sounds.kick
            result["EditorPreviousWord"] = sounds.kick
            result["EditorNextWord"] = sounds.kick
            result["EditorPreviousWordWithSelection"] = sounds.kick
            result["EditorNextWordWithSelection"] = sounds.kick
            result["EditorSelectWord"] = sounds.kick
            result["EditorUnSelectWord"] = sounds.kick
            result["\$SelectAll"] = sounds.kick
            result["EditorLineStart"] = sounds.jumpSmall
            result["EditorLineEnd"] = sounds.jumpSmall
            result["EditorLineStartWithSelection"] = sounds.jumpSmall
            result["EditorLineEndWithSelection"] = sounds.jumpSmall
            result["EditorPageUp"] = sounds.jumpSuper
            result["EditorPageDown"] = sounds.jumpSuper
            result["GotoPreviousError"] = sounds.jumpSuper
            result["GotoNextError"] = sounds.jumpSuper
            result["FindNext"] = sounds.jumpSuper
            result["FindPrevious"] = sounds.jumpSuper
            result["MethodUp"] = sounds.jumpSuper
            result["MethodDown"] = sounds.jumpSuper
            result["Back"] = sounds.jumpSuper
            result["Forward"] = sounds.jumpSuper
            result["GotoSuperMethod"] = sounds.jumpSuper
            result["GotoDeclaration"] = sounds.jumpSuper
            result["GotoImplementation"] = sounds.jumpSuper
            result["EditSource"] = sounds.jumpSuper // this is F4 navigate action
            result["EditorPaste"] = sounds.fireball
            result["ReformatCode"] = sounds.fireball
            result["EditorToggleCase"] = sounds.fireball
            result["ExpandLiveTemplateByTab"] = sounds.fireball
            result["EditorCompleteStatement"] = sounds.fireball
            result["EditorChooseLookupItem"] = sounds.fireball
            result["EditorChooseLookupItemReplace"] = sounds.fireball
            result["HippieCompletion"] = sounds.fireball
            result["HippieBackwardCompletion"] = sounds.fireball
            result["MoveStatementUp"] = sounds.fireball
            result["MoveStatementDown"] = sounds.fireball
            result["EditorStartNewLineBefore"] = sounds.fireball
            result["EditorStartNewLine"] = sounds.fireball
            result["EditorDuplicate"] = sounds.fireball
            result["EditorBackSpace"] = sounds.breakblock
            result["EditorJoinLines"] = sounds.breakblock
            result["EditorDelete"] = sounds.breakblock
            result["EditorDeleteLine"] = sounds.breakblock
            result["EditorDeleteToWordStart"] = sounds.breakblock
            result["EditorDeleteToWordEnd"] = sounds.breakblock
            result["CommentByLineComment"] = sounds.breakblock
            result["CommentByBlockComment"] = sounds.breakblock
            result["ToggleBookmark"] = sounds.stomp
            result["ToggleBookmarkWithMnemonic"] = sounds.stomp
            result["ToggleLineBreakpoint"] = sounds.stomp
            result["HighlightUsagesInFile"] = sounds.stomp
            result["NextTab"] = sounds.jumpSuper
            result["PreviousTab"] = sounds.jumpSuper
            result["CloseEditor"] = sounds.fireworks
            result["CloseAllEditorsButActive"] = sounds.fireworks
            result["\$Undo"] = sounds.fireworks
            result["\$Redo"] = sounds.fireworks
            result["ExpandAllRegions"] = sounds.stomp
            result["CollapseAllRegions"] = sounds.stomp
            result["ExpandRegion"] = sounds.stomp
            result["CollapseRegion"] = sounds.stomp
            result["CollapseSelection"] = sounds.stomp
            result["PasteMultiple"] = sounds.stomp
            result["FileStructurePopup"] = sounds.stomp
            result["ShowBookmarks"] = sounds.stomp
            result["ViewBreakpoints"] = sounds.stomp
            result["QuickJavaDoc"] = sounds.stomp
            result["ParameterInfo"] = sounds.stomp
            result["ShowIntentionActions"] = sounds.stomp
            result["EditorToggleColumnMode"] = sounds.stomp
            result["SurroundWith"] = sounds.stomp
            result["InsertLiveTemplate"] = sounds.stomp
            result["SurroundWithLiveTemplate"] = sounds.stomp
            result["NewElement"] = sounds.stomp
            result["Generate"] = sounds.stomp
            result["OverrideMethods"] = sounds.stomp
            result["ImplementMethods"] = sounds.stomp
            result["ChangeSignature"] = sounds.stomp
            result["ExtractMethod"] = sounds.stomp
            result["Inline"] = sounds.stomp
            result["Move"] = sounds.stomp
            result["Find"] = sounds.stomp
            result["FindInPath"] = sounds.stomp
            result["Replace"] = sounds.stomp
            result["ReplaceInPath"] = sounds.stomp
            result["ChangesView.Diff"] = sounds.stomp
            result["CompareClipboardWithSelection"] = sounds.stomp
            result["Switcher"] = sounds.stomp
            result["RecentFiles"] = sounds.stomp
            result["GotoClass"] = sounds.stomp
            result["GotoFile"] = sounds.stomp
            result["GotoSymbol"] = sounds.stomp
            result["SearchEverywhere"] = sounds.stomp
            result["GotoLine"] = sounds.stomp
            result["ShowUsages"] = sounds.stomp
            result["FindUsages"] = sounds.stomp
            result["ShowNavBar"] = sounds.stomp
            result["RunInspection"] = sounds.stomp
            result["SelectIn"] = sounds.stomp
            result["QuickChangeScheme"] = sounds.stomp
            result["ActivateProjectToolWindow"] = sounds.stomp
            result["ActivateStructureToolWindow"] = sounds.stomp
            result["ActivateFindToolWindow"] = sounds.stomp
            result["ActivateChangesToolWindow"] = sounds.stomp
            result["ActivateRunToolWindow"] = sounds.stomp
            result["ActivateDebugToolWindow"] = sounds.stomp
            result["ActivateMessagesToolWindow"] = sounds.stomp
            result["ActivateFavoritesToolWindow"] = sounds.stomp
            result["AddToFavoritesPopup"] = sounds.stomp
            result["TypeHierarchy"] = sounds.stomp
            result["HideActiveWindow"] = sounds.stomp
            result["Vcs.QuickListPopupAction"] = sounds.stomp
            result["Vcs.ShowMessageHistory"] = sounds.stomp
            result["ChooseRunConfiguration"] = sounds.stomp
            result["ChooseDebugConfiguration"] = sounds.stomp
            return result
        }
    }

    init {
        soundsByAction = editorSounds(sounds)
        soundsByRefactoring = refactoringSounds(sounds)
    }
}