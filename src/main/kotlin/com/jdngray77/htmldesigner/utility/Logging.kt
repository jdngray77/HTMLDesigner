
/*░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
 ░                                                                                                ░
 ░ Jordan T. Gray's                                                                               ░
 ░                                                                                                ░
 ░          HTML Designer                                                                         ░
 ░                                                                                                ░
 ░ FOSS 2022.                                                                                     ░
 ░ License decision pending.                                                                      ░
 ░                                                                                                ░
 ░ https://www.github.com/jdngray77/HTMLDesigner/                                                 ░
 ░ https://www.jordantgray.uk                                                                     ░
 ░                                                                                                ░
 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.BackgroundTask.onUIThread
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.IDE
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvcIfAvail
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvcIsAvail
import com.jdngray77.htmldesigner.frontend.controls.RunAnything
import com.jdngray77.htmldesigner.frontend.controls.SearchableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.text.Text
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import org.controlsfx.control.NotificationPane
import org.controlsfx.control.Notifications
import org.controlsfx.control.PopOver
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Shows a floating notification in the lower right, displaying the [error]
 *
 * Has no effect if [Configs.SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL] is true
 */
fun showErrorNotification(error: Throwable, suppress: Boolean = Config[Configs.SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL] as Boolean ) {

    logStatus(
        (if (suppress) "(suppressed) " else "") +
                "Error: ${error.message} (${error.javaClass.simpleName})"
    )

    if (suppress)
        return

    onUIThread {
        Notifications.create()
            .title("An error has occurred with the editor.")
            .text(
                "${error::class.simpleName} \n ${
                    if (error.message != null)
                        error.message
                    else if (error.cause?.message != null)
                        error.cause!!.message
                    else
                        "No further explanation has been provided."
                }"
            )
            .onAction {
                IDE.mvcIfAvail()?.MainView?.textEditor_Open(
                    StringWriter().let {
                        error.printStackTrace(PrintWriter(it))
                        it.toString()
                    }

                )
            }
            .showWarning()
    }
}

fun showWarningNotification(title: String = "", message: String = "") {

    logStatus("\nWarning: $title - $message")


    onUIThread {
        Notifications.create()
            .title(title)
            .text(message)
            .onAction {
                showInformationalAlert("$title \n\n $message")
            }
            .showWarning()
    }

    logWarning("$title ; $message")
}

fun showNotification(title: String = "", message: String = "", onAction: (() -> Unit)? = null) {

    logStatus("Notification: $title - $message")

    onUIThread() {
        Notifications.create()
            .title(title)
            .text(message)
            .onAction {
                if(onAction != null)
                    onAction()
                else
                    showInformationalAlert("$title \n\n $message")
            }
            .showInformation()
    }
}

fun showListOfStrings(title: String = "", strings: Collection<String>) {
    onUIThread {
        Dialog<ButtonType>().apply {
            this.title = title
            dialogPane.content = SearchableList(strings)
            dialogPane.setPrefSize(800.0, 500.0)
            isResizable = true

            dialogPane.buttonTypes.addAll(ButtonType.CLOSE)

            JMetro(Style.LIGHT).scene = dialogPane.scene

            setOnShown {
                RunAnything.clearSearch()
                RunAnything.searchBox.requestFocus()
            }
            show()
        }
    }
}

/**
 * A utility to create a notification pane.
 *
 * It is not shown, so take the return value and place it within the GUI.
 */
fun NotificationPane(string: String, buttonText: String = "", showOnBottom: Boolean = false, runnable: Runnable? = null) =
        NotificationPane(
            BorderPane().also {

                it.left = HBox(Label(string)).also { it.alignment = Pos.CENTER }
                if (runnable != null)
                    it.right = Button(buttonText).also {
                        it.setOnAction {
                            runnable.run()
                        }
                    }
            }
        ).apply {
            padding = Insets(10.0)
            prefHeight = 48.0
            isShowFromTop = !showOnBottom
            show()
        }


/**
 * Prints [string] to the error output stream, and
 * also displays it in left of the status tray.
 */
fun logWarning(string: String) {
    logStatus(string)

    onUIThread {
        mvcIfAvail()?.MainView?.setStatus(string)
    }
}

/**
 * Displays the last action took by the IDE.
 *
 * This displays any status message that is not a direct result of the user's action.
 * @see setAction for logging actions that were directly caused by the user.
 *
 * i.e event notifications, backups, builds, etc.
 *
 * @returns The message that was displayed.
 */
fun logStatus(string: String) {
    println("IDE Status: $string")

    onUIThread {
        mvcIfAvail()?.MainView?.setStatus(string)
    }
}

/**
 * Displays the last action that the user performed.
 *
 * These are displayed in the right of the status tray.
 *
 * This is specific to actions performed by the user only.
 *
 * i.e document changed, saved, undo, redo, etc.
 *
 * @param action The action to display.
 */
fun setAction(string: String) {
    println("User Action : $string")

    onUIThread {
        mvcIfAvail()?.MainView?.setAction(string)
    }
}

/**
 * Shows an [Alert] with a message and an OK button.
 *
 * Returns once the user has dismissed it.
 */
fun showInformationalAlert(message: String) {
    logStatus("Info alert: $message")

    onUIThread {
        Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait()
    }
}

/**
 * Shows a distracting [Alert] with a error message and an OK button.
 *
 *
 * Returns once the user has dismissed it.
 */
@Deprecated("This is distracting and obnoxious, try to avoid annoying the user. if more suitable use ContextMessage(...)", ReplaceWith("ContextMessage (If more appropriate)"))
fun showErrorAlert(message: String) {
    logStatus("Error alert: $message")

    onUIThread {
        Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait()
    }
}

