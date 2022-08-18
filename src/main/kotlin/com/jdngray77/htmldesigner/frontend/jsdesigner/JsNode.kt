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

        txtElementName.text = e.name

        e.recievers().forEach {
            addReceiver(it)
        }

        e.emitters().forEach {
            addEmitter(it)
        }
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
     * connections on the right of this node.
     * [graphNode]'s events.
     */
    private val emittingLines = mutableListOf<Triple<JsNodeEmitter, JsNodeReceiver, Line>>()

    /**
     * Lines in the [graphEditor] that show the
     * connections on the left of this node.
     * [graphNode]'s events.
     */
    private val receivingLines = mutableListOf<Triple<JsNodeEmitter, JsNodeReceiver, Line>>()
    //#endregion

    /**
     * Adds a [JsNodeEmitter] to the GUI.
     */
    private fun addEmitter(_emitter: JsGraphEmitter) {
        loadFXMLComponent<AnchorPane>("JsNodeEvent.fxml", javaClass).apply {
            vboxEvents.children.add(this.first)
            with ((second as JsNodeEmitter)) {
                initEmitter(_emitter)
                this.graphEditor = this@JsNode.graphEditor
                this.guiNode = this@JsNode
            }
        }
    }

    /**
     * Adds a [JsNodeReceiver] to the GUI.
     */
    fun addReceiver(_receiver: JsGraphReciever) {
        loadFXMLComponent<AnchorPane>("JsNodeAttr.fxml", javaClass).apply {
            vboxAttrs.children.add(this.first)
            with ((second as JsNodeReceiver)) {
                initReceiver(_receiver)
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
        emittingLines.forEach {
            it.evalPosition()
        }

        receivingLines.forEach {
            it.evalPosition()
        }


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
    fun emitConnection(from: JsNodeEmitter, to: JsNodeReceiver) {
        // Edit the graph.
        from.emitter.emit(to.receiver)

        // Create the line.
        with (graphEditor.temporaryLine) {
            Line(startX, startY, endX, endY).also {

                Triple(from, to, it).apply {
                    emittingLines.add(this)
                    to.guiNode.receivingLines.add(this)
                }

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

        emittingLines.forEach {
            // it = Triple(fromNode, toNode, Line)
            // Remove from remote node.
            it.second.guiNode.receivingLines.remove(it)

            // Remove from GUI.
            graphEditor.root.children.remove(it.third)
        }

        // Remove all from local node.
        emittingLines.clear()

        gc()
    }

    //#endregion

    companion object {

        /**
         * Updates a lines position to match the position of the
         * [JsNodeEmitter] emitting the line, and the [JsNodeReceiver]
         * receiving the line.
         */
        fun Triple<JsNodeEmitter, JsNodeReceiver, Line>.evalPosition() {
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