
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

import com.jdngray77.htmldesigner.backend.BackgroundTask
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.data.Project.Companion.projectFile
import com.jdngray77.htmldesigner.backend.data.config.ProjectPreference
import com.jdngray77.htmldesigner.backend.userConfirm
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.Editor.Companion.project
import com.jdngray77.htmldesigner.utility.ButtonType_CLOSEWITHOUTSAVE
import com.jdngray77.htmldesigner.utility.ButtonType_SAVE
import javafx.application.Platform
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.layout.BorderPane
import javafx.scene.web.WebView
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
 * In the GUI, this is a [tab] that opens within the IDE displaying a document.
 * The IDE will access and edit the [DocumentEditor] that is selected.
 *
 * All docks will update to display and edit the selected editor via
 * [EventType.EDITOR_DOCUMENT_SWITCH].
 *
 * The editor is created in [MVC.openEditor]. The document is also
 * set here.
 */
class DocumentEditor {

    //region initalization

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

        tab.text = document.title()

        clean()
        reRender()
    }


    //#region initalization


    /**
     * The document being edited.
     */
    lateinit var document : Document
        private set

    /**
     * The file on disk where the [document] is located.
     *
     * This is where it will be saved to.
     */
    lateinit var file : File
        private set

    /**
     * A [WebView] placed inside the [tab] that displays the [Document]
     * to the user.
     *
     * @see [reRender]
     */
    @FXML lateinit var contentRenderer : WebView

    /**
     * The tab displayed in the IDE, which contains this editor.
     */
    lateinit var tab: Tab

    /**
     * The root container of this [DocumentEditor]
     *
     * See the parenting FXML file.
     */
    @FXML lateinit var editorRoot : BorderPane

    /**
     * Label which shows the zoom level to the user.
     */
    @FXML lateinit var lblZoom: Label




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
        set(value) {
            //FIXME these debug lines will be saved into the output document.

            // Guard against repetition ; issue #19.
            if (value == field) return

            field?.removeClass("debug-outline")
            field = value
            field?.addClass("debug-outline")

            reRender()
            EventNotifier.notifyEvent(EventType.EDITOR_SELECTED_TAG_CHANGED)
        }



    //region dirty

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
     */
    fun documentChanged() {
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)
        reRender()

        if (isDirty) return

        tab.text += DIRTY_SUFFIX
        isDirty = true
    }

    fun clean() {
        tab.text = document.title()
        isDirty = false
    }


    //endregion Dirty


    /**
     * Updates [WebView] to display the current state of the [document]
     */
    fun reRender() = contentRenderer.engine.loadContent(document.toString())

    /**
     * Saves the document this editor is for.
     */
    fun save() {
        mvc().Project.saveDocument(document)
        clean()
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                   EDITING
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




    /**
     * Asks this editor to close.
     *
     * Identical to the user clicking the 'x'.
     *
     * Performs [tab.onCloseRequest], and if the user is happy to close,
     * [forceClose] is used to close the tab.
     *
     * @return false if will not close.
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

    fun zoomOut() {
        contentRenderer.zoom -= project().PREFERENCES[ProjectPreference.ZOOM_STEP_SIZE_DOUBLE] as Double
        zoomChanged()
    }

    fun zoomIn() {
        contentRenderer.zoom += project().PREFERENCES[ProjectPreference.ZOOM_STEP_SIZE_DOUBLE] as Double
        zoomChanged()
    }

    private fun zoomChanged() {
        lblZoom.text = "${(contentRenderer.zoom * 100).roundToInt()}%"
    }

    fun resetZoom() {
        contentRenderer.zoom = 1.0
        zoomChanged()
    }

    fun back() {
        Platform.runLater {
            contentRenderer.engine.executeScript("history.back()")
        }
    }

    fun forward() {
        Platform.runLater {
            contentRenderer.engine.executeScript("history.forward()")
        }
    }

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


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                   GUI
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


}