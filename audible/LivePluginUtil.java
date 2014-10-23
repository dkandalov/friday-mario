package audible;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public class LivePluginUtil {
    private static final Logger LOG = Logger.getInstance(LivePluginUtil.class);

    public static AnAction wrapAction(String actionId, final ActionWrapper actionWrapper) {
        ActionManager actionManager = ActionManager.getInstance();
        final AnAction action = actionManager.getAction(actionId);
        if (action == null) {
            LOG.warn("Couldn't wrap action '${actionId}' because it was not found");
            return null;
        }

        AnAction newAction = new WrappedAction(actionWrapper, action);
        newAction.getTemplatePresentation().setText(action.getTemplatePresentation().getText());
        newAction.getTemplatePresentation().setIcon(action.getTemplatePresentation().getIcon());
        newAction.getTemplatePresentation().setHoveredIcon(action.getTemplatePresentation().getHoveredIcon());
        newAction.getTemplatePresentation().setDescription(action.getTemplatePresentation().getDescription());
        newAction.copyShortcutFrom(action);

        actionManager.unregisterAction(actionId);
        actionManager.registerAction(actionId, newAction);
        LOG.info("Wrapped action '${actionId}'");

        return newAction;
    }

    public static AnAction unwrapAction(String actionId) {
        ActionManager actionManager = ActionManager.getInstance();
        AnAction wrappedAction = actionManager.getAction(actionId);
        if (wrappedAction == null) {
            LOG.warn("Couldn't unwrap action '${actionId}' because it was not found");
            return null;
        }
        if (!(wrappedAction instanceof WrappedAction)) {
            LOG.warn("Action '${actionId}' is not wrapped");
            return wrappedAction;
        }

        actionManager.unregisterAction(actionId);
        actionManager.registerAction(actionId, ((WrappedAction) wrappedAction).originalAction);
        LOG.info("Unwrapped action '${actionId}'");

        return ((WrappedAction) wrappedAction).originalAction;
    }

    public interface ActionWrapper {
        public void call(AnActionEvent event, AnAction action);
    }

    private static class WrappedAction extends AnAction {
        private final ActionWrapper actionWrapper;
        public final AnAction originalAction;

        public WrappedAction(ActionWrapper actionWrapper, AnAction originalAction) {
            this.actionWrapper = actionWrapper;
            this.originalAction = originalAction;
        }

        @Override public void actionPerformed(@NotNull AnActionEvent event) {
            actionWrapper.call(event, originalAction);
        }

        @Override public void update(@NotNull AnActionEvent event) {
            originalAction.update(event);
        }
    }
}
