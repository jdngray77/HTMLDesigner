package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.StoringTreeItem
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import javafx.scene.control.Button
import javafx.scene.control.SelectionMode
import javafx.scene.layout.HBox
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


/**
 * A dock which can display the hierarchy of a single
 * [Document].
 *
 * Displays current document on [EventType.EDITOR_DOCUMENT_EDITED] and [EventType.EDITOR_DOCUMENT_SWITCH]
 */
class TagHierarchy : HierarchyDock<Element>({it!!.tagName()}), Subscriber {

    private val buttons = HBox()

    /**
     * Reference to the document whose tags we are displying.
     */
    private lateinit var document: Document

    init {
        tree.selectionModel.selectionMode = SelectionMode.MULTIPLE

        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_SWITCH, EventType.EDITOR_DOCUMENT_EDITED)

        tree.setOnMouseClicked {
            selectedTags().apply {
                mvc().MainView.textEditor_Open(
                    if (size != 1)
                        ""
                    else
                        first().toString()
                )
            }
        }

        top = buttons
        buttons.children.apply {
            add(Button("Delete").apply {
                setOnAction {
                    mvc().apply {
                        deleteTag(*selectedTags().toTypedArray())

                    }
                    showDocument(document)
                }
            })
        }
    }

    /**
     * Changes to show the model of [doc].
     *
     * Will be overwritten on the next document edit or editor switch.
     */
    fun showDocument(doc: Document) {
        document = doc
        setRoot(doc.body()).also {
            it.value = doc.title()
        }
    }

    override fun notify(e: EventType) {
        //TODO remove this once notifier is completed
        if (e == EventType.EDITOR_DOCUMENT_SWITCH || e == EventType.EDITOR_DOCUMENT_EDITED)
            showDocument(mvc().MainView.currentDocument())
    }

    /**
     * If an element **inside** the root is selected within the [tree], it is returned.
     */
    fun selectedTags() =
        tree.selectionModel.selectedItems
            .filterNot { it == tree.root }
            .map { (it as StoringTreeItem<Element>).data }
            .filterNotNull()

    override fun getChildrenFor(el: Element): Iterable<Element> = el.children()

}