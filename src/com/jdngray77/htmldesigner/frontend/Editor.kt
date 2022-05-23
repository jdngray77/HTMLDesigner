package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.MVC
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.loadFXMLScene
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.File


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
        lateinit var EDITOR : Editor
    }

    lateinit var mvc : MVC
    private lateinit var controller : MainViewController
    private lateinit var scene: Pair<Scene, MainViewController>


    override fun start(stage: Stage) {
        EDITOR = this


        // Load the main view from FXML.
        // It's controller will take over from here.
        scene = loadFXMLScene("MainView.fxml") as Pair<Scene, MainViewController>

        // Fullscreen the window, and show it.
        stage.isFullScreen = true
        stage.scene = scene.first
        stage.show()
        mvc = MVC(Project.loadOrCreate("./testproject/"), scene.second)
    }
}
