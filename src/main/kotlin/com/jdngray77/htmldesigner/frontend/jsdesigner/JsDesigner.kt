package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphCompiler
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNode
import com.jdngray77.htmldesigner.backend.showErrorNotification
import com.jdngray77.htmldesigner.backend.showWarningNotification
import com.jdngray77.htmldesigner.backend.userConfirm
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.controls.ItemSelectionDialog
import com.jdngray77.htmldesigner.utility.*
import javafx.fxml.FXML
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File
import java.lang.System.gc

/**
 * An advanced visual editor for designing Javascript events.
 *
 * Concept is similar to the visual scripting editors found in
 * engine editors like unity and unreal, where nodes are connected together.
 *
 * This is used to create javascript event listeners without the need to type code.
 *
 * @author Jordan T. Gray
 */
class JsDesigner {


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                       FXML Controls & GUI components.
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * The root pane of all nodes and lines.
     *
     * Sibling to [contextPane], which is placed below
     * on the z axis.
     *
     * Fills the entire IDE, and contains [JsNode]'s
     * that can be dragged around within, and lines displaying
     * the connections.
     *
     * @see clearScreen to remove all nodes and lines from the screen.
     */
    @FXML
    internal lateinit var root: Pane

    /**
     * A pane that fills the [root], used for
     * context menu events, such that context
     * menus are only requestes when not moused
     * over a node.
     *
     * Prior to this, context menu request on the [root]
     * would consume secondary mouse clicks anywhere within the editor.
     */
    @FXML
    private lateinit var contextPane: Pane


    /**
     * Context menu used to create new nodes.
     */
    private val contextMenu: ContextMenu = ContextMenu()

    /**
     * Constructor for the [contextMenu] only.
     */
    init {
        contextMenu.items.add(
            MenuItem("From Document").also {
                it.setOnAction { action ->

                    // Fuck this shit, man. Why can't i just put this in the brackets?
                    val filter: (Element) -> Boolean = {
                        it.tagName() != "style" && it.id().isNotEmpty()
                    }

                    document.getElementById(
                        ItemSelectionDialog<String>(
                            document.allElements.filter(filter).map { it.id() }.toList()
                        ).showAndWait()
                    )?.let {
                        try {
                            implNewNode(
                                graph.addElement(it),
                                contextMenu.x,
                                contextMenu.y
                            )
                        } catch (e: Exception) {
                            if (e.message != null) {
                                showWarningNotification("Failed to create the node.", e.message!!)
                            } else {
                                showErrorNotification(e, suppress = false)
                            }


                        }
                    }
                }
            }
        )

        contextMenu.items.addAll(*JsFunctionFactory.asMenus {
            // On user selecting an item, add the function to the graph
            // and create a new node for it.
            implNewNode(
                graph.addFunction(it),
                contextMenu.x - EDITOR.stage.x,
                contextMenu.y - EDITOR.stage.y
            )
        }.toTypedArray())
    }


    /**
     * A re-usable line used to give feedback to the user
     * when dragging [JsGraphConnection]s, before they
     * have been committed.
     */
    internal val uncommittedLine = Line()

