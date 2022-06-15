package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.MVC
import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage


/**
 * Main entry point to the front end.
 *
 * Launches the FXML, configures and stores
 * references to the [EDITOR] and the [mvc].
 */
class Editor : Application() {

    companion object {

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
                return EDITOR.mvc != null
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
    private lateinit var scene: Pair<Scene, MainViewController>

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

        // Load the main view from FXML.
        // It's controller will take over from here.
        scene = loadFXMLScene("MainView.fxml") as Pair<Scene, MainViewController>

        // Fullscreen the window, and show it.
        if (!System.getProperty("os.name").contains("Mac"))
            stage.isFullScreen = true

        stage.scene = scene.first
//        stage.scene.stylesheets.add("stylesheet.css");
        stage.show()

        mvc = MVC(usrChooseProject(), scene.second)
    }

    /**
     * The boot behaviour project chooser.
     *
     * Will obtain a project from the user,
     * weather it be new or existing, then
     * returns it.
     *
     * Handles cancellation & problems.
     * User will just be prompted [implUsrSelectProject]
     * until a project is obtained.
     */
    private fun usrChooseProject() : Project {
        var projToLoad : Project? = null

        while (true) {
            try {
                projToLoad = implUsrSelectProject()
            } catch (e: Exception) {
                showErrorAlert("Project access failed : \n${e.message}")
            }

            if (projToLoad == null)
                continue
            else
                return projToLoad
        }
    }

    /**
     * A single instance of [usrChooseProject],
     * where a flow of dialogs and choosers are used to create
     * or load a project.
     *
     * Can return null if cancelled, or throw exceptions
     * if project access or creation fails.
     */
    private fun implUsrSelectProject(): Project? {

        val ButtonTypeLoad = ButtonType("Load existing Project")
        val ButtonTypeCreate = ButtonType("Create a new Project")

        val x = Alert(Alert.AlertType.INFORMATION, "Welcome! What would you like to do?", ButtonTypeCreate, ButtonTypeLoad)
        x.showAndWait()

        return when (x.result) {
            ButtonTypeLoad -> {
                Project.load(DirectoryChooser().let {
                    it.title = "Locate a project root"
                    it.showDialog(stage).path
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

}
