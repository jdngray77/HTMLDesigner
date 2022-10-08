package com.jdngray77.htmldesigner.frontend.editors

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.KeyBindings.bindKey
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.editors.jsdesigner.VisualScriptEditor
import com.jdngray77.htmldesigner.utility.*
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File

/**
 * Global manager for all open editors within the IDE
 *
 * An [Editor] may edit a [Document] or a [Prefab] or something else,
 * but they are all controlled and managed by this class
 *
 * @author Jordan T. Gray
 */
object EditorManager : IDEEarlyBootListener {

    override fun onIDEBootEarly() {
        openEditors.clear()
    }

    init {
        bindKey(KeyBindings.KeyEvent.EDITOR_REQUEST_CLOSE) { activeEditor()?.requestClose() }
        bindKey(KeyBindings.KeyEvent.EDITOR_UNDO) { activeEditor()?.undo() }
        bindKey(KeyBindings.KeyEvent.EDITOR_UNDO) { activeEditor()?.redo() }
        bindKey(KeyBindings.KeyEvent.EDITOR_REQUEST_SAVE) { activeEditor()?.requestSave() }
        bindKey(KeyBindings.KeyEvent.EDITOR_NEXT) { nextEditor() }
        bindKey(KeyBindings.KeyEvent.EDITOR_PREVIOUS) { previousEditor() }
    }

    /**
     * The tab pane within the GUI that contains the editor tabs.
     */
    private val editorDock: TabPane
        get() = mvc().MainView.dockEditors

    /**
     * Checks that all editors are open, and that
     * there are no unassigned tabs.
     *
     * Also requests editors to validate themselves.
     */
    fun validateEditors() {
        editorDock.tabs.concmod().forEach {
            try {
                findEditorByTab(it).requestValidation()
            } catch (e: IllegalArgumentException) {
                // No editor associated.
                editorDock.tabs.remove(it)
                setAction("A dead tab was removed, because it was not assigned to an editor.")
            }

            openEditors.concmod().forEach {
                if (!editorDock.tabs.contains(it.tab)) {
                    openEditors.remove(it)
                    setAction("Editor ${it.title} was closed because it was not open in the GUI.")
                }
            }
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                Raw editors
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * All editors that are open within the IDE.
     *
     * May be empty, if no editors are open.
     */
    private val openEditors = mutableListOf<Editor<*>>()

    /**
     * @return a non-mutable list of the editors that are currently open.
     */
    fun getOpenEditors(): List<Editor<*>> = openEditors.toList()

    /**
     * @return a non-mutable list of editors that are currently open that are subclasses of the given type.
     */
    inline fun <reified T> getEditorsOfType() = getOpenEditors().filterIsInstance<T>()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                Raw editors
    //region                                Utility functions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun nextEditor() {
        editorDock.selectionModel.selectNext()
    }

    fun previousEditor() {
        editorDock.selectionModel.selectPrevious()
    }


    /**
     * Returns the tab that is currently active within the GUI.
     *
     * May be null, if there are no tabs open.
     */
    fun activeTab() = editorDock.selectionModel.selectedItem

    /**
     * @return the editor for the [activeTab]
     */
    fun activeEditor() = activeTab()?.let { findEditorByTab(it) }

    /**
     * @return true if [activeTab] is not null. i.e there is at least one tab open.
     */
    fun editorAvailable() = activeEditor() != null

    /**
     * Determines if the active editor is of the given type.
     *
     * @param T the type of editor.
     * @return true if there is an editor active, and it's of type [T]
     */
    inline fun <reified T : Editor<*>> activeEditorIsType() = activeTab().let {
        it != null && findEditorByTab(it) is T
    }

    /**
     * @return the dirty flag for the active editor.
     */
    fun activeEditorIsDirty() = activeEditor()?.dirty

    /**
     * @param T the type to compare with.
     * @return true if the [activeEditor] is of type [T]
     */
    inline fun <reified T> activeEditorIsT() = activeEditor() is T

    /**
     * @return the title of the active editor.
     */
    fun activeEditorTitle() = activeEditor()?.title ?: ""

    /**
     * Searches for an [Editor] instance that is associated with the given [Tab].
     *
     * @return the [Editor] instance
     */
    fun findEditorByTab(tab: Tab) =
        openEditors.find { it.tab == tab }
            ?: throw IllegalArgumentException("Tab is not associated with an open editor.")

    /**
     * @return true if the given [Editor] is being managed correctly by this class.
     *         False indicates that the editor is not open
     */
    fun hasEditor(editor: Editor<*>) = openEditors.contains(editor) && editorDock.tabs.contains(editor.tab)

    /**
     * @return true if the given [Tab] is open and being managed correctly.
     */
    fun Editor<*>.isOpen() = hasEditor(this)

    /**
     * Switches focus to the given editor.
     */
    fun switchToEditor(editor: Editor<*>): Boolean {
        return if (hasEditor(editor) && activeTab() !== editor.tab) {
            editorDock.selectionModel.select(editor.tab)
            true
        } else
            false
    }

    /**
     * Switches focus to the given editor.
     *
     * @param editor DocumentEditor - The editor to switch to
     */
    fun Editor<*>.focus() {
        if (hasEditor(this))
            switchToEditor(this)
    }

    /**
     * Invoked by an [Editor] when created only.
     *
     * Begins tracking and managing an editor
     *
     * @param editor the editor to manage.
     */
    @Deprecated("Internal call only. Automatically called when an editor is created.")
    internal fun editorCreated(editor: Editor<*>) {
        if (!openEditors.contains(editor)) {
            openEditors.add(editor)
        }



        editorDock.tabs.addIfAbsent(editor.tab)
    }

    /**
     * Notification from an [Editor] that it has been closed.
     *
     * Removes the editor from the list of open editors.
     *
     * @param editor the editor to remove.
     */
    @Deprecated("Internal call only. Automatically called when an editor is created.")
    internal fun editorClosed(editor: Editor<*>) {
        editorDock.tabs.remove(editor.tab)
        openEditors.remove(editor)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_CLOSED)
        validateEditors()
    }

