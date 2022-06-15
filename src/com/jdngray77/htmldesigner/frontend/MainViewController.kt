package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.Pages
import com.jdngray77.htmldesigner.frontend.docks.ProjectDock
import com.jdngray77.htmldesigner.frontend.docks.TagHierarchy
import com.jdngray77.htmldesigner.frontend.docks.TagProperties
import com.jdngray77.htmldesigner.frontend.docks.dockutils.ExampleAutoDock
import com.jdngray77.htmldesigner.frontend.docks.dockutils.TestDock
import com.jdngray77.htmldesigner.frontend.docks.toolbox.ToolboxDock
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.web.HTMLEditor
import org.jsoup.nodes.Document
import java.io.File


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
            textEditor_Open(mvc().currentDocument().html())
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
        dockLeftTop.tabs.add(Tab(ToolboxDock::class.simpleName!!.CamelToSentence(), ToolboxDock()))
        dockLeftTop.tabs.add(Tab(ExampleAutoDock::class.simpleName!!.CamelToSentence(), ExampleAutoDock()))
        dockLeftTop.tabs.add(Tab(TestDock::class.simpleName!!.CamelToSentence(), TestDock()))

        dockLeftBottom.tabs.add(Tab(Pages::class.simpleName!!.CamelToSentence(), Pages()))
        dockLeftBottom.tabs.add(Tab(TagHierarchy::class.simpleName!!.CamelToSentence(), TagHierarchy()))
        dockLeftBottom.tabs.add(Tab(ProjectDock::class.simpleName!!.CamelToSentence(), ProjectDock()))

        dockRight.tabs.add(Tab(TagProperties::class.simpleName!!.CamelToSentence(), TagProperties()))
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Setup
    //region                                                  MCV API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
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
    //region                                                    Menu
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun menu_debug_err() {
        throw Exception("This is a test error, not a real problem.")
    }

    fun menu_debug_dirty() {
        mvc().currentEditor().documentChanged()
    }

    fun menu_debug_showcache() {
        showInformationalAlert(
            "Project files loaded into cache are : "
            +
            mvc().Project.getCache().entries.joinToString {
                it.key + if (File(it.key).exists()) "" else "(Missing)" +"\n"
            }
        )
    }

    fun menu_debug_showserial() {
        val serial = Project::class.hashCode()
        CopyToClipboard(serial.toString())
        showInformationalAlert("The current project serial hash version is : \n $serial")
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                 Menu
    //region                                          Private Utility Methods
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




}


