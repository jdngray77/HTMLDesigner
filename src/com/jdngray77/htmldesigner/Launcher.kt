package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.html.style.StyleSheet
import com.jdngray77.htmldesigner.html.dom.Tag.Companion.test
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
        lateinit var INSTANCE : HTMLDesigner
    }


    override fun start(stage: Stage) {
        INSTANCE = this

        val scene = FXMLUtility.loadFXMLScene("MainView.fxml")

        stage.isFullScreen = true
        stage.scene = scene
        stage.show()

        test()
        Project.test()
        StyleSheet.test()
    }
}


