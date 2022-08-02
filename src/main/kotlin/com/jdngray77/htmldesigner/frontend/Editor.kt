
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

package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.MVC
import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.utility.Restartable
import com.jdngray77.htmldesigner.utility.everyInstanceOf
import com.jdngray77.htmldesigner.utility.loadFXMLScene
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.MenuBar
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.lang.System.gc


/**
 * Main entry point to the front end.
 *
 * Launches the FXML, configures and stores
 * references to the [EDITOR] and the [mvc].
 */
class Editor : Application() {

    companion object {

        private val ButtonTypeLoad = ButtonType("Load existing Project")

        private val ButtonTypeCreate = ButtonType("Create a new Project")

        /**
         * A static reference to the application instance
         */
        @Deprecated("Don't directly access", ReplaceWith("mvc(), project(), maingui"))
        lateinit var EDITOR : Editor

        /**
         * Static reference to the model view controller.
         *
         * > ***NOTE WELL : CANNOT BE ACCESSED BEFORE THE EDITOR HAS LOADED.***
         *
         * If accessing from somewhere where the mvc *may* not yet be loaded,
         * consider using [mvcIfAvail]
         */
        fun mvc() = EDITOR.mvc!!

        /**
         * Returns the [mvc], if it is available.
         *
         * Slower, but adds null safety where it's required.
         *
         * For use in places where the access is optional,
         * and calls may be made early - but it doesn't matter
         * if the mvc does not exist yet.
         */
        fun mvcIfAvail() = if (mvcIsAvail()) mvc() else null

        /**
         * Returns true if [mvc] can return the MVC.
         */
        fun mvcIsAvail() : Boolean {
            try {
                return this::EDITOR.isInitialized && EDITOR.mvc != null
            } catch (e : EarlyEditorAccessException) {
                return false
            }
        }

        /**
         * Static reference to the model view controller.
         *
         * > ***NOTE WELL : CANNOT BE ACCESSED IF NOT [mvcIsAvail].***
         */
        fun project() = EDITOR.mvc!!.Project

        /**
         * Static reference to the model view controller.
         *
         * > ***NOTE WELL : CANNOT BE ACCESSED IF NOT [mvcIsAvail].***
         */
        fun maingui() = EDITOR.mvc!!.MainView
    }

    /**
     * The model view controller for the IDE.
     *
     * Acts as a mid-point between the front-end (particularly the current document editor) and back-end.
     */
    var mvc : MVC? = null
        set(value) {
            field = value
            EventNotifier.notifyEvent(EventType.EDITOR_LOADED)
        }
    get() = field?: run {throw EarlyEditorAccessException()}

    private class EarlyEditorAccessException() : java.lang.NullPointerException("The Editor or MVC was accessed before it was initialised. Early accesses to a subscriber of EDITOR_LOADED")

    /**
     * A tuple of the JavaFX Scene which hosts the [MainView],
     * and it's [MainViewController].
     *
     * Typically, the controller is accessed using [maingui]
     * via the [mvc]
     */
    lateinit var scene: Pair<Scene, MainViewController>
        private set

    /**
     * The stage that is used to show scenes.
     */
    lateinit var stage: Stage

    /**
     * Loads and initalises the GUI.
     *
     * No access to the editor or the MVC may
     * ocour until this method has returned.
     */
    override fun start(stage: Stage) {

        this.stage = stage

        EDITOR = this

        //TODO invoke something else, something empty.
        Config.load()

        // Load the main view from FXML.
        // It's controller will take over from here.
        scene = loadFXMLScene("MainView.fxml") as Pair<Scene, MainViewController>

        // Fullscreen the window, and show it.
        if (!System.getProperty("os.name").contains("Mac"))
            stage.isFullScreen = true
        else
            if (Config[Configs.USE_MAC_MENU_BOOL] as Boolean)
                (scene.first.lookup("#MenuBar") as MenuBar)
                    .useSystemMenuBarProperty().set(true)


        stage.scene = scene.first



        mvc = MVC(determineProject(), scene.second)
    }

