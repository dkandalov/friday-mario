package fridaymario.sounds;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;

public class SilentSound extends Sound {
	private final String name;

	public SilentSound(byte[] bytes, String name) {
		super(bytes, name);
		this.name = name;
	}

	@Override public Sound play() {
		show(name);
		return this;
	}

	@Override public Sound playAndWait() {
		show(name);
		return this;
	}

	@Override public Sound playInBackground() {
		show(name);
		return this;
	}

	@Override public Sound stop() {
		show("stopped: " + name);
		return this;
	}

	@Override public String toString() {
		return "SilentLogSound{name='" + name + '\'' + '}';
	}

	private static void show(String message) {
		String noTitle = "";
		Notification notification = new Notification("Friday Mario Silent Sound", noTitle, message, NotificationType.INFORMATION);
		ApplicationManager.getApplication().getMessageBus().syncPublisher(Notifications.TOPIC).notify(notification);
	}
}
