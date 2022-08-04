
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
import com.jdngray77.htmldesigner.backend.data.Project.Companion.projectFile
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.data.config.ProjectPreference
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.Editor.Companion.project
import com.jdngray77.htmldesigner.utility.ButtonType_CLOSEWITHOUTSAVE
import com.jdngray77.htmldesigner.utility.ButtonType_SAVE
import com.jdngray77.htmldesigner.utility.SerializableDocument
import com.jdngray77.htmldesigner.utility.toHex
import com.sun.javafx.scene.control.skin.Utils
import javafx.application.Platform
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.web.WebView
import org.controlsfx.control.BreadCrumbBar
import org.controlsfx.control.ToggleSwitch
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File
import kotlin.math.roundToInt

/**
 * # Central document editor.
 *
 * This file is the FXML Controller for the 'DocumentEditor.fxml' GUI.
 *
 * It holds an open document in order to make and save changes, but note that
 * it **Does not handle modifying the [document]. For that, see [MVC].**
 *
 * In the IDE's interface, this is a [tab] that opens within the IDE displaying a document.
 * The IDE will access and edit the [DocumentEditor] that is selected.
 *
 * There is a reference to the [tab] used to hold this [DocumentEditor] for convenience.
 *
 * All docks will update to display and edit the selected editor via
 * [EventType.EDITOR_DOCUMENT_SWITCH].
 *
 * The editor is created in [MVC.openEditor]. The document is also
 * set here.
 */
class DocumentEditor {

    //region initialization

    /**
     * Late 'init' called by FXML.
     *
     * Kotlin init is too early. The GUI and @FXML lateinits won't have
     * been created yet.
     *
     * This automatically the tab in the main GUI, and places this
     * [DocumentEditor] into it, and configures the teardown logic.
     *
     * > N.B During initalization, there is no document yet.
     *
     * The editor is created in [MVC.openEditor]. The document is also
     * set here.
     */
    @FXML
    fun initialize() {
        val mainView = mvc().MainView

        // Create a Tab, store it locally, and add it to the main editor gui.
        tab = Tab("", editorRoot).let {
            mainView.dockEditors.tabs.add(it)


            it.setOnCloseRequest {
                if (isDirty) {
                    val result = userConfirm("${document.title()} has not been saved. Save?", ButtonType_SAVE, ButtonType_CLOSEWITHOUTSAVE, ButtonType.CANCEL)
                    if (result == ButtonType_SAVE) {
                        save()
                        mainView.setAction("Closed ${document.title()}")
                    } else if (result == ButtonType_CLOSEWITHOUTSAVE){
                        if (!userConfirm("You're absolutely sure you don't want to save ${document.title()}?")) {
                            it.consume()
                            mainView.setAction("Closed ${document.title()} without saving.")
                            // TODO take a backup of document not saved.
                        }
                    } else {
                        mainView.setAction("Not closing ${document.title()} ; It's not been saved.")
                        it.consume()
                    }
                }
            }

            it.setOnClosed {
                mvc().onEditorClosed(this)
            }

            it
        }


        // Update zoom label.
        zoomChanged()

        // Breadcrumb only shows tag name
        breadCrumb.setCrumbFactory {
            BreadCrumbBar.BreadCrumbButton(
                it.value.tagName() + if (it.value.id().isNotBlank()) "#${it.value.id()}" else ""
            )
        }

        // When the user click's a crumb, select that tag.
        breadCrumb.setOnCrumbAction {
            selectTag(it.selectedCrumb)
        }
    }



    /**
     * For creation only.
     *
     * Rejects if has been set already.
     */
    @Deprecated("Only used for creation. You can't change the document.")
    fun setDocument(document: Document) {
        if (this::document.isInitialized) return

        this.file = document.projectFile()
        this.document = document

        documentHistory = DocumentUndoRedo(SerializableDocument(document))

        tab.text = document.title()

        clean()
        reRender()
    }


    //#endregion initalization


    /**
     * The document held by this editor.
     *
     * Remember, this file does not actually edit the
     * [document].
     */
    lateinit var document : Document
        private set

    lateinit var documentHistory : DocumentUndoRedo<SerializableDocument>
        private set

    /**
     * The file on disk where the [document] is located.
     *
     * This is where it will be saved to.
     */
    lateinit var file : File
        private set


    /**
     * The tab displayed in the IDE, which contains this editor.
     *
     * Close request listeners are automatically added to this tab
     * when the editor is created, that prevent closing
     * if [isDirty].
     */
    lateinit var tab: Tab

