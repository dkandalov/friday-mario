package fridaymario.sounds;

import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.util.Function;

import java.io.IOException;
import java.io.InputStream;

public class Sounds {
	public final Sound oneUp;
	public final Sound oneDown;
	public final Sound coin;
	public final Sound bowserfalls;
	public final Sound breakblock;
	public final Sound fireball;
	public final Sound fireworks;
	public final Sound gameover;
	public final Sound jumpSmall;
	public final Sound jumpSuper;
	public final Sound kick;
	public final Sound stomp;
	public final Sound powerupAppears;
	public final Sound powerup;

	public final Sound marioSong;
	public final Sound zeldaSong;

	public static Sounds create(final boolean actionSoundsEnabled, final boolean backgroundMusicEnabled) {
		return new Sounds(new Function<Config, Sound>() {
			@Override public Sound fun(Config config) {
				boolean enabled = (config.isBackgroundMusic && backgroundMusicEnabled) || (!config.isBackgroundMusic && actionSoundsEnabled);
				return enabled ?
						new Sound(loadBytes(config.filePath), config.filePath) :
						new SilentSound(loadBytes(config.filePath), config.filePath, SilentSound.Listener.none);
			}
		});
	}

	public static Sounds createSilent(final SilentSound.Listener listener) {
		return new Sounds(new Function<Config, Sound>() {
			@Override public Sound fun(Config config) {
				return new SilentSound(loadBytes(config.filePath), config.filePath, listener);
			}
		});
	}

	private Sounds(Function<Config, Sound> load) {
		oneUp = load.fun(new Config("/fridaymario/sounds/smb_1-up.au"));
		oneDown = load.fun(new Config("/fridaymario/sounds/smb_pipe.au"));
		coin = load.fun(new Config("/fridaymario/sounds/smb_coin.au"));
		bowserfalls = load.fun(new Config("/fridaymario/sounds/smb_bowserfalls.au"));
		breakblock = load.fun(new Config("/fridaymario/sounds/smb_breakblock.au"));
		fireball = load.fun(new Config("/fridaymario/sounds/smb_fireball.au"));
		fireworks = load.fun(new Config("/fridaymario/sounds/smb_fireworks.au"));
		gameover = load.fun(new Config("/fridaymario/sounds/smb_gameover.au", true));
		jumpSmall = load.fun(new Config("/fridaymario/sounds/smb_jump-small.au"));
		jumpSuper = load.fun(new Config("/fridaymario/sounds/smb_jump-super.au"));
		kick = load.fun(new Config("/fridaymario/sounds/smb_kick.au"));
		stomp = load.fun(new Config("/fridaymario/sounds/smb_stomp.au"));
		powerup = load.fun(new Config("/fridaymario/sounds/smb_powerup.au"));
		powerupAppears = load.fun(new Config("/fridaymario/sounds/smb_powerup_appears.au"));
		marioSong = load.fun(new Config("/fridaymario/sounds/mario_08.au", true));
		zeldaSong = load.fun(new Config("/fridaymario/sounds/zelda_04.au", true));
	}

	private static byte[] loadBytes(String fileName) {
		try {
			InputStream inputStream = Sounds.class.getResourceAsStream(fileName);
			if (inputStream == null) throw new RuntimeException("Cannot find " + fileName);
			return StreamUtil.loadFromStream(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static class Config {
		public final String filePath;
		public final boolean isBackgroundMusic;

		public Config(String filePath) {
			this(filePath, false);
		}

		public Config(String filePath, boolean isBackgroundMusic) {
			this.filePath = filePath;
			this.isBackgroundMusic = isBackgroundMusic;
		}
	}
}

