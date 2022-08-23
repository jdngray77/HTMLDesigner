package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.*
import com.jdngray77.htmldesigner.frontend.jsdesigner.JsDesigner.Companion.themeLine
import com.jdngray77.htmldesigner.utility.addIfAbsent
import com.jdngray77.htmldesigner.utility.concmod
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.shape.Line
import java.lang.Exception
import java.lang.System.gc
import kotlin.math.abs

typealias EmissionLine = Triple<JsNodeEmitter, JsNodeReceiver, Line>

/**
 * Visual representation of a [JsGraphNode].
 *
 * @author Jordan T. Gray
 */
class JsNode {

    /**
     * Configures this node with the information it needs to display.
     *
     * Performed by the [JsDesigner] after the JavaFX [initialize] is all complete.
     */
    fun init(e: JsGraphNode, graphEditor: JsDesigner) {
        // Store graph info.
        this.graphNode = e
        this.graphEditor = graphEditor

        // Move the node to match the graph node.
        // This is mostly for reloading existing saved
        // graphs, so the nodes stay in the same place.
        root.relocate(e.x, e.y)

        txtElementName.text = e.name

        e.recievers().map { addReceiver(it) }
        e.emitters().map { addEmitter(it) }

        root.styleClass.add(
            when (e) {
                is JsGraphFunction -> "function"
                is JsGraphElement -> "element"
                //            is JsGraphVariable -> "variable"
                //            is JsGraphValue -> "value"
                else -> ""
            }
        )


        // If the node can be duplicated, add a dupe item to the context menu.
        if (graphNode !is JsGraphElement) {
            txtElementName.contextMenu!!.items.addAll(
                SeparatorMenuItem(),

                MenuItem("Duplicate").also {
                    it.setOnAction { dupeNode() }
                }
            )
        }
    }

    //#region GUI elements
    @FXML
    internal lateinit var root: AnchorPane

    @FXML
    private lateinit var vboxEvents: VBox

    @FXML
    private lateinit var vboxAttrs: VBox

    @FXML
    private lateinit var txtElementName: Label

    //#endregion

    @FXML
    fun initialize() {
        // TODO is one context menu per node memory safe?
        txtElementName.contextMenu = ContextMenu().apply {
            items.addAll(
                MenuItem("Delete").also {
                    it.setOnAction { delete() }
                }
            )
        }
    }

    //#region model
    /**
     * The node within the JsGraph that this
     * represents.
     */
    private lateinit var graphNode: JsGraphNode

    /**
     * The GUI editor that this node is a child of.
     */
    private lateinit var graphEditor: JsDesigner

    /**
     * Controllers for each of emitter within this node.
     */
    private val emitters: MutableList<JsNodeEmitter> = mutableListOf()

    fun getEmitters(): List<JsNodeEmitter> = emitters.concmod()

    /**
     * Controllers for each of receiver within this node.
     */
    private val receivers: MutableList<JsNodeReceiver> = mutableListOf()

    fun getReceivers(): List<JsNodeReceiver> = receivers.concmod()


    /**
     * The GUI editor that this node is a child of.
     */
    fun getGraphNode(): JsGraphNode {
        return graphNode
    }

    /**
     * The GUI editor that this node is a child of.
     */
    fun getGraphEditor(): JsDesigner {
        return graphEditor
    }

    /**
     * Lines in the [graphEditor] that show the
     * connections on the right of this node.
     * [graphNode]'s events.
     */
    internal val emittingLines = mutableListOf<EmissionLine>()

    /**
     * Lines in the [graphEditor] that show the
     * connections on the left of this node.
     * [graphNode]'s events.
     */
    internal val receivingLines = mutableListOf<EmissionLine>()
    //#endregion

    /**
     * Adds a [JsNodeEmitter] to the GUI.
     */
    private fun addEmitter(_emitter: JsGraphEmitter) {
        loadFXMLComponent<AnchorPane>("JsNodeEmitter.fxml", javaClass).apply {
            vboxEvents.children.add(this.first)
            with((second as JsNodeEmitter)) {
                emitters.add(this)
                initEmitter(_emitter)
                this.graphEditor = this@JsNode.graphEditor
                this.guiNode = this@JsNode
            }
        }
    }

    /**
     * Adds a [JsNodeReceiver] to the GUI.
     */
    fun addReceiver(_receiver: JsGraphReceiver) {
        loadFXMLComponent<AnchorPane>("JsNodeReceiver.fxml", javaClass).apply {
            vboxAttrs.children.add(this.first)
            with((second as JsNodeReceiver)) {
                receivers.add(this)
                initReceiver(_receiver)
                this.graphEditor = this@JsNode.graphEditor
                this.guiNode = this@JsNode
            }
        }
    }


    //#region GUI events

    /**
     * When the node has been dragged, this stores the
     * original location prior to moving.
     *
     * Used to determine which direction the node has been
     * dragged, such that the movement can be constrained to
     * a single axis.
     */
    @Volatile
    private var dragDownLocation: Pair<Double, Double> = Pair(0.0, 0.0)

