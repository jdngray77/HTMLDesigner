
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


enum class USER_PREF {
    EXPORT_AUTO_ENABLE_BOOL,
    EXPORT_AUTO_FREQUENCY_INT,

    MISC_VALIDATION_SKIP_BOOL,

    BACKUP_DEPTH_INT,
    BACKUP_ENABLE_BOOL
}

/**
 * A registry used to store project-specific preferences.
 *
 * Instances of this class are found serialized in the [Project].
 */
class Preferences : Registry<USER_PREF>() {

    init {

        // CONFIGURE DEFAULT PROJECT PREFERENCES HERE.

        put(USER_PREF.EXPORT_AUTO_ENABLE_BOOL, true)
        put(USER_PREF.EXPORT_AUTO_FREQUENCY_INT, 10)
        put(USER_PREF.MISC_VALIDATION_SKIP_BOOL, false)
        put(USER_PREF.BACKUP_DEPTH_INT, 20)
        put(USER_PREF.BACKUP_ENABLE_BOOL, true)

        validate()
    }


    /**
     * Redundancy check for developers to ensure
     * that all preference keys have been initalized.
     */
    private fun validate() {
        // TODO i want this validation to happen on boot,
        //      rather than project creation - otherwise
        //      it could still get missed.
        USER_PREF.values().map {
            // This throws an exception if the key did not exist.
            get(it)
        }
    }
}