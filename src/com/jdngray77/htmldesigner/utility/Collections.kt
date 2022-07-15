
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

package com.jdngray77.htmldesigner.utility

import com.sun.javafx.scene.control.skin.TreeTableViewSkin
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import java.util.function.Consumer

/**
 * Resizes a [TreeTableColumn] to fit the content within it.
 *
 * JavaFX does have a method for this, but it's not public
 * so this bodge uses reflection to access it.
 */
fun TreeTableColumn<*, *>.pack(table : TreeTableView<*>) {
    table.skin?.let {
        TreeTableViewSkin::class.java.getDeclaredMethod(
            "resizeColumnToFitContent",
            TreeTableColumn::class.java,
            Int::class.java
        ).apply {
            isAccessible = true
            invoke(it, this@pack, -1)
        }
    }
}

/**
 * Mutates this array to remove duplicate values.
 *
 * This differs from [distinct], in that it's mutative.
 */
fun ArrayList<*>.removeDuplicates(): ArrayList<*> {
    this.removeAll((this - this.distinct().toSet()).toSet())
    return this
}

/**
 * A [TreeItem] with exposed and retrievable value.
 */
class StoringTreeItem <T>(val data: T?) : TreeItem<T>(data)

/**
 * Recursively applies [function] to every item in the tree
 * held by this [TreeItem].
 */
fun <T> TreeItem<T>.applyToAll(function: Consumer<TreeItem<T>>) {
    this.children.forEach {
        it.applyToAll(function)
    }
    function.accept(this)
}

/**
 * Resizes columns in a [TreeTableView] to fit the content within them.
 *
 * JavaFX does have a method for this, but it's not public
 * so this bodge uses reflection to access it.
 */
fun TreeTableView<*>.pack() {
    columns.forEach {
        it.pack(this)
    }
}

/**
 * Inverse of [get]
 *
 * Finds the key using the value.
 */
fun <V,K> HashMap<V,K>.reverseGet(value : V) =
    entries.find { it.value == value }?.key