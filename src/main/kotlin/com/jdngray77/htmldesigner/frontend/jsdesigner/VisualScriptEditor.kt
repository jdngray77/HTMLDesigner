package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.BackgroundTask.submitToUI
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphCompiler
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNode
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.controls.ItemSelectionDialog
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.utility.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import org.jsoup.nodes.Element
import java.lang.System.gc

/**
 * A editor for designing simple Javascript events.
 *
 * Concept is similar to the visual scripting editors found in
 * engine editors like unity and unreal, where nodes are connected together.
 *
 * This is used to create javascript event listeners without the need to type code.
 *
 * Displays scripts from the current document when selected in the [TagHierarchy]
 *
 * > ***Note : This class should be loaded FXML first. Create new instances with [new]***
 *
 * @author Jordan T. Gray
 */
class VisualScriptEditor : Dock(), Subscriber {

    // TODO store the js builder in here. Reuse it?
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                       FXML Controls & GUI components.
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Root of the entire editor.
     *
     * Holds the [editorRootPane], with a [TextArea] below it
     * to hold the [compileOutput]
     */
    @FXML
    lateinit var split: SplitPane

    /**
     * A [TextArea] that can display the output of the compiled graph, if the user chooses
     * to show it.
     */
    @FXML
    private lateinit var compileOutput: TextArea

    /**
     * The root pane of all nodes and lines.
     *
     * Note that this is not the root of the entire editor.
     *
     * The hierarchy is [Dock.center] -> [split] -> [editorRootPane]
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
    internal lateinit var editorRootPane: Pane

    /**
     * A pane that fills the [editorRootPane].
     *
     * Used for context menu events, such that context
     * menus are only requestes when not moused
     * over a node.
     *
     * Prior to this, context menu request on the [editorRootPane]
     * would consume secondary mouse clicks anywhere within the editor.
     */
    @FXML
    internal lateinit var contextPane: Pane

    /**
     * Context menu used to create new nodes.
     */
    private val newNodeContextMenu: ContextMenu

    internal fun newNodeContextMenuBoundsInEditor() =
        screenToLocal(newNodeContextMenu.x, newNodeContextMenu.y)

    /**
     * Constructor for the [newNodeContextMenu] only.
     */
    init {
        newNodeContextMenu = menu()
            .item("Add a new node :")
            .separator()
            .item("Element from Web Page") { ctx_createNodeFromDocumentElement() }
            .item("[WIP] Node") { ctx_createNote() }
            .separator()
            // All other nodes
            .addAll(
                *JsFunctionFactory.asMenus(this::ctx_newFunctionNode).toTypedArray()
            )
            .toContextMenu()
    }



    /**
     * A re-usable line used to give feedback to the user
     * when dragging [JsGraphConnection]s, before they
     * have been committed.
     */
    internal val draggingLine = Line()

    /**
     * Constructor for the [draggingLine] only.
     */
    init {
        with(draggingLine) {
            styleClass.addAll(

                // Unique class to this line.
                // Differenciates this line from the comitted ones.
                "dragging",

                // Style for this line. Differenciates from the 'line' class used on
                // menu seporator items.
                "connection-line",

                // Legacy, used on some branches.
                // TODO remove 'line when merged.'
                "line"
            )

            isVisible = false

            visibleProperty().addListener { _, _, _ ->
                toFront()
            }
        }
    }

    /**
     * Manager for selecting and grouping multiple nodes.
     *
     * Added via [addImportant] in [initialize]
     */
    internal val grouper = JsNodeGrouper(this)

    /**
     * Adds some FXML component to the editor's [root] that is ***NOT*** a part of the graph.
     *
     * This will be marked as important GUI, and thus will be retained when the editor
     * is cleared via [clearScreen]
     */
    internal fun addImportant(it: Node) {
        editorRootPane.children.add(it)
        it.styleClass.add("important")
    }

    fun childToLocal(child: Node): Bounds =
        sceneToLocal(child.boundsInScene())

//    internal fun sceneToLocal(bounds: Point2D) =
//        sceneToLocal(bounds.x, bounds.y)
//
//    override fun sceneToLocal(x: Double, y: Double) =
//        editorRootPane.sceneToLocal(x, y)


