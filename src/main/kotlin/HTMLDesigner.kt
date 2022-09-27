import com.jdngray77.htmldesigner.backend.ExceptionListener
import com.jdngray77.htmldesigner.frontend.IDE.Companion.TESTING
import com.jdngray77.htmldesigner.frontend.splash.SplashScreen
import javafx.application.Application

/**
 * Entry point with command line arguments.
 *
 *  - `-tm` : test mode. Raises the [TESTING] flag.
 */
fun main(args: Array<String>) {
    if (args.getOrNull(0) == "-tm") {
        testMain()
    } else
        main()
}

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
 * An alternate main method that is used to start the editor at runtime in testing mode.
 *
 * The test helper uses this method to start the editor.
 *
 * This could be used to tell that when the editor has been started for testing,
 * as opposed to booted by a user.
 */
fun testMain() {
    TESTING = true
    main()
}

enum class ExitCodes {
    OK,
    ERROR,
    ERROR_NO_MVC,
}