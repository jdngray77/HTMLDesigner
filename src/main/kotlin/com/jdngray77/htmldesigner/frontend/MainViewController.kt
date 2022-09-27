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

import com.jdngray77.SplashX6.audio.Spotify
import com.jdngray77.SplashX6.audio.SpotifyAuthHelper
import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.Editor.Companion.project
import com.jdngray77.htmldesigner.frontend.controls.RegistryEditor
import com.jdngray77.htmldesigner.frontend.controls.RunAnything
import com.jdngray77.htmldesigner.frontend.docks.*
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.frontend.docks.tagproperties.TagProperties
import com.jdngray77.htmldesigner.frontend.docks.toolbox.ToolboxDock
import com.jdngray77.htmldesigner.frontend.jsdesigner.VisualScriptEditor
import com.jdngray77.htmldesigner.utility.CopyToClipboard
import com.jdngray77.htmldesigner.utility.camelToSentence
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import com.jdngray77.htmldesigner.utility.openURL
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import org.eclipse.fx.ui.controls.tabpane.DndTabPane
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory.FeedbackType
import org.eclipse.fx.ui.controls.tabpane.skin.DnDTabPaneSkin
import org.jsoup.nodes.Document
import java.io.File
import java.lang.System.gc
import javax.script.ScriptEngineManager


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

    @FXML
    lateinit var root: VBox

    @FXML
    lateinit var anchorDockLeftBottom: VBox

    @FXML
    lateinit var anchorDockLeftTop: VBox

    @FXML
    lateinit var anchorDockRight: VBox

    @FXML
    lateinit var dockEditors: TabPane

    @FXML
    lateinit var dockLeftTop: DndTabPane

    @FXML
    lateinit var dockLeftBottom: DndTabPane

    @FXML
    lateinit var dockRight: DndTabPane

    @FXML
    lateinit var dockBottom: TabPane

    @FXML
    lateinit var htmlEditor: HTMLEditor

    @FXML
    lateinit var lblLeftStatus: Label

    @FXML
    lateinit var lblRightStatus: Label

    @FXML
    lateinit var documentation: WebView

    /**
     * Stupid image view in the GUI to view the album art.
     *
     * Added to [dockRight] in [initialize]
     */
    val albumTest = ImageView()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                UI References
    //region                                                   Setup
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Sets up the window when it's created.
     */
    @FXML
    fun initialize() {

        // Load the menu bar - It's now stored in a seperate file.
        root.children.add(0, loadFXMLComponent<MenuBar>("MenuBar.fxml").first)

        val skin = DnDTabPaneSkin(dockLeftTop)
        val skin1 = DnDTabPaneSkin(dockLeftBottom)
        val skin2 = DnDTabPaneSkin(dockRight)

        // Setup the dragging.
        DndTabPaneFactory.setup(FeedbackType.MARKER, anchorDockLeftTop, skin)
        DndTabPaneFactory.setup(FeedbackType.MARKER, anchorDockLeftBottom, skin1)
        DndTabPaneFactory.setup(FeedbackType.MARKER, anchorDockRight, skin2)

        dockLeftTop.skin = skin
        dockLeftBottom.skin = skin1
        dockRight.skin = skin2
        anchorDockRight.children.add(albumTest)



        htmlEditor.setOnContextMenuRequested {
            textEditor_Open(mvc().currentDocument().html())
        }

        htmlEditor.setOnKeyReleased {
//            EDITOR.mvc
//            renderer_Open(htmlEditor.htmlText)
            EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)
        }

        // Trigger switch event when user switches tabs.
        dockEditors.selectionModel.selectedItemProperty().addListener { _, _, _ ->
            EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
        }

        dockEditors.background = Background(
            BackgroundImage(
                Image("/com/jdngray77/htmldesigner/frontend/template.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
            )
        )

        addDocks()
        documentation.engine.load("https://www.github.com/Jdngray77/HTMLDesigner/wiki")
    }

    /**
     * Adds dock windows to the dock tabs.
     */
    internal fun addDocks() {

        // TOP LEFT
        implAddDock(dockLeftTop, ToolboxDock(), Prefabs())

        // BOTTOM LEFT
        implAddDock(dockLeftBottom, TagHierarchy(), Pages(), ProjectDock())

        // RIGHT
        implAddDock(dockRight, TagProperties(), TestDock(), HistoryDock())

        // BOTTOM
        implAddDock(dockBottom, VisualScriptEditor.new().second)
    }

    private fun implAddDock(to: TabPane, vararg it: Dock) {
        it.forEach {
            to.tabs.add(
                Tab(
                    it::class.simpleName!!.camelToSentence(),
                    it
                )
            )
        }
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

    var lastStatus = ""

    fun showLastStatus() {
        showListOfStrings("Last status", lastStatus.lines())
    }

    fun setStatus(string: String) {
        lastStatus = string
        val lines = string.lines()

        var output = lines.first()

        if (lines.size > 1)
            output += "..."

        lblLeftStatus.text = output
    }

    fun setAction(string: String) {
        lblRightStatus.text = string
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               MCV API
    //region                                          Private Utility Methods
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    companion object {

        internal fun clearDock(dock: TabPane) {
            dock.tabs.apply {
                map {
                    if (it is Subscriber)
                        EventNotifier.unsubscribeFromAll(it)
                }
                clear()
            }
        }

    }

}


