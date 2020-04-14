package fridaymario;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnusedDeclaration")
@State(name = "FridayMarioConfig", storages = {@Storage(file = "$APP_CONFIG$/friday_mario_config.xml")})
public class Settings implements PersistentStateComponent<Settings> {
	public Boolean pluginEnabled;
	public boolean actionSoundsEnabled = true;
	public boolean backgroundMusicEnabled = true;

	public static Settings getInstance() {
		return ServiceManager.getService(Settings.class);
	}

	@Nullable @Override public Settings getState() {
		return this;
	}

	@Override public void loadState(Settings state) {
		XmlSerializerUtil.copyBean(state, this);
		if (pluginEnabled != null) {
			actionSoundsEnabled = pluginEnabled;
			backgroundMusicEnabled = pluginEnabled;
			pluginEnabled = null;
		}
	}

	public boolean isPluginEnabled() {
		return actionSoundsEnabled || backgroundMusicEnabled;
	}

	public void setActionSoundsEnabled(boolean actionSoundsEnabled) {
		this.actionSoundsEnabled = actionSoundsEnabled;
	}

	public void setBackgroundMusicEnabled(boolean backgroundMusicEnabled) {
		this.backgroundMusicEnabled = backgroundMusicEnabled;
	}
}
