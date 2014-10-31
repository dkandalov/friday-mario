package fridaymario;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnusedDeclaration")
@State(name = "FridayMarioConfig", storages = {@Storage(id = "main", file = "$APP_CONFIG$/friday_mario_config.xml")})
public class Settings implements PersistentStateComponent<Settings> {
	public boolean pluginEnabled = true;

	public static Settings getInstance() {
		return ServiceManager.getService(Settings.class);
	}

	@Nullable @Override public Settings getState() {
		return this;
	}

	@Override public void loadState(Settings state) {
		XmlSerializerUtil.copyBean(state, this);
	}

	public boolean isPluginEnabled() {
		return pluginEnabled;
	}

	public void setPluginEnabled(boolean pluginEnabled) {
		this.pluginEnabled = pluginEnabled;
	}
}
