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

import com.jdngray77.htmldesigner.backend.WebServer
import com.jdngray77.htmldesigner.frontend.IDE
import com.jdngray77.htmldesigner.frontend.docks.toolbox.ToolboxDock
import com.jdngray77.htmldesigner.utility.IDEEarlyBootListener
import com.jdngray77.htmldesigner.utility.subFile
import jfxtras.styles.jmetro.Style.*
import jfxtras.styles.jmetro.JMetro
import java.lang.Thread.UncaughtExceptionHandler
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import java.io.File
import java.util.function.Predicate

/**
 * Keys for the [config] registry.
 *
 * See the [Documentation](https://github.com/jdngray77/HTMLDesigner/wiki/Registries#keys)
 */
enum class Configs {

    // Java docs here appear in the Dokka HTML renderings.

    /**
     * Stores an absolute path to the project that was last loaded.
     *
     * When empty, the editor will prompt the user to select a project.
     *
     * This is cleared to close the project.
     */
    LAST_PROJECT_PATH_STRING,

    /**
     * When high, [LAST_PROJECT_PATH_STRING] will be automatically loaded on boot.
     *
     * If low, the user will always be prompted to load a project.
     *
     * Default is true
     */
    AUTO_LOAD_PROJECT_BOOL,

    /**
     * When high, [JMetro] will be used with [DARK], else [LIGHT].
     *
     * Requires a soft restart to take effect.
     *
     * Default is true
     */
    DARK_MODE_BOOL,

    /**
     * Whem high, notifications will not be raised when exceptions are
     * caught by the [UncaughtExceptionHandler].
     *
     * Default is true
     */
    SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL,

    /**
     * When high, the [ToolboxDock]'s search feature will
     * use [String.startsWith], instead of [String.contains]
     * for a more exact match.
     *
     * Default is true
     */
    TOOLBOX_DOCK_FILTER_EXACT_BOOL,

    /**
     * When high and on mac os x, the IDE will offload the menu bar to the system menu bar.
     *
     * Requires soft restart to take effect.
     *
     * Default is true
     */
    USE_MAC_MENU_BOOL,

    /**
     * When high, the selected tag will be outlined with a red border.
     *
     * Default is true
     */
    OUTLINE_SELECTED_TAG_BOOL,

    /**
     * Determines what port the [WebServer] will use.
     *
     * Default is 8080
     */
    WEB_SERVER_PORT_INT,

    /**
     * When the [WebServer] is set to auto-refresh, this
     * determines the delays between auto-refreshing.
     *
     * Time is in seconds.
     *
     * Default is 0
     */
    WEB_SERVER_REFRESH_DELAY_INT,

    KEY_BINDINGS_STRING,
    /**
     * Determines the maximum number of document states
     * that can be held in RAM for undo/redo.
     *
     * This limit is to limit the amount of memory used by the editor.
     *
     * Default is 20.
     */
    UNDO_HISTORY_MAX_INT,

    /**
     * User provided client ID.
     *
     * TODO see documentation
     */
    SPOTIFY_CLIENT_ID_STRING,

    /**
     * User provided spotify secret
     *
     * TODO see documentation
     */
    SPOTIFY_CLIENT_SECRET_STRING,

    /**
     * Spotify provided authentication token.
     *
     * This is a one time password used to authenticate the connection.
     */
    SPOTIFY_AUTH_OTP__DONT_MODIFY__STRING,

    /**
     * Spotify provided access token.
     *
     * used to access spotify and authenticate requests.
     */
    SPOTIFY_TOKEN_ACCESS__DONT_MODIFY__STRING,

    /**
     * The refresh token is used to refresh the access token
     * when it expires.
     */
    SPOTIFY_TOKEN_REFRESH__DONT_MODIFY__STRING,


    /**
     * The length of time that the [SPOTIFY_TOKEN_ACCESS__DONT_MODIFY__STRING] token is valid for,
     * before it may be refreshed.
     *
     * This is compared with the [SPOTIFY_LAST_REFRESH__DONT_MODIFY__LONG] to determine if the token
     * has expired.
     */
    SPOTIFY_ACCESS_TTL__DONT_MODIFY__INT,

    /**
     * The last time the [SPOTIFY_TOKEN_ACCESS__DONT_MODIFY__STRING] token was refreshed.
     */
    SPOTIFY_LAST_REFRESH__DONT_MODIFY__LONG,

    /**
     * When a user attempts to clear an [JsNodeEmitter] with more emitters than
     * this threshold, the user will be prompted to confirm the action as it'll
     * more of a pain-in-the-ass for them to undo.
     */
    JSDESIGNER_EMITTER_BREAKDOWN_CONFIRM_THRESHOLD_INT,

