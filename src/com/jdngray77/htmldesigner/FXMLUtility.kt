package com.jdngray77.htmldesigner

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import java.awt.Toolkit


object FXMLUtility {

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

    fun <T : Parent> loadFXMLComponent(urlFromSrcRoot: String) =
        FXMLLoader(FXMLUtility::class.java.getResource(urlFromSrcRoot)).let { loader ->
            loader.load<T>().let {
                Pair<T, Any>(it, loader.getController())
            }
        }
}