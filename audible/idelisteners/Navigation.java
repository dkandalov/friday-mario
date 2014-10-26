package audible.idelisteners;

import audible.ActionWrapper;

import java.util.ArrayList;
import java.util.List;

import static audible.ActionWrapper.unwrapAction;
import static audible.ActionWrapper.wrapAction;

public class Navigation implements Restartable {
    private final List<String> actionsToWrap;
    private final Listener listener;

    public Navigation(Listener listener) {
        this.listener = listener;

        actionsToWrap = new ArrayList<String>();

        actionsToWrap.add("EditorUp");
        actionsToWrap.add("EditorDown");
        actionsToWrap.add("EditorPreviousWord");
        actionsToWrap.add("EditorNextWord");
        actionsToWrap.add("EditorPreviousWordWithSelection");
        actionsToWrap.add("EditorNextWordWithSelection");

        actionsToWrap.add("EditorLineStart");
        actionsToWrap.add("EditorLineEnd");
        actionsToWrap.add("EditorLineStartWithSelection");
        actionsToWrap.add("EditorLineEndWithSelection");

        actionsToWrap.add("EditorPageUp");
        actionsToWrap.add("EditorPageDown");

        actionsToWrap.add("NextTab");
        actionsToWrap.add("PreviousTab");
        actionsToWrap.add("CloseActiveTab"); // TODO doesn't work?
    }

    @Override
    public void start() {
        for (final String actionId : actionsToWrap) {
            wrapAction(actionId, new ActionWrapper.Listener() {
                @Override
                public void beforeAction() {
                    listener.onEditorNavigation(actionId);
                }
            });
        }
    }

    @Override
    public void stop() {
        for (String actionId : actionsToWrap) {
            unwrapAction(actionId);
        }
    }

    public interface Listener {
        void onEditorNavigation(String actionId);
    }
}
