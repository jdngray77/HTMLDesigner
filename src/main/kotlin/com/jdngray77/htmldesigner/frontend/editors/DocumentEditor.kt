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

package com.jdngray77.htmldesigner.frontend.editors

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.BackgroundTask.onUIThread
import com.jdngray77.htmldesigner.backend.data.Project.Companion.projectFile
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.data.config.ProjectPreference
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.IDE.Companion.project
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.focus
import com.jdngray77.htmldesigner.utility.*
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
 * [Editor] for a HTML document.
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
class DocumentEditor(

    /**
     * The document to edit.
     */
    document: Document

) : Editor<SerializableDocument>(

    // Load the GUI from fxml.
    loadFXMLComponent<BorderPane>("DocumentEditor.fxml", DocumentEditor::class.java, this).first,

    // initial document value, within a wrapper for the undo/redo history.
    SerializableDocument(document)
){

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                  Data
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



    /**
     * The document held by this editor.
     *
     * Remember, this file does not actually edit the
     * [document].
     */
    var document: Document = document
        private set


    /**
     * The file on disk where the [document] is located.
     *
     * This is where it will be saved to.
     */
    var file: File = document.projectFile()
        private set



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                 Data
    //region                                    FXML
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    // This includes both the @FXML nodes and the FXML event methods.

    /**
     * A [WebView] that displays the [Document] to the user.
     *
     * @see [reRender]
     */
    @FXML
    var contentRenderer: WebView = root.lookup("#contentRenderer") as WebView

    /**
     * Button which shows the zoom level to the user,
     * and allows them to reset the zoom when clicked.
     */
    @FXML
    var btnZoom: Button = root.lookup("#btnResetZoom") as Button

    /**
     * Breadcrumb bar for the element the user has selected.
     *
     * Selects elements that are clicked.
     */
    @FXML
    var breadCrumb: BreadCrumbBar<Element> = root.lookup("#breadCrumb") as BreadCrumbBar<Element>


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
    override fun reset() {
        selectTag(null)
        resetZoom()

        project().apply {
            removeFromCache(document)
            document = loadDocument(file)
        }

        setAction("Reset changes made to ${file.nameWithoutExtension}")

        focus()
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
    //endregion                               FXML
    //region                                Editing support
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

            // Clear breadcrumb.
            // If selecting via hierarchy, it will be configured again correctly.
            // If not, it will remain empty.
            // Particularly important if the selected tag is cleared, or made obsolete
            // when undoing or redoing.
            breadCrumb.selectedCrumb = null

            unhighlightSelectedTag()
            field = value
            highlightSelectedTag()

            reRender()
            EventNotifier.notifyEvent(EventType.EDITOR_SELECTED_TAG_CHANGED)
        }

    fun highlightSelectedTag() {
        if (Config[Configs.OUTLINE_SELECTED_TAG_BOOL] as Boolean && selectedTag?.hasClass("debug-outline") == false)
            selectedTag?.addClass("debug-outline")
    }

    fun unhighlightSelectedTag() {
        selectedTag?.removeClass("debug-outline")
    }


    override fun onHistoryTraversed() {
        selectedTag = null
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //#region standalone edit mode
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * A color picker that determines the background color of the editor
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
    private val standaloneNotificationPane = NotificationPane(
        "You're in standalone edit mode. Only the selected tag will be rendered.",
        "Exit standalone edit mode"
    ) { standaloneEditMode = false }.apply {
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

            (root as BorderPane).top =
                if (field)
                    standaloneNotificationPane
                else
                    null

            reRender()
        }

    fun toggleStandaloneEditMode() {
        standaloneEditMode = !standaloneEditMode
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
    }

    /**
     * Selects the given element, but does and populates the [breadCrumb].
     */
    fun selectTag(treeItem: TreeItem<Element>) {
        selectedTag = treeItem.value
        breadCrumb.selectedCrumb = treeItem
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                          Editing support
    //region                             Super events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    // But remember, this does not actually manipulate any data. That's the MVC's job.


    /**
     * Notifies this document editor that the document it holds has been modified.
     *
     * Marks the editor dirty & in need of saving.
     * Also notifies of document edit, and [reRender]'s [contentRenderer].
     */
    override fun onDocumentChanged() {
        unhighlightSelectedTag()
        value = SerializableDocument(document)


    }

    override fun afterDocumentChanged() {

        // If document no longer matches, deserialize the current version.
        // TODO this will likely get slow for large documents.
        //      There would be a noticable delay on every fucking change they make. me no like.
        value.get().let {
            if (!document.equalsDocument(it))
                document = it
        }

        // Make sure the project cache holds the current INSTANCE of document.
        // Instances de-sync because of the serialization on the undo/redo stack.
        mvc().Project.assertCached(file, document)

        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)
        highlightSelectedTag()
        reRender()
    }

    /**
     * Updates [WebView] to display the current state of the [document]
     */
    fun reRender() {
        onUIThread {
            contentRenderer.engine.loadContent(
                if (standaloneEditMode)
                    Element("div")
                        .attr(
                            "style",
                            "background: ${standaloneColorPicker.value.toHex()}; height: 100%; width: 100%; ${
                                if (standaloneCenter.isSelected)
                                    "display: flex; " +
                                            "justify-content: center; " +
                                            "align-items: center; "
                                else ""
                            }"
                        )
                        .appendChild(selectedTag!!.clone()).outerHtml()
                else
                    document.toString()

            )
        }
    }

    /**
     * Saves the document this editor is for.
     */
    override fun save() : Boolean {
        val tag = selectedTag
        selectedTag = null

        try {
            mvc().Project.saveDocument(document)
        } catch (e: Exception) {
            ExceptionListener.uncaughtException(Thread.currentThread(),e)
            return false
        }

        selectedTag = tag
        clean()

        return true
    }

    override fun validate() {
        if (!file.exists()) {
            clean()
            requestClose()
        } else {
            mvc().Project.assertCached(
                file, document
            )
        }
    }


    override fun onCloseRequested(e: Event) {
        mvc().MainView.let {
            if (dirty) {

                val result = userConfirm(
                    "${document.title()} has not been saved. Save?",
                    ButtonType_SAVE,
                    ButtonType_CLOSEWITHOUTSAVE,
                    ButtonType.CANCEL
                )

                if (result == ButtonType_SAVE) {

                    save()
                    it.setAction("Closed ${document.title()}")

                } else if (result == ButtonType_CLOSEWITHOUTSAVE) {

                    if (!userConfirm("You're absolutely sure you don't want to save ${document.title()}?")) {
                        e.consume()
                        it.setAction("Closed ${document.title()} without saving.")
                        // TODO take a backup of document not saved.
                    }

                } else {
                    it.setAction("Not closing ${document.title()} ; It's not been saved.")
                    e.consume()
                }

                // Otherwise don't consume, close the editor.
            }
        }
    }

    override fun onClosed() {
        mvc().Project.removeFromCache(document)
    }




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
    init {
        // Update zoom label.
        zoomChanged()

        // Breadcrumb only shows tag name
        breadCrumb.setCrumbFactory {
            BreadCrumbBar.BreadCrumbButton(
                (it.value as Element).userString()
            )
        }

        // When the user click's a crumb, select that tag.
        breadCrumb.setOnCrumbAction {
            selectTag(it.selectedCrumb)
        }

        title = document.title()
        reRender()
    }
}