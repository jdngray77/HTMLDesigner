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

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.backend.html.Prefab
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.Editor.Companion.project
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import com.jdngray77.htmldesigner.utility.menu
import com.jdngray77.htmldesigner.utility.setOnDoubleClick
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import org.jsoup.nodes.Document

/**
 * A simple dock that displays available prefabs, and injects them into the document.
 */
class Prefabs : HierarchyDock<Prefab>({ it?.locationOnDisk!!.name }), Subscriber {
    init {
        EventNotifier.subscribe(this, EventType.IDE_FINISHED_LOADING, EventType.PROJECT_PREFAB_CREATED)
    }

    override fun notify(e: EventType) {
        tree.root = TreeItem<Prefab?>().also {
            project().PREFABS.listFiles()?.forEach { prefab ->
                it.children.add(TreeItem(Prefab(prefab)))
            }
        }
    }


    init {
        tree.isShowRoot = false
        tree.selectionModel.selectionMode = SelectionMode.SINGLE

        tree.columns.setAll(
            TreeTableColumn<Prefab, String>("Name").also {
                it.setCellValueFactory { p -> SimpleObjectProperty(p.value.value.name) }

                // TODO make this editable. When editing the ID, rename the file on disk.
                it.isEditable = false
            },

            TreeTableColumn<Prefab, String>("Type").also {
                it.setCellValueFactory { SimpleStringProperty(it.value.value.element.tagName()) }

                it.isEditable = false
            }
        )

        tree.setOnDoubleClick {
            selectedItem()?.element?.clone().apply {
                mvc().currentEditor().let {
                    it.selectedTag?.appendChild(this)
                    it.documentChanged("Added prefab ${selectedItem()?.locationOnDisk!!.name ?: ""} to ${it.selectedTag?.tagName()}")
                    it.selectTag(this)
                }
            }
        }

        setContextMenu(
            menu()
                .item("Edit Prefab") { editSelected() }
                .item("Delete Prefab")
                .item("[test] Commit Prefab") { commitSelected() }
                .toContextMenu()
        )
    }

    private fun editSelected() {
        selectedItem()?.let {
            mvc().apply {
                openDocument(
                    Document("").apply {
                        body().appendChild(
                            it.element
                        )
                    }
                )
            }
        }
    }

    private fun commitSelected() {
        selectedItem()?.commitChange()
    }


    override fun getChildrenFor(el: Prefab) = emptyList<Prefab>()
}