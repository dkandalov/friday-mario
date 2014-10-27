import fridaymario.ActionWrapper
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.editor.actionSystem.EditorAction
import liveplugin.testrunner.IntegrationTestsRunner
import org.junit.Test

import static fridaymario.ActionWrapper.unwrapAction
import static fridaymario.ActionWrapper.wrapAction

// add-to-classpath $PLUGIN_PATH/out/artifacts/audible_actions/audible-actions.jar

IntegrationTestsRunner.runIntegrationTests([ActionWrapperTest], project, pluginPath)

class ActionWrapperTest {
	@Test void "can wrap and unwrap actions"() {
		unwrapAction("EditorDown")
		assert !isWrapped("EditorDown")

		wrapAction("EditorDown", new ActionWrapper.Listener() {
			@Override void beforeAction() {}
		})
		assert isWrapped("EditorDown")

		unwrapAction("EditorDown")
		assert !isWrapped("EditorDown")
	}

	private static boolean isWrapped(String actionId) {
		def actionClassName = ActionManager.instance.getAction(actionId).class.name
		def actionHandlerClassName = ((EditorAction) ActionManager.instance.getAction(actionId)).handler.class.name
		actionClassName.endsWith("WrappedEditorAction") && actionHandlerClassName.contains("WrappedEditorAction")
	}
}
