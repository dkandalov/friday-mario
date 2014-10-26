package audible;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.DocCommandGroupId;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.NotNull;

public class ActionWrapper {
    private static final Logger LOG = Logger.getInstance(ActionWrapper.class);

    public static AnAction wrapAction(String actionId, final Listener listener) {
        ActionManager actionManager = ActionManager.getInstance();
        final AnAction action = actionManager.getAction(actionId);
        if (action == null) {
            LOG.warn("Couldn't wrap action " + actionId + " because it was not found");
            return null;
        }

        AnAction newAction;
        if (action instanceof EditorAction) {
            newAction = new WrappedEditorAction(listener, (EditorAction) action);
        } else {
            newAction = new WrappedAction(listener, action);
        }
        newAction.getTemplatePresentation().setText(action.getTemplatePresentation().getText());
        newAction.getTemplatePresentation().setIcon(action.getTemplatePresentation().getIcon());
        newAction.getTemplatePresentation().setHoveredIcon(action.getTemplatePresentation().getHoveredIcon());
        newAction.getTemplatePresentation().setDescription(action.getTemplatePresentation().getDescription());
        newAction.copyShortcutFrom(action);

        actionManager.unregisterAction(actionId);
        actionManager.registerAction(actionId, newAction);
        LOG.info("Wrapped action " + actionId);

        return newAction;
    }

    public static void unwrapAction(String actionId) {
        ActionManager actionManager = ActionManager.getInstance();
        AnAction wrappedAction = actionManager.getAction(actionId);
        if (wrappedAction == null) {
            LOG.warn("Couldn't unwrap action "  + actionId + " because it was not found");
            return;
        }
        if (wrappedAction instanceof DelegatesToAction) {
            actionManager.unregisterAction(actionId);
            actionManager.registerAction(actionId, ((DelegatesToAction) wrappedAction).originalAction());
            LOG.info("Unwrapped action " + actionId);
        } else {
            LOG.warn("Action " + actionId + " is not wrapped");
        }
    }


    public interface Listener {
        void beforeAction();
    }


    private static interface DelegatesToAction {
        AnAction originalAction();
    }


    private static class WrappedAction extends AnAction implements DelegatesToAction {
        private final Listener listener;
        public final AnAction originalAction;

        public WrappedAction(Listener listener, AnAction originalAction) {
            this.listener = listener;
            this.originalAction = originalAction;
        }

        @Override public void actionPerformed(@NotNull AnActionEvent event) {
            listener.beforeAction();
            originalAction.actionPerformed(event);
        }

        @Override public void update(@NotNull AnActionEvent event) {
            originalAction.update(event);
        }

        @Override
        public AnAction originalAction() {
            return originalAction;
        }
    }


    private static class WrappedEditorAction extends EditorAction implements DelegatesToAction {
        private final EditorAction originalAction;

        protected WrappedEditorAction(final Listener listener, final EditorAction originalAction) {
            super(new EditorActionHandler() {
                @Override
                protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {
                    listener.beforeAction();
                    originalAction.getHandler().execute(editor, caret, dataContext);
                }

                @Override
                protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
                    return originalAction.getHandler().isEnabled(editor, caret, dataContext);
                }

                @Override
                public DocCommandGroupId getCommandGroupId(Editor editor) {
                    return originalAction.getHandler().getCommandGroupId(editor);
                }

                @Override
                public boolean runForAllCarets() {
                    return originalAction.getHandler().runForAllCarets();
                }

                @Override
                public boolean executeInCommand(Editor editor, DataContext dataContext) {
                    return originalAction.getHandler().executeInCommand(editor, dataContext);
                }
            });
            this.originalAction = originalAction;
        }

        @Override public void update(@NotNull AnActionEvent event) {
            originalAction.update(event);
        }

        @Override
        public AnAction originalAction() {
            return originalAction;
        }
    }
}
