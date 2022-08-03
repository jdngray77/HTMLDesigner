import com.jdngray77.htmldesigner.backend.ExceptionListener
import com.jdngray77.htmldesigner.frontend.SplashScreen
import javafx.application.Application

fun main() {
    Thread.setDefaultUncaughtExceptionHandler(ExceptionListener)
    Application.launch(SplashScreen::class.java)
}

enum class ExitCodes {
    OK,
    ERROR,
    ERROR_NO_MVC,
}