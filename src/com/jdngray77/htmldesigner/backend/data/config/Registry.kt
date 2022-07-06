

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

import com.jdngray77.htmldesigner.backend.html.dom.data
import com.jdngray77.htmldesigner.backend.html.dom.html
import com.jdngray77.htmldesigner.backend.utility.loadObjectFromDisk
import com.jdngray77.htmldesigner.backend.utility.saveObjectToDisk
import org.jsoup.nodes.Document
import java.io.File
import java.lang.System.gc

// Format :
// EPIC_NOUN_PROPERTY_DATATYPE
// aka
// MAJORSYSTEM_FEATURE_BEHAVIOUR_DATATYPE


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
 *  - HTML
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

    protected fun defferedInit() {
        if (saveLocation.exists())
            load()
        else {
            initialize()
            flush()
        }
    }

    /**
     * Enters a value into the registry,
     * if the type matches
     */
    final override fun put(key: T, value: Any) {
        if (!isValidType(key, value))
            throw MismatchedTypeException(key, value)
        else
            super.put(key, value)

        // TODO i don't like that this is called for every item when
        //      adding many items at once.
        flush()
    }

    final override fun putAll(from: Map<out T, Any>) {
        from.map {
            put(it.key, it.value)
        }
    }

    final override fun get(key: T): Any {
        try {
            return super.get(key)!!
        } catch (e : ClassCastException) {
            // TODO can this even be reached?
            throw IllegalArgumentException("Tried to read a preferences value as the wrong type.")
        } catch (e : NullPointerException) {
            throw MissingEntryException(key)
        }
    }

    @Deprecated("Values are not optional in the registry. Default is ignored.", ReplaceWith("get(key)"))
    final override fun getOrDefault(key: T, defaultValue: Any) =
        get(key)

    companion object {
        fun isValidType(key: Any?, value: Any) =
            value::class.simpleName == keyType(key)


        fun keyType(key: Any?) : String? {
            return when (key.toString().split("_").last()) {
                // IF THIS LIST IS ALTERED, UPDATE THE LIST CLASS DOCS.
                "BOOL" -> Boolean::class.simpleName
                "INT" -> Integer::class.simpleName
                "SHORT" -> Short::class.simpleName
                "LONG" -> Long::class.simpleName
                "DOUBLE" -> Double::class.simpleName
                "STRING" -> String::class.simpleName
                "DOC" -> Document::class.simpleName
                "HTML" -> html::class.simpleName
                else -> throw IllegalStateException("Preference key name is not suffixed with a permitted data type : $key")
            }
        }
    }

    final fun flush() = saveObjectToDisk(saveLocation)

    /**
     * (re)loads from disk.
     *
     * If there is no file on disk, then [flush]es to create one.
     */
    final fun load (): Boolean {

        return if (saveLocation.exists()) {
            (loadObjectFromDisk(saveLocation) as Registry<T>).let {
                clear()
                it.entries.map { put(it.key, it.value) }
                gc()
            }
            true
        } else {
            flush()
            false
        }
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
    protected open fun validate() {}



    class MismatchedTypeException(key : Any?, value: Any) : IllegalArgumentException("[DEV - DO NOT COMMIT] Registry value was ${value::class.simpleName}, but the key states that it should be ${keyType(key)}")
    class MissingEntryException(key: Any?) : Exception("$key wass requested, but it was not created when the registry was. ")
}