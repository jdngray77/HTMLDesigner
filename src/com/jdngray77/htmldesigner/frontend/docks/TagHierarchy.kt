package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


/**
 * A dock which can display the hierarchy of a single
 * [Document].
 *
 * Displays current document on [EventType.EDITOR_DOCUMENT_EDITED] and [EventType.EDITOR_DOCUMENT_SWITCH]
 */
class TagHierarchy : HierarchyDock<Element>({it!!.tagName()}), Subscriber {


    /**
     * Reference to the document whose tags we are displying.
     */
    private lateinit var document: Document

    init {
        tree.selectionModel.selectionMode = SelectionMode.MULTIPLE
        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_SWITCH, EventType.EDITOR_DOCUMENT_EDITED)


        // Add buttons to the button bar.

        buttons.children.addAll(
//            Button("Delete").apply {
//                setOnAction {
//                    mvc().apply {
//                        deleteTag(*selectedTags().toTypedArray())
//                    }
//                    showDocument(document)
//                }
//            },

            Button("Expand").apply {
                setOnAction {
                    tree.root.applyToAll { it.isExpanded = true }
                }
            },

            Button("Collapse").apply {
                setOnAction {
                    tree.root.applyToAll { it.isExpanded = false }
                }
            },
        )



        // Configure the tree.

        tree.columns.setAll(
            TreeTableColumn<Element, String>("Tag").also {
                it.setCellValueFactory { p -> SimpleObjectProperty(p.value.value.tagName()) }
            },

            TreeTableColumn<Element, String>("Content").also {
                it.setCellValueFactory { p -> SimpleObjectProperty(p.value.value.text()) }
            }
        )




        // When a new item is selected, display it in the editor.

        tree.setOnMouseClicked {
            selectedTags().apply {
                mvc().let {
                    it.MainView.textEditor_Open(
                        if (size != 1) {
                            it.currentEditor().selectedTag = null
                            ""
                        } else {
                            it.currentEditor().selectedTag = first()
                            first().toString()
                        }
                    )
                }
            }
        }



        // Context menu
        // TODO only show some of these items when one item is selected.
        val ctx = ContextMenu()
        ctx.items.addAll(
                MenuItem("「TODO」Edit alone"),
                SeparatorMenuItem(),
                MenuItem("Delete"),
                MenuItem("「TODO」Cut"),
                MenuItem("「TODO」Copy"),
                SeparatorMenuItem(),
                MenuItem("「TODO」Paste clipboard as above"),
                MenuItem("「TODO」Paste clipboard as child"),
                MenuItem("「TODO」Paste clipboard as below"),
                MenuItem("「TODO」Wrap with clipboard"),
                SeparatorMenuItem(),
                MenuItem("「TODO」Move up"),
                MenuItem("「TODO」Move down"),
                SeparatorMenuItem(),
                MenuItem("「TODO」Delete...")
        )

        tree.setRowFactory {
            TreeTableRow<Element?>().apply {
                contextMenu = ctx
                setOnContextMenuRequested {
                    ctx.items[2].setOnAction {
                        selectedTags().apply {
                            if (size > 1)
                                mvc().deleteTag(*toTypedArray())
                            else item?.let { mvc().deleteTag(it) }
                        }
                    }
                }
            }
        }


        tree.pack()
    }

    /**
     * Changes to show the model of [doc].
     *
     * Will be overwritten on the next document edit or editor switch.
     */
    fun showDocument(doc: Document) {
        document = doc
        setRoot(doc.body())//.also {
//            it.value = doc.title()
//        }
    }

    override fun notify(e: EventType) {
        //TODO remove this once notifier is completed
        if (e == EventType.EDITOR_DOCUMENT_SWITCH || e == EventType.EDITOR_DOCUMENT_EDITED)
            showDocument(mvc().currentDocument())
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