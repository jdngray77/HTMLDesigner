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

package com.jdngray77.htmldesigner.frontend.controls

import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.VBox

open class SearchableList <T> (var items: Iterable<T> = arrayListOf()): VBox() {

    val searchBox = TextField()

    val list = ListView<T>()

    init {

        searchBox.setOnKeyPressed {
            if (it.code == KeyCode.ENTER && list.items.isNotEmpty()) {
                list.selectionModel.selectedItem?.let {
                    onAction(it)
                } ?: run {
                    list.items.first().apply {
                        onAction(this)
                    }
                }
            } else if (it.code == KeyCode.DOWN || it.code == KeyCode.UP) {
                if (!list.isFocused) {
                    list.requestFocus()
                    list.selectionModel.select(0)
                }
            } else searchBox.requestFocus()

            searchBox.textProperty().addListener { a, b, c ->
                doSearch()
            }
        }

        list.onKeyPressed = searchBox.onKeyPressed

        children.addAll(searchBox, list)
        doSearch()
    }

    private fun doSearch() {
        list.items.clear()

        list.items.addAll(items.filter { it.toString().toLowerCase().contains(searchBox.text.toLowerCase()) })
    }

    fun clearSearch() {
        searchBox.text = ""
    }

    /**
     * Automatically invoked when the user presses enter.
     *
     * If an item is highlighted, it's that one.
     * Otherwise it's the topmost item.
     */
    protected open fun onAction(item: T) {}
}