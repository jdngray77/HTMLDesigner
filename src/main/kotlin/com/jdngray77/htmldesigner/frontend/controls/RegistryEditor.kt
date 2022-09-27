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
import com.jdngray77.htmldesigner.frontend.IDE
import javafx.beans.value.ObservableValue
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import org.controlsfx.control.PropertySheet
import java.util.*

/**
 * A property sheet control for displaying and
 * editing a [registry].
 *
 *
 * Can be used as a control anywhere in the GUI as a stand-alone sheet,
 * or displayed as a pop-up dialog with [showDialog].
 *
 * Side note : For now, dialogs are forced to light mode.
 *             Dark mode caused issues with the rendering of text boxes.
 *             I'm not sure why.
 *
 * Changes made by the user are instantly and automatically
 * saved to the registry, and to disk.
 *
 * This can be prevented by setting the registry's [Registry.autosave]
 * flag low.
 *
 * @param T The type of key, typically an enum.
 */
class RegistryEditor<T>(

    /**
     * The registry to edit and display.
     */
    val registry: Registry<T>


): PropertySheet() {

    init {
        mode = Mode.CATEGORY
        styleClass.clear() // Fix broken css.

        refreshEditor()
    }

    fun refreshEditor() {
        // FIXME when sorted by category, this clears the selected category.

        items.clear()

        registry.entries.forEach {
            items.add(
                RegistryEntry(it.key)
            )
        }
    }

    /**
     * A Property sheet item representing an entry within the
     * [registry].
     */
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

    /**
     * Convenience method to create and display this registry editor
     * into a dialog, so you don't have to faf around with placing it into
     * a ui, or make your own.
     */
    fun showDialog() {
        Dialog<ButtonType>().apply {

            // Force light mode, dark mode fucks up the text boxes.
            // FIXME textboxes broken in dark mode. idky
            JMetro(Style.LIGHT).scene = dialogPane.scene

            title = "Registry Editor"
            headerText = "Warning - For advanced users only!"

            dialogPane.content = BorderPane().apply {


                top = Text("Changes here affect the IDE directly. Make sure you know what you're doing first.\n\n( This page is temporarily in light mode because of a bug. Sorry :(  )")
                center = this@RegistryEditor

                bottom = VBox(
                    Button("Reset defaults...").also {
                        it.setOnAction {
                            if (userConfirm("This will reset all settings to default.\nAre you sure?")) {
                                registry.reset()
                                refreshEditor()
                            }
                        }
                    },
                    Button("Quick restart").also {
                        it.setOnAction {
                            IDE.EDITOR.restart() // This waits for the entire restart, including project selection.

                            toFront()       // Once the ide has loaded, bring the dialog back into focus.

                            refreshEditor() // Update the registry editor. The IDE may have made changes during the restart.
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