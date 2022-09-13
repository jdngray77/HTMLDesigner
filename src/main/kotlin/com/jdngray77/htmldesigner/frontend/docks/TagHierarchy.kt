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
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import com.jdngray77.htmldesigner.utility.applyToAll
import com.jdngray77.htmldesigner.utility.asElement
import com.jdngray77.htmldesigner.utility.clipboard
import com.jdngray77.htmldesigner.utility.createPrefab
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldTreeTableCell
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


/**
 * A dock which can display the hierarchy of a single [Document].
 *
 * Displays current document on [EventType.EDITOR_DOCUMENT_EDITED] and [EventType.EDITOR_DOCUMENT_SWITCH]
 */
class TagHierarchy : HierarchyDock<Element>({ it!!.tagName() }), Subscriber {

    /**
     * Reference to the document whose tags we are displaying.
     *
     * Reduces calls to the mvc.
     */
    private lateinit var document: Document

    init {
        tree.selectionModel.selectionMode = SelectionMode.MULTIPLE
        EventNotifier.subscribe(
            this,
            EventType.EDITOR_DOCUMENT_SWITCH,
            EventType.EDITOR_DOCUMENT_EDITED,
            EventType.EDITOR_SELECTED_TAG_CHANGED,
            EventType.TAG_CREATED
        )


        tree.selectionModel.selectionMode = SelectionMode.MULTIPLE
        tree.isEditable = true


        buttonBar.children.addAll(
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

        val col1 = TreeTableColumn<Element, String>("Tag").also {
            it.setCellValueFactory { p -> SimpleObjectProperty(p.value.value.tagName()) }
            it.prefWidth = 100.0
        }

        val col2 = TreeTableColumn<Element, String>("Content").also {
            it.setCellValueFactory { p -> SimpleStringProperty(p.value.value.ownText()) }

            it.setOnEditCommit {
                // TODO we can access the old value too. This will be useful for implementing undo.
                it.rowValue.value.text(it.newValue)
                mvc().currentEditor().documentChanged("Content of '${it.rowValue.value.tagName()}' changed to '${it.newValue}' ")
            }
            it.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn())

            it.prefWidthProperty().bind(widthProperty().subtract(col1.widthProperty().add(5)))
        }

        tree.columns.setAll(col1, col2)


        // When a new item is selected, display it in the editor.

        tree.setOnMouseClicked {
            selectedTableItems().apply {
                mvc().let {
                    it.MainView.textEditor_Open(
                        if (size != 1) {
                            it.currentEditor().selectTag(null)
                            ""
                        } else {
                            it.currentEditor().selectTag(first())
                            (first() as TreeItem<Element>).value.toString()
                        }
                    )
                }
            }
        }


        // Context menu
        // TODO only show some of these items when one item is selected.

        val menuItem: (String, EventHandler<ActionEvent>) -> MenuItem = { name, action ->
            MenuItem(name).also {
                it.onAction = action
            }
        }

        setContextMenu(
            menuItem("Edit alone") {
                mvc().currentEditor().apply {
                    selectTag(selectedTableItems().first())
                    standaloneEditMode = true
                }
            },

            menuItem("「WIP」Save As Prefab") {
                selectedItems().map {
                    it.createPrefab()
                }
            },

            SeparatorMenuItem(),

            menuItem("Delete") {
                selectedItems().apply {
                    if (size > 1)
                        mvc().deleteTag(*toTypedArray())
                    else
                        contextRow?.let { mvc().deleteTag(it.item) }
                }
            },

            menuItem("Cut") {
                selectedItem()?.apply {
                    clipboard {
                        it.putHtml(this.toString())
                    }
                    mvc().implDeleteTag(this)
                }
            },

            menuItem("Copy") {
                clipboard {
                    it.putHtml(selectedItem().toString())
                }
            },

            SeparatorMenuItem(),

            menuItem("Paste above") {
                selectedItem()?.before(
                    clipboard().html.asElement().let{
                        mvc().currentEditor().documentChanged("${it.tagName()} pasted before ${selectedItem()!!.tagName()}")
                        it
                    }
                )
            },

            menuItem("Paste inside") {
                selectedItem()?.insertChildren(
                    0,
                    clipboard().html.asElement()
                )
            },

            menuItem("Paste below") {
                selectedItem()?.after(
                    clipboard().html.asElement()
                )

                // TODO
                //mvc().currentDocumentModified()
            },

            menuItem("Wrap with clipboard") {
                selectedItem()?.wrap(clipboard().html)
//                mvc().currentDocumentModified()
            },

            SeparatorMenuItem(),

            menuItem("Move up within parent") {
                selectedItem()?.apply {
                    parent()?.let {
                        val index = it.children().indexOf(this)
                        if (index == 0) return@apply

                        it.insertChildren(index + 1, this)

//                        mvc().currentDocumentModified()
                    }
                }
            },

            menuItem("Move down within parent") {
                selectedItem()?.apply {
                    parent()?.let {
                        val index = it.children().indexOf(this)
                        if (index == 0) return@apply

                        it.insertChildren(index + 2, this)

//                        mvc().currentDocumentModified()
                    }
                }
            },

            SeparatorMenuItem(),

            menuItem("Move out of parent") {
                selectedItem()?.apply {
                    parent()?.let {
                        mvc().implDeleteTag(this)
                        it.before(this)
//                        mvc().currentDocumentModified()
                    }
                }
            }
        )
    }

    /**
     * Changes to show the model of a new [doc].
     *
     * Will be overwritten on the next document edit or editor switch.
     */
    fun showDocument(doc: Document) {
        document = doc
        setRoot(Element("a"))
        setRoot(doc.body())
    }

    override fun notify(e: EventType) {
        with (mvc()) {
            if (e == EventType.EDITOR_DOCUMENT_CLOSED && !documentAvail()) {
                tree.root = null
                return
            }

            showDocument(currentDocument())

            // Saves refreshing if there is no change.
            // Ignore tag changes, if the item selected in the hierarchy already matches the one selected in the editor.
            if (e == EventType.EDITOR_SELECTED_TAG_CHANGED && selectedItem() == currentEditor().selectedTag)
                return
        }
    }



    override fun getChildrenFor(el: Element): Iterable<Element> = el.children()

}