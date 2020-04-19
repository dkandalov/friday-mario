package fridaymario.listeners;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;

public abstract class Compilation implements Restartable {
	@Override public void start(Disposable disposable) {
	}

	public static Factory factory = (project, listener) -> new Compilation() {
	};

	public interface Factory {
		Compilation create(Project project, Listener listener);
	}

	public interface Listener {
		void compilationSucceeded();

		void compilationFailed();
	}
}
