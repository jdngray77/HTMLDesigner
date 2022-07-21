

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

package com.jdngray77.htmldesigner.backend.data.config

import com.jdngray77.htmldesigner.backend.data.config.Registry.Companion.keyType
import com.jdngray77.htmldesigner.backend.showErrorAlert
import com.jdngray77.htmldesigner.backend.showWarningNotification
import com.jdngray77.htmldesigner.utility.loadObjectFromDisk
import com.jdngray77.htmldesigner.utility.saveObjectToDisk
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvcIfAvail
import org.jsoup.nodes.Document
import java.io.File
import java.io.InvalidClassException
import java.io.NotSerializableException
import java.lang.System.gc
import kotlin.reflect.KClass


/*
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *
 *
 *                                         _
 *                                        (_)
 *               __      ____ _ _ __ _ __  _ _ __   __ _
 *               \ \ /\ / / _` | '__| '_ \| | '_ \ / _` |
 *                \ V  V / (_| | |  | | | | | | | | (_| |
 *                 \_/\_/ \__,_|_|  |_| |_|_|_| |_|\__, |
 *                                                  __/ |
 *                                                 |___/
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *                  THIS FILE IS SENSITIVE TO CHANGES.
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *                                   _ _   _
 *                ___  ___ _ __  ___(_) |_(_)_   _____
 *               / __|/ _ \ '_ \/ __| | __| \ \ / / _ \
 *               \__ \  __/ | | \__ \ | |_| |\ V /  __/
 *               |___/\___|_| |_|___/_|\__|_| \_/ \___|
 *
 *                _
 *               | |_ ___
 *               | __/ _ \
 *               | || (_) |
 *                \__\___/
 *
 *                     _
 *                 ___| |__   __ _ _ __   __ _  ___  ___
 *                / __| '_ \ / _` | '_ \ / _` |/ _ \/ __|
 *               | (__| | | | (_| | | | | (_| |  __/\__ \
 *                \___|_| |_|\__,_|_| |_|\__, |\___||___/
 *                                       |___/
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *
 *      TRY TO AVOID CHANGING THE SCHEMA OF THIS CLASS IF POSSIBLE.
 *
 *      IT IMPACTS BACKWARDS COMPATABILITY WITH LOADING.
 *
 *      CHANGES TO THE SCHEMA OF THIS CLASS WILL FORCE THE IDE TO
 *      RESET REGISTRIES, DELETING USERS SETTINGS AND PREFERENCES
 *      WHEN THEY UPDATE THEIR IDE.
 *
 *      What's OK :
 *
 *          Altering code within an existing functions
 *
 *      What breaks compatability :
 *
 *          Literally anything that changes the schema of the class, i.e
 *
 *          - Changing a functions
 *              - return type
 *              - name
 *              - parameters
 *
 *          - Adding new or removing members from the class
 *              (functions & variables)
 *
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */


/**
 * A HashMap for storing configurable values that are
 * automatically saved to and loaded from
 * disk.
 *
 * All keys must be entered at initalisation.
 * Keys can be modified later, but not created.
 *
 * Keys can be anything, but Strings or Enum's are suggested.
 * If using an object as a key, it must have a compatable toString.
 *
 * # Key Format
 *
 * The key format is a SNAKE_CASE breadcrumb, suffixed with the expected
 * rudimentary data type. This is to help keep track of what data is expected.
 *
 * The breadcrumb can be anything you desire, but use the breadcumb well
 * to organise together configurations relevant to the same thing.
 *
 * Valid data types are :
 *
 *  - BOOL
 *  - INT
 *  - FLOAT
 *  - DOUBLE
 *  - SHORT
 *  - DOC
 *  - STRING
 *
 * More data types can be added by adding them to [keyType],
 * but they must be serializable.
 *
 *  @see [keyType]
 *
 * Examples :
 *
 *  > PROJECT_AUTOEXPORT_BOOL
 *
 *  > PROJECT_SKIPVALIDATION_BOOL,
 *
 *  > PROJECT_BACKUP_DEPTH_INT
 *
 * @param T The type used for the KEY.
 *
 * @see initialize
 * @see validate
 * TODO validation is removed.
 */
open class Registry <T>(val saveLocation: File) : HashMap<T, Any>() {

    /**
     * A work around for late initalising.
     *
     * I can't remember exactly why this was
     * required.
     */
    protected fun defferedInit() {
        if (saveLocation.exists())
            load()
        else
            reset()

        autosave = true
    }

