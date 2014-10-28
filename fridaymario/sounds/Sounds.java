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

	public static Sounds create() {
		return new Sounds(new Function<String, Sound>() {
			@Override public Sound fun(String fileName) {
				return new Sound(loadBytes(fileName), fileName);
			}
		});
	}

	public static Sounds createSilent(final SilentSound.Listener listener) {
		return new Sounds(new Function<String, Sound>() {
			@Override public Sound fun(String fileName) {
				return new SilentSound(loadBytes(fileName), fileName, listener);
			}
		});
	}

	private Sounds(Function<String, Sound> load) {
		oneUp = load.fun("/fridaymario/sounds/smb_1-up.wav");
		oneDown = load.fun("/fridaymario/sounds/smb_pipe.wav");
		coin = load.fun("/fridaymario/sounds/smb_coin.wav");
		bowserfalls = load.fun("/fridaymario/sounds/smb_bowserfalls.wav");
		breakblock = load.fun("/fridaymario/sounds/smb_breakblock.wav");
		fireball = load.fun("/fridaymario/sounds/smb_fireball.wav");
		fireworks = load.fun("/fridaymario/sounds/smb_fireworks.wav");
		gameover = load.fun("/fridaymario/sounds/smb_gameover.wav");
		jumpSmall = load.fun("/fridaymario/sounds/smb_jump-small.wav");
		jumpSuper = load.fun("/fridaymario/sounds/smb_jump-super.wav");
		kick = load.fun("/fridaymario/sounds/smb_kick.wav");
		stomp = load.fun("/fridaymario/sounds/smb_stomp.wav");
		powerup = load.fun("/fridaymario/sounds/smb_powerup.wav");
		powerupAppears = load.fun("/fridaymario/sounds/smb_powerup_appears.wav");

		marioSong = load.fun("/fridaymario/sounds/mario_08.wav");
		zeldaSong = load.fun("/fridaymario/sounds/zelda_04.wav");
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
}

