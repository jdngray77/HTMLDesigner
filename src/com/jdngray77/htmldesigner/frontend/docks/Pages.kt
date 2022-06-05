package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.StoringTreeItem
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import java.io.File

class Pages : HierarchyDock<File>({it!!.name}), Subscriber {

    init {
        center = tree
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
    }

    override fun notify(e: EventType) {
        refresh()
    }

    fun refresh() {
        Editor.mvc().Project.HTML.apply {
            tree.root = setRoot(this)
            tree.root.isExpanded = true
        }
    }

    override fun getChildrenFor(el: File) = el.listFiles()?.toList() ?: arrayListOf()
}