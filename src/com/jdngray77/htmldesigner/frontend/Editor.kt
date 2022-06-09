package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.MVC
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.loadFXMLScene
import com.sun.org.apache.xpath.internal.operations.Bool
import javafx.application.Application
import javafx.scene.Scene
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
         */
        fun mvc() = EDITOR.mvc!!

        /**
         * Returns the [mvc], if it is available.
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
         * > ***NOTE WELL : CANNOT BE ACCESSED BEFORE THE EDITOR HAS LOADED.***
         */
        fun project() = EDITOR.mvc!!.Project

        /**
         * Static reference to the model view controller.
         *
         * > ***NOTE WELL : CANNOT BE ACCESSED BEFORE THE EDITOR HAS LOADED.***
         */
        fun maingui() = EDITOR.mvc!!.MainView
    }

    /**
     * The model view controller for this editor.
     *
     * A mid-point between the front-end and back-end.
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
     * Loads and initalises the GUI.
     *
     * No access to the editor or the MVC may
     * ocour until this method has returned.
     */
    override fun start(stage: Stage) {
        EDITOR = this

        // Load the main view from FXML.
        // It's controller will take over from here.
        scene = loadFXMLScene("MainView.fxml") as Pair<Scene, MainViewController>

        // Fullscreen the window, and show it.
        if (!System.getProperty("os.name").contains("Mac"))
            stage.isFullScreen = true


        stage.scene = scene.first
        stage.show()
        mvc = MVC(Project.loadOrCreate("./testproject/"), scene.second)
    }
}
