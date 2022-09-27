
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
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.data.config.ProjectPreference
import com.jdngray77.htmldesigner.frontend.DocumentEditor
import com.jdngray77.htmldesigner.frontend.MainViewController
import com.jdngray77.htmldesigner.utility.*
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
        processStartupPage()
    }

    /**
     * At boot, shows the selected start-up page, as determined by [ProjectPreference.STARTUP_PAGE_PATH_STRING].
     */
    private fun processStartupPage() {

        if (Config[Configs.IGNORE_PROJECT_STARTUP_PAGE_BOOL] as Boolean)
            return

        val startupPage = Project.PREFERENCES[ProjectPreference.STARTUP_PAGE_PATH_STRING] as String

        run determineStartupPage@ {

            if (startupPage.isBlank())
                return@determineStartupPage

            Project.HTML.subFile(startupPage).let checkFile@ {
                if (!it.exists())
                    return@checkFile

                openDocument(
                    Project.loadDocument(it)
                )
            }

            return
        }

        // If could not determine a file, default to the first page we find.
        Project.PREFERENCES[ProjectPreference.STARTUP_PAGE_PATH_STRING] = ""

        if (Project.documents().isNotEmpty())
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
        openEditors.find { it.document === document }

    /**
     * Finds the editor for the given tab.
     *
     * @param tab The tab to find the editor for.
     */
    fun findEditorFor(tab: Tab)  =
        openEditors.find { it.tab === tab }

    /**
     * Finds the editor for the given File
     *
     * @param file the file to find the editor for.
     */
    fun findEditorFor(file: File)  =
        openEditors.find { it.file === file }

    /**
     * Creates and opens a new document editor for the
     * given [document]
     *
     * Create a new document editor, set the document,
     * add the editor to the list of open editors, and switch to the
     * new editor
     *
     * @param document Document - The document to open
     * @return the document editor created.
     */
    fun openDocument(document: Document) : DocumentEditor {
        loadFXMLComponent<BorderPane>("DocumentEditor.fxml").apply {
            (second as DocumentEditor).let {
                it.setDocument(document)
                openEditors.add(it)
                switchToEditor(it)
                return it
            }
        }
    }

    /**
     * Loads a project document from disk and opens it.
     *
     * If there's already an editor, it's switched to.
     * Else, one is created.
     */
    fun openDocument(document: File) =
        switchToDocument(Project.loadDocument(document)).also {
            MainView.setAction("Opened ${document.name}")
        }

    /**
     * This function switches to the editor tab that is passed in as a parameter
     *
     * @param editor DocumentEditor - The editor to switch to
     * @return editor
     */
    fun switchToEditor(editor: DocumentEditor) : DocumentEditor {
        MainView.dockEditors.selectionModel.select(editor.tab)
        return editor
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
     * @return the document editor, wether it was created anew or already existed.
     */
    fun switchToDocument(document: Document) =
        findEditorFor(document)?.let { switchToEditor(it) }
            ?: openDocument(document)

    fun validateEditors() {
        // Find tabs that are not in [openEditors], and remove them.

        MainView.dockEditors.tabs.removeAll(
            MainView.dockEditors.tabs.filter { tab ->
                openEditors.find { it.tab == tab } == null
            }
        )

        openEditors.concmod().forEach {
            it.validate()
        }
    }

    /**
     * @throws InterruptedException if the user refuses to close an editor.
     */
    fun closeAllEditors() {
        openEditors.concmod().forEach {
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

    /**
     * Confirms action with user, then deletes a project file.
     *
     * If a directory, deletes recursively.
     *
     * Handles deletion of associated files, depending on type of file,
     * and validates the editors once done. (i.e closes editors of deleted files)
     *
     * [projectFile] must be within a project subdirectory.
     *
     * @param projectFile The file to delete.
     * @param isRecurse Should not be provided. Used to skip repeating dialogs when deleting recursively.
     */
    fun deleteProjectFile(projectFile: File, isRecurse: Boolean = false) {
        // Check permissions, but only on first call.
        if (!isRecurse) {
            if (!projectFile.isInProject()) {
                logWarning("Refused to delete file because it's outside of the project: $projectFile")
                return
            }

            if (projectFile.isInProjectRoot()) {
                logWarning("Refused to delete file because it's in the project root: $projectFile")
                return
            }
        }

        if ((projectFile.isDirectory && projectFile.list()?.isNotEmpty() == true) &&
            (isRecurse || userConfirm("${projectFile.name} is not empty. \n Are you sure you want to delete it's contents?"))
        ) {
            projectFile.listFiles()?.map { deleteProjectFile(it, true) }
            projectFile.delete()
        }

        Project.deleteFile(projectFile)
        if (projectFile.name.endsWith(".html")) {
            findEditorFor(projectFile)?.forceClose()
            EventNotifier.notifyEvent(EventType.PROJECT_PAGE_DELETED)
        }

        validateEditors()
    }

    /**
     * *Moves* a file within the project, whilst applying safety measures and limitations.
     *
     * Unlike some system operations, this function cannot be used to rename files.
     * it is **move**, afterall.
     *
     * Rejected if :
     * - File must be within the project.
     * - Project root is protected. Files cannot be move or added. thus :
     * - File must be within a subdirectory of the project.
     * - Directories cannot be moved into themselves
     *
     * Skipped if :
     * - Already in projected destination
     *
     * @param file The file to move.
     * @param to If a directory, the new parent file. If a file, then [projectFile] will be moved to be a sibling of [to] ([to.parent] == [projectFile.parent]).
     * @return true iff successfully moved the file. False if rejected, skipped, or failed (i.e ioException).
     */
    fun moveProjectFile(projectFile: File, to: File) : Boolean {

        // TODO update project cache
        // TODO update graph files.

        // Test is in the project
        if (!projectFile.isInProject()) {
            logWarning("Refusing to move file because it's not in the project : ${projectFile.path}")
            return false
        }

        // Protect files directly in the project directory.
        // Only move files in subdirectories.
        if (projectFile.isInProjectRoot()) {
            logWarning("Refusing to move file because it's in the project root : ${projectFile.path}")
            return false
        }

        // Test if the destination is in the project
        if (!to.isInProject()) {
            logWarning("Refusing to move file because the destination is not in the project : ${to.path}")
            return false
        }

        // Protect adding root files
        if (to.isProjectRoot()) {
            logWarning("Refusing to move file into project root : ${projectFile.path}")
            return false
        }

        // Skip if already in destination
        if (projectFile.parentFile == to) {
            logWarning("Skipped moving file because it's already in the destination : ${projectFile.path}")
            return false
        }

        // Skip if already in target destination if adding as sibling
        if (to.isFile && to.parentFile == projectFile.parentFile) {
            logWarning("Skipped moving file because it's already in the destination : ${projectFile.path}")
            return false
        }

        // Skip if the destination is a child of the file
        if (projectFile.isDirectory && to.isDirectory && to.absolutePath.startsWith(projectFile.absolutePath)) {
            logWarning("Skipped moving folder because it cannot be moved within itself : ${projectFile.path}")
            return false
        }

        openEditors.map {
            it.save()
        }


        // If moving to file, add as sibling. Otherwise, add as child.
        val destination = if (to.isFile) to.parentFile else to

        var success = if (projectFile.isDirectory) {
            // Moving a directory.

            // Determine & create dir
            val newDir = destination.subFile(projectFile.name)
            newDir.mkdirs()

            // Moving an entire directory.
            projectFile.copyRecursively(newDir)
        } else {
            // Moving a single file.
            projectFile.copyTo(destination.subFile(projectFile.name))
            true
        }

        // If the copy was successful, delete the original(s)
        if (success)
            success = projectFile.deleteRecursively()

        Project.invalidateCache()
        validateEditors()
        return success
    }

    fun renameProjectFile(file: File, newName: String) : Boolean {
        if (!file.isInProject()) {
            logWarning("Refusing to rename file because it's not in the project : ${file.path}")
            return false
        }

        if (file.isInProjectRoot()) {
            logWarning("Refusing to rename file because it's in the project root : ${file.path}")
            return false
        }

        val newFile = file.parentFile.subFile(newName + file.extension)
        if (newFile.exists()) {
            logWarning("Refusing to rename file because a file with that name already exists : ${newFile.path}")
            return false
        }

        openEditors.map {
            it.save()
        }

        return if (file.renameTo(newFile)) {
            Project.invalidateCache()
            validateEditors()
            true
        } else {
            logWarning("Failed to rename file : ${file.path}")
            false
        }
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