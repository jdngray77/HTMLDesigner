
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
import com.jdngray77.htmldesigner.backend.utility.applyToAll
import com.jdngray77.htmldesigner.backend.utility.pack
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
            selectedItems().apply {
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
        setContextMenu(
            MenuItem("「TODO」Edit alone"),
            SeparatorMenuItem(),
            MenuItem("Delete").also {
                it.setOnAction {
                    selectedItems().apply {
                        if (size > 1)
                            mvc().deleteTag(*toTypedArray())
                        else contextRow?.let { mvc().deleteTag(it.item) }
                    }
                }
            },
            MenuItem("「TODO」Cut"),
            MenuItem("「TODO」Copy"),
            SeparatorMenuItem(),
            MenuItem("「TODO」Paste clipboard as above"),
            MenuItem("「TODO」Paste clipboard as child"),
            MenuItem("「TODO」Paste clipboard as below"),
            MenuItem("「TODO」Wrap with clipboard"),
            SeparatorMenuItem(),
            MenuItem("「TODO」Move up").also {
                it.setOnAction {
                    TODO()
                }
            },
            MenuItem("「TODO」Move down"),
        )


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



    override fun getChildrenFor(el: Element): Iterable<Element> = el.children()

}