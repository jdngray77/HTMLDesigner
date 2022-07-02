package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvcIfAvail
import com.sun.javafx.scene.control.skin.TreeTableViewSkin
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.text.Text
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import org.controlsfx.control.Notifications
import org.controlsfx.control.PopOver
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.awt.Toolkit
import java.io.*
import java.nio.file.Files
import java.util.*
import java.util.function.Consumer
import kotlin.reflect.KProperty
import kotlin.reflect.KMutableProperty1

/**
 * # Container for generic utility methods to aid you in doing whatever it is you do.
 */


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                                                       String
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * Asserts that the string ends with the [suffix].
 *
 * If it does, the string is returns unmodified, else the result is string + [suffix]
 **/
fun String.assertEndsWith(that: String) =
    if (this.endsWith(that)) this else this + that


/**
 * Converts a camel cased string to a sentence,
 * with spaces between capitlized words and
 * capitalizations on the first character.
 */
fun String.camelToSentence() : String =
    this
        .map { if (it.isUpperCase()) " " + it.lowercase() else it }
        .joinToString("")
        .capitaliseEveryWord()


/**
 * Splits string by spaces, then capitalises each substring.
 *
 * Returns the joining of each substring.
 */
fun String.capitaliseEveryWord() =
    this.split(" ").joinToString(" ") {
        // For some reason, [String.capitalize] says to use replace it with this. I hate it.
        it.replaceFirstChar { (if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString())}
    }

/**
 * Copies the string to the system clipboard.
 */
fun String.copyToClipboard() =
    CopyToClipboard(this)

/**
 * It takes a string, puts it on the clipboard, and returns nothing
 *
 * @param string The string to copy to the clipboard.
 */
fun CopyToClipboard(string: String) {
    Clipboard.getSystemClipboard().setContent(
        ClipboardContent().apply {
            putString(string)
        }
    )
}


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                       String
//region                                                        Collections
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * Modifies a mutable ArrayList to remove duplicate values
 *
 * Counter to distinct, this modified the original array.
 *
 * @return this
 */
// TODO test this.
fun ArrayList<*>.removeDuplicates(): ArrayList<*> {
    this.removeAll((this - this.distinct().toSet()).toSet())
    return this
}


//region FXML Trees


/**
 * A tree item which can display one thing, but store something else
 * for later retrieval.
 *
 * I.e it can store the underlying data of a tree item, but display a different string to the user.
 */
class StoringTreeItem <T> (val data: T?, titler : (T?) -> String) : TreeItem<T>(data)

/**
 * Applies [function] to every item in the tree
 * held by this [TreeItem].
 */
fun <T> TreeItem<T>.applyToAll(function: Consumer<TreeItem<T>>) {
    this.children.forEach {
        it.applyToAll(function)
    }
    function.accept(this)
}

/**
 * Resizes columns in a [TreeTableView] to fit the content within them.
 *
 * JavaFX does have a method for this, but it's not public
 * so this bodge uses reflection to access it.
 */
fun TreeTableView<*>.pack() {
    columns.forEach {
        it.pack(this)
    }
}

/**
 * Resizes a [TreeTableColumn] to fit the content within it.
 *
 * JavaFX does have a method for this, but it's not public
 * so this bodge uses reflection to access it.
 */
fun TreeTableColumn<*, *>.pack(table : TreeTableView<*>) {
    table.skin?.let {
        TreeTableViewSkin::class.java.getDeclaredMethod(
            "resizeColumnToFitContent",
            TreeTableColumn::class.java,
            Int::class.java
        ).apply {
            isAccessible = true
            invoke(it, this@pack, -1)
        }
    }
}

//endregion FXML Trees


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                     Collections
//region                                                            File
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * Returns true if a direct child of this directory
 * has the [_name].
 *
 * Cannot search subdirectories.
 *
 * Returns false if [this] is not a directory
 */
fun File.hasFile(_name : String) =
    this.isDirectory && this.listFiles { _, name -> name == _name }?.isNotEmpty() == true

/**
 * Returns true if the [path] relative to this directory exists.
 *
 * Can be a multi-level path pointing to any sub-directory or file
 * anywhere in the tree in this directory, as long as the [path] provided
 * is suitable to be appended to the location of [this].
 *
 * @return true if "$[this.path]/[path].exists()". Also false if [this] is not a directory.
 **/
fun File.hasChild(path: String) =
    this.isDirectory && File(this.toPath().toString() + "/" + path).exists()

/**
 * Ensures that the file exists on disk, weather it be a
 * file or a directory.
 *
 * @return true if file exists after operation.
 */
fun File.assertExists(): Boolean {
    if (isDirectory)
        mkdirs()

    if (isFile && !exists())
        createNewFile()
    return exists()
}


/**
 * Requires that the file exists on disk, but does not create it
 * if it doesn't.
 *
 * For automatic file creation,
 * @see File.assertExists
 *
 * @throws NoSuchFileException if it does not exist, but is supposed to.
 */
