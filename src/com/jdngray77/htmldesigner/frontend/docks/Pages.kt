package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import java.io.File
import java.sql.Time
import java.time.Instant
import java.util.*

class Pages : HierarchyDock<File>({it!!.name}), Subscriber {

    init {
        EventNotifier.subscribe(this, EventType.PROJECT_PAGE_DELETED, EventType.PROJECT_PAGE_CREATED)

        tree.setOnMouseClicked {
            implOpenSelected()
        }

        tree.setOnKeyPressed {
            if (it.code == KeyCode.ENTER)
                implOpenSelected()
        }


        tree.columns.setAll(
            TreeTableColumn<File, String>("Name").also {
                it.setCellValueFactory { p ->
                    SimpleObjectProperty(p.value.value.name)
                }
            },

            TreeTableColumn<File, Date>("Last Modified").also {
                it.setCellValueFactory { p ->
                    SimpleObjectProperty(Time.from(Instant.ofEpochMilli(p.value.value.lastModified())))
                }
            }
        )

        // TODO these are not cancelable


        setContextMenu(
            MenuItem("New Page").also {
                it.setOnAction {
                    implCreateNewPage()
                }
            },


            MenuItem("New Folder").also {
                it.setOnAction {
                    implCreateNewFolder()
                }
            },


            SeparatorMenuItem(),
            MenuItem("Delete").also {
                it.setOnAction {
                    contextItem?.let { it1 -> mvc().delete(it1) }
                    refresh()
                }
            },
            MenuItem("「TODO」Cut"),
            MenuItem("「TODO」Copy"),
            SeparatorMenuItem(),
            MenuItem("「TODO」Paste clipboard as above"),
            MenuItem("「TODO」Paste clipboard as below"),
            SeparatorMenuItem(),
            MenuItem("「TODO」New folder with selected items"),
        )


    }

    override fun notify(e: EventType) {
        refresh()
    }

    // FIXME everything is expanded on refresh.
    fun refresh() {
        mvc().Project.HTML.apply {
            setRoot(this)
            tree.root.isExpanded = true
        }
    }

    override fun getChildrenFor(el: File) = el.listFiles()?.toList() ?: arrayListOf()

    private fun implCreateNewFolder() {
        mvc().Project.apply {
            File(
                (contextOrSelectedOrNull()?.let {
                    if (it.isDirectory)
                        it.path + "/"
                    else
                        it.parentFile.path
                } ?: "HTML")
                + "/" + TextInputDialog("FancyProject").let{
                it.showAndWait()
                if (it.result.isNullOrBlank())
                    return
                else
                    it.result
            }).mkdirs()
            refresh()
        }
    }

    private fun contextOrSelectedOrNull() = contextItem ?: selectedItems().getOrNull(0)

    private fun implCreateNewPage() {
        mvc().Project.apply {
            createDocument(
                (contextOrSelectedOrNull()?.let {
                    if (it.isDirectory)
                        it.relativeTo(HTML).path  + "/"
                    else
                        it.relativeTo(HTML).parentFile?.let{ it.path + "/" } ?: ""
                } ?: "")
                        +
                        TextInputDialog("FancyPage.html").let{
                            it.showAndWait()
                            if (it.result.isNullOrBlank())
                                return
                            else
                                it.result.AssertEndsWith(".html")
                        }
            ).open()
            refresh()
        }
    }

    private fun implOpenSelected() {
        tree.selectionModel.selectedItem?.apply {
            (this as StoringTreeItem<File>).data!!.apply{
                if (this.isDirectory)
                    return

                mvc().openDocument(this)
            }
        }
    }

}