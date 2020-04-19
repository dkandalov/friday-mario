package fridaymario.listeners

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.compiler.CompilationStatusListener
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompilerTopics
import com.intellij.openapi.project.Project
import fridaymario.util.appMessageBus

class InitJavaCompilation {
    init {
        appMessageBus.connect().subscribe(AppLifecycleListener.TOPIC, object: AppLifecycleListener {
            override fun appFrameCreated(commandLineArgs: List<String>) {
                Compilation.factory = object: Compilation.Factory {
                    override fun create(project: Project, listener: Compilation.Listener) =
                        object: Compilation() {
                            override fun start(disposable: Disposable) {
                                project.messageBus.connect(disposable).subscribe(CompilerTopics.COMPILATION_STATUS, object: CompilationStatusListener {
                                    override fun compilationFinished(aborted: Boolean, errors: Int, warnings: Int, compileContext: CompileContext) {
                                        if (errors > 0) listener.compilationFailed()
                                        else listener.compilationSucceeded()
                                    }
                                })
                            }
                        }
                }
            }
        })
    }
}
