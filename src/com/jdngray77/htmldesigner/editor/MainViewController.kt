package com.jdngray77.htmldesigner.editor

import com.jdngray77.htmldesigner.editor.docks.ExampleAutoDock
import com.jdngray77.htmldesigner.html.dom.Tag
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import javafx.scene.web.WebView


/**
 * UI Controller for the main window.
 */
class MainViewController {

    @FXML lateinit var contentRenderer : WebView
    @FXML lateinit var dockleft : AnchorPane
    @FXML lateinit var dockright : AnchorPane
    @FXML lateinit var dockbottom : AnchorPane
    @FXML lateinit var cssEditor : WebView

    @FXML
    fun initialize() {
        contentRenderer.engine.loadContent(Tag.testDOM.toString())

        cssEditor.engine.load("https://vscode.dev")
        addDocks()
    }

    private fun addDocks() {
        dockleft.children.add(ExampleAutoDock())
    }


}


