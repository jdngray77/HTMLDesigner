
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

package com.jdngray77.htmldesigner.backend.data

import com.jdngray77.htmldesigner.backend.html.dom.html
import org.jsoup.nodes.Document

// Format :
// EPIC_NOUN_PROPERTY_DATATYPE
// aka
// MAJORSYSTEM_FEATURE_BEHAVIOUR_DATATYPE

enum class PREFERENCE {

    // Examples.
    EDITOR_THEME_DARK_BOOL,
    EDITOR_UNDOHISTORY_LENGTH_INT,

    PROJECT_AUTOEXPORT_BOOL,
    PROJECT_SKIPVALIDATION_BOOL,
    PROJECT_BACKUP_DEPTH_INT
}


/**
 * Stores values for the keys of [PREFERENCE] in
 * a project.
 */
class Preferences : HashMap<PREFERENCE, Any>() {
    init {
        put(PREFERENCE.EDITOR_THEME_DARK_BOOL, true)
        put(PREFERENCE.EDITOR_UNDOHISTORY_LENGTH_INT, 30)
        put(PREFERENCE.PROJECT_AUTOEXPORT_BOOL, false)
        put(PREFERENCE.PROJECT_SKIPVALIDATION_BOOL, false)
        put(PREFERENCE.PROJECT_BACKUP_DEPTH_INT, 20)
    }


    /**
     * Gets an entry from the preferences.
     *
     * @param T The type of data
     * @param key The data to retrieve.
     */
    fun <T> read(key : PREFERENCE): Any {
        try {
            return get(key)!!
        } catch (e : ClassCastException) {
            throw IllegalArgumentException("Tried to read a preferences value as the wrong type.")
        } catch (e : NullPointerException) {
            throw IllegalStateException("No preferences entry for : $key")
        }
    }


    fun typeOf(key: PREFERENCE) : String? {
        return when (key.name.split("_").last()) {
            "BOOL" -> Boolean::class.simpleName
            "INT" -> Integer::class.simpleName
            "SHORT" -> Short::class.simpleName
            "LONG" -> Long::class.simpleName
            "DOUBLE" -> Double::class.simpleName
            "String" -> Boolean::class.simpleName
            "DOC" -> Document::class.simpleName
            "HTML" -> html::class.simpleName
            else -> throw IllegalStateException("Preference key name did not end in a listed data type : ${key.name}")
        }
    }

    // TODO Write to file.
    fun flush (){
        TODO()
    }
}