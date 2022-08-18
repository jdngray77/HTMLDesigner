package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.frontend.jsdesigner.JsDesigner.Companion.themeLine
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.scene.Cursor
import javafx.scene.control.ComboBox
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.shape.Line
import org.jsoup.nodes.Element
import java.lang.System.gc

/**
 * Visual representation of a [JsGraphNode].
 *
 * @author Jordan T. Gray
 */
class JsNode {

    /**
     * Configures this node with the information
     * it needs to display.
     */
    fun init(e: JsGraphNode, graphEditor: JsDesigner) {
        this.graphNode = e
        this.graphEditor = graphEditor
        this.e = graphEditor.document.getElementById(e.id)!!

        txtElementName.text = this.e.id()

        // Test attributes.
        addAttr("visible")
        addAttr("color")

        addEvent("Click")
        addEvent("Mouse Over")
    }

    //#region GUI elements
    @FXML
    private lateinit var root: AnchorPane

    @FXML
    private lateinit var vboxEvents: VBox

    @FXML
    private lateinit var vboxAttrs: VBox

    @FXML
    private lateinit var cmbNewAttr: ComboBox<Any>

    @FXML
    private lateinit var txtElementName: Label

    //#endregion

    //#region model
    /**
     * The HTML element that this node represents.
     */
    private lateinit var e: Element

    /**
     * The node within the JsGraph that this
     * represents.
     */
    lateinit var graphNode: JsGraphNode

    /**
     * The GUI editor that this node is a child of.
     */
    private lateinit var graphEditor: JsDesigner

    /**
     * Lines in the [graphEditor] that show the
     * connections emminating from this
     * [graphNode]'s events.
     */
    private val connectionLines = mutableListOf<Triple<JsNodeEvent, JsNodeAttr, Line>>()
    //#endregion

    /**
     * Adds a [JsNodeEvent] to the GUI.
     */
    private fun addEvent(name: String) {
        loadFXMLComponent<AnchorPane>("JsNodeEvent.fxml", javaClass).apply {
            vboxEvents.children.add(this.first)
            with ((second as JsNodeEvent)) {
                setEvent(name)
                this.graphEditor = this@JsNode.graphEditor
                this.guiNode = this@JsNode
            }
        }
    }

    /**
     * Adds a [JsNodeAttr] to the GUI.
     */
    fun addAttr(name: String) {
        loadFXMLComponent<AnchorPane>("JsNodeAttr.fxml", javaClass).apply {
            vboxAttrs.children.add(this.first)
            with ((second as JsNodeAttr)) {
                setAttr(name)
                this.graphEditor = this@JsNode.graphEditor
                this.guiNode = this@JsNode
            }
        }
    }


    //#region GUI events

    /**
     * Handles the node being dragged within the [JsDesigner].
     */
    @FXML
    private fun drag(mouseEvent: MouseEvent) {
        // Move the node.
        root.translateX = mouseEvent.x + root.translateX - root.width / 2
        root.translateY = mouseEvent.y + root.translateY - 20

        // Update the lines being emitted
        connectionLines.forEach {
            it.evalPosition()
        }

        // TODO update the lines being recieved. How?

        mouseEvent.consume()
    }

    fun mEnter() {
        root.cursor = Cursor.HAND
    }

    fun mExit() {
        root.cursor = Cursor.DEFAULT
    }

    fun mPress() {
        root.cursor = Cursor.MOVE
    }

    fun mRelease() {
        root.cursor = Cursor.DEFAULT
    }

    fun mContext(mouseEvent: MouseEvent) {
        ContextMenu().apply {
            items.add(
                MenuItem("Delete Node").also {
                    it.setOnAction { delete() }
                })
        }.show(this.root, mouseEvent.screenX, mouseEvent.screenY)
    }
    //#endregion

    //#region MVC

    /**
     * Create a connection being emitted an event in this node.
     */
    fun emitConnection(from: JsNodeEvent, to: JsNodeAttr) {
        // Edit the graph.
        graphNode.connect(
            from.event,
            to.guiNode.graphNode.id,
            to.attr
        )

        // Create the line.
        with (graphEditor.temporaryLine) {
            Line(startX, startY, endX, endY).also {
                connectionLines.add(Triple(from, to, it))
                graphEditor.root.children.add(it)
                themeLine(it)
            }
        }

        println(graphEditor.graph.toString())
    }

    /**
     * Deletes this node from the graph,
     * and removes the GUI elements.
     */
    fun delete() {
        graphEditor.graph.removeNode(this.graphNode)

        connectionLines.forEach { (_, _, line) ->
            graphEditor.root.children.remove(line)
        }

        connectionLines.clear()

        gc()
    }

    //#endregion

    companion object {

        /**
         * Updates a lines position to match the position of the
         * [JsNodeEvent] emitting the line, and the [JsNodeAttr]
         * receiving the line.
         */
        fun Triple<JsNodeEvent, JsNodeAttr, Line>.evalPosition() {
            with(third) {
                val startBounds: Bounds = first.socket.localToScene(first.socket.boundsInLocal)
                startX = startBounds.centerX
                startY = startBounds.centerY

                val endBounds: Bounds = second.socket.localToScene(second.socket.boundsInLocal)
                endX = endBounds.centerX
                endY = endBounds.centerY
            }
        }

    }

}