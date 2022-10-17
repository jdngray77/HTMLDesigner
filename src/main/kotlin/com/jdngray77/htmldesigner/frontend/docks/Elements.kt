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

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.html.Prefab.Companion.createPrefab
import com.jdngray77.htmldesigner.backend.html.Prefab.Companion.updatePrefabFromInstance
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.HierarchyDock
import com.jdngray77.htmldesigner.frontend.editors.EditorManager
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeEditorIsDocument
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeDocument
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeDocumentEditor
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.focus
import com.jdngray77.htmldesigner.frontend.editors.jsdesigner.VisualScriptEditor
import com.jdngray77.htmldesigner.utility.*
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldTreeTableCell
import javafx.scene.input.MouseEvent
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


/**
 * A dock which can display the hierarchy of a single [Document].
 *
 * Displays the [Element]'s of the current document, and
 * allows the user to move them around, and change the text content.
 *
 * Displays current document on [EventType.EDITOR_DOCUMENT_EDITED] and [EventType.EDITOR_DOCUMENT_SWITCH]
 */
class Elements : HierarchyDock<Element>({ it!!.tagName() }), Subscriber {

    init {
        // Able to select more than one tag at a time.
        tree.selectionModel.selectionMode = SelectionMode.SINGLE

        tree.isEditable = true

        EventNotifier.subscribe(
            this,
            EventType.EDITOR_DOCUMENT_SWITCH,
            EventType.EDITOR_DOCUMENT_EDITED,
            EventType.EDITOR_SELECTED_TAG_CHANGED,
            EventType.TAG_CREATED
        )

        var draggedElement: Element? = null
        var targetElement: Element? = null

        val dragMenu = menu()
            .item("Place this item : ")
            .separator()
            .item("Inside") { moveInside(draggedElement!!, targetElement!!) }
            .separator()
            .item("Above") { moveAbove(draggedElement!!, targetElement!!) }
            .item("Below") { moveBelow(draggedElement!!, targetElement!!) }
            .toContextMenu()

        tree.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            if (it.isStillSincePress)
                dragMenu.hide()
        }

        setOnDragCommit {
            dragged, target, event ->

            // If dragging to root, skip dialog. Just prepend
            if (target.item.tagName() == "body") {
                moveInside(dragged.item, target.item)
                return@setOnDragCommit false
            }

            draggedElement = dragged.item
            targetElement = target.item
            dragMenu.show(tree, event.screenX, event.screenY)
            return@setOnDragCommit false
        }

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


        // Configure the columns.

        val col1 = TreeTableColumn<Element, String>("Tag").also {
            it.setCellValueFactory { p -> SimpleObjectProperty(p.value.value.tagName()) }
            it.prefWidth = 100.0
        }

        val col2 = TreeTableColumn<Element, String>("Content").also {
            it.setCellValueFactory { p -> SimpleStringProperty(p.value.value.ownText()) }

            // User has edited the content of a tag.
            it.setOnEditCommit {
                if (!activeEditorIsDocument())
                    return@setOnEditCommit

                it.rowValue.value.text(it.newValue)
                activeDocumentEditor()!!.changed("Content of '${it.rowValue.value.tagName()}' changed to '${it.newValue}' ")
            }

            it.cellFactory = TextFieldTreeTableCell.forTreeTableColumn()

            // Fill the remaining width of the dock.
            it.prefWidthProperty().bind(widthProperty().subtract(col1.widthProperty().add(5)))
        }

        tree.columns.setAll(col1, col2)


        // When a new item is selected, display it in the editor.
        tree.setOnMouseClicked {

            selectedTableItems().apply {
                if (!activeEditorIsDocument())
                    return@setOnMouseClicked

                mvc().MainView.textEditor_Open(
                        if (size != 1) {
                            activeDocumentEditor()!!.selectTag(null)
                            ""
                        } else {
                            val treeItem = first() as TreeItem<Element>
                            val tag = treeItem.value
                            activeDocumentEditor()!!.selectTag(treeItem)

                            if (tag.tagName() == "script")
                                EditorManager.switchToScript(tag)

                            tag.toString()
                        }
                    )
                }
            }

        // TODO only show some of these items when one item is selected.

