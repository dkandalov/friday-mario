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
		oneUp = load.fun("/fridaymario/sounds/smb_1-up.au");
		oneDown = load.fun("/fridaymario/sounds/smb_pipe.au");
		coin = load.fun("/fridaymario/sounds/smb_coin.au");
		bowserfalls = load.fun("/fridaymario/sounds/smb_bowserfalls.au");
		breakblock = load.fun("/fridaymario/sounds/smb_breakblock.au");
		fireball = load.fun("/fridaymario/sounds/smb_fireball.au");
		fireworks = load.fun("/fridaymario/sounds/smb_fireworks.au");
		gameover = load.fun("/fridaymario/sounds/smb_gameover.au");
		jumpSmall = load.fun("/fridaymario/sounds/smb_jump-small.au");
		jumpSuper = load.fun("/fridaymario/sounds/smb_jump-super.au");
		kick = load.fun("/fridaymario/sounds/smb_kick.au");
		stomp = load.fun("/fridaymario/sounds/smb_stomp.au");
		powerup = load.fun("/fridaymario/sounds/smb_powerup.au");
		powerupAppears = load.fun("/fridaymario/sounds/smb_powerup_appears.au");

		marioSong = load.fun("/fridaymario/sounds/mario_08.au");
		zeldaSong = load.fun("/fridaymario/sounds/zelda_04.au");
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

