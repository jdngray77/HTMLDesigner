package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.CamelToSentence
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.html.dom.Tag
import com.jdngray77.htmldesigner.frontend.docks.TagHierarchy
import com.jdngray77.htmldesigner.frontend.docks.Pages
import com.jdngray77.htmldesigner.frontend.docks.ProjectDock
import com.jdngray77.htmldesigner.frontend.docks.dockutils.TestDock
import com.jdngray77.htmldesigner.loadFXMLComponent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.web.HTMLEditor
import org.jsoup.nodes.Document


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

    @FXML lateinit var lblLeftStatus : Label
    @FXML lateinit var lblRightStatus : Label



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
            textEditor_Open(currentDocument().html())
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

        dockLeftBottom.tabs.add(Tab(Pages::class.simpleName!!.CamelToSentence(), Pages()))
        dockLeftBottom.tabs.add(Tab(TagHierarchy::class.simpleName!!.CamelToSentence(), TagHierarchy()))
        dockLeftBottom.tabs.add(Tab(ProjectDock::class.simpleName!!.CamelToSentence(), ProjectDock()))
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Setup
    //region                                                  MCV API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * It returns the document of the current editor
     */
    fun currentDocument() =
        currentEditor().document

    /**
     * It returns the current editor
     */
    fun currentEditor() =
        findDocumentFor(dockEditors.selectionModel.selectedItem)

    /**
     * Create a new document editor, set the document,
     * add the editor to the list of open editors, and switch to the
     * new editor
     *
     * @param document Document - The document to open
     */
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

                    it.setOnCloseRequest {
                        if (isDirty) {
                            if (userConfirm("${document.title()} has not been saved. Save?", ButtonType.YES, ButtonType.CANCEL) == ButtonType.YES) {
                                save()
                            } else
                                it.consume()
                        }
                    }
                }



                it.setOnClosed {
                    openEditors.remove(second)
                }
            }
        }
    }


    /**
     * This function switches to the editor tab that is passed in as a parameter
     *
     * @param editor DocumentEditor - The editor to switch to
     */
    fun switchToEditor(editor: DocumentEditor) {
        dockEditors.selectionModel.select(editor.tab)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
    }


    /**
     * "If there's an editor for the given document, switch to it, otherwise create a new editor."
     *
     * The first line of the function is a call to the function findEditorFor, which returns an Editor?. If it's not null,
     * the apply function is called on it. The apply function takes a lambda as its argument, and the lambda is executed
     * with the Editor as its receiver. The lambda in this case is a call to the function switchToEditor, which takes an
     * Editor as its argument
     *
     * @param document The document to switch to.
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



    /**
     * It returns the text from the htmlEditor.
     */
    fun textEditor_Read() = htmlEditor.htmlText

    /**
     * `textEditor_Open` opens a document in the text editor
     *
     * @param doc The document to open.
     */
    fun textEditor_Open(doc: Document) = textEditor_Open(doc.toString())

    /**
     * A function that takes a string as an argument and sets the htmlText property
     * of the htmlEditor object to the value of the string
     *
     * @param rawHTML The raw HTML to be loaded into the editor.
     */
    fun textEditor_Open(rawHTML: String) {
        htmlEditor.htmlText = rawHTML
    }


    fun setStatus(string: String) {
        lblLeftStatus.text = string
    }

    fun setAction(string: String) {
        lblRightStatus.text = string
    }



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               MCV API
    //region                                          Private Utility Methods
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun findEditorFor(document: Document)  =
        openEditors.find { it.document == document }

    fun findDocumentFor(tab: Tab) = openEditors.first {
            it.tab == tab
    }


}


