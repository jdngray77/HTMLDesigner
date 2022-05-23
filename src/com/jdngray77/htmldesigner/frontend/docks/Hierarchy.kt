package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.backend.html.dom.Tag
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import org.jsoup.nodes.Element

/**
 */
class Hierarchy : Dock(), Subscriber {

    val tree = TreeView<String>()

    init {
        add(tree, 0, 0)
        tree.isEditable = false
        tree.setOnMouseClicked {
            EDITOR.mvc.MainView.textEditor_Open((tree.selectionModel.selectedItem as HTMLTreeItem).element.toString())
        }

        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_SWITCH, EventType.EDITOR_DOCUMENT_EDITED)
    }



    private fun addItem(parent : HTMLTreeItem, root : Element): HTMLTreeItem {
        root.children().forEach {
            addItem(
                HTMLTreeItem(it).also {
                    parent.children?.add(it)
                }
                ,
                it
            )
        }
        return parent
    }


    inner class HTMLTreeItem(
        val element : Element
    ) : TreeItem<String>(element.tagName())

    override fun notify(e: EventType) {
        EDITOR.mvc.MainView.currentDocument().apply {
            tree.root = addItem(HTMLTreeItem(this).also { it.value = this.title() }, this)
        }
    }
}