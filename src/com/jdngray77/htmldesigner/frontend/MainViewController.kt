package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.CamelToSentence
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.html.dom.Tag
import com.jdngray77.htmldesigner.frontend.docks.Hierarchy
import com.jdngray77.htmldesigner.frontend.docks.ExampleAutoDock
import com.jdngray77.htmldesigner.frontend.docks.ProjectDock
import com.jdngray77.htmldesigner.frontend.docks.TestDock
import com.jdngray77.htmldesigner.loadFXMLComponent
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import org.jsoup.nodes.Document
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

    @FXML lateinit var dockEditors : TabPane

    @FXML lateinit var dockLeftTop : TabPane
    @FXML lateinit var dockLeftBottom : TabPane

    @FXML lateinit var dockRight : TabPane
    @FXML lateinit var dockBottom : TabPane

    @FXML lateinit var htmlEditor : HTMLEditor


    private val openEditors = ArrayList<DocumentEditor>()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                UI References
    //region                                                   Setup
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Sets up the window when it's created.
     */
    @FXML
    fun initialize() {



        htmlEditor.setOnContextMenuRequested {
            textEditor_Open(Tag.testDOM.toString())
        }

        htmlEditor.setOnKeyReleased {
//            EDITOR.mvc
//            renderer_Open(htmlEditor.htmlText)
            EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)
        }

        addDocks()
    }

    /**
     * Adds dock windows to the dock tabs.
     */
    private fun addDocks() {
//        dockLeftTop.tabs.add(Tab(ExampleAutoDock::class.simpleName!!.CamelToSentence(), ExampleAutoDock()))
        dockLeftTop.tabs.add(Tab(TestDock::class.simpleName!!.CamelToSentence(), TestDock()))

        dockLeftBottom.tabs.add(Tab(Hierarchy::class.simpleName!!.CamelToSentence(), Hierarchy()))
        dockLeftBottom.tabs.add(Tab(ProjectDock::class.simpleName!!.CamelToSentence(), ProjectDock()))
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Setup
    //region                                                  MCV API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun currentDocument() =
        currentEditor().document

    fun currentEditor() =
        findDocumentFor(dockEditors.selectionModel.selectedItem)

    fun openDocument(document: Document) {
        loadFXMLComponent<BorderPane>("DocumentEditor.fxml").apply {
            Tab(document.title(), first).let {
                dockEditors.tabs.add(it)

                first.prefWidthProperty().bind(dockEditors.widthProperty())
                first.prefHeightProperty().bind(dockEditors.heightProperty())

                (second as DocumentEditor).apply {
                    setDocument(document, it)
                    openEditors.add(this)
                    switchToEditor(this)
                }
            }
        }
    }

    /**
     * Updates the UI to display a new document.
     */
    fun switchToEditor(editor: DocumentEditor) {
        dockEditors.selectionModel.select(editor.tab)
//        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH) TODO put this back once events are threaded.
    }


    /**
     *
     */
    fun switchToDocument(document: Document) =
        findEditorFor(document)?.apply { switchToEditor(this) }
            ?: run { openDocument(document) }


    /**
     * Updates the GUI to represent
     * the [openDocument].
     *
     * Invoked after changing the document to
     * display the changes.
     */
    fun updateDisplay(document: Document) {
        textEditor_Open(document)
    }



    fun textEditor_Read() = htmlEditor.htmlText

    fun textEditor_Open(doc: Document) = textEditor_Open(doc.toString())

    fun textEditor_Open(rawHTML: String) {
        htmlEditor.htmlText = rawHTML
    }



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               MCV API
    //region                                          Private Utility Methods
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun findEditorFor(document: Document) : DocumentEditor? {
        openEditors.forEach {
            if (it.document == document) return it
        }

        return null
    }

    fun findDocumentFor(tab: Tab) = openEditors.first {
            it.tab == tab
    }


}


