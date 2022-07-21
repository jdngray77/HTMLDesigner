
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
import com.jdngray77.htmldesigner.utility.subFile

/**
 * Keys for the [ProjectPreferences] registry.
 *
 * See the [Documentation](https://github.com/jdngray77/HTMLDesigner/wiki/Registry-Keys)
 */
enum class ProjectPreference {
    BACKUP_DEPTH_INT,
    BACKUP_ENABLE_BOOL,
    ZOOM_STEP_SIZE_DOUBLE
}

/**
 * A registry used to store project-specific preferences.
 *
 * Instances of this class are found serialized in the [Project].
 */
class ProjectPreferences(project: Project) : Registry<ProjectPreference>(project.locationOnDisk.subFile("ProjectPrefs.registry")) {

    init {
        defferedInit()
    }

    override fun initialize() {
        put(ProjectPreference.BACKUP_DEPTH_INT, 20)
        put(ProjectPreference.BACKUP_ENABLE_BOOL, true)
        put(ProjectPreference.ZOOM_STEP_SIZE_DOUBLE, .1)
    }

    override fun validate() {
        // Simply try to read every value.
        // This throws an exception if the key did not exist.
        ProjectPreference.values().map { get(it) }
    }
}