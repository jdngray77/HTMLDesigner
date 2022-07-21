
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
import com.jdngray77.htmldesigner.utility.open
import com.jdngray77.htmldesigner.utility.StoringTreeItem
import com.jdngray77.htmldesigner.utility.assertEndsWith
import com.jdngray77.htmldesigner.utility.pack
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.control.TextInputDialog
import javafx.scene.control.TreeTableColumn
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
 */
class Pages : HierarchyDock<File>({it!!.name}), Subscriber {

    init {
        EventNotifier.subscribe(this, EventType.EDITOR_LOADED, EventType.PROJECT_PAGE_DELETED, EventType.PROJECT_PAGE_CREATED)

        // Open documents that are clicked
        tree.setOnMouseClicked {
            implOpenSelected()
        }

        // Open documents if the user uses the arrow keys and presses enter.
        tree.setOnKeyPressed {
            if (it.code == KeyCode.ENTER)
                implOpenSelected()
        }

        // Add columns to the tree.
        tree.columns.setAll(
            TreeTableColumn<File, String>("Name").also {
                it.setCellValueFactory { p ->
                    SimpleObjectProperty(p.value.value.name)
                }
            },

            TreeTableColumn<File, Date>("Last Modified").also {
                it.setCellValueFactory { p ->
                    SimpleObjectProperty(Time.from(Instant.ofEpochMilli(p.value.value.lastModified())))
                }
            }
        )

        // Configure the context menu.
        setContextMenu(
            MenuItem("New Page").also {
                it.setOnAction {
                    implCreateNewPage()
                }
            },


            MenuItem("New Folder").also {
                it.setOnAction {
                    implCreateNewFolder()
                }
            },


            SeparatorMenuItem(),
            MenuItem("Delete").also {
                it.setOnAction {
                    contextItem?.let { it1 -> mvc().delete(it1) }
                    refresh()
                }
            },
            MenuItem("「TODO」Cut"),
            MenuItem("「TODO」Copy"),
            SeparatorMenuItem(),
            MenuItem("「TODO」Paste clipboard as above"),
            MenuItem("「TODO」Paste clipboard as below"),
            SeparatorMenuItem(),
            MenuItem("「TODO」New folder with selected items"),
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
            tree.pack()
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
        mvc().Project.apply {
            File(
                (contextOrSelectedOrNull()?.let {
                    if (it.isDirectory)
                        it.path + "/"
                    else
                        it.parentFile.path
                } ?: "HTML")
                + "/" + TextInputDialog("FancyProject").let{
                it.showAndWait()
                if (it.result.isNullOrBlank())
                    return
                else
                    it.result
            }).mkdirs()
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
                            it.relativeTo(HTML).path  + "/"
                        else
                            it.relativeTo(HTML).parentFile?.let{ it.path + "/" } ?: ""
                    } ?: "")
                            +
                            TextInputDialog("FancyPage.html").let{
                                it.showAndWait()
                                if (it.result.isNullOrBlank())
                                    return
                                else
                                    it.result.assertEndsWith(".html")
                            }
                ).open()

            } catch (e: FileAlreadyExistsException) {
                // Issue 24
                showErrorAlert("A file with that name already exists!")
            }
            refresh()
        }
    }

    /**
     * Opens an editor for the selected document.
     */
    private fun implOpenSelected() {
        tree.selectionModel.selectedItem?.apply {
            (this as StoringTreeItem<File>).data!!.apply{
                if (this.isDirectory)
                    return

                mvc().openDocument(this)
            }
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