    /**
     * Asks all [openEditors] to close.
     *
     * Editors may ask the user to confirm if there are unsaved changes.
     *
     * @throws IllegalStateException if an editor refuses to close.
     */
    fun requestCloseAllEditors() {
        openEditors.concmod().forEach {
            if (!it.requestClose()) {
                showNotification("Editor refused to close", "Couldn't close all editors because one refused to close.")
                throw IllegalStateException("Editor refused to close")
            }
        }
    }

    fun requestSaveAllEditors() {
        openEditors.forEach {
            it.requestSave()
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                Utility functions
    //region                                   Document utility functions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * @return true if the current editor is a [DocumentEditor].
     */
    fun activeEditorIsDocument() = activeDocumentEditor() is DocumentEditor


    /**
     * @return the [DocumentEditor] that is currently active, or null if the current
     *          editor is not [DocumentEditor]
     * @see activeEditorIsDocument to determine if the current editor is a [DocumentEditor]
     */
    fun activeDocumentEditor() = try {
        activeEditor() as DocumentEditor
    } catch (e: Exception) {
        null
    }

    /**
     * @return the [Document] that is currently active, or null if the current
     *         editor is not [DocumentEditor]
     * @see activeEditorIsDocument to determine if the current editor is a [DocumentEditor]
     */
    fun activeDocument() = activeDocumentEditor()?.document

    /**
     * @return the [Element] selected in the current [DocumentEditor]
     *         that is currently active, or null if the current
     *         editor is not [DocumentEditor]
     * @see activeEditorIsDocument to determine if the current editor is a [DocumentEditor]
     */
    fun selectedTag() = activeDocumentEditor()?.selectedTag

    /**
     * Finds an [DocumentEditor] by the document itself.
     *
     * Strict pointer equality is used to determine if the document is the same.
     *
     * @return the editor that's editing [document], or null if no editor is editing it.
     */
    fun findDocumentEditorByDocument(document: Document): DocumentEditor? {
        validateEditors()

        return getEditorsOfType<DocumentEditor>()
            .find { it.document.equalsDocument(document) }
    }

    /**
     * Finds an [DocumentEditor] by the document's file.
     *
     * @return the editor that's editing [document], or null if no editor is editing it.
     */
    fun findDocumentEditorByFile(file: File): DocumentEditor? =
        getEditorsOfType<DocumentEditor>()
            .find { it.file == file }

    /**
     * Either switches to an existing [DocumentEditor], or creates a new one.
     *
     * Attempts to locate an existing [DocumentEditor] that is editing the given [Document],
     * and if found, switches to it. If not found, a new [DocumentEditor] is created and
     * that is switched to instead.
     *
     * @param document Document - The document to open
     * @return the document editor created.
     */
    fun openDocument(document: Document) {
        val editor = findDocumentEditorByDocument(document)

        if (editor != null) {
            if (switchToEditor(editor))
                setAction("Switched to document '${document.title()}'")
        } else {
            switchToEditor(DocumentEditor(document))
            setAction("Opened document '${document.title()}'")
        }
    }


    /**
     * [openDocument] from a file.
     *
     * Loads the file via the project, then opens it.
     *
     * @see the sister [openDocument] for opening logic.
     */
    fun openDocument(document: CachedFile<Document>) =
        openDocument(document.data)

    //#endregion
    //region script

    fun switchToScript(tag: Element) {
        getEditorsOfType<VisualScriptEditor>().find {
            it.scriptElement.id() == tag.id()
        }?.focus() ?: run {
            VisualScriptEditor(
                tag
            ).focus()
        }
    }
}