package com.jdngray77.htmldesigner.frontend.docks.toolbox

import com.jdngray77.htmldesigner.backend.ContextMessage
import com.jdngray77.htmldesigner.backend.html.dom.ALLTAGS
import com.jdngray77.htmldesigner.backend.html.dom.Tag
import com.jdngray77.htmldesigner.backend.injectSiblingBefore
import com.jdngray77.htmldesigner.backend.loadFXMLComponent
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

/**
 * A list of all tags that for the user to add to the dom.
 */
class ToolboxDock : Dock() {

    private val list = VBox()

    private val labelMenuItem = MenuItem().also {
        it.isDisable = true
    }

    private var contextItem: ToolBoxItem? = null

    private val contextMenu = ContextMenu(
        labelMenuItem,
        MenuItem("Add Above selected tag").apply {
            setOnAction {
                mvc().currentEditor().apply {
                    selectedTag?.injectSiblingBefore(Element(contextItem!!.controller.name.text))
                    documentChanged()
                }
            }
        },
        MenuItem("「TODO」Add below selected tag").apply {

        },
        MenuItem("「TODO」Add as child selected tag").apply {

        }
    )

    private fun <T : Tag> addItem(t : T) {

        center = ScrollPane(list)

        list.children.add(
            loadFXMLComponent<AnchorPane>("docks/toolbox/ToolBoxItem.fxml").let {
                ToolBoxItem(it.first).also {
                    item ->
                        item.initialize(t, it.second as ToolBoxItemController)
                        item.pane.prefWidthProperty().bind(widthProperty())
                        item.pane.setOnMouseClicked {
                            mouse ->
                            mvc().currentEditor().selectedTag?.let {
                                labelMenuItem.text = "Creating a '${item.controller.tag.text}' tag"
                                contextItem = item
                                contextMenu.show(item.pane, mouse.screenX, mouse.screenY)
                            } ?: run {
                                ContextMessage(item.pane, "There is no tag selected.\nNew tags are added relative to the selected tag.\nUse the 'Tag Hierarchy' to select a tag, first.")
                            }
                        }
                }.pane
            }
        )


    }

    init {
        ALLTAGS.forEach {
            addItem(it)
        }
    }
}

