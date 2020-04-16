package fridaymario.listeners;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.compiler.CompilationStatusListener;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InitJavaCompilation implements AppLifecycleListener {
	@Override public void appFrameCreated(@NotNull List<String> commandLineArgs) {
		Compilation.factory = (project, listener) -> {
			CompilationStatusListener compilationListener = new CompilationStatusListener() {
				@Override
				public void compilationFinished(boolean aborted, int errors, int warnings, @NotNull CompileContext compileContext) {
					if (errors > 0) {
						listener.compilationFailed();
					} else {
						listener.compilationSucceeded();
					}
				}
			};
			return new Compilation() {
				@Override public void start() {
					CompilerManager.getInstance(project).addCompilationStatusListener(compilationListener);
				}

				@Override public void stop() {
					CompilerManager.getInstance(project).removeCompilationStatusListener(compilationListener);
				}
			};
		};
	}
}