        setContextMenu(
            menu()
                .item("Edit alone") {
                    activeDocumentEditor()?.apply {
                        selectTag(selectedTableItems().first())
                        standaloneEditMode = true
                    }
                }
                .subMenu("Prefabs")
                    .item("Create new prefab") {
                        selectedItems().map {
                            it.createPrefab()
                        }
                    }

                    .item("Update prefab master") {
                        selectedItems().map {
                            it.updatePrefabFromInstance()
                        }
                    }

                    .item("Update master & all instances") {
                        selectedItems().map {
                            it.updatePrefabFromInstance(true)
                        }
                    }

                    .menuDone()

                .item("New script") {
                    val x = activeDocumentEditor()!!

                    VisualScriptEditor(
                        VisualScriptEditor.createNewScript()
                    ).focus()

                    x.changed("Created new script")
                }

                .separator()
                .item("Delete") {
                    selectedItems().apply {
                        if (size > 1)
                            mvc().deleteTag(*toTypedArray())
                        else
                            contextRow?.let { mvc().deleteTag(it.item) }
                    }

                    // Refreshes via tag deleted
                }
                .item("Cut") {
                    selectedItem()?.apply {
                        clipboard {
                            it.putHtml(this.toString())
                        }
                        mvc().implDeleteTag(this)
                    }

                    // Refreshes via tag deleted
                }
                .item("Copy") {
                    clipboard {
                        it.putHtml(selectedItem().toString())
                    }
                }
                .separator()
                .item("Paste above") {
                    if (!activeEditorIsDocument())
                        return@item

                    selectedItem()?.before(
                        clipboard().html.asElement().let {
                            activeDocumentEditor()!!.changed("${it.tagName()} pasted before ${selectedItem()!!.tagName()}")
                            it
                        }
                    )
                    refresh()
                }
                .item("Paste inside") {
                    selectedItem()?.insertChildren(
                        0,
                        clipboard().html.asElement()
                    )
                    refresh()
                }
                .item("Paste below") {
                    if (!activeEditorIsDocument())
                        return@item

                    selectedItem()?.apply {
                        after (clipboard().html.asElement())
                        activeDocumentEditor()!!.changed("Pasted below ${tagName()}")
                    }

                    refresh()
                }
                .item("Wrap with clipboard") {
                    if (!activeEditorIsDocument())
                        return@item

                    selectedItem()?.apply {
                        wrap(clipboard().html)
                        activeDocumentEditor()!!.changed("Wrapped ${tagName()} with clipboard")
                    }

                    refresh()
                }
                .separator()
                .item("Move up within parent") {
                    if (!activeEditorIsDocument())
                        return@item

                    selectedItem()?.apply {
                            moveAbove(this, lastElementSibling())
                    }
                }
            .item("Move down within parent") {
                selectedItem()?.apply {
                    nextElementSibling()?.let { moveAbove(this, it) }
                }
            }
            .separator()
            .item("Move out of parent") {
                selectedItem()?.apply {
                    parent()?.let {
                        if (it.tagName() == "body")
                            return@apply

                        mvc().implDeleteTag(this)
                        moveAbove(this, it)
                    }
                }
            }

            .toContextMenu()
        )

        setOnContextMenuRequested { it ->
            if (!activeEditorIsDocument()) {
                ContextMessage(tree, "You're not currently editing a page.")
                it.consume()
            }
        }
    }



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                      Updating
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Changes to show the model of a new [doc].
     *
     * Will be overwritten on the next document edit or editor switch.
     */
    private fun showDocument(doc: Document) {
        // Clear out the tree first. I can't remember why this was important to do.
        setRoot(Element("a"))

        setRoot(doc.body())
    }

    /**
     * invokes [showDocument] with the current document.
     */
    private fun refresh() {
         activeDocument()?.let {
             showDocument(it)
         }
    }

    /**
     * Clears out the tag hierarchy.
     *
     * Used when no documents are open.
     */
    private fun clear() {
        setRoot(Element("a"))
        tree.root = null
    }

    override fun notify(e: EventType) {
        with (mvc()) {

            // If there is no document available, clear the tree.
            if (!activeEditorIsDocument()) {
                clear()
            } else {
                refresh()
                // Select the tag in the tree that is selected in the editor.
                val selectedInDocument = activeDocumentEditor()!!.selectedTag

                if (selectedInDocument != null && selectedItem() != selectedInDocument)
                    select(selectedInDocument)
            }
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                   Updating
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Selects a given element within the tree.
     */
    private fun select(el: Element) {
        // Searching can be slow, especially on
        // large documents - so we'll do it in the background.
        BackgroundTask.submit {
            tree.findItem(el)?.let {
                tree.selectionModel.select(it)
            } ?: run {
                tree.selectionModel.clearSelection()
                logWarning("Tag hierarchy failed to select the currently selected tag, because it was not found in the tree.")
            }
        }
    }

    private fun moveInside(a: Element, b: Element) {
        activeDocumentEditor()!!.apply {
            b.prependChild(a)
            changed("Moved ${a.tagName()} inside ${b.tagName()}")
            showDocument(document)
        }
    }

    private fun moveAbove(a: Element, b: Element) {
        activeDocumentEditor()!!.apply {
            b.before(a)
            changed("Moved ${a.tagName()} before ${b.tagName()}")
            showDocument(document)
        }
    }

    private fun moveBelow(a: Element, b: Element) {
        b.after(a)
        activeDocumentEditor()!!.apply {
            changed("Moved ${a.tagName()} after ${b.tagName()}")
            showDocument(document)
        }
    }




    override fun getChildrenFor(el: Element): Iterable<Element> = el.children()

}