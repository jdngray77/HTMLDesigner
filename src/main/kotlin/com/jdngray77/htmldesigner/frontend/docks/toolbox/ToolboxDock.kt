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

package com.jdngray77.htmldesigner.frontend.docks.toolbox

import com.jdngray77.htmldesigner.backend.ContextMessage
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.html.AllElements
import com.jdngray77.htmldesigner.backend.html.Headings
import com.jdngray77.htmldesigner.backend.html.Text
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import com.jdngray77.htmldesigner.utility.readPrivateProperty
import javafx.event.EventType
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import org.jsoup.nodes.Element
import org.jsoup.safety.Safelist

/**
 * A list of all tags that for the user to add to the dom.
 */
class ToolboxDock : Dock() {

    private val list = VBox()

    private val filter = ComboBox<String>()
    private val search = TextField()

    private val labelMenuItem = MenuItem().also {
        it.isDisable = true
    }

    private var contextItem: ToolBoxItem? = null

    private val contextMenu = ContextMenu(
        labelMenuItem,
        MenuItem("Add Above selected tag").apply {
            setOnAction {
                mvc().currentEditor().apply {
                    Element(contextItem!!.controller.name.text).let {
                        selectedTag?.before(it)
                        selectTag(it)
                        documentChanged("Added " + it.tagName() + " above ${selectedTag?.tagName() ?: "selected tag"}")
                    }


                    clearSearch()
                }
            }
        },
        MenuItem("Add below selected tag").apply {
            setOnAction {
                mvc().currentEditor().apply {
                    Element(contextItem!!.controller.name.text).let {
                        selectedTag?.after(it)
                        selectTag(it)
                        documentChanged("Added " + it.tagName() + " below ${selectedTag?.tagName() ?: "selected tag"}")
                    }
                    clearSearch()
                }
            }
        },
        MenuItem("Add as child selected tag").apply {
            setOnAction {
                mvc().currentEditor().apply {
                    Element(contextItem!!.controller.name.text).let {
                        selectedTag?.insertChildren(0, Element(contextItem!!.controller.name.text))
                        selectTag(it)
                        documentChanged("Added " + it.tagName() + " inside ${selectedTag?.tagName() ?: "selected tag"}")
                    }
                    clearSearch()
                }
            }
        }
    )

    // TODO This was changed from Tag to Element. Check it works okay.
    private fun <T : Element> addItem(t: T) {

        center = ScrollPane(list)

        list.children.add(
            loadFXMLComponent<AnchorPane>("docks/toolbox/ToolBoxItem.fxml").let {
                ToolBoxItem(it.first).also { item ->
                    item.initialize(t, it.second as ToolBoxItemController)
                    item.pane.prefWidthProperty().bind(widthProperty())
                    item.pane.setOnMouseClicked { mouse ->
                        mvc().selectedTag()?.let {
                            labelMenuItem.text = "Creating a '${item.controller.tag.text}' tag"
                            contextItem = item
                            contextMenu.show(item.pane, mouse.screenX, mouse.screenY)
                        } ?: run {
                            ContextMessage(
                                item.pane,
                                "There is no tag selected.\nNew tags are added relative to the selected tag.\nUse the 'Tag Hierarchy' to select a tag, first."
                            )
                        }
                    }
                }.pane
            }
        )


    }

    init {
        top = HBox(filter, search).also {
            filter.prefWidthProperty().bind(it.widthProperty())

            search.prefWidthProperty().bind(it.widthProperty())
            search.prefHeightProperty().bind(it.heightProperty())
        }


        search.promptText = "Search"
        search.textProperty().addListener { prop, old, new ->
            refresh()
        }

        // If you press enter in the serach field,
        // auto select the first item.
        search.setOnKeyPressed {
            if (it.code == KeyCode.ENTER && list.children.isNotEmpty()) {
                list.children.first().apply {
                    val position = localToScreen(boundsInLocal)

                    // Fake a mouse event to trigger the existing code to open the context menu.
                    // It's the easiest way.
                    onMouseClicked.handle(
                        MouseEvent(
                            MouseEvent.MOUSE_CLICKED,
                            position.minX,
                            position.minY,
                            position.minX,
                            position.minY,
                            MouseButton.PRIMARY,
                            1,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            null
                        )
                    )
                }
                it.consume()
            }
        }

        filter.items.addAll(
            "Common",
            "Text",
            "Headings",
            "Less common",
            "Everything",
        )

        filter.selectionModel.select(0)

        filter.setOnAction {
            clearSearch()
            refresh()
        }


        refresh()
    }

    fun refresh() {
        list.children.clear()

        filter.selectionModel.selectedItem.let {
            when (it) {
                "Common" -> Safelist.basicWithImages()
                "Text" -> Text
                "Headings" -> Headings
                "Less common" -> Safelist.relaxed()
                "Everything" -> AllElements
                else -> Safelist.none()
            }
        }.readPrivateProperty<Set<Any>>(Safelist::class, "tagNames")
            .sortedBy { it.toString() }
            .filter {
                if (Config[Configs.TOOLBOX_DOCK_FILTER_EXACT_BOOL] as Boolean)
                    it.toString().startsWith(search.text)
                else
                    it.toString().contains(search.text)
            }
            .forEach {
                // it is Safelist.TagName, but that class is private. It's value can be accessed with the tostring, though.
                addItem(Element(it.toString()))
            }
    }

    fun clearSearch () {
        search.text = ""
    }
}