    /**
     * A [WebView] that displays the [Document] to the user.
     *
     * @see [reRender]
     */
    @FXML lateinit var contentRenderer : WebView

    /**
     * The root container of this [DocumentEditor]
     *
     * Contains the [WebView], [breadCrumb], and toolbar.
     *
     * Is placed directly into the [tab].
     *
     * See the parenting FXML file.
     */
    @FXML lateinit var editorRoot : BorderPane

    /**
     * Button which shows the zoom level to the user,
     * and allows them to reset the zoom when clicked.
     */
    @FXML lateinit var btnZoom: Button

    /**
     * Breadcrumb bar for the element the user has selected.
     *
     * Selects elements that are clicked.
     */
    @FXML lateinit var breadCrumb: BreadCrumbBar<Element>




    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                   EDITING
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    // But remember, this does not actually manipulate any data. That's the MVC's job.


    /**
     * A tag selected via the [TagHierarchy].
     *
     * Provides context for the MVC to perform tag modifications.
     */
    var selectedTag: Element? = null
        private set(value) {

            // Guard against repetition ; issue #19.
            if (value == field) return

            // Guard against excluding head
            if (value == document.body()) return

            // Exit standalone if no tag is selected.
            if (value == null) standaloneEditMode = false

            field?.removeClass("debug-outline")
            field = value

            if (Config[Configs.OUTLINE_SELECTED_TAG_BOOL] as Boolean)
                field?.addClass("debug-outline")

            reRender()
            EventNotifier.notifyEvent(EventType.EDITOR_SELECTED_TAG_CHANGED)
        }


    fun undo() {
        document = documentHistory.undo().get()
        onDocumentChanged()
    }

    fun redo() {
        document = documentHistory.redo().get()
        onDocumentChanged()
    }




    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //#region standalone edit mode
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Determines the background color of the editor
     * when in standalone edit mode.
     */
    private val standaloneColorPicker = ColorPicker().also {
        it.value = Color.web("#808080")
        it.setOnAction {
            reRender()
        }
    }

    /**
     * A toggle switch that determines if the content is center aligned when in standalone edit mode.
     */
    private val standaloneCenter = ToggleSwitch("Align to center").also {
            it.isSelected = true
            it.setOnMouseClicked {
                reRender()
            }
        }

    /**
     * A notification pane shown when the editor is in standalone edit mode.
     *
     * Contains controls to exit standalone edit mode, background color and toggle center alignment.
     */
    private val standaloneNotificationPane = NotificationPane("You're in standalone edit mode. Only the selected tag will be rendered.", "Exit standalone edit mode") {standaloneEditMode = false}.apply {
        (content as BorderPane).apply {
            val vbox = HBox(
                right,
                standaloneColorPicker,
                standaloneCenter
            ).apply {
                spacing = 20.0
            }
            right = vbox
        }
    }


    /**
     * Standalone editor mode.
     *
     * When true, [selectedTag] becomes the only content to be rendered.
     *
     * if [Configs.STANDALONE_MODE_ALIGN_CENTER_BOOL] is true, the content will be
     * rendered in the center of the editor.
     *
     * Cannot be enabled while [selectedTag] is null, and
     * clearing selectedTab will clear this flag.
     */
    var standaloneEditMode: Boolean = false
        set(value) {
            if (selectedTag == null)
                return

            field = value


            editorRoot.top =
                if (field)
                    standaloneNotificationPane
                else
                    null



            reRender()
        }




    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //#endregion standalone edit mode
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




    /**
     * Selects the given element, but does not populate the [breadCrumb].
     *
     * A [TreeItem] from [TagHierarchy]'s [TreeTableView] or equivellant
     * is required to populate the [breadCrumb].
     */
    @Deprecated("This method of selecting a tag cannot not populate the breadcrumb view, giving the user no feedback on thier selection.")
    fun selectTag(tag: Element?) {
        selectedTag = tag
        breadCrumb.selectedCrumb = null
    }

