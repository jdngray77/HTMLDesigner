package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.IDE.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeEditor
import com.jdngray77.htmldesigner.utility.IDEEarlyBootListener
import javafx.scene.control.Labeled
import javafx.scene.input.*
import org.controlsfx.control.PopOver
import java.awt.Toolkit

/**
 * Binds keyboard shortcuts to actions.
 *
 * A binding may execute a runnable, or invoke a method.
 *
 * Bindings are stored in the [Config] registry.
 *
 * @author Dylan Brand
 */
object KeyBindings : Subscriber, IDEEarlyBootListener {

    /**
     * Used to automatically find and init the class when the IDE starts.
     *
     * Causes [init] upon first boot.
     */
    override fun onIDEBootEarly() {}

    /**
     * Checks caps notification and binds to completion of IDE load (after project loaded)
     *
     * We can't configure the bindings or caps notification this early, as we need to access the GUI
     * via the MVC, but the MVC isn't available until after the project has been loaded - so we
     * subscribe to be notified after the project has been loaded.
     */
    init {
        EventNotifier.subscribe(this, EventType.IDE_FINISHED_LOADING)
    }

    /**
     * Sets up the key bindings when the IDE finished loading / restarts.
     */
    override fun notify(e: EventType) {
        checkCapsActive()
        assignKeys()
    }

    //    val keyControl = Config[Configs.KEY_BINDINGS_HASHMAP] as HashMap<Predicate<KeyEvent>, Runnable>

    /**
     * An array of key bindings.
     *
     * Each row of the table represents one key binding.
     *
     * Columns are as follows :
     *
     * Buttons | Runnable scripts | Mac bindings | Windows bindings | Linux bindings
     */
    val keyBindings = arrayOf(
        // Buttons
        arrayListOf<Labeled?>(), // save
        // Runnables
        arrayListOf<Runnable>(),
        // Mac
        arrayListOf<KeyCombination>(),
        // Windows
        arrayListOf<KeyCombination>(),
        // Linux
        arrayListOf<KeyCombination>()
    )
    

    /**
     * Initialises the key bindings.
     */
    fun assignKeys() {

        checkCapsActive()

        EDITOR.stage.focusedProperty().addListener { _, _, _ ->
            checkCapsActive()
        }

        val os = PlatformType.getPlatformType() // user operating system
        val systemColumn = os.ordinal + 2

        // Keyboard shortcuts
        val metaS = KeyCharacterCombination("s", KeyCombination.META_DOWN)
        val metaZ = KeyCharacterCombination("z", KeyCombination.META_DOWN)
        val metaR = KeyCharacterCombination("y", KeyCombination.CONTROL_ANY)
        val caps = KeyCodeCombination(KeyCode.CAPS)


        // initialise the bindings
        bind(metaS) { activeEditor()?.requestSave() }
        bind(metaZ) { activeEditor()?.undo() }
        bind(caps) { checkCapsActive() }

        for (i in keyBindings[0].indices) {
            val keyCombo = keyBindings[systemColumn][i] as KeyCombination
            val button = keyBindings[0][i]

            if (button != null) {
                val b = Mnemonic(button as Labeled, keyCombo)
                EDITOR.scene.first.addMnemonic(b)
            } else {
                EDITOR.scene.first.accelerators[keyCombo] = keyBindings[1][i] as Runnable
            }
        }
    }

    /**
     * Binds [KeyCombination]'s to [Runnable]'s to enable keyboard shortcuts.
     *
     * @param [bindingLinux] The key binding that will be used if the application is running on a linux target.
     * @param [bindingMac] The key binding that will be used if the application is running on a mac target.
     * @param [bindingWindows] The key binding that will be used if the application is running on a windows target.
     * @param [runnable] The [Runnable] that will be executed when the key binding is pressed.
     * @param [button] Optional : A button that will be clicked when the key binding is pressed.
     */
    private fun bind(
        bindingWindows: KeyCombination,
        bindingMac: KeyCombination = bindingWindows,
        bindingLinux: KeyCombination = bindingWindows,
        button: Labeled? = null,
        runnable: Runnable
    ) {
        (keyBindings[3] as ArrayList<KeyCombination>).add(bindingWindows) // windows
        (keyBindings[2] as ArrayList<KeyCombination>).add(bindingMac) // mac
        (keyBindings[4] as ArrayList<KeyCombination>).add(bindingLinux) // linux
        (keyBindings[0] as ArrayList<Labeled?>).add(button)
        (keyBindings[1] as ArrayList<Runnable>).add(runnable)
    }

    /**
     * Displays a notification on the editor when caps lock is on.
     */
    private fun checkCapsActive() {
        if (Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK))
            showCapsActiveNotif()
        else
            hideCapsActiveNotif()
    }


    private fun showCapsActiveNotif() {
        mvc().MainView.capsHBox.isVisible = true

        if (!(Config[Configs.KEY_BINDINGS_SUPPRESS_CAPS_WARNING_BOOL] as Boolean)) {
            ContextMessage(
                mvc().MainView.capsHBox,
                "CAPS LOCK ON\n\nKeyboard shortcuts do not work.\n Shortcuts are case sensitive.",
                PopOver.ArrowLocation.BOTTOM_CENTER
            )

            Config[Configs.KEY_BINDINGS_SUPPRESS_CAPS_WARNING_BOOL] = true
        }
    }

    private fun hideCapsActiveNotif() {
        mvc().MainView.capsHBox.isVisible = false
    }
}