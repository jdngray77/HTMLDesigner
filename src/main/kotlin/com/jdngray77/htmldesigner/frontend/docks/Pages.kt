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

package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import com.jdngray77.htmldesigner.utility.*
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.*
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.KeyCode
import java.io.File
import java.sql.Time
import java.time.Instant
import java.util.*

/**
 * A Dock which shows the pages in the loaded project.
 *
 * It will load pages which are clicked, and permit the creation and
 * deletion of them
 *
 * TODO add searching
 *
 * FIXME every refresh resets the collapsed state of rows.
 *
 * TODO creation and deletion don't handle user cancellation
 *
 * TODO it's possible to delete the HTML folder.
 */
class Pages : HierarchyDock<File>({ it!!.name }), Subscriber {

    init {
        EventNotifier.subscribe(
            this,
            EventType.IDE_FINISHED_LOADING,
            EventType.PROJECT_PAGE_DELETED,
            EventType.PROJECT_CREATED
        )

        // Open documents that are clicked
        tree.setOnMouseClicked {
            if(it.isStillSincePress)
                implOpenSelected()
        }

        // Open documents if the user uses the arrow keys and presses enter.
        tree.setOnKeyPressed {
            if (it.code == KeyCode.ENTER)
                implOpenSelected()
        }

        setOnContextMenuRequested { row, event ->
            if (row.item == mvc().Project.HTML)
                event.consume()
        }

        // If the user drags rows, move the files
        setOnDragCommit { dragging, target ->
            implMovePage(dragging.treeItem.value, target.treeItem.value)
        }

        // Add columns to the tree.

        val col1 = TreeTableColumn<File, String>("Name").also {
            it.setCellValueFactory { p ->
                SimpleObjectProperty(p.value.value.name)
            }

            it.prefWidth = 180.0
        }

        val col2 = TreeTableColumn<File, Date>("Last Modified").also {
            it.setCellValueFactory { p ->
                SimpleObjectProperty(Time.from(Instant.ofEpochMilli(p.value.value.lastModified())))
            }

            it.prefWidthProperty().bind(widthProperty().subtract(col1.widthProperty().add(5)))
        }

        tree.columns.setAll(col1, col2)

        // Configure the context menu.
        setContextMenu(
            menu()
                .item("Rename") { implRenameSelected() }
                .separator()
                .item("New Page") { implCreateNewPage() }
                .item("New Folder") { implCreateNewFolder() }
                .separator()
                .item("[inop] Cut") { implCut() }
                .item("[inop] Copy") { implCopy() }
                .item("[inop] Paste") { implPaste() }
                .item("Delete") { implDeleteSelected() }
                .separator()
                .item("Refresh list") { refresh() }
                .item("Show in file browser") { implShowInFileBrowser() }
                .item("Open with web browser") { implShowInWebBrowser() }
                .toContextMenu()
        )
    }

    override fun notify(e: EventType) {
        refresh()
    }

    /**
     * Clears and sets the contents of the tree.
     */
    fun refresh() {
        mvc().Project.HTML.apply {
            setRoot(this)
            tree.root.isExpanded = true
        }
    }

    /**
     * Determines the files within a given folder. Used by super
     * to create the tree.
     */
    override fun getChildrenFor(el: File) = el.listFiles()?.toList() ?: arrayListOf()

    /**
     * The code which will create a new folder
     *
     * This was made available for use in the button bar, but
     * those don't make much sense in this dock. Context menu is better.
     *
     * Asks the user for the name, then creates it either inside a selected folder,
     * or inside the same directory as a selected file.
     */
    private fun implCreateNewFolder() {

        // TODO new folder with selected items
        mvc().Project.apply {
            File(
                (contextOrSelectedOrNull()?.let {
                    if (it.isDirectory)
                        it.path + "/"
                    else
                        it.parentFile.absolutePath
                } ?: mvc().Project.HTML.absolutePath)
                        + "/" +
                        userInput("What should the new folder be called?") {
                            if (it.isBlank()) "Provide a name." else null
                        }
                ).mkdirs()
            refresh()
        }
    }

    /**
     * Creates a new page
     *
     * Asks the user for the name, then creates it either inside a selected folder,
     * or inside the same directory as a selected file.
     */
    private fun implCreateNewPage() {
        mvc().Project.apply {
            try {
                createDocument(
                    (contextOrSelectedOrNull()?.let {
                        if (it.isDirectory)
                            it.relativeTo(HTML).path + "/"
                        else
                            it.relativeTo(HTML).parentFile?.let { it.path + "/" } ?: ""
                    } ?: "")
                            +
                            userInput("What should the new page be called?") {
                                if (it.isBlank()) "Provide a name" else null
                                // TODO Validate name input more.
                            }.assertEndsWith(".html")
                ).open()

            } catch (e: FileAlreadyExistsException) {
                // Issue 24
                showErrorAlert("A file with that name already exists!")
            }
            refresh()
        }
    }

    private fun implDeleteSelected() {
        // TODO delete js graphs.
        contextItem?.let {
            if (userConfirm("Are you sure you want to delete ${it.name}?"))
                mvc().deleteProjectFile(it)
        }
        refresh()
    }

    /**
     * Opens an editor for the selected document.
     */
    private fun implOpenSelected() {
        tree.selectionModel.selectedItem?.apply {
            (this as StoringTreeItem<File>).data!!.apply {
                if (this.isDirectory)
                    return

                mvc().openDocument(this)
            }
        }

        tree.selectionModel.clearSelection()
    }

    private fun implRenameSelected() {
        // TODO make this validator a standard somewhere.
        mvc().renameProjectFile(contextItem!!, userInput("What should ${contextItem!!.name} be called instead?") {
            if (it.isBlank()) "Provide a name." else null
        })

        refresh()
    }

    private fun implCopy() {
        Clipboard.getSystemClipboard().setContent(
            ClipboardContent().apply {
                putString(contextItem!!.absolutePath)
                putHtml(contextItem!!.readText())
            }
        )
    }

    private fun implCut() {
        implCopy()
        mvc().deleteProjectFile(contextItem!!)
        refresh()
    }

    private fun implPaste() {
        val absPath = Clipboard.getSystemClipboard().string
        val html = Clipboard.getSystemClipboard().html
        val file = File(absPath)

        mvc().apply {
            val newFile = (if (contextItem!!.isDirectory) contextItem!! else contextItem!!.parentFile).subFile(file.name)

            if (newFile.exists()) {
                showWarningNotification("Did not paste.", "${newFile.name} already exists!\n\nDelete the existing file first.")
                return
            }

            val doc = Project.createDocument(newFile.relativeTo(Project.HTML).path)

            Project.fileForDocument(doc).writeText(html)

            openDocument(doc)
        }

        refresh()
    }

    private fun implShowInFileBrowser() {
        contextItem?.openFolderInSystem()
    }

    private fun implShowInWebBrowser() {
        contextItem?.openFileInSystem()
    }

    private fun implMovePage(file: File, to: File): Boolean {
        return mvc().moveProjectFile(file,to).also {
            refresh()
        }
    }

    /**
     * In order, returns either the [contextItem] if not null, else the first [selectedItems],
     * else null if there's no [contextItem] and no items are selected.
     *
     * Makes the implementations above able to function via button bar or via context menu.
     */
    private fun contextOrSelectedOrNull() = contextItem ?: selectedItems().getOrNull(0)

}