
/*░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
 ░                                                                                                ░
 ░ Jordan T. Gray's                                                                               ░
 ░                                                                                                ░
 ░          HTML Designer                                                                         ░
 ░                                                                                                ░
 ░ FOSS 2022.                                                                                     ░
 ░ License decision pending.                                                                      ░
 ░                                                                                                ░
 ░ https://www.github.com/jdngray77/HTMLDesigner/                                                 ░
 ░ https://www.jordantgray.uk                                                                     ░
 ░                                                                                                ░
 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.Editor.Companion.project
import com.jdngray77.htmldesigner.frontend.controls.RegistryEditor
import com.jdngray77.htmldesigner.frontend.controls.RunAnything
import com.jdngray77.htmldesigner.frontend.docks.*
import com.jdngray77.htmldesigner.frontend.docks.dockutils.ExampleAutoDock
import com.jdngray77.htmldesigner.frontend.docks.tagproperties.TagProperties
import com.jdngray77.htmldesigner.frontend.docks.toolbox.ToolboxDock
import com.jdngray77.htmldesigner.utility.CopyToClipboard
import com.jdngray77.htmldesigner.utility.camelToSentence
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import org.jsoup.nodes.Document
import java.io.File
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext


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

    @FXML lateinit var documentation : WebView






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
        documentation.engine.load("https://www.github.com/Jdngray77/HTMLDesigner/wiki")
    }

    /**
     * Adds dock windows to the dock tabs.
     */
    private fun addDocks() {

        // TODO automate fetching of class names.

        // TOP LEFT

        dockLeftTop.tabs.add(Tab(ToolboxDock::class.simpleName!!.camelToSentence(), ToolboxDock()))
        dockLeftTop.tabs.add(Tab(Prefabs::class.simpleName!!.camelToSentence(), Prefabs()))

        dockLeftTop.tabs.add(Tab(ExampleAutoDock::class.simpleName!!.camelToSentence(), ExampleAutoDock()))
//        dockLeftTop.tabs.add(Tab(TestDock::class.simpleName!!.camelToSentence(), TestDock()))


        // BOTTOM LEFT

        dockLeftBottom.tabs.add(Tab(Pages::class.simpleName!!.camelToSentence(), Pages()))
        dockLeftBottom.tabs.add(Tab(TagHierarchy::class.simpleName!!.camelToSentence(), TagHierarchy()))
        dockLeftBottom.tabs.add(Tab(ProjectDock::class.simpleName!!.camelToSentence(), ProjectDock()))

        // RIGHT

        dockRight.tabs.add(Tab(TagProperties::class.simpleName!!.camelToSentence(), TagProperties()))
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

    fun menu_debug_reset_projprefs() =
        mvc().Project.PREFERENCES.reset()


    fun menu_debug_reset_config() =
        Config.reset()

    fun menu_soft_restart() =
        Editor.EDITOR.restart()

    fun menu_project_close() =
        Editor.EDITOR.closeProject()

    fun menu_registry() {
        RegistryEditor(Config).showDialog()
    }

    fun menu_debug_eval() {
        ScriptEngineManager().engineFactories.forEach { println(it.extensions) }
        val input = userInput("Enter Kotlin code to evaluate in the MVC.")

        showInformationalAlert(ScriptEngineManager().getEngineByExtension("kts")!!.eval(input).toString())
    }

    fun menu_debug_run_anything(actionEvent: ActionEvent) {
        RunAnything.showDialog()
    }

    fun menu_project_projprefs(actionEvent: ActionEvent) {
        RegistryEditor(mvc().Project.PREFERENCES).showDialog()
    }

    fun menu_file_revert() {
        mvc().currentEditor().apply {
            // Store the current tab position
            val index = dockEditors.tabs.indexOf(tab)

            // Close and re-load from disk
            forceClose()
            project().removeFromCache(document)
            mvc().openDocument(file)

            // Move the new editor to the same index
            mvc().currentEditor().tab.let {
                with(dockEditors) {
                    tabs.remove(it)
                    tabs.add(index, it)
                    selectionModel.select(it)
                }
            }
        }
    }

    fun menu_file_save() {
        mvc().currentEditor().save()
    }

    fun menu_exit() {
        Platform.exit()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                 Menu
    //region                                          Private Utility Methods
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




}


