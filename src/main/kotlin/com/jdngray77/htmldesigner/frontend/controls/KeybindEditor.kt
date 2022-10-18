package com.jdngray77.htmldesigner.frontend.controls

import com.jdngray77.htmldesigner.backend.ContextMessage
import com.jdngray77.htmldesigner.backend.KeyBindings
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.logStatus
import com.jdngray77.htmldesigner.utility.loadControllerlessFXMLComponent
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import org.controlsfx.control.ToggleSwitch

/**
 * A dialog for editing keybindings within the [Config].
 */
class KeybindEditor() {

    init {
        val d = Dialog<Unit>()
        d.dialogPane = DialogPane()
        loadFXMLComponent<BorderPane>("KeybindDialog.fxml", this::class.java).apply {
            d.dialogPane.content = first
            (second as KeybindDialogController).dialog = d
        }
        d.result = Unit
        d.isResizable = true
        d.show()
    }

}

/**
 * Actual controller for the [KeybindEditor] dialog.
 */
class KeybindDialogController {

    @FXML lateinit var scrollPane: ScrollPane

    @FXML lateinit var btnCheck: Button

    @FXML lateinit var root: BorderPane

    @FXML lateinit var dialog: Dialog<Unit>

    /**
     * List of key binding entries.
     */
    @FXML lateinit var entryList: VBox

    /**
     * List of controllers for the [entryList]
     */
    var _entryList = mutableListOf<KeybindEntryController>()

    @FXML
    fun initialize() {
        populate()

        // Add the header from file.
        root.top = loadControllerlessFXMLComponent("KeybindHeader.fxml", this::class.java)
    }

    private fun populate(config: String = Config[Configs.KEY_BINDINGS_STRING] as String) {
        // Populate the list of keybindings from the config.
        config.lines()
            .filter { it.isNotBlank() }
            .map { newEntry(it) }
    }

    /**
     * Creates a new entry with no content.
     */
    fun newEntry(): Pair<AnchorPane, KeybindEntryController> {
        val entry = KeybindEntryController.create(this)
        val controller = entry.second as KeybindEntryController
        val node = entry.first

        entryList.children.add(0, node)
        _entryList.add(0, controller)

        entryList.requestLayout()
        entryList.layout()

        scrollPane.vvalue = 0.0


        return entry as Pair<AnchorPane, KeybindEntryController>
    }

    /**
     * Creates a new entry with the given key binding string.
     * @param config the key binding config string to be parsed by the new entry.
     */
    fun newEntry(config: String) {
        newEntry().second.setConfig(config)
    }

    /**
     * Closes the dialog with no changes.
     */
    fun cancel() {
        dialog.close()
    }

    /**
     * Saves the changes to the registry, reloads the key bindings, and closes the dialog.
     */
    fun apply() {
        if (!checkAllOK())
            return

        Config[Configs.KEY_BINDINGS_STRING] = toString()
        KeyBindings.loadBindingsFromConfig()
        dialog.close()
        logStatus("Updated key bindings.")
    }

    override fun toString() = _entryList.map { it.toString() }.joinToString("\n")

    /**
     * Checks that every entry is OK.
     *
     * @return true if all entries are OK, otherwise false.
     */
    fun checkAllOK() : Boolean {
        _entryList.joinToString("\n") { it.checkOK()?: "" }.apply {
            return if (this.isNotBlank()) {
                this.lines().filter { it.isNotEmpty() }.apply sanitized@{
                    ContextMessage(btnCheck, "$size problems found : \n\n ${joinToString("\n")}")
                }
                false
            } else {
                ContextMessage(btnCheck, "Looks good!")
                true
            }
        }
    }

    fun clear() {
        entryList.children.clear()
        _entryList.clear()
    }

    fun defaults() {
        clear()
        populate(KeyBindings.DEFAULT_KEYBINDINGS)
    }

    fun revert() {
        clear()
        populate()
    }

}

/**
 * Controller for an entry in the [KeybindDialogController.entryList].
 */
class KeybindEntryController {

    @FXML
    lateinit var txtTarget: TextField

    @FXML
    lateinit var txtLinux: TextField

    @FXML
    lateinit var txtWindows: TextField

