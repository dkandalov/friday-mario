package audible.idelisteners;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import audible.LivePluginUtil;

import java.util.ArrayList;
import java.util.List;

public class Navigation implements Restartable {
    private final List<String> actionsToWrap;
    private final Listener listener;

    public Navigation(Listener listener) {
        this.listener = listener;

        actionsToWrap = new ArrayList<String>();
        actionsToWrap.add("EditorUp");
        actionsToWrap.add("EditorDown");
        actionsToWrap.add("EditorLeft");
        actionsToWrap.add("EditorRight");
        actionsToWrap.add("EditorUpWithSelection");
        actionsToWrap.add("EditorDownWithSelection");
        actionsToWrap.add("EditorLeftWithSelection");
        actionsToWrap.add("EditorRightWithSelection");

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
    }

    @Override
    public void start() {
        for (final String actionId : actionsToWrap) {
            LivePluginUtil.wrapAction(actionId, new LivePluginUtil.ActionWrapper() {
                @Override
                public void call(AnActionEvent event, AnAction action) {
                    listener.onEditorNavigation(actionId);
                    action.actionPerformed(event);
                }
            });
        }
    }

    @Override
    public void stop() {
        for (String actionId : actionsToWrap) {
            LivePluginUtil.unwrapAction(actionId);
        }
    }

    public interface Listener {
        void onEditorNavigation(String actionId);
    }
}
