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

package com.jdngray77.htmldesigner.frontend.controls

import com.jdngray77.htmldesigner.backend.data.config.Registry
import com.jdngray77.htmldesigner.backend.userConfirm
import com.jdngray77.htmldesigner.backend.utility.getTheme
import com.jdngray77.htmldesigner.frontend.Editor
import javafx.beans.value.ObservableValue
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Stage
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import org.controlsfx.control.PropertySheet
import java.util.*

/**
 * @param T The type of key.
 */
class RegistryEditor<T>(
    val registry: Registry<T>
): PropertySheet() {

    init {
        mode = Mode.CATEGORY
        styleClass.clear() // Fix broken css.

        reload()
    }

    fun reload() {
        items.clear()

        registry.entries.forEach {
            items.add(
                RegistryEntry(it.key)
            )
        }
    }

    inner class RegistryEntry(
        val key: T
    ) : Item {
        override fun getType(): Class<*> =
            Registry.keyClass(key).java

        override fun getCategory(): String =
            Registry.keyType(key) ?: ""

        override fun getName(): String =
            key.toString()

        override fun getDescription() = ""

        override fun getValue() =
            registry[key]

        override fun setValue(value: Any) {
            registry[key] = value
        }

        override fun getObservableValue(): Optional<ObservableValue<out Any>> =
            Optional.empty()
    }



    fun showDialog() {
        Dialog<ButtonType>().apply {

            // Force light mode, dark mode fucks up the text boxes.
            // FIXME textboxes broken in dark mode. idky
            JMetro(Style.LIGHT).scene = dialogPane.scene

            title = "Registry Editor"
            headerText = "Warning - For advanced users only!"

//            (dialogPane.scene.window as Stage).isAlwaysOnTop = true

            dialogPane.content = BorderPane().apply {

                // Keep the dialog on top.


                top = Text("Changes here affect the IDE directly. Make sure you know what you're doing first.\n\n( This page is temporarily in light mode because of a bug. Sorry :(  )")
                center = this@RegistryEditor

                bottom = VBox(
                    Button("Reset defaults...").also {
                        it.setOnAction {
                            if (userConfirm("This will reset all settings to default.\nAre you sure?")) {
                                registry.reset()
                                reload()
                            }
                        }
                    },
                    Button("Quick restart").also {
                        it.setOnAction {
                            Editor.EDITOR.restart()
                            toFront()
                        }
                    },
                    Text("Most changes require a restart to take effect.")
                )
            }
            dialogPane.setPrefSize(800.0, 500.0)
            isResizable = true

            dialogPane.buttonTypes.addAll(ButtonType.OK)
        }.showAndWait()
    }

}