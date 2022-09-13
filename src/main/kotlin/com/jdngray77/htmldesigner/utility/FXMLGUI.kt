

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
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent.MOUSE_CLICKED
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import java.awt.Toolkit
import kotlin.math.roundToInt

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
 *
 * @param path Relative path to the FXML file to load.
 * @param pathRelativeTo The class that the path is relative to.
 * @param controller Optional. An existing controller instance to use. If not provided, once will be created using the FXML.
 */
fun <T : Parent> loadFXMLComponent(path: String, pathRelativeTo: Class<*> = Editor::class.java, controller: Any? = null) =
    FXMLLoader(pathRelativeTo.getResource(path)).let { loader ->
        controller?.let { loader.setController(it) }
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

/**
 * Generates a hex representation of the color.
 */
fun Color.toHex(): String {
    return String.format(null, "#%02x%02x%02x",
            (red * 255).roundToInt(),
            (green * 255).roundToInt(),
            (blue * 255).roundToInt()
        )
}

fun Node.growH() = HBox.setHgrow(this, Priority.ALWAYS)
fun Node.growV() = VBox.setVgrow(this, Priority.ALWAYS)
fun Node.grow() { growH(); growV() }

fun openURL(url: String) {
    java.awt.Desktop.getDesktop().browse(java.net.URI(url))
}

fun Control.setTooltip(text: String) {
    tooltip = Tooltip(text)
}
fun Node.setTooltip(text: String) =
    Tooltip(text).also {
        Tooltip.install(this, it)
    }

fun Node.setOnDoubleClick(r: Runnable) {
    addEventHandler(MOUSE_CLICKED) {
        if (it.button != MouseButton.PRIMARY)
            return@addEventHandler

        if (it.clickCount >= 2) {
            r.run()
        }
    }
}


/**
 * A helper class for the [menu] method.
 *
 * Creates menus with simple method calls, in a much more readable way.
 * // TODO usage example
 * @param menu The menu that this instance is editing.
 * @param parent The parent menu of this menu, if there is one.
 */
class IdiomaticMenuFactory(

    private val parent : IdiomaticMenuFactory? = null,

    private val menu: Menu

) {

    /**
     * Creates a menu within this menu.
     *
     * Further chained calls will apply to this sub-menu, until
     * [menuDone] is called, at which point the context
     * will drop back to [this] menu.
     *
     * @param text The text to display on the menu.
     * @param _onAction Optional. An action to perform when the menu is clicked.
     * @return this
     */
    fun subMenu(text: String, _onAction : EventHandler<ActionEvent>? = null): IdiomaticMenuFactory {
        return IdiomaticMenuFactory(
            this,
            Menu(text).let {
            // If there's an action, store add it to the menu.
            _onAction?.apply {it.onAction = this}

            // Add the menu to the context menu.
            menu.items.add(it)
            it
            }
        )
    }

    /**
     * Adds a new [MenuItem] to this menu.
     *
     * @param text The text to display on the menu item.
     * @param disabled Optional. Overrides whether the menu item is disabled.
     * @param _onAction Optional. An action to perform when the menu item is clicked.
     *
     * Note that if [disabled] is not provided, the menu item will be disabled
     * automatically if _onAction is null.
     *
     * @return this
     */
    fun item(text: String, disabled: Boolean? = null, _onAction : EventHandler<ActionEvent>? = null) : IdiomaticMenuFactory {
        MenuItem(text).let {
            // If there's an action, store add it to the menu.
            _onAction?.apply {it.onAction = this}

            if (disabled != null && disabled) {
                it.isDisable = disabled
            } else {
                it.isDisable = _onAction == null
            }

            // Add the menu to the context menu.
            menu.items.add(it)
        }

        return this
    }

    /**
     * Creates a new checkbox menu item within this menu.
     *
     * @param text The text to display on the menu item.
     * @param selected Whether the checkbox is selected by default.
     * @param onCheckChanged A function invoked when the check is changed, with the state of the check.
     * @return this
     */
    fun checkItem(text: String, selected: Boolean = false, onCheckChanged: (Boolean) -> Unit) : IdiomaticMenuFactory {
        CheckMenuItem(text).let {
            it.isSelected = selected
            it.setOnAction {
                ae->
                onCheckChanged(it.isSelected)
            }

            // Add the menu to the context menu.
            menu.items.add(it)
        }

        return this
    }

    /**
     * Adds a new [SeparatorMenuItem] to this menu.
     *
     * @return this
     */
    fun separator() : IdiomaticMenuFactory {
        menu.items.add(SeparatorMenuItem())
        return this
    }

    fun add(item : MenuItem) = this.also { menu.items.add(item) }

    fun addAll(vararg items : MenuItem) = this.also { menu.items.addAll(items) }

    /**
     * For use on sub-menus only.
     *
     * Returns idiomatic context the parent menu.
     *
     * If there is no parent, i.e this wasn't called
     * on a sub-menu, this will throw an npe.
     */
    fun menuDone() : IdiomaticMenuFactory {
        return parent!!
    }

    /**
     * When you're happy with your menu,
     * calling this will convert this [menu]
     * and it's children into a [ContextMenu].
     */
    fun toContextMenu() : ContextMenu {
        return ContextMenu().also {
            it.items.addAll(menu.items)
        }
    }
}


fun menu(text: String = "", _onAction : EventHandler<ActionEvent>? = null) = IdiomaticMenuFactory (
    menu = Menu(text).also {
        it.onAction = _onAction
    }
)

fun Parent.addContext(menu: ContextMenu) {
    setOnContextMenuRequested {
        menu.show(this, it.screenX, it.screenY)
    }

    addEventHandler(MOUSE_CLICKED) {
        if (it.button == MouseButton.PRIMARY)
            menu.hide()
    }
}

fun Control.addContext(menu: ContextMenu) {
    contextMenu = menu
}

fun Node.boundsInScene() = localToScene(boundsInLocal)