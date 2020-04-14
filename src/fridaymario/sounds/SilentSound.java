package fridaymario.sounds;

public class SilentSound extends Sound {
	private final String soundName;
	private final Listener listener;

	public SilentSound(byte[] bytes, String soundName, Listener listener) {
		super(bytes, soundName);
		this.soundName = soundName;
		this.listener = listener;
	}

	@Override public void play() {
		listener.playing(soundName);
	}

	@Override public void playAndWait() {
		listener.playing(soundName);
	}

	@Override public void playInBackground() {
		listener.playing(soundName);
	}

	@Override public void stop() {
		listener.stopped("stopped: " + soundName);
	}

	@Override public String toString() {
		return "SilentLogSound{name='" + soundName + '\'' + '}';
	}

	public interface Listener {
		Listener none = new Listener() {
			@Override public void playing(String soundName) {}
			@Override public void stopped(String soundName) {}
		};

		void playing(String soundName);
		void stopped(String soundName);
	}
}
