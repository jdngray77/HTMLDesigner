
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
import com.jdngray77.htmldesigner.backend.data.project.ProjectStructure
import com.jdngray77.htmldesigner.utility.subFile

/**
 * Keys for the [ProjectPreferences] registry.
 *
 * See the [Documentation](https://github.com/jdngray77/HTMLDesigner/wiki/Registry-Keys)
 */
enum class ProjectPreference {

    /**
     * Number of backups of the project to store on the disk.
     *
     * Higher the number, the more disk space consumed.
     *
     * Default is 20.
     */
    BACKUP_DEPTH_INT,

    /**
     * When high, the project will be auto-backed up.
     */
    BACKUP_ENABLE_BOOL,

    /**
     * Alters how large the effect of the zoom buttons is.
     *
     * Zoom is a floating percentage (0 = 0%, 1 = 100%).
     *
     * Default is .1 (10% zoom change per click).
     */
    ZOOM_STEP_SIZE_DOUBLE,


    /**
     * When loading the project, the page that will be opened by default.
     */
    STARTUP_PAGE_PATH_STRING,

    /**
     * When loading the project the user is warned if the project directory name is
     * different to the project name.
     *
     * This suppresses that after being declined once.
     */
    SUPPRESS_PROJECT_NAME_MISMATCH_WARNING_BOOL,

    /**
     * When making changes in direct edit mode, changes made within
     * this delay are considered part of the same edit.
     *
     * @see com.jdngray77.htmldesigner.utility.ResettableEventTimer
     */
    DIRECT_EDIT_CHANGE_DELAY_MS_LONG,
}

/**
 * A registry used to store project-specific preferences.
 *
 * Instances of this class are found serialized in the [Project].
 */
class ProjectPreferences(project: Project) : Registry<ProjectPreference>(
    project.fileStructure.subFile(ProjectStructure.PROJECT_PATH_PREFERENCES)
) {

    init {
        defferedInit()
    }

    override fun initialize() {
        put(ProjectPreference.BACKUP_DEPTH_INT, 20)
        put(ProjectPreference.BACKUP_ENABLE_BOOL, true)
        put(ProjectPreference.ZOOM_STEP_SIZE_DOUBLE, .1)
        put(ProjectPreference.STARTUP_PAGE_PATH_STRING, "index.html")
        put(ProjectPreference.SUPPRESS_PROJECT_NAME_MISMATCH_WARNING_BOOL, false)
        put(ProjectPreference.DIRECT_EDIT_CHANGE_DELAY_MS_LONG, 1000L)
    }

    override fun validate() {
        // Simply try to read every value.
        // This throws an exception if the key did not exist.
        ProjectPreference.values().map { get(it) }
    }
}