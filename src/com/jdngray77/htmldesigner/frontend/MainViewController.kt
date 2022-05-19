package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.CamelToSentence
import com.jdngray77.htmldesigner.frontend.docks.ExampleAutoDock
import com.jdngray77.htmldesigner.frontend.docks.TestDock
import com.jdngray77.htmldesigner.backend.html.dom.Tag
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView


/**
 * Front-End controller for the Main View.
 *
 * Manages the UI of the main window only.
 * Data handling should not be here.
 */
class MainViewController {

    @FXML lateinit var contentRenderer : WebView
    @FXML lateinit var dockleft : TabPane
    @FXML lateinit var dockright : TabPane
    @FXML lateinit var dockbottom : AnchorPane
    @FXML lateinit var htmlEditor : HTMLEditor

    @FXML
    fun initialize() {
        contentRenderer.engine.loadContent(Tag.testDOM.toString())
//        contentRenderer.engine.load("https://jordantgray.uk")




        htmlEditor.setOnContextMenuRequested {
            //htmlEditor.htmlText = Tag.testDOM.TagModel.children.first().children.first().toString()
            htmlEditor.htmlText = contentRenderer.engine.executeScript("document.documentElement.outerHTML") as String
        }

        htmlEditor.setOnKeyReleased {
//            Tag.testDOM.TagModel.children.first().children.first().content(
//                htmlEditor.htmlText
//            )
            val x = contentRenderer.engine.document.documentElement.firstChild
            contentRenderer.engine.loadContent(htmlEditor.htmlText)
        }

        addDocks()
    }

    private fun addDocks() {
        dockleft.tabs.add(Tab(ExampleAutoDock::class.simpleName!!.CamelToSentence(), ExampleAutoDock()))
        dockleft.tabs.add(Tab(TestDock::class.simpleName!!.CamelToSentence(), TestDock()))
    }


}


