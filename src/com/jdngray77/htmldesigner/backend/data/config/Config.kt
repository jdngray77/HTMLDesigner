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

import java.io.File

/**
 * Keys for the [config] registry.
 *
 * See the [Documentation](https://github.com/jdngray77/HTMLDesigner/wiki/Registry-Keys)
 */
enum class Configs {

    LAST_PROJECT_PATH_STRING,
    DARK_MODE_BOOL,
    SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL,
    TOOLBOX_DOCK_FILTER_EXACT_BOOL,
    USE_MAC_MENU_BOOL,

    OUTLINE_SELECTED_TAG_BOOL
}

/**
 * The IDE specific registry.
 *
 * Stores settings and state that's not relative to a given project.
 *
 * Saved in the pwd.
 */
object Config : Registry<Configs>(File("./HTMLDesignerCfg.registry")) {

    init {
        // FIXME i don't like this work-around.
        defferedInit()
    }

    override fun initialize() {
        put(Configs.DARK_MODE_BOOL, true)
        put(Configs.LAST_PROJECT_PATH_STRING, "")
        put(Configs.SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL, true)
        put(Configs.TOOLBOX_DOCK_FILTER_EXACT_BOOL, true)
        put(Configs.USE_MAC_MENU_BOOL, true)
        put(Configs.OUTLINE_SELECTED_TAG_BOOL, true)
    }

    override fun validate() {
        Configs.values().map {
            get(it)
        }
    }
}