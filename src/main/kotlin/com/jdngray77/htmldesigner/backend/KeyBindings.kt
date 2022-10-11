package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.IDE.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.utility.IDEEarlyBootListener
import com.jdngray77.htmldesigner.utility.boundsInScene
import com.jdngray77.htmldesigner.utility.concmod
import javafx.scene.control.Labeled
import javafx.scene.input.*
import org.controlsfx.control.PopOver
import java.awt.Toolkit
import java.lang.System.gc

/**
 * Binds keyboard shortcuts to actions.
 *
 * Executable scripts or buttons can be subscribed to a [KeyEvent].
 *
 * [KeyEvent]s are raised by key combinations, as defined by the configuration.
 *
 * This means that multiple executables can be invoked by one key press by simply
 * binding multiple to the same event. Standardising the events also means that
 * the key combinations can easily be changed.
 *
 * The user could even have multiple key combinations for the same event.
 *
 * Basically :
 *
 * [KeyBindingSubscriber] subscribes to [KeyEvent]
 * [KeyToEventBinding] raises [KeyEvent]
 * [KeyEvent] triggers subscribed [KeyBindingSubscriber]s.
 *
 * The configuration stores a string like the following to represent a [KeyToEventBinding] :
 *
 * `EDITOR_UNDO,Meta+Z,Ctrl+Z,Ctrl+Z`
 *
 * The first part is the [KeyEvent] name, the rest are the key combinations.
 *
 * Combinations are in the order :
 *
 * Mac | Windows | Linux
 *
 * The correct combination for the current target will be selected automatically at runtime.
 *
 * @author Dylan Brand
 */
object KeyBindings : Subscriber, IDEEarlyBootListener {

    //region init

    /**
     * Used to automatically find and init the class when the IDE starts.
     *
     * Causes [init] upon first boot.
     */
    override fun onIDEBootEarly() {
        EventNotifier.subscribe(this, EventType.IDE_FINISHED_LOADING)
    }

    /**
     * Sets up the key bindings when the IDE finished loading / restarts.
     */
    override fun notify(e: EventType) {
        checkCapsActive()
        loadBindingsFromConfig()
    }

    //endregion
    //region Event binding

    /**
     * An enumeration of events that can be triggered by a key binding.
     */
    enum class KeyEvent {
        EDITOR_REQUEST_CLOSE,
        EDITOR_REQUEST_SAVE,
        EDITOR_UNDO,
        EDITOR_REDO,
        EDITOR_NEXT,
        EDITOR_PREVIOUS,
        META_CAPS_LOCK_CHANGED,
        IDE_RESTART,
    }

    /**
     * An event that may be triggered by a [KeyEvent] when raised by a key press.
     */
    class KeyBindingSubscriber(

        /**
         * If provided, a primary click on this button will be
         * simulated when the key combination is pressed.
         */
        val button: Labeled? = null,

        /**
         * If provided, this script will be executed when the key combination is pressed.
         */
        val runnable: Runnable? = null

    ) : Runnable {
        init {
            if (button == null && runnable == null)
                throw IllegalArgumentException("Either a button or runnable must be provided.")
        }

        override fun run() {
            if (button != null)
                button.fireEvent(MouseEvent(MouseEvent.MOUSE_CLICKED, button.layoutX, button.layoutY, button.boundsInScene().centerX, button.boundsInScene().centerY, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null))
            else runnable!!.run()
        }

        fun unbind() {
            registeredBindings.forEach {
                if (it.value.contains(this))
                    it.value.remove(this)
            }
        }
    }

    /**
     * A collection of [KeyBindingSubscriber]s that have been bound to [KeyEvent].
     */
    private val registeredBindings = HashMap<KeyEvent, MutableList<KeyBindingSubscriber>>().also {
        hm ->
        KeyEvent.values().forEach {
            hm[it] = mutableListOf()
        }
    }