    /**
     * After editor's FXML has loaded, initializes the editor with blank graph.
     */
    @FXML
    fun initialize() {
        // Display in the parenting dock.
        center = split

        addImportant(draggingLine)
        addImportant(grouper)

        grouper.setupListeners()

        // Hide this by default. It's added by the menu item.
        split.items.remove(compileOutput)

        // Context menu
        contextPane.addContext(newNodeContextMenu)
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                         FXML Controls & GUI components.
    //region                                                    Graph data
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * The data model that this editor is currently editing.
     *
     * On [EventType.EDITOR_SELECTED_TAG_CHANGED], when a
     * script is selected, this is the graph for the selected script
     * tag.
     *
     * When the view is manipulated, this is the model that is modified.
     * When compiling, this is the model compiled.
     *
     * This data model is serializable such that the graph can be
     * saved, and the JsDesigner view can be re-created from the graph.
     * TODO make optional
     * @see implLoadGraph
     */
    @Deprecated("Don't set this. Use loadGraph instead.")
    lateinit var graph: JsGraph

    /**
     * List of all graphical representations of nodes in the [graph].
     *
     * These are essentially the controllers for the node panes in the [editorRootPane].
     */
    internal val guiNodes = mutableListOf<JsNode>()

    /**
     * The script element that the [graph] compiles into.
     */
    private var scriptElement: Element? = null

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                 Graph Data
    //region                                                  Graph functions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Returns the GUI Node that represents the given [JsGraphNode].
     */
    fun getGUINodeFor(node: JsGraphNode) =
        guiNodes.firstOrNull { it.graphNode == node }

    /**
     * Returns a list of all GUI node groups that contain the given gui node.
     */
    fun getGroupsContaining(node: JsNode) =
        getGroups().filter { it.getNodes().contains(node) }

    /**
     * Returns all GUI node groups in the editor.
     */
    fun getGroups() =
        editorRootPane.children.filterIsInstance<JsNodeGroup>()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                          Graph Functions > Breakdown
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


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

        implDeleteNode(node)
    }

