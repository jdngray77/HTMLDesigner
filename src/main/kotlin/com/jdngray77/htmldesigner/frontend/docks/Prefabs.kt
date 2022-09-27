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
import javafx.scene.control.*

/**
 * A simple dock that displays available prefabs, and injects them into the document.
 */
class Prefabs : HierarchyDock<Prefab>({ it?.locationOfMaster!!.name }), Subscriber {

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
                it.setCellValueFactory { SimpleStringProperty(it.value.value.masterElement.tagName()) }

                it.isEditable = false
            }
        )

        tree.setOnDoubleClick {
            selectedItem()?.masterElement?.clone().apply {
                mvc().currentEditor().let {
                    it.selectedTag?.appendChild(this)
                    it.documentChanged("Added prefab ${selectedItem()?.locationOfMaster!!.name ?: ""} to ${it.selectedTag?.tagName()}")
                    it.selectTag(this)
                }
            }
        }

        setContextMenu(
            menu()
                .item("Rename...") { renameSelected() }
                .item("Edit") { editSelected() }
                .separator()
                .item("Create new instance") { newInstance() }
                .item("Delete Prefab...") { deleteSelected() }
                .item("Update all instances") { updateAllInstances() }
                .toContextMenu()
        )
    }

    private fun renameSelected() {
        TODO()
    }

    private fun newInstance() {
        TODO()
    }

    private fun deleteSelected() {
        val usersDecision = Dialog<ButtonType>().let {
            it.title = "Confirm prefab deletion"

            it.headerText = it.title
            it.contentText = "Would you like to keep all instances of the prefab, or delete them as well?"

            it.dialogPane.buttonTypes.addAll(
                unlinkButtonType,
                deleteButtonType,
                ButtonType.CANCEL
            )

            it.showAndWait()
            it.result
        }

        val prefab = selectedItem()!!

        when (usersDecision) {

            unlinkButtonType -> {
                prefab.unlinkAllInstances()
            }

            deleteButtonType -> {
                prefab.deleteAllInstances()
            }

            ButtonType.CANCEL -> {
                return
            }

            else -> {}
        }

        // Should be pointless, but just to be safe.
        if (prefab.masterElement.hasParent())
            prefab.masterElement.remove()

        // Delete file.
        prefab.locationOfMaster.delete()
    }

    fun editSelected() {
        selectedItem()?.let {
            with (mvc()) {

                val prefabEditor = mvc().openDocument(
                    mvc().Project.PREFAB_EDIT_DOCUMENT
                )

                with(prefabEditor.document.body()) {
                    children().forEach {
                        it.remove()
                    }

                    appendChild(it.masterElement.clone())
                }

                prefabEditor.reRender()
                prefabEditor.standaloneEditMode = true
            }
        }
    }

    private fun updateAllInstances() {
        selectedItem()?.updateInstances()
    }


    override fun getChildrenFor(el: Prefab) = emptyList<Prefab>()

    companion object {
        val unlinkButtonType = ButtonType("Delete prefab only")
        val deleteButtonType = ButtonType("Delete all instances")
    }
}