    /**
     * Handles the node being dragged within the [JsDesigner].
     */
    @FXML
    private fun drag(mouseEvent: MouseEvent) {

        // Sometimes the mouse drifts from the label and loses the correct state.
        // This forces the mouse to stay closed.
        root.cursor = Cursor.CLOSED_HAND

        // Determine new location for the node, including the mouses position.
        // This new location aligns the center of node's label under the mouse.
        var translatex = mouseEvent.x + root.translateX - root.width / 2
        var translatey = mouseEvent.y + root.translateY - 10

        // If holding shift, pin one axis to the original value.
        if (mouseEvent.isShiftDown) {
            if (abs(translatex - dragDownLocation.first) > abs(translatey - dragDownLocation.second))
                translatey = dragDownLocation.second // Dragging x - Hold y.
            else
                translatex = dragDownLocation.first  // Dragging y = Hold x
        } else {
            dragDownLocation = Pair(mouseEvent.screenX, mouseEvent.sceneY)
        }


        // Commit the movement.
        root.translateX = translatex
        root.translateY = translatey

        invalidatePosition()

        mouseEvent.consume()
    }

    fun breakdownConnection(emission: JsGraphEmission) {
        (emittingLines + receivingLines).find {
            it.first.emitter === emission.emitter && it.second.receiver === emission.receiver
        }!!.breakdown()
    }

    private fun invalidatePosition() {
        root.toFront()

        // Update the lines being emitted
        emittingLines.forEach {
            it.evalPosition()
        }

        // Update the lines being received.
        receivingLines.forEach {
            it.evalPosition()
        }

        with(root.boundsInParent) {
            graphNode.x = minX
            graphNode.y = minY
        }


    }

    fun mEnter() {
        root.cursor = Cursor.HAND
    }

    fun mExit() {
        root.cursor = Cursor.DEFAULT
    }

    fun mPress(mouseEvent: MouseEvent) {
        root.cursor = Cursor.CLOSED_HAND
        dragDownLocation = Pair(mouseEvent.sceneX, mouseEvent.sceneY)
        root.toFront()
    }

    fun mRelease() {
        root.cursor = Cursor.HAND
    }

    fun mContext(mouseEvent: ContextMenuEvent) {
//        mouseEvent.consume()
    }
    //#endregion

    //#region MVC

    /**
     * Commits a brand new connection. Modifies the data.
     */
    fun emitConnection(from: JsNodeEmitter, to: JsNodeReceiver) {
        // Edit the graph.
        from.emitter.emit(to.receiver)

        // Create the line.
        emitConnectionLine(from, to)

        println(graphEditor.graph.toString())
    }

    /**
     * Draws a line between two nodes to represent a connection.
     */
    fun emitConnectionLine(from: JsNodeEmitter, to: JsNodeReceiver) {
//        // FIXME bounds in parent won't transfer well if JsDesigner is later placed
//        //       in a tab pane in the main view later on.
//        val start: Bounds = from.socket.localToScene(from.socket.boundsInLocal)
//        val end: Bounds = to.socket.localToScene(to.socket.boundsInLocal)

        Line(0.0,0.0,0.0,0.0).also { line ->
            graphEditor.root.children.add(line)
            Triple(from, to, line).apply {
                emittingLines.add(this)
                to.guiNode.receivingLines.add(this)

                line.setOnContextMenuRequested {
                    breakdown()
                    it.consume()
                }

                evalPosition()
            }


            themeLine(line)
        }
    }

    /**
     * Deletes this node from the graph,
     * and removes the GUI elements.
     */
    fun delete() {
        graphEditor.graph.removeNode(this.graphNode)

        breakdownConnections()

        // Remove all from local node.
        // TODO is this redundant after the breakdown?
        emittingLines.clear()
        receivingLines.clear()

        graphEditor.nodes.remove(this)
        graphEditor.root.children.remove(root)

        gc()
    }

    /**
     * Disconnects all connections being received and emitted.
     */
    fun breakdownConnections() {
        emittingLines.toTypedArray().forEach {
            it.breakdown()
        }

        receivingLines.toTypedArray().forEach {
            it.breakdown()
        }
    }

    /**
     * Creates a new node in the graph of the same type and value,
     * then adds it to the editor.
     *
     * For elements, the new nodes contain the same Element ID's,
     * for functions, the new nodes contain a new instance of the same class of function.
     * @see JsFunctionFactory.byName
     */
    fun dupeNode() =
        graphEditor.implNewNode(
            when (graphNode) {
                is JsGraphFunction -> graphEditor.graph.addFunction((graphNode as JsGraphFunction).function.clone())
                is JsGraphElement -> {
                    graphEditor.graph.addElement(
                        graphEditor.document.getElementById((graphNode as JsGraphElement).name)!!
                    )
                }

                else -> throw Exception("Not equipped to duplicate a node of type ${graphNode::class.simpleName}")
            },
            root.boundsInParent.minX,
            root.boundsInParent.minY
        )

    /**
     * Updates the color of the header to match the [JsGraphNode.touch] state.
     *
     * Notifies the user that a node is unused.
     */
    fun invalidateTouched() {
        if (graphNode.touched)
            txtElementName.styleClass.addIfAbsent("touched")
        else
            txtElementName.styleClass.remove("touched")
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

                toFront()
            }
        }

        /**
         * Breaks down the visual line drawn over a connection AND
         * modifies the graph to reflect the change.
         */
        fun EmissionLine.breakdown() {

            // Remove the connection in the data.
            second.receiver.admission?.breakdown()

            // Remove references to the graphical line from sender and receiver.
            first.guiNode.emittingLines.remove(this)
            second.guiNode.receivingLines.remove(this)

            // Notify the emitter and reciever, which updates the 'populated' css class.
            first.breakdown()
            second.breakdown()

            // Recompile to check for touches.
            first.graphEditor.invalidateTouches()

            // Remove from the scene.
            first.guiNode.graphEditor.root.children.remove(third)
            third.isVisible = false

            // We want this fucker gone.
            gc()
        }
    }

}