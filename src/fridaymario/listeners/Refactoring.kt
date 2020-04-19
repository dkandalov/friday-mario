package fridaymario.listeners

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.refactoring.listeners.RefactoringEventData
import com.intellij.refactoring.listeners.RefactoringEventListener

class Refactoring(private val project: Project, private val listener: Listener): Restartable {

    override fun start(disposable: Disposable) {
        project.messageBus.connect(disposable).subscribe(RefactoringEventListener.REFACTORING_EVENT_TOPIC, object: RefactoringEventListener {
            override fun refactoringDone(refactoringId: String, afterData: RefactoringEventData?) = listener.onRefactoring(refactoringId)
            override fun refactoringStarted(refactoringId: String, beforeData: RefactoringEventData?) {}
            override fun conflictsDetected(refactoringId: String, conflictsData: RefactoringEventData) {}
            override fun undoRefactoring(refactoringId: String) {}
        })
    }

    interface Listener {
        fun onRefactoring(refactoringId: String?)
    }
}