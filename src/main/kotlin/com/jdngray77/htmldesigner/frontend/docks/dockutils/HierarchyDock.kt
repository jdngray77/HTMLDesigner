
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

package com.jdngray77.htmldesigner.frontend.docks.dockutils

import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.utility.StoringTreeItem
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TreeTableRow
import javafx.scene.control.TreeTableView

/**
 * A dock which can display the hierarchy of anything.
 *
 * Displays current document on [EventType.EDITOR_DOCUMENT_EDITED] and [EventType.EDITOR_DOCUMENT_SWITCH]
 */
abstract class HierarchyDock <T> (val titler: (T?) -> String) : Dock() {

    /**
     * The tree element displayed in this tab.
     */
    protected val tree = TreeTableView<T>()

    init {
        center = tree
    }

    /**
     * Recursively constructs tree items from a tree of elements.
     *
     * @see getChildrenFor
     */
    fun setRoot(root: T) = setRoot(StoringTreeItem(root), root).apply {
        tree.root = this
        tree.root.isExpanded = true
    }


    /**
     * Stores the context of the row that was right-clicked.
     *
     * Used by items in the context-menu
     */
    protected var contextRow : TreeTableRow<T>? = null
        set(value) {
            field = value
            contextItem = field?.item
        }

    /**
     * Quick access to the item stored by the row that
     * was right clicked.
     */
    protected var contextItem : T? = null


    protected fun setContextMenu(menu : ContextMenu) {
        tree.contextMenu = menu

        tree.setRowFactory {
            TreeTableRow<T>().apply {
                setOnContextMenuRequested {
                    contextRow = this
                }
            }
        }
    }

    /**
     * Recursively constructs tree items from a tree of elements.
     */
    private fun setRoot(parent : StoringTreeItem<T>, root : T): StoringTreeItem<T> {
        getChildrenFor(root).forEach { it ->
            setRoot(
                StoringTreeItem<T>(it).also {
                    parent.children?.add(it)
                    it.isExpanded = true
                }
                ,
                it
            )
        }
        return parent
    }

    /**
     * If an element **inside** the root is selected within the [tree], it is returned.
     */
    protected fun selectedItems() =
        selectedTableItems()
            .mapNotNull { it.value }

    protected fun selectedTableItems() =
        tree.selectionModel.selectedItems
            .filterNotNull()

    /**
     * Returns the FIRST selected item, if
     * any are selected at all.
     */
    protected fun selectedItem(): T? {
        try {
            return selectedItems()[0]
        } catch (e: java.lang.IndexOutOfBoundsException) {
            return null
        }
    }



    /**
     * Provides the children for an element.
     */
    protected abstract fun getChildrenFor(el : T) : Iterable<T>
}