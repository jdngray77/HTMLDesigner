package com.jdngray77.htmldesigner

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import java.awt.Toolkit
import kotlin.reflect.KProperty1

/**
 * Container for generic utility methods to aid you in
 * doing whatever it is you do.
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









//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                        Logging
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

fun DeveloperWarning(string: String) {
    System.err.println(string)
}

fun UserWarning(string: String) {
    System.err.println(string)
    //TODO show in-editor
}

fun UserMessage(string: String) {
    // TODO show in-editor
    println(string)
}







//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                         FXML
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


/**
 * @param urlFromSrcRoot The url to the FXML relative to the root of the
 *        project sources.
 */
fun loadFXMLScene(urlFromSrcRoot: String, css : String = "blank.css") : Scene =
    Toolkit.getDefaultToolkit().screenSize.let {
        Scene(
            loadFXMLComponent<Parent>(urlFromSrcRoot).first,
            it.width.toDouble(),
            it.height.toDouble()
        ).also {
            val jMetro = JMetro(Style.DARK)
            jMetro.scene = it

        }

    }

/**
 * Loads an FXML document that contains something that isn't a whole scene,
 *
 * i.e something that goes inside of the editor like a custom list item or dock.
 */
fun <T : Parent> loadFXMLComponent(urlFromSrcRoot: String) =
    FXMLLoader(HTMLDesigner::class.java.getResource(urlFromSrcRoot)).let { loader ->
        loader.load<T>().let {
            Pair<T, Any>(it, loader.getController())
        }
    }

