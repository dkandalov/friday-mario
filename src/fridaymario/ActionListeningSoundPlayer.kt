package fridaymario

import fridaymario.listeners.*
import fridaymario.sounds.Sound
import fridaymario.sounds.Sounds

class ActionListeningSoundPlayer(private val sounds: Sounds, private val listener: Listener):
    Compilation.Listener, Refactoring.Listener, UnitTests.Listener, VcsActions.Listener, AllActions.Listener {

    private val soundsByAction: Map<String, Sound> = editorSounds(sounds)
    private val soundsByRefactoring: Map<String, Sound> = refactoringSounds(sounds)
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

    override fun onUnitTestSucceeded() = sounds.oneUp.play()

    override fun onUnitTestFailed() = sounds.oneDown.play()

    override fun onVcsCommit() = sounds.powerupAppears.play()

    override fun onVcsUpdate() = sounds.powerup.play()

    override fun onVcsPush() = sounds.powerup.play()

    override fun onVcsPushFailed() = sounds.oneDown.play()

    interface Listener {
        fun unmappedAction(actionId: String)
        fun unmappedRefactoring(refactoringId: String)
    }

    companion object {
        private fun refactoringSounds(sounds: Sounds) = mapOf(
            "refactoring.rename" to sounds.coin,
            "refactoring.extractVariable" to sounds.coin,
            "refactoring.extract.method" to sounds.coin,
            "refactoring.inline.local.variable" to sounds.coin,
            "refactoring.safeDelete" to sounds.coin,
            "refactoring.introduceParameter" to sounds.coin
        )

        private fun editorSounds(sounds: Sounds): Map<String, Sound> {
            return mapOf(
                "EditorUp" to sounds.kick,
                "EditorDown" to sounds.kick,
                "EditorUpWithSelection" to sounds.kick,
                "EditorDownWithSelection" to sounds.kick,
                "EditorPreviousWord" to sounds.kick,
                "EditorNextWord" to sounds.kick,
                "EditorPreviousWordWithSelection" to sounds.kick,
                "EditorNextWordWithSelection" to sounds.kick,
                "EditorSelectWord" to sounds.kick,
                "EditorUnSelectWord" to sounds.kick,
                "\$SelectAll" to sounds.kick,
                "EditorLineStart" to sounds.jumpSmall,
                "EditorLineEnd" to sounds.jumpSmall,
                "EditorLineStartWithSelection" to sounds.jumpSmall,
                "EditorLineEndWithSelection" to sounds.jumpSmall,
                "EditorPageUp" to sounds.jumpSuper,
                "EditorPageDown" to sounds.jumpSuper,
                "GotoPreviousError" to sounds.jumpSuper,
                "GotoNextError" to sounds.jumpSuper,
                "FindNext" to sounds.jumpSuper,
                "FindPrevious" to sounds.jumpSuper,
                "MethodUp" to sounds.jumpSuper,
                "MethodDown" to sounds.jumpSuper,
                "Back" to sounds.jumpSuper,
                "Forward" to sounds.jumpSuper,
                "GotoSuperMethod" to sounds.jumpSuper,
                "GotoDeclaration" to sounds.jumpSuper,
                "GotoImplementation" to sounds.jumpSuper,
                "EditSource" to sounds.jumpSuper, // This is for F4 navigate action
                "EditorPaste" to sounds.fireball,
                "ReformatCode" to sounds.fireball,
                "EditorToggleCase" to sounds.fireball,
                "ExpandLiveTemplateByTab" to sounds.fireball,
                "EditorCompleteStatement" to sounds.fireball,
                "EditorChooseLookupItem" to sounds.fireball,
                "EditorChooseLookupItemReplace" to sounds.fireball,
                "HippieCompletion" to sounds.fireball,
                "HippieBackwardCompletion" to sounds.fireball,
                "MoveStatementUp" to sounds.fireball,
                "MoveStatementDown" to sounds.fireball,
                "EditorStartNewLineBefore" to sounds.fireball,
                "EditorStartNewLine" to sounds.fireball,
                "EditorDuplicate" to sounds.fireball,
                "EditorBackSpace" to sounds.breakblock,
                "EditorJoinLines" to sounds.breakblock,
                "EditorDelete" to sounds.breakblock,
                "EditorDeleteLine" to sounds.breakblock,
                "EditorDeleteToWordStart" to sounds.breakblock,
                "EditorDeleteToWordEnd" to sounds.breakblock,
                "CommentByLineComment" to sounds.breakblock,
                "CommentByBlockComment" to sounds.breakblock,
                "ToggleBookmark" to sounds.stomp,
                "ToggleBookmarkWithMnemonic" to sounds.stomp,
                "ToggleLineBreakpoint" to sounds.stomp,
                "HighlightUsagesInFile" to sounds.stomp,

                "NextTab" to sounds.jumpSuper,
                "PreviousTab" to sounds.jumpSuper,
                "CloseEditor" to sounds.fireworks,
                "CloseAllEditorsButActive" to sounds.fireworks,
                "\$Undo" to sounds.fireworks,
                "\$Redo" to sounds.fireworks,
                "ExpandAllRegions" to sounds.stomp,
                "CollapseAllRegions" to sounds.stomp,
                "ExpandRegion" to sounds.stomp,
                "CollapseRegion" to sounds.stomp,
                "CollapseSelection" to sounds.stomp,
                "PasteMultiple" to sounds.stomp,
                "FileStructurePopup" to sounds.stomp,
                "ShowBookmarks" to sounds.stomp,
                "ViewBreakpoints" to sounds.stomp,
                "QuickJavaDoc" to sounds.stomp,
                "ParameterInfo" to sounds.stomp,
                "ShowIntentionActions" to sounds.stomp,
                "EditorToggleColumnMode" to sounds.stomp,
                "SurroundWith" to sounds.stomp,
                "InsertLiveTemplate" to sounds.stomp,
                "SurroundWithLiveTemplate" to sounds.stomp,
                "NewElement" to sounds.stomp,
                "Generate" to sounds.stomp,
                "OverrideMethods" to sounds.stomp,
                "ImplementMethods" to sounds.stomp,

                "ChangeSignature" to sounds.stomp,
                "ExtractMethod" to sounds.stomp,
                "Inline" to sounds.stomp,
                "Move" to sounds.stomp,

                "Find" to sounds.stomp,
                "FindInPath" to sounds.stomp,
                "Replace" to sounds.stomp,
                "ReplaceInPath" to sounds.stomp,

                "ChangesView.Diff" to sounds.stomp,
                "CompareClipboardWithSelection" to sounds.stomp,

                "Switcher" to sounds.stomp,
                "RecentFiles" to sounds.stomp,
                "GotoClass" to sounds.stomp,
                "GotoFile" to sounds.stomp,
                "GotoSymbol" to sounds.stomp,
                "SearchEverywhere" to sounds.stomp,
                "GotoLine" to sounds.stomp,
                "ShowUsages" to sounds.stomp,
                "FindUsages" to sounds.stomp,
                "ShowNavBar" to sounds.stomp,
                "RunInspection" to sounds.stomp,

                "SelectIn" to sounds.stomp,
                "QuickChangeScheme" to sounds.stomp,
                "ActivateProjectToolWindow" to sounds.stomp,
                "ActivateStructureToolWindow" to sounds.stomp,
                "ActivateFindToolWindow" to sounds.stomp,
                "ActivateChangesToolWindow" to sounds.stomp,
                "ActivateRunToolWindow" to sounds.stomp,
                "ActivateDebugToolWindow" to sounds.stomp,
                "ActivateMessagesToolWindow" to sounds.stomp,
                "ActivateFavoritesToolWindow" to sounds.stomp,
                "AddToFavoritesPopup" to sounds.stomp,
                "TypeHierarchy" to sounds.stomp,
                "HideActiveWindow" to sounds.stomp,
                "Vcs.QuickListPopupAction" to sounds.stomp,
                "Vcs.ShowMessageHistory" to sounds.stomp,
                "ChooseRunConfiguration" to sounds.stomp,
                "ChooseDebugConfiguration" to sounds.stomp
            )
        }
    }
}