package audible.idelisteners;

import com.intellij.openapi.compiler.CompilationStatusAdapter;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;

public class Compilation implements Restartable {
    private final Project project;
    private final CompilationStatusAdapter compilationListener;

    public Compilation(Project project, final Listener listener) {
        this.project = project;
        this.compilationListener = new CompilationStatusAdapter() {
            @Override
            public void compilationFinished(boolean aborted, int errors, int warnings, CompileContext compileContext) {
                if (errors > 0) {
                    listener.compilationFailed();
                } else {
                    listener.compilationSucceeded();
                }
            }
        };
    }

    @Override
    public void start() {
        CompilerManager.getInstance(project).addCompilationStatusListener(compilationListener);
    }

    @Override
    public void stop() {
        CompilerManager.getInstance(project).removeCompilationStatusListener(compilationListener);
    }

    public interface Listener {
        void compilationSucceeded();
        void compilationFailed();
    }
}
