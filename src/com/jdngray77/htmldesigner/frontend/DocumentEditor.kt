
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

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.data.Project.Companion.projectFile
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.web.WebView
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File

/**
 * # Central document editor.
 *
 * Holds open documents in order to make and save changes.
 *
 * **Does not handle modifying the [document]. See [MVC].**
 *
 * In the GUI, this is a tab that opens within the IDE displaying a document.
 * The IDE will access and edit the selected editor.
 *
 * All of the docks will update to display and edit the selected editor.
 */
class DocumentEditor {

    /**
     * The document being edited.
     */
    lateinit var document : Document
        private set

    /**
     * The document being edited.
     */
    lateinit var file : File
        private set

    /**
     * The tab displayed in the IDE, which contains the [contentRenderer]
     */
    lateinit var tab: Tab

    /**
     * A [WebView] that displays the [Document]
     *
     * @see [reRender]
     */
    @FXML lateinit var contentRenderer : WebView

    /**
     * A tag selected via the [TagHierarchy].
     *
     * Provides context for the IDE to perform tag modifications.
     */
    var selectedTag: Element? = null
        set(value) {
            // Guard against repetition ; issue #19.
            if (value == field) return

            field?.removeClass("debug-outline")
            field = value
            field?.addClass("debug-outline")
            //FIXME these debug lines will be saved into the output document.
            reRender()
            EventNotifier.notifyEvent(EventType.EDITOR_SELECTED_TAG_CHANGED)
        }



    //#region Dirty


    /**
     * When high, indicates that this editor has been changed.
     *
     * Marked high when [documentChanged] is called, and remains so until [document]
     * is saved via [save]
     */
    var isDirty = false
        private set

    /**
     * Notifies the editor that the document has beem modified.
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


    //#endregion Dirty


    /**
     * For creation only.
     *
     * Rejects if has been set already.
     */
    @Deprecated("Used only to create. Don't modify the document.")
    fun setDocument(document: Document, tab: Tab) {
        if (this::document.isInitialized) return

        this.file = document.projectFile()
        this.document = document
        this.tab = tab

        clean()
        reRender()
    }

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

    fun requestClose() {
        val e = Event(EDITOR_CLOSE_REQUEST)
        tab.onCloseRequest?.handle(e)

        if (e.isConsumed) return

        forceClose()
    }

    fun forceClose() {
        mvc().MainView.dockEditors.tabs.remove(tab)

        tab.onClosed?.handle(null)
    }

    companion object {

        /**
         * A particle appended to the name when [isDirty]
         */
        private const val DIRTY_SUFFIX = " *"

        private val EDITOR_CLOSE_REQUEST = javafx.event.EventType<Event>("EDITOR_CLOSE_REQUEST")
    }
}