    /**
     * Interruptable shutdown routine.
     *
     * Performs a clean shutdown of the application.
     *
     * Called by [exit], and by JavaFX.
     *
     * @throws InterruptedException if the shutdown is interrupted, typically by the user.
     */
    @Deprecated("Use 'exit' to request closure.")
    override fun stop() {

        // ==================
        // Perform interruptable operations.
        // Ensure we're clear to shutdown.
        // ==================

        mvcIfAvail()?.apply {
            getOpenEditors().forEach {
                if (!it.requestClose()) {
                    showNotification("Shutdown or restart aborted", "An editor refused to close.")
                    throw InterruptedException("Shutdown or restart aborted. An editor refused to close.")
                }
            }
        }


        // ==================
        // Clear to close. Commit to actually closing IDE.
        // ==================

        EventType.EXIT.notify()

        // Notify every [Restartable] that we're restarting / closing.
        everyInstanceOf(Restartable::class).forEach {
            (it as Restartable).restart()
        }
    }

    /**
     * Requests the application to close.
     *
     * @see stop
     */
    fun exit() {
        stop()
        Platform.exit()
    }

    /**
     * Closes the current project & restarts fresh.
     */
    fun closeProject() {
        Config[Configs.LAST_PROJECT_PATH_STRING] = ""
        restart()
    }

    /**
     * Performs the shutdown routine, then the startup routine.
     *
     * Just be aware that this is a soft restart, so only non-static things are reloaded.
     *
     * Static classes in the JVM persist. For cases where this matters, see [Restartable].
     */
    fun restart() {
        stop()
        gc()

        start(stage)
    }


    /**
     * Sub routine of the startup.
     *
     * Determines what the IDE should load.
     *
     * If [Configs.AUTO_LOAD_PROJECT_BOOL] permits, and [Configs.LAST_PROJECT_PATH_STRING] is set, this will
     * auto load the project path stored.
     *
     * If not, the user will be prompted.
     * @see usrChooseProject
     */
    private fun determineProject(): Project =
        if (!(Config[Configs.AUTO_LOAD_PROJECT_BOOL] as Boolean) || Config[Configs.LAST_PROJECT_PATH_STRING] == "")
            usrChooseProject().also {
                Config.put(Configs.LAST_PROJECT_PATH_STRING, it.locationOnDisk.path)
            }
        else
            Project.load(Config[Configs.LAST_PROJECT_PATH_STRING] as String) ?: usrChooseProject()

    /**
     * Startup subroutine that prompts the user to choose a project.
     *
     * Will obtain a project from the user,
     * weather it be new or existing, then
     * returns it.
     *
     * Handles cancellation & problems.
     *
     * User will just be prompted [implUsrSelectProject] until a project is obtained.
     *
     * This will not return until the user has chosen a project.
     */
    private fun usrChooseProject() : Project {
        var projToLoad : Project? = null

        while (true) {
            try {
                projToLoad = implUsrSelectProject()
            } catch (e: Exception) {
                showErrorAlert("Project access failed : \n(${e::class.simpleName})\n\n${e.message ?: "No reason was provided"}")
            }

            if (projToLoad == null)
                continue
            else
                return projToLoad
        }
    }

    /**
     * A subroutine of [usrChooseProject]
     * where a flow of dialogs and choosers are used to create
     * or load a project.
     *
     * Can return null if cancelled, or throw exceptions
     * if project access or creation fails.
     */
    private fun implUsrSelectProject(): Project? =
        when (
            Alert(Alert.AlertType.INFORMATION, "Welcome! What would you like to do?", ButtonTypeCreate, ButtonTypeLoad).also { it.showAndWait() }.result
        ) {

            ButtonTypeLoad -> {
                Project.load(DirectoryChooser().let {
                    it.title = "Locate a project root"
                    it.showDialog(stage)?.path ?: run { return@implUsrSelectProject null }
                })
            }

            ButtonTypeCreate -> {
                Project.create(FileChooser().let {
                    it.title = "Create a new project"
                    it.showSaveDialog(stage).path
                })
            }

            else -> null
        }
}