
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
import com.jdngray77.htmldesigner.utility.hasDescendant
import javafx.scene.control.ContextMenu
import javafx.scene.control.TreeTableRow
import javafx.scene.control.TreeTableView
import javafx.scene.input.ClipboardContent
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.Border
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.BorderWidths
import javafx.scene.paint.Color

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

    private var onContextMenuRequested : ((TreeTableRow<T>, ContextMenuEvent) -> Unit)? = null

    fun setOnContextMenuRequested(onContextMenuRequested: (TreeTableRow<T>, ContextMenuEvent) -> Unit) {
        this.onContextMenuRequested = onContextMenuRequested
    }

    protected fun setContextMenu(menu : ContextMenu) {
        tree.contextMenu = menu

        tree.setRowFactory {
            TreeTableRow<T>().apply {
                setOnContextMenuRequested {
                    this@HierarchyDock.onContextMenuRequested?.invoke(this, it)

                    if (!it.isConsumed)
                        contextRow = this
                }

                // Drag and Drop
                setOnDragDetected {
                    if (onDragCommit == null) return@setOnDragDetected

                    startDragAndDrop(TransferMode.MOVE).let {
                        db ->
                        db.dragView = snapshot(null, null)
                        rowBeingDragged = this

                        ClipboardContent().apply {
                            putString(item.toString())
                            db.setContent(this)
                        }

                        it.consume()
                    }
                }

                setOnDragOver {
                    // Don't drag if not configured
                    if (onDragCommit == null || rowBeingDragged == null) return@setOnDragOver

                    // Don't drag on rows that are empty.
                    if (itemProperty().get() == null) return@setOnDragOver

                    // Don't drag onto self
                    if (rowBeingDragged == this) return@setOnDragOver

                    // Don't drag into self
                    if (rowBeingDragged!!.treeItem.hasDescendant(treeItem)) return@setOnDragOver

                    markDragOver()
                    it.acceptTransferModes(TransferMode.MOVE)
                    it.consume()
                }

                setOnDragDropped {
                    unmarkDragOver()
                    if (onDragCommit != null && rowBeingDragged != null) {
                        it.isDropCompleted = onDragCommit!!.invoke(rowBeingDragged!!, this)
                        rowBeingDragged = null
                        it.consume()
                    }
                }

                setOnDragExited {
                    unmarkDragOver()
                }
            }
        }
    }

    /**
     * When dragging a row, this is the row being dragged.
     */
    private var rowBeingDragged : TreeTableRow<T>? = null

    private var onDragCommit : ((TreeTableRow<T>, TreeTableRow<T>) -> Boolean)? = null

    /**
     * Enables dragging of rows, and determines what happens when a row is dropped onto another.
     *
     * @param onDragCommit A function ran when a row is dragged onto another.
     * @param onDragCommit.first The row being dragged.
     * @param onDragCommit.second The row being dropped onto.
     * @param onDragCommit.return True if the drag was successful, false otherwise.
     */
    fun setOnDragCommit(onDragCommit : (TreeTableRow<T>, TreeTableRow<T>) -> Boolean) {
        this.onDragCommit = onDragCommit
    }

    private fun TreeTableRow<T>.markDragOver() {
        this.border = ROW_HOVER_BORDER
    }

    private fun TreeTableRow<T>.unmarkDragOver() {
        this.border = null
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

    protected fun selectedTableItem() =
        selectedTableItems().firstOrNull()

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

    companion object {
        val ROW_HOVER_BORDER = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, BorderWidths(2.0)))
    }
}