import com.jdngray77.htmldesigner.backend.ExceptionListener;
import com.jdngray77.htmldesigner.frontend.Editor;
import javafx.application.Application;

fun main() {
    Thread.setDefaultUncaughtExceptionHandler(ExceptionListener)
    Application.launch(Editor::class.java)
}