fun File.requireExists(): File {
    if (!exists())
        throw NoSuchFileException(this, reason = "project document is missing!")

    return this
}

/**
 * Returns a list of every file in the tree
 * of this directory, including sub-directories.
 *
 * Return list is empty if [this] is not a directory.
 */
fun File.flattenTree(): ArrayList<File> {
    val x = ArrayList<File>()
    if (!isDirectory) return x

    Files.walk(this.toPath())
        .filter(Files::isRegularFile)
        .forEach{ x.add(it.toFile()) }
    return x
}



//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                        File
//region                                                       Serialization
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * Saves [this] to the given path via Java's
 * object serialization.
 */
fun Serializable.saveObjectToDisk(path: String) =
    saveObjectToDisk(File(path))

/**
 * Saves [this] to the given file via Java's
 * object serialization.
 */
fun Serializable.saveObjectToDisk(file: File) {
    file.createNewFile()

    val fos = FileOutputStream(file)
    val os = ObjectOutputStream(fos)

    os.writeObject(this)

    os.close()
    fos.close()
}

/**
 * Loads an object serialized with Java's object serialization
 * from disk.
 */
fun loadObjectFromDisk(f: File): Any? {
    val fos = FileInputStream(f)
    val os = ObjectInputStream(fos)
    return os.readObject().also {
        os.close()
        fos.close()
    }
}


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                    Serialization
//region                                                           HTML
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * Writes the given jsoup element to the disk as HTML.
 *
 * Automatically asserts that the file exists.
 */
fun Element.saveToDisk(f: File) {
    f.assertExists()
    f.writeText(toString())
}

/**
 * Injects a child into [this]'s parent prior to [this].
 */
fun Element.injectSiblingBefore(element: Element) = injectRelativeSibling(element)

/**
 * Injects a child into [this]'s parent after to [this].
 */
fun Element.injectSiblingAfter(element: Element) = injectRelativeSibling(element, 1)

/**
 * It's a method that takes an element and inserts it into the parent of the element that called it.
 *
 * The sibling is injected at the offset, where 0 replaces this, and shifts the remainder right.
 *
 * Essentially, 0 = inject left, 1 = inject right.
 */
fun Element.injectRelativeSibling(element: Element, offset: Int = 0) {
    parent()?.let {
        it.insertChildren(it.childNodes().indexOf(this) + offset, element)
    }
}


/**
 * Shortcut to open a jsoup document in the editor.
 *
 * Creates or switches to an existing editor in the IDE that
 * holds this document.
 */
fun Document.open() =
    mvcIfAvail()?.openDocument(this)




//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                     HTML
//region                                                Logging & User messages
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



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
            mvcIfAvail()?.MainView?.textEditor_Open(
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
    mvcIfAvail()?.MainView?.setStatus(string)
}

/**
 * Prints [string] to the Standard output stream, and
 * also displays it in left of the status tray.
 *
 * Used to log internal status'.
 */
fun logStatus(string: String) {
    mvcIfAvail()?.MainView?.setStatus(string)
    println(string)
}

/**
 * A static shortcut to log the completion of a user-requested actions
 * in the right of the status tray.
 */
fun logAction(string: String) =
    mvcIfAvail()?.MainView?.setAction(string)

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


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                               logging
//region                                                   FXML
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


/**
 * Loads a FXML file as a scene, and initalises it's controllers and stylesheets.
 *
 * @param urlFromSrcRoot The url to the FXML relative to the root of the
 *        frontend package.
 *
 * @return Pair<Scene, Controller>
 */
fun loadFXMLScene(urlFromSrcRoot: String, css : String = "blank.css") : Pair<Scene, Any> =
    Toolkit.getDefaultToolkit().screenSize.let {
        val component = loadFXMLComponent<Parent>(urlFromSrcRoot)

        Pair<Scene, Any>(
            Scene(
                component.first,
                it.width.toDouble(),
                it.height.toDouble()
            ).also {
                val jMetro = JMetro(Style.DARK)
                jMetro.scene = it
            }
            ,
            component.second
        )

    }


/**
 * Loads an FXML document that contains something that isn't a whole scene,
 *
 * i.e something that goes inside of the editor like a custom list item or dock.
 */
fun <T : Parent> loadFXMLComponent(urlFromSrcRoot: String) =
    FXMLLoader(Editor::class.java.getResource(urlFromSrcRoot)).let { loader ->
        loader.load<T>().let {
            Pair<T, Any>(it, loader.getController())
        }
    }


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                 FXML
//region                                                    MISC
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * Uses Kotlin reflection to mutate a variable in an object.
 *
 * @param [prop] the property to mutate. KProperties can be obtained from the list of an object's properties - `X::class.memberProperties`
 */
fun <R, T> changeProperty(prop : KProperty<*>, instance: R, newValue: T) =
    (prop as KMutableProperty1<R,T>).set(instance, newValue)