    /**
     * When validating a jsgraph, this determines if nodes
     * not reached during compile are to be kept or
     * automatically removed.
     */
    JS_GRAPH_AUTO_DELETE_UNCOMPILED_NODES_BOOL,

    /**
     * Permits JsGraph validation to attempt to automatically fix minor issues.
     */
    JS_GRAPH_AUTOFIX_MINOR_BOOL,

    /**
     * Permits JsGraph validation to attempt to automatically fix major issues.
     */
    JS_GRAPH_AUTOFIX_MAJOR_BOOL,
    /**
     * If true, the ide will show a restart suggestion prompt
     * when [LARGE_ERROR_COUNT_THRESHOLD] unhandled exceptions have been caught.
    LARGE_ERROR_COUNT_PROMPT_BOOL,

    /**
     * When [LARGE_ERROR_COUNT_PROMPT_BOOL] permits,
     * the IDE will show a restart suggestion prompt
     * every x errors.
     */
    LARGE_ERROR_COUNT_STEP_THRESHOLD_INT,

    /**
     * If high at project load, the no pages will be loaded.
     * Effectively ignoring the project's startup page preference.
     */
    IGNORE_PROJECT_STARTUP_PAGE_BOOL

     */
    KEY_BINDINGS_HASHMAP,
    KEY_BINDINGS_CAPS_WARNING_BOOL,
}

/**
 * The IDE specific registry.
 *
 * Stores settings and state that's not relative to a given project.
 *
 * Saved in the pwd.
 */
object Config : Registry<Configs>(IDE.IDEDirectory.subFile("./config.registry")), IDEEarlyBootListener {

    init {
        // FIXME i don't like this work-around.
        defferedInit()
    }

    /**
     * Provides default values for [Configs].
     *
     * Used when a registry is created for the first time, reset, or invalidated.
     */
    override fun initialize() {
        put(Configs.DARK_MODE_BOOL, true)
        put(Configs.LAST_PROJECT_PATH_STRING, "")
        put(Configs.SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL, true)
        put(Configs.TOOLBOX_DOCK_FILTER_EXACT_BOOL, true)
        put(Configs.USE_MAC_MENU_BOOL, true)
        put(Configs.OUTLINE_SELECTED_TAG_BOOL, true)
        put(Configs.AUTO_LOAD_PROJECT_BOOL, true)
        put(Configs.WEB_SERVER_PORT_INT, 8080)
        put(Configs.WEB_SERVER_REFRESH_DELAY_INT, 0)
        put(
            Configs.KEY_BINDINGS_STRING,
            """
            Menu > Edit > [WIP] Undo,Meta+Z,Ctrl+Z,Ctrl+Z
            Menu > Edit > [WIP] Redo,Meta+shift+Z,Ctrl+Y,Ctrl+Y
            """.trimIndent()
        )
        put(Configs.UNDO_HISTORY_MAX_INT, 20)
        put(Configs.SPOTIFY_CLIENT_ID_STRING, "")
        put(Configs.SPOTIFY_CLIENT_SECRET_STRING, "")
        put(Configs.SPOTIFY_TOKEN_ACCESS__DONT_MODIFY__STRING, "")
        put(Configs.SPOTIFY_TOKEN_REFRESH__DONT_MODIFY__STRING, "")
        put(Configs.SPOTIFY_ACCESS_TTL__DONT_MODIFY__INT, 0)
        put(Configs.SPOTIFY_AUTH_OTP__DONT_MODIFY__STRING, "")
        put(Configs.SPOTIFY_LAST_REFRESH__DONT_MODIFY__LONG, 0L)
        put(Configs.JSDESIGNER_EMITTER_BREAKDOWN_CONFIRM_THRESHOLD_INT, 2)
        put(Configs.JS_GRAPH_AUTO_DELETE_UNCOMPILED_NODES_BOOL, false)
        put(Configs.JS_GRAPH_AUTOFIX_MINOR_BOOL, true)
        put(Configs.JS_GRAPH_AUTOFIX_MAJOR_BOOL, false)
        put(Configs.LARGE_ERROR_COUNT_PROMPT_BOOL, true)
        put(Configs.LARGE_ERROR_COUNT_STEP_THRESHOLD_INT, 10)
        put(Configs.IGNORE_PROJECT_STARTUP_PAGE_BOOL, false)
        put(Configs.KEY_BINDINGS_CAPS_WARNING_BOOL, true)
    }

    /**
     * Confirms that all expected [Configs] are present in the registry.
     */
    override fun validate() {
        Configs.values().map {
            get(it)
        }
    }

    override fun onIDEBootEarly() {
        load()
    }
}