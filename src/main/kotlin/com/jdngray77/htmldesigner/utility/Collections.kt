
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

import javafx.collections.ObservableList
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
 * @return true if [item] is somewhere within the tree below [this].
 */
fun <T> TreeItem<T>.hasDescendant(item : TreeItem<T>): Boolean {
    val parent = item.parentProperty().get() ?:
    return false

    // Are siblings, reduces search by one level.
    if (parent == this.parent)
        return false

    if (parent == this)
        return true

    return hasDescendant(parent)
}


/**
 * Inverse of [get]
 *
 * Finds the key using the value.
 */
fun <V,K> HashMap<V,K>.reverseGet(value : V) =
    entries.find { it.value == value }?.key

fun <T> ObservableList<T>.addIfAbsent(that: T) =
    if (!this.contains(that)) this.add(that) else false


fun <K, V> HashMap<K, V>.copy() : HashMap<K, V> = HashMap<K, V>().also { hm ->
    forEach { hm[it.key] = it.value }
}

/**
 * To avoid concurrent modification exceptions.
 *
 * Simply place this before the itterative mutation function, and you'll be free to
 * mutate the original array as you go.
 *
 * e.g
 * ```
 * list.concmod().forEach {
 *    it.taredown()
 *    list.remove(it)
 * }
 *
 * ```
 *
 * > I also use this as a shortcut to create copies of arrays.
 *
 * @return a copy of the list, which can then be used to perform the itteration.
 */
fun <T> MutableCollection<T>.concmod() = toList()