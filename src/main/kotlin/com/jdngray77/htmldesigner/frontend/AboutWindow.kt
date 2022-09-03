package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage

class AboutWindow {


    init {
        // TODO create a proper About window FXML
        val splash = loadFXMLComponent<AnchorPane>("SplashScreen.fxml")

        Stage().apply {
            scene = Scene(splash.first)
            isResizable = false
            show()
        }
    }
}