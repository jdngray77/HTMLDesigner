
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

import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.utility.subFile


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
class ProjectPreferences(project: Project) : Registry<USER_PREF>(project.locationOnDisk.subFile("ProjectPrefs.registry")) {

    init {
        defferedInit()
    }

    override fun initialize() {
        put(USER_PREF.EXPORT_AUTO_ENABLE_BOOL, true)
        put(USER_PREF.EXPORT_AUTO_FREQUENCY_INT, 10)
        put(USER_PREF.MISC_VALIDATION_SKIP_BOOL, false)
        put(USER_PREF.BACKUP_DEPTH_INT, 20)
        put(USER_PREF.BACKUP_ENABLE_BOOL, true)
    }

    override fun validate() {
        // Simply try to read every value.
        // This throws an exception if the key did not exist.
        USER_PREF.values().map { get(it) }
    }
}