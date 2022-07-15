
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
import com.jdngray77.htmldesigner.utility.asElement
import com.jdngray77.htmldesigner.utility.createPrefab
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import com.jdngray77.htmldesigner.utility.applyToAll
import com.jdngray77.htmldesigner.utility.clipboard
import com.jdngray77.htmldesigner.utility.pack
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldTreeTableCell
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

        tree.isEditable = true

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
                it.setCellValueFactory { p -> SimpleStringProperty(p.value.value.ownText())}

                it.setOnEditCommit {
                            // TODO we can access the old value too. This will be useful for implementing undo.
                            it.rowValue.value.text(it.newValue)
                            mvc().currentEditor().reRender()
                }
                it.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn())
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
        // TODO There's so much fucking repetition here.

        setContextMenu(
            // TODO
            MenuItem("「INOP」Edit alone"),
            MenuItem("「WIP」Save As Prefab").also {
                it.setOnAction {
                    selectedItems().map {
                        it.createPrefab()
                    }
                }
            },
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
            MenuItem("Cut").also {
                it.setOnAction {
                    selectedItem()?.apply {

                        clipboard {
                            it.putHtml(this.toString())
                        }

                        mvc().implDeleteTag(this)

                    }
                }
            },
            MenuItem("Copy").also {
                it.setOnAction {
                    clipboard {
                        it.putHtml(selectedItem().toString())
                    }
                }
            },
            SeparatorMenuItem(),
            MenuItem("Paste above").also {
                it.setOnAction {
                    selectedItem()?.before(
                        clipboard().html.asElement()
                    )
                    mvc().currentDocumentModified()
                }
            },
            MenuItem("Paste inside").also {
                it.setOnAction {
                    selectedItem()?.insertChildren(0,
                        clipboard().html.asElement()
                    )
                    mvc().currentDocumentModified()
                }
            },
            MenuItem("Paste below").also {
                it.setOnAction {
                    selectedItem()?.after(
                        clipboard().html.asElement()
                    )
                    mvc().currentDocumentModified()
                }
            },
            MenuItem("Wrap with clipboard").also {
                it.setOnAction {
                    selectedItem()?.wrap(clipboard().html)
                    mvc().currentDocumentModified()
                }
            },

            SeparatorMenuItem(),
            MenuItem("Move up within parent").also {
                it.setOnAction {
                    selectedItem()?.apply {
                        parent()?.let {
                            val index = it.children().indexOf(this)
                            if (index == 0) return@apply

                            it.insertChildren(index + 1, this)

                            mvc().currentDocumentModified()
                        }
                    }
                }
            },
            MenuItem("Move down within parent").also {
                it.setOnAction {
                    selectedItem()?.apply {
                        parent()?.let {
                            val index = it.children().indexOf(this)
                            if (index == 0) return@apply

                            it.insertChildren(index + 2, this)

                            mvc().currentDocumentModified()
                        }
                    }
                }
            },
            SeparatorMenuItem(),
            MenuItem("Move out of parent").also {
                it.setOnAction {
                    selectedItem()?.apply {
                        parent()?.let {
                            mvc().implDeleteTag(this)
                            it.before(this)
                            mvc().currentDocumentModified()
                        }
                    }
                }
            },
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
        setRoot(doc.body())
    }

    override fun notify(e: EventType) {
        //TODO remove this once notifier is completed
        if (e == EventType.EDITOR_DOCUMENT_SWITCH || e == EventType.EDITOR_DOCUMENT_EDITED)
            showDocument(mvc().currentDocument())
    }



    override fun getChildrenFor(el: Element): Iterable<Element> = el.children()

}