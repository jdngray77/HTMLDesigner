
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

import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.Stream


/**
 * Mutates this array to remove duplicate values.
 *
 * This differs from [distinct], in that it's mutative.
 */
fun <T> ArrayList<T>.removeDuplicates(): ArrayList<T> {
    this.removeAll((this - this.distinct().toSet()).toSet())
    return this
}

/**
 * Adds an item to an array list if it's not already in it.
 *
 * @return true if the item was added, false if already exists.
 **/
fun <T> ArrayList<T>.addIfAbsent(item: T): Boolean {
    if (find { it == item } == null) {
        add(item)
        return true
    }
    return false
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

fun <T> TreeTableView<T>.findItem(item : T): TreeItem<T>? =
    root.flatten().collect(Collectors.toList()).find { it.value == item }


fun <T> TreeItem<T>.flatten(): Stream<TreeItem<T>> =
    flatten(this)

fun <T> TreeItem<T>.flatten(item : TreeItem<T>): Stream<TreeItem<T>> =
    Stream.concat(
        Stream.of(item),
        item.children.stream().flatMap { flatten(it) }
    )


/**
 * Inverse of [get]
 *
 * Finds the key using the value.
 */
fun <V,K> HashMap<V,K>.reverseGet(value : V) =
    entries.find { it.value == value }?.key