    @FXML
    lateinit var txtMac: TextField

    @FXML
    lateinit var tglEnable: ToggleSwitch

    @FXML
    lateinit var root: AnchorPane

    @FXML
    lateinit var parent: KeybindDialogController


    /**
     * Populates this entry with a key binding.
     *
     * Assumes target and all three key combinations are valid.
     *
     * Performs [checkOK] after.
     *
     * @param string a key config string to parse.
     */
    fun setConfig(string: String) {
        tglEnable.isSelected = !string.startsWith("//")

        var sanitized = string

        // Drop the // at the beginning.
        if (!tglEnable.isSelected)
            sanitized = sanitized.drop(2)

        // Drop the ; at the end.
        if (sanitized.endsWith(";"))
            sanitized = sanitized.dropLast(1)

        val split = sanitized.split(",")


        txtTarget.text = split.getOrElse(0) { "" }
        txtMac.text = split.getOrElse(1) { "" }
        txtWindows.text = split.getOrElse(2) { "" }
        txtLinux.text = split.getOrElse(3) { "" }

        checkOK()
    }

    @FXML
    fun initialize() {
        txtTarget.focusedProperty().addListener { _, new, _ -> if (new) checkOK() }
        txtLinux.focusedProperty().addListener { _, new, _ -> if (new) checkOK() }
        txtWindows.focusedProperty().addListener { _, new, _ -> if (new) checkOK() }
        txtMac.focusedProperty().addListener { _, new, _ -> if (new) checkOK() }
    }

    fun promptTarget() {
        txtTarget.text = KeybindTargetPrompt.promptTarget()
    }

    /**
     * Deletes this entry from the dialog.
     */
    fun delete() {
        parent.entryList.children.remove(root)
        parent._entryList.remove(this)
    }

    /**
     * Checks that this dialog entry is set-up ok.
     */
    fun checkOK() : String? {
        KeyBindings.bindStringIsValid(toString()).apply {
            if (this != null)
                ContextMessage(root, this)

            return this
        }
    }

    override fun toString() = "${if (tglEnable.isSelected) "" else "//"}${txtTarget.text},${txtMac.text},${txtWindows.text},${txtLinux.text};"

    companion object {

        fun create(p: KeybindDialogController) =
            loadFXMLComponent<AnchorPane>("KeybindEntry.fxml", this::class.java).also {
                (it.second as KeybindEntryController).parent = p
            }

    }

}

class KeybindTargetPrompt {

    @FXML
    lateinit var txtMenuSelection: Label

    @FXML
    lateinit var paneEventTab: AnchorPane

    @FXML
    lateinit var tabPane: TabPane

    @FXML
    lateinit var cmbEvents: ChoiceBox<Any>

    lateinit var dialog: Dialog<String>

    @FXML
    fun initialize() {
        KeyBindings.KeyEvent.values().map {
            cmbEvents.items.add(it.toString())
        }

        paneEventTab.children.add(
            loadFXMLComponent<MenuBar>("MenuBar.fxml").first.apply {
                useSystemMenuBarProperty().set(false)
                menus.forEach {
                    setMenuItemsRecursive("Menu > ${it.text} > ", it)
                }
            }
        )
    }

    private fun setMenuItemsRecursive(path: String, item: MenuItem) {
        if (item is Menu) {
            item.items.map {
                setMenuItemsRecursive(path + "${it.text} > ",it)
            }
        } else
            item.setOnAction {
                txtMenuSelection.text = path.dropLast(3)
            }
    }

    fun OK() {
        dialog.result =
            if (tabPane.selectionModel.selectedIndex == 1) {
                if (cmbEvents.selectionModel.selectedItem != null) {
                    (cmbEvents.selectionModel.selectedItem as String)
                } else return
            }else
                txtMenuSelection.text


        dialog.close()
    }


    companion object {

        fun promptTarget() : String {

            val d = Dialog<String>()
            d.dialogPane = DialogPane()
            loadFXMLComponent<BorderPane>("KeybindTargetPrompt.fxml", this::class.java).apply {
                d.dialogPane.content = first
                (second as KeybindTargetPrompt).dialog = d
            }
            d.isResizable = true
            d.showAndWait()
            return d.result
        }

    }

}