/**
 * Shows an unobtrusive pop-up message in context to some GUI element showing a message.
 *
 * Use to show a brief message, i.e the user tries something wrong. Use this to guide them
 * in the right direction.
 *
 *
 * This does not require user action or dismissal.
 *
 * Automatically closes.
 */
fun ContextMessage(contextGUIObject: Node, message: String, arrowLocation: PopOver.ArrowLocation = PopOver.ArrowLocation.TOP_CENTER) {
    onUIThread {
        PopOver().apply {
            isAutoHide = true
            isAutoFix = true // automatically fix location, i.e if it goes off screen.
            isHideOnEscape = true

            isDetachable = false
            isDetached = false


            contentNode = Text(message)
            this.arrowLocation = arrowLocation
            show(contextGUIObject)
            (contentNode as Text).fill = javafx.scene.paint.Color.BLACK
        }
    }
}

/**
 * Asks the user to confirm an action.
 * Displays a dialog requiring user's attention and response.
 *
 * returns after the user has responded.
 *
 * @returns true if user confirms action, else false.
 */
fun userConfirm(message : String) = userConfirm(message, ButtonType.NO, ButtonType.YES) == ButtonType.YES

/**
 * Asks the user to confirm an action.
 * Displays a dialog requiring user's attention and response.
 *
 * returns after the user has responded.
 *
 * Accepts custom button types
 * @returns The type of the button that the user clicked.
 */
fun userConfirm(message : String, vararg buttonType: ButtonType) = Alert(
    Alert.AlertType.CONFIRMATION,
    message,
    *buttonType
).let {
    logStatus("Confirming with user: $message")

    it.showAndWait()

    logStatus("Confirmed with user (${it.result.text}): $message")

    return@let it.result
}

/**
 *
 * @param inputValidator Optional input tester. Return an error message if you don't like what the user typed in, else null if OK.
 */
fun userInput(message: String, inputValidator: ((String) -> String?)? = null): String {
    logStatus("Prompting user for input: $message")

    TextInputDialog().apply {
        headerText = message
        if (inputValidator == null)
            return showAndWait().get()
        else {
            while (true) {
                val result = showAndWait().get()
                inputValidator(result)?.let {
                    showErrorAlert(it)
                } ?: run {
                    logStatus("User provided input ($result): $message")
                    return result
                }
            }
        }
    }
}

/**
 * A complete log of all err and out text, obtained via
 * the [SystemOutCapturer]
 */
val IDE_LOG = StringBuilder()


/**
 * A wrapper for a given steam that sends a copy of the stream to a string builder.
 *
 * The original stream still operates as before, the wrapper just takes a live copy.
 */
class SystemOutCapturer(

    /**
     * The stream that will be re-directed
     */
    val stream: PrintStream,

    /**
     * The builder that the output will be re-directed to.
     *
     * Holds the entire log of the stream.
     */
    val builder: StringBuilder

): PrintStream(ByteArrayOutputStream()) {

    companion object {

        /**
         * Wrapper for the error output that re-directs a copy of the output to the IDE_LOG.
         */
        val wrappedErr = SystemOutCapturer(System.err, IDE_LOG).also {
            System.setErr(it)
        }

        /**
         * Wrapper for the standard output that re-directs a copy of the output to the IDE_LOG.
         */
        val wrappedOut = SystemOutCapturer(System.out, IDE_LOG).also {
            System.setOut(it)
        }

        /**
         * Configures or restarts log capturing
         * for the standard output and error output.
         */
        fun init () {

            // MVC is only available if editor is restarting.
            if (mvcIsAvail())
                println("\n\n\n\nSOFT RESTART\n=======================\n")
            else
                println("\n\n\n\nBEGIN SESSION LOG\n=======================\n")

            wrappedErr.builder.clear()
            wrappedOut.builder.clear()
        }

        fun timestamp() = "[${SimpleDateFormat("HH:mm:ss").format(Date())}] "

    }

    /**
     * @return the entire history of the stream.
     */
    override fun toString(): String = builder.toString()

    private fun onPrint(a: Any) {
        val b = timestamp() + a
        stream.print(b)
        builder.append(b)
        EventNotifier.notifyEvent(EventType.LOG)
    }

    private fun onPrintLn(b: Any) {
        val a = timestamp() + b
        stream.println(a)
        builder.appendLine(a)
        EventNotifier.notifyEvent(EventType.LOG)
    }

    override fun print(b: Boolean) = onPrint(b)
    override fun print(c: Char) = onPrint(c)
    override fun print(i: Int) = onPrint(i)
    override fun print(l: Long) = onPrint(l)
    override fun print(f: Float) = onPrint(f)
    override fun print(d: Double) = onPrint(d)
    override fun print(s: CharArray) = onPrint(s)
    override fun print(s: String) = onPrint(s)
    override fun print(obj: Any) = onPrint(obj)
    override fun println() = onPrintLn("")

    override fun println(x: Boolean) = onPrintLn(x)
    override fun println(x: Char) = onPrintLn(x)
    override fun println(x: Int) = onPrintLn(x)
    override fun println(x: Long) = onPrintLn(x)
    override fun println(x: Float) = onPrintLn(x)
    override fun println(x: Double) = onPrintLn(x)
    override fun println(x: CharArray) = onPrintLn(x)
    override fun println(x: String) = onPrintLn(x)
    override fun println(x: Any) = onPrintLn(x)
}