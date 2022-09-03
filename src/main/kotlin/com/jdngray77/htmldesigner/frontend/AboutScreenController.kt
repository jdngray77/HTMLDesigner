package com.jdngray77.htmldesigner.frontend

import javafx.fxml.FXML
import javafx.scene.control.Label

class AboutScreenController {

    lateinit var lblIDEVer: Label
    lateinit var lblProjectVer: Label
    lateinit var lblJsGraphVer: Label
    lateinit var lblRegistryVer: Label


    @FXML
    fun initialize() {
        lblIDEVer.text = Editor.getVersionString()
    }
}