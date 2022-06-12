package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvcIfAvail
import com.sun.javafx.scene.control.skin.TreeTableViewSkin
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import org.controlsfx.control.Notifications
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.awt.Toolkit
import java.io.*
import java.nio.file.Files
import java.util.function.Consumer
import kotlin.reflect.KProperty
import kotlin.reflect.KMutableProperty1

/**
 * # Container for generic utility methods to aid you in doing whatever it is you do.
 */


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                     Extension methods
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


fun String.AssertEndsWith(that: String) =
    if (this.endsWith(that)) this else this + that

// TODO test this.
fun ArrayList<*>.RemoveDuplicates() {
    this.removeAll((this - this.distinct().toSet()).toSet())
}

/**
 * Converts a camel cased string to a sentence,
 * with spaces between capitlized words and
 * capitalizations on the first character.
 */
fun String.CamelToSentence() : String =
    this
        .map { if (it.isUpperCase()) " " + it.lowercase() else it }
        .joinToString("")
        .capitalize()

fun File.hasFile(_name : String) =
    this.listFiles { _, name -> name == _name }?.isNotEmpty() == true

fun File.assertExists() {
    if (isDirectory)
        mkdirs()

    if (isFile && !exists())
        createNewFile()
}

fun File.hasChild(path: String) =
    this.isDirectory && File(this.toPath().toString() + "/" + path).exists()

fun File.listTree(): ArrayList<File> {
    val x = ArrayList<File>()

    Files.walk(this.toPath())
        .filter(Files::isRegularFile)
        .forEach{ x.add(it.toFile()) }

    return x
}



fun java.io.Serializable.saveObjectToDisk(f: String) =
    saveObjectToDisk(File(f))

fun java.io.Serializable.saveObjectToDisk(f: File) {
    f.createNewFile()

    val fos = FileOutputStream(f)
    val os = ObjectOutputStream(fos)

    os.writeObject(this)

    os.close()
    fos.close()
}

fun loadObjectFromDisk(f: File): Any? {
    val fos = FileInputStream(f)
    val os = ObjectInputStream(fos)

    val x = os.readObject()

    os.close()
    fos.close()

    return x
}


fun Element.saveToDisk(f: File) {
    f.assertExists()
    f.writeText(toString())
}










//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                        Logging
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

fun NotifyOfError(e: Throwable) {
    Notifications.create()
        .title("An error has occurred with the editor.")
        .text("${e::class.simpleName} \n ${
                if (e.message != null)
                    e.message 
                else if (e.cause?.message != null)
                    e.cause!!.message
                else    
                    "No further explanation has been provided."
                    }")
        .onAction {
            mvcIfAvail()?.MainView?.textEditor_Open(
                StringWriter().let {
                    e.printStackTrace(PrintWriter(it))
                    it.toString()
                }

            )
        }
        .showWarning()
}


fun logWarning(string: String) {
    System.err.println(string)
    mvcIfAvail()?.MainView?.setStatus(string)
}


fun logStatus(string: String) {
    mvcIfAvail()?.MainView?.setStatus(string)
    println(string)
}

fun AlertUser(message: String) {
    Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait()
}





//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                         FXML
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


/**
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

/**
 * Display a confirmation dialog to confirm or deny an action.
 *
 * Returns true if user confirms action, else false.
 */
fun userConfirm(message : String) = userConfirm(message, ButtonType.NO, ButtonType.YES) == ButtonType.YES

fun userConfirm(message : String, vararg buttonType: ButtonType) = Alert(
    Alert.AlertType.CONFIRMATION,
    message,
    *buttonType
).let {
    it.showAndWait()
    return@let it.result
}


fun <T> TreeItem<T>.applyToAll(e: Consumer<TreeItem<T>>) {
    this.children.forEach {
        it.applyToAll(e)
    }
    e.accept(this)
}


fun TreeTableView<*>.pack() {
    columns.forEach {
        it.pack(this)
    }
}

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


fun Document.open() {
    mvcIfAvail()?.openDocument(this)
}

fun CopyToClipboard(string: String) {
    Clipboard.getSystemClipboard().setContent(
        ClipboardContent().apply {
            putString(string)
        }
    )
}


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                    REFLECTION
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

fun <R, T> changeProperty(prop : KProperty<*    >, rec: R, value: T) {
    (prop as KMutableProperty1<R,T>).set(rec, value)
}





/**
 * A tree item which can display one thing, but store something else
 * for later retrieval.
 *
 * I.e it can store the underlying data of a tree item, but display a different string to the user.
 */
class StoringTreeItem <T> (val data
: T?, titler : (T?) -> String) : TreeItem<T>(data)