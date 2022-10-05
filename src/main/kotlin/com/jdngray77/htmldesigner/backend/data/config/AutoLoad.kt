package com.jdngray77.htmldesigner.backend.data.config

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs


/**
 * Simple object-oriented wrapper for Configs.AUTO_LOAD stuff, for convenience.
 *
 * Wraps data that determines auto-load behavior for the IDE.
 */
internal object AutoLoad {

    /**
     * @return true if [Configs.AUTO_LOAD_PROJECT_BOOL] permits auto-loading, and the there is a project to load
     *         within [Configs.AUTO_LOAD_PROJECT_PATH]
     */
    fun isAvailable() =
        (Config[Configs.AUTO_LOAD_PROJECT_BOOL] as Boolean)
                && Config[Configs.LAST_PROJECT_PATH_STRING] != ""

    /**
     * Raises [Configs.AUTO_LOAD_PROJECT_BOOL]
     */
    fun enable() {
        Config[Configs.AUTO_LOAD_PROJECT_BOOL] = true
    }

    /**
     * Lowers [Configs.AUTO_LOAD_PROJECT_BOOL]
     */
    fun disable() {
        Config[Configs.AUTO_LOAD_PROJECT_BOOL] = false
    }

    /**
     * @return the value of [Configs.AUTO_LOAD_PROJECT_PATH]
     */
    fun getLastProjectLoaded() =
        Config[Configs.LAST_PROJECT_PATH_STRING] as String

    /**
     * Sets [Configs.AUTO_LOAD_PROJECT_PATH] to nothing.
     *
     * Makes [AutoLoad] not [isAvailable]
     */
    fun clearLastProjectLoaded() {
        Config[Configs.LAST_PROJECT_PATH_STRING] = ""
    }

    /**
     * Stores a project that could be loaded at next boot.
     *
     * Typically set when project is loaded.
     *
     * [Configs.AUTO_LOAD_PROJECT_PATH] to the specified value.
     */
    fun storeLastProjectLoaded(path: String) {
        Config[Configs.LAST_PROJECT_PATH_STRING] = path
    }
}