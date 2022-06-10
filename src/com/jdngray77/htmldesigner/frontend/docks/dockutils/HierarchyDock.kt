package com.jdngray77.htmldesigner.frontend.docks.dockutils

import com.jdngray77.htmldesigner.backend.StoringTreeItem
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.pack
import javafx.scene.control.TreeTableView
import javafx.scene.layout.HBox

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

    /**
     * A row of buttons to perform actions.
     */
    val buttons = HBox()

    init {
        top = buttons
        center = tree
        tree.isEditable = false
    }


    /**
     * Recursively constructs tree items from a tree of elements.
     *
     * @see getChildrenFor
     */
    fun setRoot(root: T) = setRoot(StoringTreeItem(root, titler), root).apply {
        tree.root = this
        tree.root.isExpanded = true
        tree.pack()
    }

    /**
     * Recursively constructs tree items from a tree of elements.
     */
    private fun setRoot(parent : StoringTreeItem<T>, root : T): StoringTreeItem<T> {
        getChildrenFor(root).forEach { it ->
            setRoot(
                StoringTreeItem<T>(it, titler).also {
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
     * Provides the children for an element.
     */
    protected abstract fun getChildrenFor(el : T) : Iterable<T>
}