
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

import com.jdngray77.htmldesigner.frontend.Editor
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.text.Text
import org.controlsfx.control.Notifications
import org.controlsfx.control.PopOver
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Shows a floating notification in the lower right, displaying the [error]
 */
fun showErrorNotification(error: Throwable) {
    Notifications.create()
        .title("An error has occurred with the editor.")
        .text("${error::class.simpleName} \n ${
                if (error.message != null)
                    error.message 
                else if (error.cause?.message != null)
                    error.cause!!.message
                else    
                    "No further explanation has been provided."
                    }")
        .onAction {
            Editor.mvcIfAvail()?.MainView?.textEditor_Open(
                StringWriter().let {
                    error.printStackTrace(PrintWriter(it))
                    it.toString()
                }

            )
        }
        .showWarning()
}

/**
 * Prints [string] to the error output stream, and
 * also displays it in left of the status tray.
 */
fun logWarning(string: String) {
    System.err.println(string)
    Editor.mvcIfAvail()?.MainView?.setStatus(string)
}

/**
 * Prints [string] to the Standard output stream, and
 * also displays it in left of the status tray.
 *
 * Used to log internal status'.
 */
fun logStatus(string: String) {
    Editor.mvcIfAvail()?.MainView?.setStatus(string)
    println(string)
}

/**
 * A static shortcut to log the completion of a user-requested actions
 * in the right of the status tray.
 */
fun logAction(string: String) =
    Editor.mvcIfAvail()?.MainView?.setAction(string)

/**
 * Shows an [Alert] with a message and an OK button.
 *
 * Returns once the user has dismissed it.
 */
fun showInformationalAlert(message: String) =
    Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait()

/**
 * Shows a distracting [Alert] with a error message and an OK button.
 *
 *
 * Returns once the user has dismissed it.
 */
@Deprecated("This is distracting and obnoxious, try to avoid annoying the user. if more suitable use ContextMessage(...)", ReplaceWith("ContextMessage (If more appropriate)"))
fun showErrorAlert(message: String) =
    Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait()

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
    it.showAndWait()
    return@let it.result
}