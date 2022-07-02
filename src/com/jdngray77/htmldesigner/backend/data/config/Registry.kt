

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

import com.jdngray77.htmldesigner.backend.html.dom.html
import org.jsoup.nodes.Document
import java.nio.file.Path

// Format :
// EPIC_NOUN_PROPERTY_DATATYPE
// aka
// MAJORSYSTEM_FEATURE_BEHAVIOUR_DATATYPE


/**
 * A HashMap for storing configurable values.
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
 */
open class Registry <T> : HashMap<T, Any>() {

    /**
     * Enters a value into the registry,
     * if the type matches
     */
    final override fun put(key: T, value: Any) =
        if (!isValidType(key, value))
            throw MismatchedTypeException(key, value)
        else
            super.put(key, value)

    final override fun putAll(from: Map<out T, Any>) {
        from.map {
            put(it.key, it.value)
        }
    }

    final override fun get(key: T): Any {
        try {
            return super.get(key)!!
        } catch (e : ClassCastException) {
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
                "STRING" -> Boolean::class.simpleName
                "DOC" -> Document::class.simpleName
                "HTML" -> html::class.simpleName
                else -> throw IllegalStateException("Preference key name is not suffixed with a permitted data type : $key")
            }
        }
    }

    // TODO Write to file.
    fun flush (){
        TODO()
    }

    class MismatchedTypeException(key : Any?, value: Any) : IllegalArgumentException("[DEV - DO NOT COMMIT] Registry value was ${value::class.simpleName}, but the key states that it should be ${keyType(key)}")
    class MissingEntryException(key: Any?) : Exception("$key was not requested, but it was not created when the registry was. ")
}