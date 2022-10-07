package com.jdngray77.htmldesigner.frontend.editors.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.*
import com.jdngray77.htmldesigner.frontend.DoubleClickToggleListener
import com.jdngray77.htmldesigner.utility.addIfAbsent
import com.jdngray77.htmldesigner.utility.concmod
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Region.USE_COMPUTED_SIZE
import javafx.scene.layout.VBox
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

    /**
     * Logic controller that manages
     * the collapsing of this node.
     *
     * Configured in [initialize]
     */
    private var collapseManager : DoubleClickToggleListener? = null

    /**
     * @return true if this node is collapsed
     * to show less information
     */
    fun isCollapsed() = collapseManager?.isToggled == true

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                FXML GUI controls
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * The root of the node.
     */
    @FXML
    internal lateinit var root: AnchorPane

    /**
     * Vbox containing emitters
     */
    @FXML
    private lateinit var vboxEmitters: VBox

    /**
     * Vbox containing receivers
     */
    @FXML
    private lateinit var vboxReceivers: VBox

    /**
     * Label containing the name of the node.
     *
     * Used to drag, collapse, and get the context menu.
     */
    @FXML
    lateinit var txtElementName: Label

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               FXML GUI controls
    //region                                                   Constructions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Configures this node with the information it needs to display.
     *
     * Performed by the [VisualScriptEditor] after the JavaFX [initialize] is all complete.
     */
    internal fun initNodeController(e: JsGraphNode, graphEditor: VisualScriptEditor) {
        if (this::graphNode.isInitialized)
            throw Exception("Attempted to re-initialize a JsNode.")

        assert(this::vboxReceivers.isInitialized)
        assert(this::vboxEmitters.isInitialized)


        // Store graph info.
        this.graphNode = e
        this.graphEditor = graphEditor

        // Move the node to match the graph node.
        // This is mostly for reloading existing saved
        // graphs, so the nodes stay in the same place.
        root.relocate(e.x, e.y)

        txtElementName.text = e.name

        e.receivers().map { addReceiver(it) }
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

        val updpos = {
            Platform.runLater {
                root.requestLayout()
                root.layout()
                invalidatePosition()
                graphEditor.invalidateGroupPositions()
                graphEditor.invalidateAllLinePositions()
            }
        }

        // Don't create a collapse manager if the node is a function.
        if (graphNode !is JsGraphNoteNode) {
            collapseManager = DoubleClickToggleListener (
                // When the label is double clicked
                txtElementName,

                // Collapse the node
                {
                    root.maxHeight = txtElementName.height + 15
                    txtElementName.text = "↓ ${graphNode.name} ↓"
                    root.children.remove(vboxEmitters)
                    root.children.remove(vboxReceivers)
                    updpos()
                },

                // and expand the node
                {
                    root.maxHeight = USE_COMPUTED_SIZE
                    txtElementName.text = graphNode.name
                    root.children.add(vboxEmitters)
                    root.children.add(vboxReceivers)
                    updpos()
                }
            )
        }
    }


    /**
     * Adds a [JsNodeEmitter] to the GUI.
     */
    private fun addEmitter(_emitter: JsGraphEmitter) {
        loadFXMLComponent<AnchorPane>("JsNodeEmitter.fxml", javaClass).apply {
            vboxEmitters.children.add(this.first)
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
            vboxReceivers.children.add(this.first)
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
        txtElementName.cursor = Cursor.HAND

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
    internal lateinit var graphNode: JsGraphNode

    /**
     * The GUI editor that this node is a child of.
     */
    fun getGraphNode() = graphNode

    /**
     * The GUI editor that this node is a child of.
     */
    private lateinit var graphEditor: VisualScriptEditor

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
     * Handles the node being dragged within the [VisualScriptEditor].
     *
     * Keeps the mouse hand closed, moves the node, updates the
     * connected lines, and snaps to an axis if the user
     * holds down the shift key.
     */
    @FXML
    private fun drag(mouseEvent: MouseEvent) {

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
        root.layoutX += translatex
        root.layoutY += translatey

        invalidatePosition()

        // This section is critical for dragging into and out of groups.
        // See [mRelease] for committing of adding/removing nodes.

        graphEditor.getGroups().forEach {
            it.styleClass.remove("enter-exit")
            root.styleClass.remove("enter-exit")

            // If this is a group we're a member of,
            // we may either move or exit it.
            if (it.getNodes().contains(this)) {

                if (!mouseEvent.isAltDown)
                    // Move the node and group.
                    it.invalidatePosition()
                else {
                    // Otherwise suggest exiting the group.
                    if (!it.boundsInParent.intersects(root.boundsInParent)) {
                        it.styleClass.add("enter-exit")
                        root.styleClass.add("enter-exit")
                    }
                }
            } else {
                // This is not a group we're a member of, we may only
                // enter it.

                if (it.boundsInParent.intersects(root.boundsInParent)) {
                    it.styleClass.add("enter-exit")
                    root.styleClass.add("enter-exit")
                }
            }

        }

        mouseEvent.consume()
    }

    /**
     * Updates the position of the node, and the connected lines.
     */
    internal fun invalidatePosition() {

        // Skip if not being displayed in editor (i.e in a collapsed group.)
        if (root.parent == null) return

        root.toFront()

        invalidateLinePositions()
    }

    fun invalidateLinePositions() {
        // Update the lines being emitted
        emittingLines.forEach {
            it.evalPosition()
        }

        // Update the lines being received.
        receivingLines.forEach {
            it.evalPosition()
        }
    }

    /**
     * Stores the current position in the underlying [graphNode]
     */
    private fun stowPosition() {
        with(root.boundsInParent) {
            if (graphNode.x != minX || graphNode.y != minY) {
                graphEditor.changed("${graphNode.name} moved to (X:$minX, Y:$minY)")

                graphNode.x = minX
                graphNode.y = minY
            }
        }
    }

    /**
     * Programmatically moves this node.
     */
    fun relocate(x: Double, y: Double) {
        root.relocate(x, y)
        invalidatePosition()
    }


    fun toFront() {
        invalidatePosition()
    }

    fun mPress(mouseEvent: MouseEvent) {
        txtElementName.cursor = Cursor.CLOSED_HAND
        dragDownLocation = Pair(mouseEvent.sceneX, mouseEvent.sceneY)
        invalidatePosition()

        root.styleClass.addIfAbsent("in-motion")
    }

    fun mRelease() {
        txtElementName.cursor = Cursor.HAND
        root.styleClass.removeAll("in-motion", "enter-exit")

        graphEditor.getGroups().forEach {
            it.styleClass.remove("enter-exit")

            if (!it.getNodes().contains(this)) {
                if (it.boundsInParent.intersects(root.boundsInParent)) {
                    it.addNode(this)
                }
            } else{
                if (!it.boundsInParent.intersects(root.boundsInParent)) {
                    it.removeNode(this)
                } else
                    it.invalidatePosition()
            }
        }

        stowPosition()
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
        graphEditor.changed("Connection broken ($emission)")
    }


    /**
     * Commits a brand-new connection.
     *
     * Creates a new [Emission] within the data, and an [EmissionLine] within the gui.
     */
    fun emitConnection(from: JsNodeEmitter, to: JsNodeReceiver) {
        // Edit the graph.
        val connection = from.emitter().emit(to.receiver())

        // Create the line.
        emitConnectionLine(from, to)

        graphEditor.changed("Connection created ($connection)")
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
            graphEditor.editorRootPane.children.add(line)

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

        graphEditor.changed("All connections for ${graphNode.name} broken")
    }

    /**
     * Deletes this node from the graph, and removes the GUI elements.
     *
     * All connections being received and emitted are broken down.
     *
     * Alias for [VisualScriptEditor.requestDeleteNode].
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
        graphEditor.implCreateGUINode(
            graphEditor.graph.cloneNode(graphNode),
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


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                              MVC operations
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

}