package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TreeTableColumn
import org.jsoup.nodes.Element
import java.io.File
import java.sql.Time
import java.time.Instant
import java.util.*

class Pages : HierarchyDock<File>({it!!.name}), Subscriber {

    init {
        EventNotifier.subscribe(this, EventType.PROJECT_PAGE_DELETED, EventType.PROJECT_PAGE_CREATED)

        tree.setOnMouseClicked {
            tree.selectionModel.selectedItem?.apply {
                    (this as StoringTreeItem<File>).data!!.apply{
                        if (this.isDirectory)
                            return@setOnMouseClicked

                        mvc().openDocument(this)
                }
            }
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
    }

    override fun notify(e: EventType) {
        refresh()
    }

    fun refresh() {
        mvc().Project.HTML.apply {
            setRoot(this)
            tree.root.isExpanded = true
        }
    }

    override fun getChildrenFor(el: File) = el.listFiles()?.toList() ?: arrayListOf()
}