    /**
     * Enters a value into the registry,
     * if the type matches
     */
    final override fun put(key: T, value: Any) {
        if (!isValidType(key, value))
            throw MismatchedTypeException(key, value)
        else
            if (value != super.put(key, value))
                dirty()
    }

    final override fun putAll(from: Map<out T, Any>) {
        from.map {
            put(it.key, it.value)
        }
    }

    final override fun get(key: T): Any {
        try {
            return super.get(key)!!
        } catch (e : NullPointerException) {
            throw MissingEntryException(key)
        }
    }

    @Deprecated("Values are not optional in the registry. Default is ignored.", ReplaceWith("get(key)"))
    final override fun getOrDefault(key: T, defaultValue: Any) =
        get(key)

    /**
     * Saves the current state of the registry to disk
     * in the prior provided [saveLocation] if there are changes to save.
     *
     * Only has effect if [dirty] is high.
     *
     */
    final fun flush() {
        if (dirty)
            forceFlush()
    }

    /**
     * Ignores the [dirty] flag, and saves the registry.
     *
     * [dirty] flag is cleared.
     */
    final fun forceFlush() {
        try {
            saveObjectToDisk(saveLocation)
        } catch (e: NotSerializableException) {
            showErrorAlert("DEVS : DO NOT COMMIT " +
                    "\n\n A registry is unserializable, and cannot be saved." +
                    "\n\n It's contents are now corrupted. " +
                    "\n\n This was likely caused by an unserializable object within an array, which is a fatal problem." +
                    "\n\n See stack trace." +
                    "\n\n ${e.message}")
            e.printStackTrace()
        }

        dirty = false
    }

    var dirty = false
        private set

    /**
     * When this flag is high, then the registry
     * is automatically saved when [dirty] is called.
     *
     * This is low for initalisation and loading,
     * but automatically raised once the registry is ready
     * for use.
     */
    var autosave = false

    fun dirty() {
        if (autosave)
            forceFlush()
        else
            dirty = true
    }

    /**
     * (re)loads from disk.
     *
     * If there is no file on disk, then [flush]es to create one.
     */
    final fun load (): Boolean {
        if (saveLocation.exists()) {
            try {
                (loadObjectFromDisk(saveLocation) as Registry<T>).let {
                    clear()
                    it.entries.map { put(it.key, it.value) }
                    gc()
                }

                validate()

                return true

            } catch (e : Exception) {
                mvcIfAvail()?.apply {
                    Project.logError(e)
                    showWarningNotification("A Registry was reset.", "A project or IDE config file was incompatible with this version of the IDE, so it was upgraded. Settings it contained have been reset to their defaults.")
                }

                reset()
            }
        } else
            forceFlush()
        return false
    }


    /**
     * Clears the registry, resets all values to defaults, then saves the changes to the disk.
     */
    fun reset() {
        autosave = false
        clear()
        initialize()
        autosave = true
        forceFlush()
    }



    /**
     * Invoked to create data, when there is nothing on the disk to load from.
     */
    protected open fun initialize() {}

    /**
     * Invoked so you can check the data within the registry is as you'd expect.
     *
     * Throw exceptions or correct it.
     */
    open fun validate() {}

    companion object {

        /**
         * Confirms that a key's name ends with a valid type.
         *
         * @see keyClass for valid types.
         */
        fun isValidType(key: Any?, value: Any) =
            value::class.simpleName == keyType(key)

        /**
         * Returns the NAME of the type
         * associated with this key.
         */
        fun keyType(key: Any?) =
            keyClass(key).simpleName

        /**
         * Returns the [KClass] of the type
         * associated with this key.
         */
        fun keyClass(key: Any?) : KClass<*> {
            return when (key.toString().split("_").last()) {
                // IF THIS LIST IS ALTERED, UPDATE THE LIST CLASS DOCS.
                "BOOL" -> Boolean::class
                "INT" -> Integer::class
                "SHORT" -> Short::class
                "LONG" -> Long::class
                "DOUBLE" -> Double::class
                "STRING" -> String::class
                "DOC" -> Document::class
                "ARRAY" -> ArrayList::class
                else -> throw IllegalStateException("Preference key name is not suffixed with a permitted data type : $key")
            }
        }
    }


    class MismatchedTypeException(key : Any?, value: Any) : IllegalArgumentException("[DEV - DO NOT COMMIT] Registry value was ${value::class.simpleName}, but the key states that it should be ${keyType(key)}")
    class MissingEntryException(key: Any?) : Exception("$key wass requested, but it was not created when the registry was. ")
}