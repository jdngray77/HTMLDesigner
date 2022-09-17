
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

package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.frontend.DocumentEditor
import com.jdngray77.htmldesigner.frontend.MainViewController
import com.jdngray77.htmldesigner.utility.flattenTree
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.scene.control.Tab
import javafx.scene.layout.BorderPane
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File


/**
 * # Model View Controller.
 *
 * Central location for reaching :
 *  - GUI
 *  - Project
 *  - Current [DocumentEditor].
 *
 *  The entire IDE uses the data and API here
 *  to access and mutate the model.
 */
class MVC (

    /**
     * The project the IDE is working on.
     *
     * Use to access files on the disk.
     */
    var Project : Project,

    /**
     * The IDE's main FXML GUI controller.
     *
     * Use to access the front-end.
     */
    var MainView : MainViewController

) : Subscriber {

    init {
        EventNotifier.subscribe(this, EventType.IDE_FINISHED_LOADING)
    }

    override fun notify(e: EventType) {
        if (e == EventType.IDE_FINISHED_LOADING && Project.documents().isNotEmpty())
            openDocument(Project.loadDocument(Project.documents().first()))
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                        Editors
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * A list of all open editors.
     */
    private val openEditors: ArrayList<DocumentEditor> = ArrayList()

    /**
     * Returns a complete list of every [DocumentEditor]
     * currently open
     */
    fun getOpenEditors() = (openEditors.clone() as ArrayList<DocumentEditor>)

    /**
     * An event invoked by an editor when it is closed by the user.
     */
    internal fun onEditorClosed(documentEditor: DocumentEditor) {
        openEditors.remove(documentEditor)
        Project.removeFromCache(documentEditor.document)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_CLOSED)
    }

    /**
     * Determines if [currentDocument] can be used without failing.
     *
     * If returns false, [currentDocument] will throw an exception.
     *
     * @returns true if there are any documents open.
     */
    fun documentAvail() = openEditors.isNotEmpty()

    /**
     * Returns the document of the current editor, if
     * any are open.
     *
     * @throws [NullPointerException] if no documents are open.
     * @see [documentAvail] to check if any are open.
     */
    fun currentDocument() =
        currentEditor().document

    /**
     * Returns the [DocumentEditor] currently in focus in the editor.
     *
     * @throws [NullPointerException] if no editors are open.
     * @see [documentAvail] to check if any are open.
     * TODO make this nullable
     */
    fun currentEditor() =
        this.findEditorFor(MainView.dockEditors.selectionModel.selectedItem)!!

    /**
     * @return the selected tag for the [currentDocument], or null if none are open.
     * @see documentAvail to check if there is a document open to get the selected tag from.
     */
    fun selectedTag() =
        if (documentAvail())
            currentEditor().selectedTag
        else
            null



    /**
     * Finds the editor for the given document.
     *
     * @param document The document to find an editor for.
     */
    fun findEditorFor(document: Document)  =
        openEditors.find { it.document == document }

    /**
     * Finds the editor for the given tab.
     *
     * @param tab The tab to find the editor for.
     */
    fun findEditorFor(tab: Tab)  =
        openEditors.find { it.tab == tab }

    /**
     * Finds the editor for the given File
     *
     * @param file the file to find the editor for.
     */
    fun findEditorFor(file: File)  =
        openEditors.find { it.file == file }

    /**
     * Creates and opens a new document editor for the
     * given [document]
     *
     * Create a new document editor, set the document,
     * add the editor to the list of open editors, and switch to the
     * new editor
     *
     * @param document Document - The document to open
     */
    fun openDocument(document: Document) {
        loadFXMLComponent<BorderPane>("DocumentEditor.fxml").apply {
            (second as DocumentEditor).let {
                it.setDocument(document)
                openEditors.add(it)
                switchToEditor(it)
            }
        }
    }

    /**
     * Loads a project document from disk and opens it.
     *
     * If there's already an editor, it's switched to.
     * Else, one is created.
     */
    fun openDocument(document: File) {
        switchToDocument(Project.loadDocument(document))
        MainView.setAction("Opened ${document.name}")
    }

    /**
     * This function switches to the editor tab that is passed in as a parameter
     *
     * @param editor DocumentEditor - The editor to switch to
     */
    fun switchToEditor(editor: DocumentEditor) {
        MainView.dockEditors.selectionModel.select(editor.tab)
    }


    /**
     * This function switches to the editor tab that is passed in as a parameter
     *
     * @param editor DocumentEditor - The editor to switch to
     */
    fun DocumentEditor.Focus() = switchToEditor(this)


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

    fun validateEditors() {
        // Find tabs that are not in [openEditors], and remove them.

        MainView.dockEditors.tabs.removeAll(
            MainView.dockEditors.tabs.filter { tab ->
                openEditors.find { it.tab == tab } == null
            }
        )

        // Check that remaining editors' files exist. Else, close the editor.
        openEditors.filter {
            !it.file.exists()
        }.map {
            it.requestClose()
        }
    }

    /**
     * @throws InterruptedException if the user refuses to close an editor.
     */
    fun closeAllEditors() {
        getOpenEditors().forEach {
            if (!it.requestClose()) {
                showNotification("Shutdown or restart aborted", "An editor refused to close.")
                throw InterruptedException("Shutdown or restart aborted. An editor refused to close.")
            }
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     Editors
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░





    /**
     * Deletes [tag] after confirming with the user.
     *
     * If a [tag] does not belong to a document
     * (i.e it's already been deleted) it will be ignored.
     */
    fun deleteTag(vararg tag: Element) {
        if (tag.isEmpty()) return
        if (!
            if (tag.size > 1)
                userConfirm("Delete multiple tags? \n\n ${tag.joinToString { "\n" + it.tagName() }}")
            else
                userConfirm("Delete ${tag[0].tagName()} ?")
        ) return

        implDeleteTag(*tag)
    }

    fun implDeleteTag(vararg tag: Element) {
        DocumentModificationTransaction("Deleted multiple tags.").apply {
            tag
                .filterNot { it.parent() == null }
                .forEach {
                val doc = it.ownerDocument() ?: return
                it.remove()
                modified(doc)
            }

            finishedModifying()
        }
    }

    fun delete(projectFile: File) {
        if (projectFile.isDirectory &&
            userConfirm("${projectFile.name} is not empty. \n Are you sure you want to delete it's contents?"))
                projectFile.flattenTree().map { delete(it) }

        Project.deleteFile(projectFile)
        if (projectFile.name.endsWith(".html")) {
            findEditorFor(projectFile)?.forceClose()
            EventNotifier.notifyEvent(EventType.PROJECT_PAGE_DELETED)
        }

        validateEditors()
    }


    /**
     * When making many changes at once, this can be used to delay the
     * change notifications to after completing all changes.
     *
     * Make an instance then use `add` or `modified` to queue documents that have been changed
     * (even if they're the same / duplicates).
     *
     * Once all changes are made, call `finishedModifying`
     *
     */
    inner class DocumentModificationTransaction(

        val batchDescription: String

    ) : ArrayList<Document>() {

        private var done = false

        fun modified(document: Document) =
            add(document)

        fun finishedModifying() {
            done = true
            distinct().forEach {
                findEditorFor(it)!!.documentChanged(batchDescription)
            }
        }

        protected fun finalize() {
            if (done) return
            finishedModifying()
            IllegalStateException("A Transaction was disposed without being completed!").printStackTrace()
        }

    }
}