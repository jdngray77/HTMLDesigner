

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

package com.jdngray77.htmldesigner.utility

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.Editor
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import java.awt.Toolkit

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

        Pair(
            Scene(
                component.first,
                it.width.toDouble(),
                it.height.toDouble()
            ).also {
                getTheme().scene = it
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
 * Returns a JMetro instance
 * in the correct color scheme.
 */
fun getTheme() = JMetro(
    if (Config[Configs.DARK_MODE_BOOL] as Boolean)
        Style.DARK
    else
        Style.LIGHT
)




// TODO Reminder for the future :
//      It is not possible to add extension properties
//      for companion objects in java classes, since java classes
//      don't java companions.
//      However, jetbrains are working on a solution.
//      If you're reading this in the future,
//      check if this works!
//      ```
//      val ButtonType.Companion.SAVE: ButtonType
//          get() = ButtonType("Save")
//      ```
//      TLDR
//      Since buttonType is java, we can't add static extension
//      properties.
//
//      ALSO this won't work if you have to use getters.
//           The instance returned must be exact, effectively final.

val ButtonType_SAVE: ButtonType = ButtonType("Save")

val ButtonType_CLOSEWITHOUTSAVE: ButtonType = ButtonType("Don't save")