    /**
     * Selects the given element, but does and populates the [breadCrumb].
     */
    fun selectTag(treeItem: TreeItem<Element>) {
        selectedTag = treeItem.value
        breadCrumb.selectedCrumb = treeItem
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region dirty
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * When raised, this flag indicates that this editor has been changed.
     *
     * Marked high when [documentChanged] is called, and remains so until [document]
     * is saved via [save]
     */
    var isDirty = false
        private set

    /**
     * Notifies this document editor that the document it holds has been modified.
     *
     * Marks the editor dirty & in need of saving.
     * Also notifies of document edit, and [reRender]'s [contentRenderer].
     *
     */
    fun documentChanged() {
        // TODO auto detection using hashes, on document access?
        onDocumentChanged()
        documentHistory.push(SerializableDocument(document))

        if (isDirty) return

        tab.text += DIRTY_SUFFIX
        isDirty = true
    }

    private fun onDocumentChanged() {
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)
        reRender()
    }

    fun clean() {
        tab.text = document.title()
        isDirty = false
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion Dirty
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Updates [WebView] to display the current state of the [document]
     */
    fun reRender() {
        contentRenderer.engine.loadContent(
            if (standaloneEditMode)
                Element("div")
                    .attr("style",
                        "background: ${standaloneColorPicker.value.toHex()}; height: 100%; width: 100%; ${
                            if (standaloneCenter.isSelected) 
                                    "display: flex; " +
                                    "justify-content: center; " +
                                    "align-items: center; "
                            else ""}")
                    .appendChild(selectedTag!!.clone()).outerHtml()
            else
                document.toString()

        )
    }

    /**
     * Saves the document this editor is for.
     */
    fun save() {
        val tag = selectedTag
        selectedTag = null
        mvc().Project.saveDocument(document)
        selectedTag = tag
        clean()
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                   EDITING
    //region                                      GUI APU
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    // This section contains functions called by the FXML framework.



    /**
     * Asks this editor to close.
     *
     * Identical to the user clicking the 'x'.
     *
     * Performs [tab.onCloseRequest], and if the user is happy to close,
     * [forceClose] is used to close the tab.
     *
     * @return true if tab closed, false if request was refused.
     */
    fun requestClose(): Boolean {
        val e = Event(EDITOR_CLOSE_REQUEST)
        tab.onCloseRequest?.handle(e)

        if (e.isConsumed) return false

        forceClose()
        return true
    }

    /**
     * Force closes the tab, without saving or asking the user.
     */
    @Deprecated("This risks the user losing changes to their document. Prefer the use of requestClose.")
    fun forceClose() {
        mvc().MainView.dockEditors.tabs.remove(tab)
        tab.onClosed?.handle(null)
    }

    /**
     * Zoom out
     *
     * decreases the zoom level of the content renderer by the zoom step size
     */
    fun zoomOut() {
        contentRenderer.zoom -= project().PREFERENCES[ProjectPreference.ZOOM_STEP_SIZE_DOUBLE] as Double
        zoomChanged()
    }

    /**
     * Zoom in button
     *
     * increases the zoom level of the content renderer by the zoom step size
     */
    fun zoomIn() {
        contentRenderer.zoom += project().PREFERENCES[ProjectPreference.ZOOM_STEP_SIZE_DOUBLE] as Double
        zoomChanged()
    }

    private fun zoomChanged() {
        btnZoom.text = "${(contentRenderer.zoom * 100).roundToInt()}%"
    }


    /**
     * [btnZoom]
     *
     * Resets the zoom level to 1.0
     */
    fun resetZoom() {
        contentRenderer.zoom = 1.0
        zoomChanged()
    }

    /**
     * Reset the editor to its default state.
     *
     * i.e undoes the visual fiddling about that the user
     * may have done, like zooming, vising other pages, or selecting tags.
     */
    fun resetEditor() {
        selectTag(null)
        resetZoom()
        reRender()
    }


    /**
     * Navigates back a page.
     *
     * FIXME
     *      Known issue : This will not navigate to the first
     *                    page in the history.
     */
    fun back() {
        Platform.runLater {
            contentRenderer.engine.executeScript("history.back()")
        }
    }

    /**
     * Navigates forward a page.
     */
    fun forward() {
        Platform.runLater {
            contentRenderer.engine.executeScript("history.forward()")
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                      GUI APU
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    companion object {

        /**
         * A particle appended to the name when [isDirty]
         */
        private const val DIRTY_SUFFIX = " *"

        /**
         * A JavaFX event thrown about when this editor creates close request events.
         * Nothing important, just an empty placeholder request.
         * It's stored here because event types can only be created once.
         */
        private val EDITOR_CLOSE_REQUEST = javafx.event.EventType<Event>("EDITOR_CLOSE_REQUEST")
    }
}