package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.StoringTreeItem
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * A dock which can display the hierarchy of a single
 * [Document].
 *
 * Displays current document on [EventType.EDITOR_DOCUMENT_EDITED] and [EventType.EDITOR_DOCUMENT_SWITCH]
 */
class TagHierarchy : HierarchyDock<Element>({it!!.tagName()}), Subscriber {


    init {
        center = tree
        tree.isEditable = false
        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_SWITCH, EventType.EDITOR_DOCUMENT_EDITED)

        tree.setOnMouseClicked {
            tree.selectionModel.selectedItem?.apply {
                EDITOR.mvc.MainView.textEditor_Open((this as StoringTreeItem<Element>).data.toString())
            }
        }
    }

    /**
     * Changes to show the model of [doc].
     *
     * Will be overwritten on the next document edit or editor switch.
     */
    fun showDocument(doc: Document) {
        setRoot(doc).also {
            it.value = doc.title()
        }
    }

    override fun notify(e: EventType) {
        showDocument(EDITOR.mvc.MainView.currentDocument())
    }

    override fun getChildrenFor(el: Element): Iterable<Element> = el.children()

}