package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.data.Project.Companion.projectFile
import com.jdngray77.htmldesigner.frontend.DocumentEditor
import com.jdngray77.htmldesigner.frontend.MainViewController
import javafx.scene.control.ButtonType
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
        EventNotifier.subscribe(this, EventType.EDITOR_LOADED)
    }

    override fun notify(e: EventType) {
        if (e == EventType.EDITOR_LOADED && Project.documents().isNotEmpty())
            openDocument(Project.loadDocument(Project.documents().first()))
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                        Editors
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * A list of all open editors.
     */
    private val openEditors =
        ArrayList<DocumentEditor>()

    /**
     * Returns the document of the current editor
     */
    fun currentDocument() =
        currentEditor()!!.document

    /**
     * It returns the current editor
     */
    fun currentEditor() =
        this.findEditorFor(MainView.dockEditors.selectionModel.selectedItem)!!


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
            Tab(document.title(), first).let {
                MainView.dockEditors.tabs.add(it)

                first.prefWidthProperty().bind(MainView.dockEditors.widthProperty())
                first.prefHeightProperty().bind(MainView.dockEditors.heightProperty())

                (second as DocumentEditor).apply {
                    setDocument(document, it)
                    openEditors.add(this)
                    switchToEditor(this)

                    it.setOnCloseRequest {
                        if (isDirty) {
                            if (userConfirm("${document.title()} has not been saved. Save?", ButtonType.YES, ButtonType.CANCEL) == ButtonType.YES) {
                                save()
                                MainView.setAction("Closed ${document.title()}")
                            } else {
                                MainView.setAction("Not closing ${document.title()} ; It's not been saved.")
                                it.consume()
                            }
                        }
                    }
                }

                it.setOnClosed {
                    openEditors.remove(second)
                }
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
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
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

        DocumentModificationTransaction().apply {
            tag.forEach {
                val doc = it.ownerDocument() ?: return
                it.remove()
                modified(doc)
            }

            finishedModifying()
        }
        MainView.setAction("Deleted tag(s)")
    }

    fun delete(projectFile: File) {
        if (projectFile.isDirectory &&
            userConfirm("${projectFile.name} is not empty. \n Are you sure you want to delete it's contents?"))
                projectFile.listTree().map { delete(it) }

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
    inner class DocumentModificationTransaction : ArrayList<Document>() {

        private var done = false

        fun modified(document: Document) =
            add(document)

        fun finishedModifying() {
            done = true
            distinct().forEach {
                findEditorFor(it)!!.documentChanged()
            }
        }

        protected fun finalize() {
            if (done) return
            finishedModifying()
            IllegalStateException("A Transaction was disposed without being completed!").printStackTrace()
        }

    }
}