package com.jdngray77.htmldesigner.frontend.editors

import com.jdngray77.htmldesigner.backend.BackgroundTask.onUIThread
import com.jdngray77.htmldesigner.backend.DocumentState
import com.jdngray77.htmldesigner.backend.DocumentUndoRedo
import com.jdngray77.htmldesigner.backend.setAction
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import javafx.event.Event
import javafx.scene.Parent
import javafx.scene.control.Tab
import java.io.Serializable


/**
 * An editor is a tab that can be opened in the IDE.
 *
 * Implementation may edit just about anything serializable.
 *
 * Manages mvc, undo/redo, and tab.
 *
 * @param T the type of serializable model that this editor edits
 * @param initialState The initial state of the document or value that the editor holds.
 * @param root The root GUI to show in the tab.
 */
abstract class Editor<T : Serializable>(

    /**
     * The GUI content shown within the tab.
     */
    val root: Parent,

    /**
     * Initial state of the value that the editor holds.
     *
     * Will become the first state in the undo/redo stack.
     */
    initialState: T
) {

    //TODO UI thread safety

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                             Undo / Redo History
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * History of the value being held.
     *
     * States are added when [changed] is called.
     */
    val history = DocumentUndoRedo(initialState)

    /**
     * Jumps to a known state within the [history].
     *
     * Typically used by the history dock to allow the
     * user to traverse the history.
     */
    fun jumpTo(state: DocumentState<T>) {
        history.jumpTo(state)
        value = history.getDocument()

        onDocumentChanged()
        onHistoryTraversed()
    }

    /**
     * Notify that the [value] has changed.
     *
     * Stores a copy of the [value] in the history stack.
     */
    open fun changed(description: String) {
        onDocumentChanged()
        history.push(value, description)
        setAction(description)
        afterDocumentChanged()
        dirty = true
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                             Undo / Redo History
    //region                                Data
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * The document or value that the editor is editing.
     *
     * *** IMPORTANT *** Invoke [changed] when changing the value, and provide a description of the change.
     *
     * If [changed] is not called, the change is not registered into the [history].
     */
    var value: T = initialState
        set(value) {
            field = value
        }

    /**
     * The string placed in the tab of the editor.
     */
    var title: String = ""
        set(value) {
            field = value
            updateTitle()
        }

    /**
     * Force updates the title of the tab.
     *
     * Not required if changing the [title]
     */
    fun updateTitle() {
        tab.text =
            if (dirty)
                "$dirtyPrefix$title$dirtyPostfix"
            else
                title
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                          Data
    //region                             Dirty
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



    /**
     * String placed after the title when editor is [dirty]
     */
    protected var dirtyPostfix = " $DEFAULT_DIRTY_FIX"

    /**
     * String placed before the title when the editor is [dirty]
     */
    protected var dirtyPrefix = ""

    /**
     * Raised to true when the [value] has been changed since the
     * last time the [value] was saved via [save]
     *
     * Automatically updates the tab title to include [dirtyPrefix] and [dirtyPostfix],
     * if [dirty]
     */
    var dirty = false
        protected set(value) {
            if (field == value) return

            field = value


            if (field)
                onUIThread {
                    updateTitle()
                }
        }

    /**
     * Clears the [dirty] flag.
     *
     * Indicates that the [value] has been saved,
     * or that any changes have been revoked.
     */
    protected fun clean() {
        dirty = false
        tab.text = title
    }



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                             Dirty
    //region                                Tab
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



    /**
     * The tab that this editor is contained within.
     *
     * Displayed within the editor, contains the [root] and [title].
     */
    val tab = Tab(title, root).also {
        it.setOnCloseRequest {
            requestClose(it)
        }

        it.selectedProperty().addListener { _, _, newValue ->
            onUIThread {
                if (newValue)
                    onEditorFocus()
            }
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                             Tab
    //region                                Actions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



    /**
     * Closes this editor without asking the user or saving.
     */
    @Deprecated("Does not confirm with user; may result in data loss.", ReplaceWith("requestClose()"))
    fun forceClose() {
        tab.onClosed?.handle(null)
        EditorManager.editorClosed(this)
        mvc().Project.removeFromCache(value)
        onClosed()
    }

    /**
     * Asks this editor to close.
     *
     * Can be called by the user clicking the 'x',
     * or by the editor itself (i.e when shutting down).
     *
     * Performs [tab.onCloseRequest], and if the user is happy to close,
     * [forceClose] is used to close the tab.
     *
     * @return true if tab closed, false if request was refused.
     */
    fun requestClose(event: Event = Event(EDITOR_CLOSE_REQUEST)): Boolean {
        // Notify implementation
        onCloseRequested(event)

        // If implementation did not cancel
        return if (event.isConsumed)
            false
        else {
            forceClose()
            true
        }
    }

    /**
     * Requests that the editor saves the [value].
     *
     * Depends on implementation.
     *
     * If [save] reports success, [clean] is called.
     */
    fun requestSave() {
        if (save())
            clean()
    }

    fun requestReset() {
        reset()
        clean()
        history.undoAll()
    }

    fun requestValidation() {
        validate()
    }

    fun undo() {
        value = history.undo()
        onHistoryTraversed()
        onDocumentChanged()
    }

    fun redo() {
        value = history.redo()
        onDocumentChanged()
        onHistoryTraversed()
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                             Actions
    //region                                Implementation events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Called when [requestSave] is called.
     *
     * Saves changes that the user has made.
     *
     * @return true if save was successful, false if not.
     */
    protected open fun save() : Boolean = false

    /**
     * Called when [changed] is called, before the [value]
     * is stored within the [history].
     *
     * Useful for any preliminary processing that needs to be done
     * before saving the state.
     */
    protected open fun onDocumentChanged() {}

    /**
     * Called after [changed] is done.
     */
    protected open fun afterDocumentChanged() {}

    /**
     * Called when [requestValidation] is called.
     *
     * Optional event to validate the state of the editor.
     *
     * Should validate that the editor is in a good state,
     * (i.e the document being edited exists).
     *
     * If in bad state, typically call [requestClose] to close the editor.
     */
    protected open fun validate() {}

    /**
     * Called when [requestReset] is called.
     *
     * Resets the editor to initial state.
     *
     * Called just before the history is reset.
     */
    protected open fun reset() {}

    /**
     * Called when the undo/redo/[jumpTo] events occour.
     */
    protected open fun onHistoryTraversed() {}

    /**
     * Invoked when [requestClose] is called.
     *
     * consume e to cancel close.
     */
    protected open fun onCloseRequested(e: Event) {}

    /**
     * Called after the editor is closed.
     *
     * Only called if confirmation was obtained to close.
     */
    protected open fun onClosed() {}

    /**
     * Called when the editor is focused within the IDE.
     *
     * Either via the focus extension method provided by
     * the Editor Manager, or by the user clicking on the tab.
     */
    protected open fun onEditorFocus() {}

    companion object {

        /**
         * A particle appended to the name when [isDirty]
         */
        private const val DEFAULT_DIRTY_FIX = "*"

        /**
         * A JavaFX event thrown about when this editor creates close request events.
         * Nothing important, just an empty placeholder request.
         * It's stored here because event types can only be created once.
         */
        private val EDITOR_CLOSE_REQUEST = javafx.event.EventType<Event>("EDITOR_CLOSE_REQUEST")
    }

    init {
        EditorManager.editorCreated(this)
    }

}