package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.CamelToSentence
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.html.dom.Document
import com.jdngray77.htmldesigner.frontend.docks.ExampleAutoDock
import com.jdngray77.htmldesigner.frontend.docks.TestDock
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import java.net.URI


/**
 * Front-End controller for the Main View.
 *
 * Manages the UI of the main window only.
 *
 * Must
 *
 */
class MainViewController {

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                   UI References.
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    @FXML lateinit var contentRenderer : WebView
    @FXML lateinit var dockleft : TabPane
    @FXML lateinit var dockright : TabPane
    @FXML lateinit var dockbottom : AnchorPane
    @FXML lateinit var htmlEditor : HTMLEditor


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                UI References
    //region                                                   Setup
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Sets up the window when it's created.
     */
    @FXML
    fun initialize() {
        htmlEditor.setOnKeyReleased {
//            EDITOR.mvc
            renderer_Open(htmlEditor.htmlText)
        }

        addDocks()
    }

    /**
     * Adds dock windows to the dock tabs.
     */
    private fun addDocks() {
        dockleft.tabs.add(Tab(ExampleAutoDock::class.simpleName!!.CamelToSentence(), ExampleAutoDock()))
        dockleft.tabs.add(Tab(TestDock::class.simpleName!!.CamelToSentence(), TestDock()))
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Setup
    //region                                                  MCV API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Updates the UI to display a new document.
     */
    fun switchToDocument(document: Document) {
        updateDisplay(document)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
    }

    /**
     * Updates the GUI to represent
     * the [openDocument].
     *
     * Invoked after changing the document to
     * display the changes.
     */
    fun updateDisplay(document: Document) {
        renderer_Open(document)
        textEditor_Open(document)
    }




    fun textEditor_Read() = htmlEditor.htmlText

    fun textEditor_Open(doc: Document) = textEditor_Open(doc.serialize())

    fun textEditor_Open(rawHTML: String) {
        htmlEditor.htmlText = rawHTML
    }



    private fun renderer_Open(document: Document) =
        renderer_Open(document.serialize())

    fun renderer_Open(rawHTML: String) =
        contentRenderer.engine.loadContent(rawHTML)

    fun renderer_Open(URL: URI) =
        contentRenderer.engine.load(URL.toString())



}


