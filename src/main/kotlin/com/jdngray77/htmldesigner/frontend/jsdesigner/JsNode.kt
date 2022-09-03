package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.*
import com.jdngray77.htmldesigner.frontend.jsdesigner.JsDesigner.Companion.themeLine
import com.jdngray77.htmldesigner.utility.addIfAbsent
import com.jdngray77.htmldesigner.utility.concmod
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.shape.Line
import java.lang.Exception
import kotlin.math.abs


/**
 * Visual representation of a [JsGraphNode].
 *
 * Contains [JsNodeReceiver]'s and [JsNodeEmitter]'s to represent and manipulate
 * the corresponding [JsGraphNode]'s [JsGraphReceiver]'s and [JsGraphEmitter]'s.
 *
 * Controller to the jsNode.fxml
 *
 * @author Jordan T. Gray
 */
class JsNode {

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                FXML GUI controls
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    @FXML
    internal lateinit var root: AnchorPane

    @FXML
    private lateinit var vboxEvents: VBox

    @FXML
    private lateinit var vboxAttrs: VBox

    @FXML
    private lateinit var txtElementName: Label

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               FXML GUI controls
    //region                                                   Constructions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Configures this node with the information it needs to display.
     *
     * Performed by the [JsDesigner] after the JavaFX [initialize] is all complete.
     */
    internal fun init(e: JsGraphNode, graphEditor: JsDesigner) {
        if (this::graphNode.isInitialized)
            throw Exception("Attempted to re-initialize a JsNode.")


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

        // Add css class to identify the type of node.
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


    /**
     * Adds a [JsNodeEmitter] to the GUI.
     */
    private fun addEmitter(_emitter: JsGraphEmitter) {
        loadFXMLComponent<AnchorPane>("JsNodeEmitter.fxml", javaClass).apply {
            vboxEvents.children.add(this.first)
            with((second as JsNodeEmitter)) {
                emitters.add(this)
                initProperty(_emitter)
                this.graphEditor = this@JsNode.graphEditor
                this.guiNode = this@JsNode
            }
        }
    }

    /**
     * Adds a [JsNodeReceiver] to the GUI.
     */
    private fun addReceiver(_receiver: JsGraphReceiver) {
        loadFXMLComponent<AnchorPane>("JsNodeReceiver.fxml", javaClass).apply {
            vboxAttrs.children.add(this.first)
            with((second as JsNodeReceiver)) {
                receivers.add(this)
                initProperty(_receiver)
                this.graphEditor = this@JsNode.graphEditor
                this.guiNode = this@JsNode
            }
        }
    }



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

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Constructions
    //region                                                    Graph data
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * The node within the JsGraph that this represents.
     */
    private lateinit var graphNode: JsGraphNode

    /**
     * The GUI editor that this node is a child of.
     */
    fun getGraphNode() = graphNode

    /**
     * The GUI editor that this node is a child of.
     */
    private lateinit var graphEditor: JsDesigner

    /**
     * The GUI editor that this node is a child of.
     */
    fun getGraphEditor() = graphEditor

    /**
     * Controllers for each of emitter within this node.
     */
    private val emitters: MutableList<JsNodeEmitter> = mutableListOf()

    /**
     * Controllers for each of emitter within this node.
     */
    fun getEmitters(): List<JsNodeEmitter> = emitters.concmod()

    /**
     * Controllers for each of receiver within this node.
     */
    private val receivers: MutableList<JsNodeReceiver> = mutableListOf()

    /**
     * Controllers for each of receiver within this node.
     */
    fun getReceivers(): List<JsNodeReceiver> = receivers.concmod()

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

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Graph data
    //region                                                   GUI events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


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
     *
     * Keeps the mouse hand closed, moves the node, updates the
     * connected lines, and snaps to an axis if the user
     * holds down the shift key.
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

    /**
     * Updates the position of the node, and the connected lines.
     *
     * Also stores the current position in the underlying [graphNode]
     */
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
        invalidatePosition()

        root.styleClass.addIfAbsent("in-motion")
    }

    fun mRelease() {
        root.cursor = Cursor.HAND
        root.styleClass.remove("in-motion")
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                GUI events
    //region                                                 MVC operations
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Breaks down a single connection, either being emitted or received
     * by this node.
     *
     * Removes the [Emission] within the data, and the corresponding [EmissionLine] within the gui.
     *
     * @param emission The data link within the graph to break down.
     */
    fun breakdownConnection(emission: JsGraphEmission) {
        (emittingLines + receivingLines).find {
            it.emitter.emitter() === emission.emitter && it.receiver.receiver() === emission.receiver
        }!!.breakdown()
    }


    /**
     * Commits a brand-new connection.
     *
     * Creates a new [Emission] within the data, and an [EmissionLine] within the gui.
     */
    fun emitConnection(from: JsNodeEmitter, to: JsNodeReceiver) {
        // Edit the graph.
        from.emitter().emit(to.receiver())

        // Create the line.
        emitConnectionLine(from, to)
    }

    /**
     * Creates the GUI lines to represent the connection between [from] and [to]
     * within the [graphEditor].
     *
     * Does not modify the data. the parameters should already be connected - but this
     * is not validated.
     */
    fun emitConnectionLine(from: JsNodeEmitter, to: JsNodeReceiver) {

        // Line is given no position, as it's set in the evalPosition() method.

        EmissionLine(from, to).apply {
            graphEditor.root.children.add(line)

            emittingLines.add(this)
            to.guiNode.receivingLines.add(this)
        }
    }

    /**
     * Disconnects all connections being received and emitted.
     *
     * Lines are removed from [emittingLines] and [receivingLines]
     */
    fun breakdownConnections() {
        emittingLines.concmod().forEach {
            it.breakdown()
        }

        receivingLines.concmod().forEach {
            it.breakdown()
        }
    }

    /**
     * Deletes this node from the graph, and removes the GUI elements.
     *
     * All connections being received and emitted are broken down.
     *
     * Alias for [JsDesigner.deleteNode].
     */
    fun delete() {
        graphEditor.deleteNode(this)
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

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                GUI events
    //region                                                 MVC operations
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


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


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                              MVC operations
    //region                                               Node Line Helpers
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

}