    /**
     * Constructor for the [uncommittedLine] only.
     */
    init {
        with(uncommittedLine) {
            styleClass.addAll(

                // Unique class to this line.
                // Differenciates this line from the comitted ones.
                "dragging",

                // Style for this line. Differenciates from the 'line' class used on
                // menu seporator items.
                "connection-line",

                // Legacy, used on some branches.
                // TODO remove 'line when merged.'
                "line",

                // Omit this line from being deleted when the scene is
                // reset in [load] or [reset].
                "important"
            )

            isVisible = false

            visibleProperty().addListener { _, _, _ ->
                toFront()
            }
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                         FXML Controls & GUI components.
    //region                                                    Graph data
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * The data model that this view represents.
     *
     * When the view is manipulated, this is the model that is modified.
     * When compiling, this is the model compiled.
     *
     * This data model is serializable such that the graph can be
     * saved, and the JsDesigner view can be re-created from the graph.
     *
     * @see loadGraph
     */
    @Deprecated("Don't set this. Use loadGraph instead.")
    lateinit var graph: JsGraph

    /**
     * List of all graphical representations of nodes in the [graph].
     *
     * These are essentially the controllers for the node panes in the [root].
     */
    internal val guiNodes = mutableListOf<JsNode>()

    /**
     * The HTML Document that this graph is targeted towards.
     *
     * TODO this should be a property of the graph, not the view.
     *      Although that may not be possible. The graphs may have
     *      to be stored with the document somehow, so we can determine the target.
     */
    var document: Document

    /**
     * Constructor for the [document] only.
     *
     * For now, just fetches the current document.
     */
    init {
        with(mvc()) {
            if (!documentAvail()) {
                showWarningNotification("Unable to open Js Designer", "No document is open.")
                throw Exception("No document is open.")
            } else
                document = currentDocument()
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               graph data
    //region                                                    set-up
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Loads an existing [JsGraph] into the editor.
     *
     * Re-creates the view from the graph.
     *
     * > FIXME Note that this is currently unable to determine the [document]
     *    that the graph may have been targeted towards. If the document differs,
     *    the graph will likely not be able to load.
     */
    fun loadGraph(g: JsGraph) {

        // Breakdown existing graph.
        if (this::graph.isInitialized)
            reset()

        graph = g

        // Re-create all nodes.
        graph.getNodes().map {
            implNewNode(it)
                // JavaFX doesn't calculate the position of the node until sometime later.
                // We need to force it to evaluate now so that the bounds are correct
                // in order to position the lines correctly in the next step.

                // This is actually still mildly inaccurate, and causes the nodes to be just
                // pixels away from thier original position.
                .root.layout()
        }

        // Re-create all the connection lines.

        // For every node
        guiNodes.forEach { guinode ->
            // For each of it's emitters
            guinode.getEmitters().forEach { emitter ->
                // For each emission coming from it
                emitter.emitter().emissions().forEach { emission ->
                    // Locate the receiver within the gui
                    // TODO this isn't efficient.
                    val receivingNode = guiNodes.find { it.getGraphNode() === emission.receiver.parent }!!
                    val receiver = receivingNode.getReceivers().find { it.receiver().admission === emission }

                    // Create a line.
                    guinode.emitConnectionLine(emitter, receiver!!)
                }
            }
        }

        // Check what nodes are touched.
        invalidateTouches()
    }


    /**
     * Creates a new GUI Node for the view.
     *
     * Does not modify the model.
     *
     * @param graphNode The node in the graph to create a GUI for.
     * @param x The x position of the node, within the scene.
     * @param y The y position of the node, within the scene.
     * @return The newly created GUI node.
     * @throws Exception If [graphNode] does not exist within the [graph]
     */
    internal fun implNewNode(graphNode: JsGraphNode, x: Double = graphNode.x, y: Double = graphNode.y): JsNode {
        graph.assertExists(graphNode)

        graphNode.x = x
        graphNode.y = y

        loadFXMLComponent<AnchorPane>("JsNode.fxml", javaClass).apply {
            root.children.add(first)
            with((second as JsNode)) {
                guiNodes.add(this)
                init(graphNode, this@JsDesigner)
                return this
            }
        }
    }


    /**
     * After FXML has loaded, initializes the editor with a
     * blank graph.
     */
    @FXML
    fun initialize() {
        root.children.add(uncommittedLine)

        contextPane.addEventHandler(MouseEvent.MOUSE_PRESSED) { contextMenu.hide() }

        contextPane.setOnContextMenuRequested {
            invalidateTouches()
            contextMenu.show(root, it.screenX, it.screenY)
        }

        tryLoadTestGraph()
    }

    /**
     * Loads the [TEST_GRAPH] from disk.
     */
    fun loadTestGraph() {
        loadGraph(loadObjectFromDisk(TEST_GRAPH) as JsGraph)
    }

    fun saveTestGraph() {
        graph.saveObjectToDisk(TEST_GRAPH)
    }

    /**
     * Attempts to load the [TEST_GRAPH], if it exists.
     *
     * If it doesn't exist, a blank graph will be loaded instead.
     */
    fun tryLoadTestGraph() {
        if (TEST_GRAPH.exists())
            loadTestGraph()
        else
            loadGraph(JsGraph())
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                 set-up
    //region                                                  breakdowns
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Removes all children from the scene, except the important
     * stuff.
     *
     * i.e removes all nodes and lines, but retains containers and the menu bar.
     *
     * Achieved with css class called 'important', which is applied to
     * contents that should not be removed.
     *
     * Due to the layout requirements of the nodes and lines, all contents within
     * the scene are siblings, making it impossible to just clear all
     * children from the [root].
     *
     * > Note that this does not clear the graph, and as such should only be
     *      invoked when clearing the graph.
     *
     * @author Jordan T. Gray
     */
    private fun clearScreen() {
        root.children.filter {
            !it.styleClass.contains("important")
        }.map {
            root.children.remove(it)
        }
    }


    /**
     * Clears the screen, and loads a blank graph.
     *
     * Doesn't break down the current graph.
     * TODO confirm with the user.
     */
    fun reset() {
        if (!userConfirm("Any unsaved changes will be lost.\n\nContinue?"))
            return


        // Remove everything from the screen.
        clearScreen()

        // Forget everything we were tracking.
        guiNodes.clear()

        // Create a fresh, clear graph.
        graph = JsGraph()

        // TODO check for memory leaks.
        //      I don't trust that all components of the view and the graph
        //      are properly disposed of, since they're interconnected with many references.
        gc()
    }

    /**
     * Modifies the [graph] to breakdown every single connection that
     * exists within it.
     *
     * TODO confirm with the user.
     */
    fun resetConnections() {
        if (!userConfirm("This will destroy ALL connections in the graph.\n\nContinue?"))
            return

        guiNodes.map {
            it.breakdownConnections()
        }
    }

    /**
     * Deletes a given node from the graph.
     *
     * Confirms with the user before deleting.
     *
     * Breaks down all connections and thier gui lines,
     * then removes the node from the data and the screen.
     *
     * @param node The node to delete.
     */
    fun deleteNode(node: JsNode) {
        if (!userConfirm("This will '${node.getGraphNode().name}',and destroy all connections it.\n\nContinue?"))
            return

        // Disconnect and delete all connections.
        // This also removes the lines from [emittingLines] and [receivingLines]
        node.breakdownConnections()

        // Remove node from data.
        graph.removeNode(node.getGraphNode())

        // Remove from GUI
        guiNodes.remove(node)
        root.children.remove(root)


        gc()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               breakdowns
    //region                                                update events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Compiles the entire graph, and in turn evaluates which nodes
     * were touched in the compile, and which were not.
     *
     * After, invokes [invalidateTouched] on all nodes to update them
     * with the touch status.
     *
     * Invoke when a connection between two nodes is created or
     * destroyed.
     */
    fun invalidateTouches() {
        // Forget the previous compile touches.
        graph.resetTouched()

        // Compile and print.
        println(JsGraphCompiler.compileGraph(graph))

        // Update the CSS to show nodes that weren't touched.
        invalidateTouched()
    }

    /**
     * Invokes [JsNode.invalidateTouched] on all nodes in the GUI
     * to update them to the current touch status.
     *
     * Invoke after a compile.
     */
    @Deprecated("Did you mean to use [invalidateTouches]?")
    private fun invalidateTouched() {
        guiNodes.forEach {
            it.invalidateTouched()
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               update events
    //region                                                  menu events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    @FXML
    private fun menu_save() = saveTestGraph()

    @FXML
    private fun menu_load() = loadTestGraph()

    @FXML
    private fun menu_close() {
        // TODO check for memory leak, as this scene is not broken down at all.
        EDITOR.stage.scene = EDITOR.scene.first
    }

    @FXML
    private fun menu_help() {
//        openURL("")
    }

    @FXML
    private fun menu_add_node() =
        contextMenu.show(root, EDITOR.stage.x + 50.0, EDITOR.stage.y + 60.0)

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               menu events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    companion object {

        /**
         * A local file that may be used
         */
        internal val TEST_GRAPH = Editor.IDEDirectory.subFile("testgraph.jvg")

        /**
         * When creating a new line, this adds
         * the appropriate css classes to it.
         */
        internal fun themeLine(line: Line) {
            line.styleClass.add("line")
            line.stroke = Color.WHITE
            line.strokeWidth = 5.0
        }
    }
}