package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.html.style.StyleSheet
import com.jdngray77.htmldesigner.html.dom.Tag.Companion.test
import com.jdngray77.htmldesigner.project.Project
import javafx.application.Application
import javafx.stage.Stage

fun main() = Application.launch(HTMLDesigner::class.java)

/**
 * Main entry point.
 *
 * Launches the FXML application.
 */
class HTMLDesigner : Application() {

    companion object {
        /**
         * A statis reference to the application instance
         */
        lateinit var INSTANCE : HTMLDesigner
    }


    override fun start(stage: Stage) {
        INSTANCE = this

        // Load the main view from FXML.
        // It's controller will take over from here.
        val scene = loadFXMLScene("editor/MainView.fxml")

        // Fullscreen the window, and show it.
        stage.isFullScreen = true
        stage.scene = scene
        stage.show()

        // Run some test methods.
        // TODO use a proper JUnit, for fucks sake broskie
        //test()
        StyleSheet.test()
    }
}