    /**
     * Registers a [KeyBindingSubscriber] to a [KeyEvent].
     *
     * When a key binding triggers that [KeyEvent], the [KeyBindingSubscriber] will be executed.
     *
     * @param executable the bindable event to register
     * @param onEvent the event type to register the bindable event to
     */
    fun bindKey(onEvent: KeyEvent, executable: KeyBindingSubscriber) {
        registeredBindings[onEvent]!!.add(executable)
    }

    /**
     * Registers a [KeyBindingSubscriber] to a [KeyEvent].
     *
     * When a key binding triggers that [KeyEvent], the [KeyBindingSubscriber] will be executed.
     *
     * @param button Optional : A button to fire when the event is triggered
     * @param runnable Optional : A runnable to execute when the event is triggered
     * @param onEvent the event type to register the bindable event to
     */
    fun bindKey(onEvent: KeyEvent, button: Labeled?, runnable: Runnable?) =
        KeyBindingSubscriber(button, runnable).also {
            bindKey(onEvent, it)
        }



    /**
     * Registers a [KeyBindingSubscriber] to a [KeyEvent].
     *
     * When a key binding triggers that [KeyEvent], the [KeyBindingSubscriber] will be executed.
     *
     * @param button Optional : A button to fire when the event is triggered
     * @param runnable Optional : A runnable to execute when the event is triggered
     * @param onEvent the event type to register the bindable event to
     */
    fun bindKey(onEvent: KeyEvent, runnable: Runnable?) =
        KeyBindingSubscriber(null, runnable).also {
            bindKey(onEvent, it)
        }

    /**
     * Registers a [KeyBindingSubscriber] to a [KeyEvent].
     *
     * When a key binding triggers that [KeyEvent], the [KeyBindingSubscriber] will be executed.
     *
     * @param button Optional : A button to fire when the event is triggered
     * @param runnable Optional : A runnable to execute when the event is triggered
     * @param onEvent the event type to register the bindable event to
     */
    fun bindKey(onEvent: KeyEvent, button: Labeled?) =
        KeyBindingSubscriber(button, null).also {
            bindKey(onEvent, it)
        }


    /**
     * Registers a [KeyBindingSubscriber] to javafx, such that javafx will call back
     * to [raise] when the key combination is pressed.
     */
    fun bindKey(b: KeyToEventBinding) {
        // TODO able to check here for overriding key bindings. Warn user?
        EDITOR.scene.first.accelerators[b.determineCombination()] = Runnable { raise(b.event) }
        logStatus("${b.event} is bound to ${b.determineCombination()}")
    }

    /**
     * Executes all executables bound to this event.
     */
    private fun raise(event: KeyEvent) {
        logStatus("Key event raised : $event")

        // Concmod just incase any of the subscribers unsubscribe once used.
        registeredBindings[event]!!.concmod().forEach {
            it.run()
        }
    }

    //endregion
    //region key to event binding

    /**
     * A binding between a key combination and a runnable or a button
     */
    data class KeyToEventBinding (

        /**
         * The event to raise when pressed.
         */
        val event: KeyEvent,

        /**
         * The key combination that triggers the button or runnable
         * when on a Mac based target
         */
        val onMac: KeyCombination,

        /**
         * The key combination that triggers the button or runnable
         * when on a Windows based target
         */
        val onWindows: KeyCombination,

        /**
         * The key combination that triggers the button or runnable
         * when on a Linux based target
         */
        val onLinux: KeyCombination
    ) {

        /**
         * returns the correct key combination for the current platform
         */
        fun determineCombination() =
            when (PlatformType.getPlatformType()) {
                PlatformType.PlatformTypes.MAC -> onMac
                PlatformType.PlatformTypes.WINDOWS -> onWindows
                PlatformType.PlatformTypes.LINUX -> onLinux
            }

    }

