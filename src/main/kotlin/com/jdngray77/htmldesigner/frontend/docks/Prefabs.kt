
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
import com.jdngray77.htmldesigner.utility.pack
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn

class Prefabs : HierarchyDock<Prefab>({it?.locationOnDisk!!.name}), Subscriber {
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
        tree.columns.setAll(
            TreeTableColumn<Prefab, String>("ID").also {
                it.setCellValueFactory { p -> SimpleObjectProperty(p.value.value.id()) }

                // TODO make this editable. When editing the ID, rename the file on disk.
                it.isEditable = false
            },

            TreeTableColumn<Prefab, String>("Type").also {
                it.setCellValueFactory { p -> SimpleStringProperty(p.value.value.element.tagName()) }
                it.isEditable = false
            }
        )

        tree.setOnMouseClicked {
            if (it.clickCount < 2)
                return@setOnMouseClicked

            selectedItem()?.element?.apply {
                mvc().currentEditor().let {
                    it.selectedTag?.appendChild( this)
                    it.documentChanged("Added prefab ${selectedItem()?.locationOnDisk!!.name ?: ""} to ${it.selectedTag?.tagName()}")
                    it.selectTag(this)
                }
            }
        }


        tree.selectionModel.selectionMode = SelectionMode.SINGLE
        tree.pack()
    }


    override fun getChildrenFor(el: Prefab) = listOf<Prefab>()
}