package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.IDE.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.utility.*
import javafx.scene.control.Labeled
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.input.*
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
        loadBindingsFromConfig()
        bindGlobal()

        everyInstanceOf(KeybindingListener::class).forEach {
            (it as KeybindingListener).bindKeybindings()
        }

    }

    /**
     * A function that applies event to code bindings.
     *
     * At runtime, binds key events to function invocations.
     *
     * Some bindings are applied through thier respective system / IDE components,
     * but some bindings are not related to any component, and so are applied here
     * instead.
     *
     * Called on [notify] (every start/restart).
     *
     * > N.B prefer to use menu bar items instead
     */
    @Deprecated("Prefer to use menu bar items instead")
    private fun bindGlobal() {

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
        EDITOR_TOGGLE_DIRECT,

        REQUEST_RUN_SERVER,
        REQUEST_RUN_ANYTHING,
        REQUEST_IDE_RESTART,

        PROJECT_CLOSE,
        PROJECT_SHOW_IN_FINDER,

        OPEN_SETTINGS,
        OPEN_PROJECT_PREFS,
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
                button.fireEvent(
                    MouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        button.layoutX,
                        button.layoutY,
                        button.boundsInScene().centerX,
                        button.boundsInScene().centerY,
                        MouseButton.PRIMARY,
                        1,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        null
                    )
                )
            else runnable!!.run()
        }

        fun unbind() {
            registeredBindings.forEach {
                if (it.value.contains(this))
                    it.value.remove(this)
            }
        }
    }

    val DEFAULT_KEYBINDINGS = """
            EDITOR_REQUEST_CLOSE,Meta+W,Ctrl+W,Ctrl+W;
            EDITOR_NEXT,Alt+Tab,Alt+Tab,Alt+Tab;
            EDITOR_PREVIOUS,Alt+Shift+Tab,Alt+Shift+Tab,Alt+Shift+Tab;
            EDITOR_TOGGLE_DIRECT,Meta+E,Ctrl+E,Ctrl+E;
                       
            Menu > HTML Designer > Run a Task...,Meta+Shift+A,Ctrl+Shift+A,Ctrl+Shift+A;
            Menu > HTML Designer > Soft Restart...,Meta+Shift+R,Ctrl+Shift+R,Ctrl+Shift+R;
            Menu > HTML Designer > Registry...,Meta+.,Ctrl+.,Ctrl+.;
            Menu > HTML Designer > Exit,Meta+Esc,Ctrl+Esc,Ctrl+Esc;
            
            Menu > Tools > Live Server > Enable debug server,Meta+R,Ctrl+R,Ctrl+R;
            
            Menu > View > Show bottom dock,Meta+B,Ctrl+B,Ctrl+B;
            
            Menu > Project > Open in Finder,Meta+Alt+O,Ctrl+Alt+O,Ctrl+Alt+O;
            Menu > Project > Close Project,Meta+Alt+W,Ctrl+Alt+W,Ctrl+Alt+W;
            Menu > Project > Registry...,Meta+Alt+.,Ctrl+Alt+.,Ctrl+Alt+.;
            
            Menu > Editor > Undo,Meta+Z,Ctrl+Z,Ctrl+Z;
            Menu > Editor > Redo,Meta+Shift+Z,Ctrl+Y,Ctrl+Y;
            Menu > Editor > Save,Meta+S,Ctrl+S,Ctrl+S;

            """.trimIndent()

    /**
     * A collection of [KeyBindingSubscriber]s that have been bound to [KeyEvent].
     */
    private val registeredBindings = HashMap<KeyEvent, MutableList<KeyBindingSubscriber>>().also { hm ->
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
    data class KeyToEventBinding(

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
        fun determineCombination() = Companion.determineCombination(onMac, onWindows, onLinux)

        companion object {
            fun determineCombination(mac: KeyCombination, windows: KeyCombination, linux: KeyCombination) =
                when (PlatformType.getPlatformType()) {
                    PlatformType.PlatformTypes.MAC -> mac
                    PlatformType.PlatformTypes.WINDOWS -> windows
                    PlatformType.PlatformTypes.LINUX -> linux
                }
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
        logStatus("======================")
        logStatus("BEGIN KEY BINDINGS")

        // TODO warn if an accellerator is used more than once.

        // Remove all existing bindings
        unbindAll()

        var configs = (Config[Configs.KEY_BINDINGS_STRING] as String).lines()

        // Sanitiztion

        configs = configs.filter { it.isNotBlank() }


        // Issue #63. Key bindings are all on the same line.
        // Split them up.
        if (configs.size == 1 && configs.first().split(",").size > 3) {
            var saneEntries = configs.first().split(",", ";")

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

        configs.forEach skip@{
            // Eg of each config
            //
            // EDITOR_UNDO,Meta+Z,Ctrl+Z,Ctrl+Z
            // OR
            // Menu > View > Show bottom dock,Meta+B,Ctrl+B,Ctrl+B

            // Collect key combinations
            val cols = it.split(",")

            var target = cols[0]
            var mac: KeyCombination?
            var windows: KeyCombination?
            var linux: KeyCombination?

            val x: (Int) -> String = {
                (cols.getOrElse(it) { cols[1] }).takeUnless { it.isBlank() } ?: cols[1]
            }

            // Disambiguate inability to parse key combinations
            try {
                mac = KeyCombination.valueOf(cols[1])
                windows = KeyCombination.valueOf(x(2))
                linux = KeyCombination.valueOf(x(3))
            } catch (e: IllegalArgumentException) {
                logWarning("One or more key combinations for ${cols[0]} is not in valid. ($it)")
                return@skip
            }


            // Determine where to assign the combinations

            try {

                if (target.lowercase().startsWith("menu > ")) {
                    // Is Menu.

                    // Assign key combination
                    determineMenuItem(target)?.apply {
                        accelerator = KeyToEventBinding.determineCombination(mac, windows, linux)
                        logStatus("Assigned $target to $accelerator")
                        boundSuccessfully++
                    }

                    // Warn if no matching menu is found
                        ?: logWarning("Could not find menu item for $target. Key binding is ignored.")

                } else {
                    // If not a menu, check if is valid event
                    try {
                        val event = KeyEvent.valueOf(target)
                        // Is event
                        bindKey(KeyToEventBinding(event, mac, windows, linux))
                        boundSuccessfully++
                    } catch (e: IllegalArgumentException) {
                        // Not event
                        logWarning("Key binding target '$target' is not a valid event or menu item. Binding is ignored.")
                        return@skip
                    }
                }
            } catch (e: Exception) {
                logWarning("Unable to parse key binding '$it'")
                ExceptionListener.uncaughtException(e)
            }
        }

        logStatus("Bound $boundSuccessfully of ${configs.size} key bindings successfully.")
        logStatus("END KEY BINDINGS")
        logStatus("======================")
    }

    private fun determineMenuItem(target: String): MenuItem? {
        val path = target.substringAfter("Menu > ").split(" > ")

        return mvc().MainView.menuBar.menus
            // Find menu
            .find { it.text == path.first() }

            // Determine menu item recursively
            ?.let { determineMenuItem(it, path.drop(1)) }
    }

    private fun determineMenuItem(it: Menu, path: List<String>): MenuItem? {
        it.items.find { it.text == path.first() }
            ?.let { menuItem ->
                if (menuItem is Menu)
                    return determineMenuItem(menuItem, path.drop(1))
                else {
                    return menuItem
                }
            }

        return null
    }


    /**
     * Determines if a binding configuration string is in
     * a valid format.
     */
    fun bindStringIsValid(string: String): String? {
        if (string.isBlank())
            return "Empty"

        if (!string.endsWith(";"))
            return "Missing ';' at end of line"


        var sanit = string

        sanit = sanit.replace(";", "")

        if (sanit.startsWith("//"))
            sanit = sanit.drop(2)

        val cols = sanit.split(",")

        if (cols.size > 4)
            return "Too many commas (max 3)"

        if (cols.size == 1)
            return "No key bindings."

        if (cols[0].isBlank())
            return "No target."

        if (cols[1].isBlank() && cols[2].isBlank() && cols[3].isBlank())
            return "No key combinations."

        if (cols[0].startsWith("Menu > ")) {
            determineMenuItem(cols[0]) ?: return "No matching menu item."
        } else {
            try {
                KeyEvent.valueOf(cols[0])
            } catch (e: IllegalArgumentException) {
                return "No event called '${cols[0]}'"
            }
        }

        return null
    }

    fun unbindAll() {
        EDITOR.scene.first.accelerators.clear()
        registeredBindings.values.concmod().forEach {
            it.concmod().forEach { it.unbind() }
            it.clear()
            assert(it.isEmpty())
        }
        gc()
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

/**
 * An interface class for static objects that need to register key bindings early in the boot
 * process.
 *
 * Instances of this interface will be found and invoked when keybindings are configured at boot.
 *
 * Just bare in mind that there must be instances of this class available at boot time, so
 * this goes hand-in-hand with the [IDEEarlyBootListener]
 */
interface KeybindingListener {
    fun bindKeybindings()
}