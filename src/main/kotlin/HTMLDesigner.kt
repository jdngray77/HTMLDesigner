import com.jdngray77.htmldesigner.backend.ExceptionListener
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.SplashScreen
import javafx.application.Application
import org.spdx.rdfparser.SPDXLicenseInfoFactory

fun main() {
    println("Starting HTML Designer")
    try {
        Thread.setDefaultUncaughtExceptionHandler(ExceptionListener)
        Application.launch(SplashScreen::class.java)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

/**
 * The test helper uses this method to start the editor.
 *
 * This could be used to tell that when the editor has been started for testing,
 * as opposed to booted by a user.
 */
fun testMain() {
    Editor.TESTING = true
    main()
}

enum class ExitCodes {
    OK,
    ERROR,
    ERROR_NO_MVC,
}