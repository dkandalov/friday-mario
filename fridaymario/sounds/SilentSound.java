package fridaymario.sounds;

public class SilentSound extends Sound {
	private final String soundName;
	private final Listener listener;

	public SilentSound(byte[] bytes, String soundName, Listener listener) {
		super(bytes, soundName);
		this.soundName = soundName;
		this.listener = listener;
	}

	@Override public Sound play() {
		listener.playing(soundName);
		return this;
	}

	@Override public Sound playAndWait() {
		listener.playing(soundName);
		return this;
	}

	@Override public Sound playInBackground() {
		listener.playing(soundName);
		return this;
	}

	@Override public Sound stop() {
		listener.stopped("stopped: " + soundName);
		return this;
	}

	@Override public String toString() {
		return "SilentLogSound{name='" + soundName + '\'' + '}';
	}

	public interface Listener {
		void playing(String soundName);
		void stopped(String soundName);
	}
}