    /**
     * Deletes a given node without questioning the user.
     */
    fun implDeleteNode(node: JsNode) {

        // Disconnect and delete all connections.
        // This also removes the lines from [emittingLines] and [receivingLines]
        node.breakdownConnections()

        // Remove node from data.
        graph.removeNode(node.getGraphNode())

        // Remove from GUI
        guiNodes.remove(node)

        submitToUI {
            editorRootPane.children.remove(node.root)
        }

        gc()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                        Graph Functions > Breakdown
    //region                                           Graph Functions > Set-up
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
    fun implLoadGraph(g: JsGraph) {
        // Breakdown existing graph.
        if (this::graph.isInitialized) {
            saveGraph()
            resetEditor()
        }

        graph = g
        reloadGraph()
    }

    /**
     * Subroutine that reloads the editor from the current [graph].
     *
     * Ensures the editor is showing the current state of the
     * [graph], but is slow for big graphs.
     *
     * Try to just invalidate areas of the graph that have changed
     * if possible, instead of reloading.
     */
    internal fun reloadGraph() {
        clearScreen()

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


        // Re-create all groups
        graph.getGroups().forEach {
            if (it.isEmpty()) {
                graph.deleteGroup(it)
                return@forEach
            }

            JsNodeGroup(this, it, *it.map { getGUINodeFor(it)!! }.toTypedArray())

            // TODO check for groups with identical contents
        }


        // Check what nodes are touched.
        invalidateTouches()

        // Clean up.
        gc()
    }


    /**
     * Wraps a given graph node with a GUI node in the editor.
     *
     * Used to load nodes from the graph.
     *
     * **Does not modify the graph.**
     *
     * To create a new node in the grape, use this kind of thing:
     * ```
     * implNewNode(
     *  graph.addElement(e)
     * )
     * ```
     *
     * @param graphNode The node in the graph to create a GUI for.
     * @param x The x position of the node, within the scene. Default to using the node's position if not provided.
     * @param y The y position of the node, within the scene. Default to using the node's position if not provided.
     * @return The newly created GUI node.
     * @throws Exception If [graphNode] does not exist within the [graph]
     */
    internal fun implNewNode(graphNode: JsGraphNode, x: Double = graphNode.x, y: Double = graphNode.y): JsNode {
        graph.assertExists(graphNode)

        graphNode.x = x
        graphNode.y = y

        // Load node fxml
        loadFXMLComponent<AnchorPane>("JsNode.fxml", javaClass).apply {

            // Add to root
            editorRootPane.children.add(first)

            with((second as JsNode)) {

                // store reference
                guiNodes.add(this)

                // Configure the node's controller
                initNodeController(graphNode, this@VisualScriptEditor)

                return this
            }
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                           Graph Functions > Set-up
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Saves the current [graph] to disk.
     */
    fun saveGraph() {
        mvc().Project.saveJsGraph(graph)
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               Graph-Functions
    //region                                                  Editor Breakdowns
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Removes all children from the scene, except the important stuff.
     *
     * i.e removes all nodes and lines, but retains containers and the menu bar.
     *
     * Achieved with css class called 'important', which is applied to
     * contents that should not be removed.
     *
     * Due to the layout requirements of the nodes and lines, all contents within
     * the scene are siblings, making it impossible to just clear all
     * children from the [editorRootPane].
     *
     * > Note that this does not clear the graph, and as such should only be
     *      invoked when clearing the graph.
     *
     * @see addImportant for adding GUI at runtime.
     *
     * @author Jordan T. Gray
     */
    private fun clearScreen() {
        editorRootPane.children.filter {
            !it.styleClass.contains("important")
        }.map {
            editorRootPane.children.remove(it)
        }

        // Forget everything we were tracking.
        guiNodes.clear()
    }

    /**
     * User requests to reset editor back to blank.
     *
     * This will confirm with the user, then perform [resetEditor].
     */
    fun requestResetEditor() {
        if (userConfirm("Any unsaved changes will be lost.\n\nContinue?"))
            resetEditor()
    }

    /**
     * Clears the screen, and loads a blank graph.
     *
     * Doesn't break down the current graph.
     * TODO confirm with the user.
     */
    fun resetEditor() {
        // Remove everything from the screen.
        clearScreen()

        // Create a fresh, clear graph.
        graph = JsGraph(".empty")

        // TODO check for memory leaks.
        //      I don't trust that all components of the view and the graph
        //      are properly disposed of, since they're interconnected with many references.
        gc()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                             Editor Breakdowns
    //region                                                update events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * [VisualScriptEditor] automatically re-compiles on changes to represent the data,
     * however when performing large oporations this feature only serve
     * to exponentially slow the programme down.
     *
     * Temporarily raise this flag whilst performing large operations
     * to prevent multiple re-compiles during.
     *
     * ***Remember to lower again after!***
     *
     */
    internal var disableCompile = false

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
    fun invalidateTouches(quietValidation: Boolean = true) {
        if (disableCompile) return

        validateAndCompile(quietValidation)

        // Update the CSS to show nodes that weren't touched.
        invalidateTouched()
    }

    /**
     * Compiles the graph, and shows warnings if [quiet] is false,
     * or the compilations fails.
     */
    private fun validateAndCompile(quiet: Boolean = true) {
        if (disableCompile) return

        // TODO rename that to validateAndCompile
        var compiled = graph.validate()

        // Compile and print.
        compileOutput.text = compiled.javascript
        scriptElement!!.text(compiled.javascript)
        mvc().currentEditor().documentChanged("Updated script : ${scriptElement!!.id()}")

        val isError = compiled.type == JsGraphCompiler.JsGraphCompilationResult.ResultType.ERROR

        if (isError)
            showWarningNotification(
                // Title
                "The graph could not be compiled.", compiled.messages.first().toString()
            )

        if (!quiet || isError) {
            if (compiled.type == JsGraphCompiler.JsGraphCompilationResult.ResultType.SUCCESS)
                showNotification("Graph Validation", "No problems were found with the data!")
            else {
                showNotification(
                    // Title
                    if (isError)
                        "The graph could not be compiled."
                    else
                        "We've detected some minor issues with the graph.", "" +

                            // Message
                            // Concat if there's too many warnings.
                            (if (compiled.messages.size <= 5)
                                compiled.messages.joinToString("\n")
                            else
                                "There are ${compiled.messages.size} messages.") +

                            "\n\n" +
                            "Click here to view warnings."
                ) {
                    // When the notification is clicked, show all warnings in a dialog.
                    showListOfStrings("There are some integrity issue(s) with your graph.", compiled.messages.map { it.toString() })
                }
            }
        }
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

    /**
     * Re-evaluates the position of all groups
     */
    fun invalidateGroupPositions() {
        getGroups().forEach {
            it.invalidatePosition()
        }
    }

    /**
     * Updates all groups to reflect groups in the data.
     *
     * i.e if nodes are removed from the data from a GUI node,
     * invoking this will update any groups that it's in.
     *
     * TODO test what happens when deleting grouped nodes.
     *
     * Invoke after adding or removing nodes from groups.
     */
    fun invalidateGroupData() {
        getGroups().forEach {
            it.invalidateData()
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               update events
    //region                                                  menu events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    @FXML
    private fun menu_help() =
        openURL("https://github.com/jdngray77/HTMLDesigner/wiki/Visual-Scripting")

    @FXML
    private fun menu_add_node() =
        newNodeContextMenu.show(editorRootPane, EDITOR.stage.x + 50.0, EDITOR.stage.y + 60.0)

    @FXML
    fun menu_toggleCompileVisible(actionEvent: ActionEvent) {
        if ((actionEvent.source as CheckMenuItem).isSelected)
            split.items.addIfAbsent(compileOutput)
        else
            split.items.remove(compileOutput)
    }

    fun validateGraph() {
        validateAndCompile(false)
    }

    /**
     * Context menu item to add a new node that represents an item from the current document.
     */
    fun ctx_createNodeFromDocumentElement() {
        val document = mvc().currentDocument()

        document.getElementById(
            ItemSelectionDialog<String>(
                document.allElements
                    .filter(VisualScriptEditor::filterElementIsGraphable)
                    .map { it.id() }
                    .toList()
            ).showAndWait()

        )?.let {
            try {
                val bound = newNodeContextMenuBoundsInEditor()

                implNewNode(
                    graph.addElement(it),
                    bound.x, bound.y
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

    // TODO abstract create new node from menu

    /**
     * Creates a new note node.
     */
    fun ctx_createNote() {
        val bound = newNodeContextMenuBoundsInEditor()

        implNewNode(
            graph.addNote(
                userInput("Enter the note text")
            ),
            bound.x,
            bound.y
        )
    }

    fun ctx_newFunctionNode(it: JsFunction) {

        val bound = newNodeContextMenuBoundsInEditor()
        // On user selecting an item, add the function to the graph
        // and create a new node for it.
        implNewNode(
            graph.addFunction(it),
            bound.x - EDITOR.stage.x,
            bound.y - EDITOR.stage.y
        )
    }


    fun menu_new_script() {
        val name = userInput("What should the script be called?") {
            if (it.isBlank()) "Enter something" else null
        }

        val e = Element("script")
            .attr("id", name)

        mvc().currentEditor().apply {
            document.body().appendChild(e)
            selectTag(e)
            documentChanged("Created script '$name'")
        }

        implLoadGraph(
            mvc().Project.newJsGraph(name)
        )


        //TODO create an event for tag created / deleted. This is fucking stupid.
        EventNotifier.notifyEvent(EventType.TAG_CREATED)
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                            menu events
    //region                                               IDE events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    init {
        EventNotifier.subscribe(
            this,
            EventType.EDITOR_DOCUMENT_SWITCH,
            EventType.PROJECT_SAVED,
            EventType.EDITOR_SELECTED_TAG_CHANGED,
        )
    }

    override fun notify(e: EventType) {
        when (e) {
            EventType.EDITOR_DOCUMENT_SWITCH -> {
                scriptElement = mvc().selectedTag()
                if (scriptElement != null && scriptElement!!.tagName() == "script") {
                    implLoadGraph(
                        mvc().Project.loadJsGraph(scriptElement!!.attr("id"))
                    )
                }
            }

            EventType.PROJECT_SAVED -> {
                saveGraph()
            }

            EventType.EDITOR_SELECTED_TAG_CHANGED -> {
                val tag = mvc().selectedTag()
                if (tag != null && tag.tagName() == "script") {
                    scriptElement = tag
                    implLoadGraph(
                        mvc().Project.loadJsGraph(tag.attr("id"))
                    )
                }
            }

            else -> {}
        }

        if (scriptElement == null)
            resetEditor()
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               IDE events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    companion object {

        /**
         * For use in filtering lists of elements.
         *
         * Returns true if the [e]lement can be used as a node.
         *
         * - Must have an ID
         * - Must not be a script
         *
         * // TODO move to instance, exlude ones that are already added.
         */
        fun filterElementIsGraphable(e: Element): Boolean =
            e.id().isNotEmpty() && e.tagName().doesNotEqual("style", "script")


        /**
         * Creates a new [VisualScriptEditor].
         *
         * @return ([SplitPane], [VisualScriptEditor]) Where the split pane is the root of the editor's GUI.
         */
        fun new(): Pair<SplitPane, VisualScriptEditor> =
            loadFXMLComponent<SplitPane>("JsDesigner.fxml", this::class.java) as Pair<SplitPane, VisualScriptEditor>

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