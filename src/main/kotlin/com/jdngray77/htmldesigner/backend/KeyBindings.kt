package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.utility.Restartable
import javafx.event.EventType
import javafx.scene.control.Labeled
import javafx.scene.input.*
import java.awt.Toolkit
import java.util.EventListener
import java.util.function.Predicate

/**
 * Keyboard shortcuts for the HTML application.
 *
 * @author Dylan Brand
 */
object KeyBindings : Restartable {

//    val keyControl = Config[Configs.KEY_BINDINGS_HASHMAP] as HashMap<Predicate<KeyEvent>, Runnable>

    val array = arrayOf(
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

        capsActive()

        EDITOR.stage.focusedProperty().addListener { _, _, _ ->
            capsActive()
        }

        val os = PlatformType.getPlatformType() // user operating system
        val systemColumn = os.ordinal + 2

        // Keyboard shortcuts
        val metaS = KeyCharacterCombination("s", KeyCombination.META_DOWN)
        val metaZ = KeyCharacterCombination("z", KeyCombination.META_DOWN)
        val metaR = KeyCharacterCombination("r", KeyCombination.META_DOWN)
        val caps = KeyCodeCombination(KeyCode.CAPS)

        // initialise the bindings
        bind(metaS) { mvc().currentEditor().save() }
        bind(metaZ) { mvc().currentEditor().redo() }
        bind(caps) { capsActive() }

        for (i in array[0].indices) {
            val keyCombo = array[systemColumn][i] as KeyCombination
            val button = array[0][i]

            if (button != null) {
                val b = Mnemonic(button as Labeled, keyCombo)
                EDITOR.scene.first.addMnemonic(b)
            } else {
                EDITOR.scene.first.accelerators[keyCombo] = array[1][i] as Runnable
            }
        }
    }

    /**
     * Binds [KeyCombination]'s to [Runnable]'s to enable keyboard shortcuts.
     */
    private fun bind(
        bindingWindows: KeyCombination,
        bindingMac: KeyCombination = bindingWindows,
        bindingLinux: KeyCombination = bindingWindows,
        button: Labeled? = null,
        runnable: Runnable
    ) {
        (array[3] as ArrayList<KeyCombination>).add(bindingWindows) // windows
        (array[2] as ArrayList<KeyCombination>).add(bindingMac) // mac
        (array[4] as ArrayList<KeyCombination>).add(bindingLinux) // linux
        (array[0] as ArrayList<Labeled?>).add(button)
        (array[1] as ArrayList<Runnable>).add(runnable)
    }

    /**
     * Displays a notification on the editor when caps lock is on.
     */
    private fun capsActive() {
        if (Config[Configs.KEY_BINDINGS_CAPS_WARNING_BOOL] as Boolean) {
            showWarningNotification(
                "CAPS LOCK ON",
                "Keyboard shortcuts do not work.\n Shortcuts are case sensitive."
            )
        }

        Config[Configs.KEY_BINDINGS_CAPS_WARNING_BOOL] = false
        mvc().MainView.capsHBox.isVisible =
            Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK)
    }

    override fun onIDEBoot() {
        assignKeys()
    }

    override fun onIDERestart() {
        assignKeys()
    }
}