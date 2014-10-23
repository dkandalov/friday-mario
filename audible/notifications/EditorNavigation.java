package audible.notifications;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import audible.LivePluginUtil;

import java.util.ArrayList;
import java.util.List;

public class EditorNavigation implements Restartable {
    private final List<String> actionsToWrap;
    private final Listener listener;

    public EditorNavigation(Listener listener) {
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
                    listener.onNavigationAction(actionId);
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
        void onNavigationAction(String actionId);
    }
}