    /**
     * Populates [KeyToEventBinding]s for every key binding in the configuration.
     *
     * Creates all of the pnemonics and chords and places them into the scene.
     *
     * Since they're enlisted into the scene, they will be lost on soft restarts
     * and will need to be re-enlisted on every soft restart, hence [notify] on [IDE_FINISHED_LOADING].
     */
    internal fun loadBindingsFromConfig() {
        var configs = (Config[Configs.KEY_BINDINGS_STRING] as String).lines()

        // Sanitiztion


        // Issue #63. Key bindings are all on the same line.
        // Split them up.
        if (configs.size == 1 && configs.first().split(",").size > 3) {
            var saneEntries = configs.first().split(",",";")

            saneEntries = saneEntries.filter { it.isNotEmpty() }

            // Check correct number of columns for each entry.
            // Otherwise it's a different malformation problem - not #63
            if (saneEntries.size % 4 != 0)
                throw IllegalArgumentException("Key bindings are not in the correct format. Please check your config file.")

            logStatus("Key bindings were malformed (Issue 63). Attempting to fix.")

            configs = saneEntries
                .chunked(4) // FIXME This does not support one binding for all platforms.
                .map { it.joinToString(",") }
        }


        configs = configs.map {
            it.replace("\n", "")
            it.replace(";", "")
        }



        logStatus("Config contains ${configs.size} keyboard bindings")

        var boundSuccessfully = 0

        configs.forEach {
            // Eg of each config
            // EDITOR_UNDO,Meta+Z,Ctrl+Z,Ctrl+Z

            val cols = it.split(",")

            try {
                bindKey(
                    KeyToEventBinding(
                        KeyEvent.valueOf(cols[0]),
                        KeyCombination.valueOf(cols[1]),
                        KeyCombination.valueOf(cols.getOrElse(2) { cols[1] } ),
                        KeyCombination.valueOf(cols.getOrElse(3) { cols[1] })
                    )
                )

                boundSuccessfully++
            } catch (e: IllegalArgumentException) {
                logWarning("Unable to bind key combination for ${cols[0]}, as there is no such key event.")
            } catch (ignored: Exception) {
                logWarning("Unable to parse key binding '$it'")
            }
        }

        logStatus("Bound $boundSuccessfully of ${configs.size} key bindings successfully.")
    }

    fun unbindAll() {
        EDITOR.scene.first.accelerators.clear()
        registeredBindings.values.concmod().forEach {
            it.concmod().forEach { it.unbind() }
            it.clear()
        }
        gc()
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


    /**
     * Checks caps notification and binds to completion of IDE load (after project loaded)
     * We can't configure the bindings or caps notification this early, as we need to access the GUI
     * via the MVC, but the MVC isn't available until after the project has been loaded - so we
     * subscribe to be notified after the project has been loaded.
     */
    init {
        // check caps notification when caps is pressed.
        bindKey(KeyEvent.META_CAPS_LOCK_CHANGED) { checkCapsActive() }
    }
}

/**
 * A utility class that allows for temporary structures in the IDE to register and unregister
 * collections of [KeyBindingSubscriber]s.
 */
class KeyBindingCollection(vararg bindings: KeyBindings.KeyBindingSubscriber = arrayOf()) {

    private val binds = bindings.toMutableList()

    fun bind(onEvent: KeyBindings.KeyEvent, button: Labeled?, runnable: Runnable?): KeyBindingCollection {
        KeyBindings.bindKey(onEvent, button, runnable).also {
            binds.add(it)
        }

        return this
    }

    fun bind(onEvent: KeyBindings.KeyEvent, runnable: Runnable?): KeyBindingCollection = bind(onEvent, null, runnable)

    fun bind(onEvent: KeyBindings.KeyEvent, button: Labeled): KeyBindingCollection = bind(onEvent, button, null)

    fun dispose() {
        binds.forEach { it.unbind() }
    }

    fun finalize() {
        dispose()
    }

}