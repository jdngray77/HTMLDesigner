package com.jdngray77.htmldesigner.frontend.editors.jsdesigner

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.BackgroundTask.onUIThread
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphCompiler
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNode
import com.jdngray77.htmldesigner.frontend.IDE.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.controls.ItemSelectionDialog
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.frontend.editors.Editor
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeDocument
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.findDocumentEditorByDocument
import com.jdngray77.htmldesigner.utility.*
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import org.jsoup.helper.ValidationException
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException
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
class VisualScriptEditor (

    /**
     * The script element that the javascript [graph]
     * will be compiled into.
     */
    val scriptElement: Element,

    /**
     * The document that the [graph] compiles into.
     */
    val scriptDocument: Document = scriptElement.ownerDocument()!!,

    ) : Editor<JsGraph>(

    // Load GUI from fxml
    loadFXMLComponent<SplitPane>("jsDesigner.fxml", VisualScriptEditor::class.java, this).first,

    initDetermineGraph(scriptElement)

) {

    // TODO store the js builder in here. Reuse it?

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                       FXML Controls & GUI components.
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    init {
        title = scriptElement.id() + scriptElement.ownerDocument()?.title().let {
            " (from $it)"
        }
    }

    /**
     * Root of the entire editor.
     *
     * Holds the [editorRootPane], with a [TextArea] below it
     * to hold the [compileOutput]
     */
    @FXML
    var split: SplitPane = root.lookup("#split") as SplitPane

    /**
     * A [TextArea] that can display the output of the compiled graph, if the user chooses
     * to show it.
     */
    @FXML
    private var compileOutput: TextArea = split.items.find { it.id == "compileOutput" } as TextArea

    /**
     * Determines if the [compileOutput] is currently visible.
     */
    fun setCompiledCodeVisible(visible: Boolean) {
        compileOutput.isVisible = visible
        if (visible)
            split.items.addIfAbsent(compileOutput)
        else
            split.items.remove(compileOutput)
    }

    /**
     * @return true if [compileOutput] is visible.
     */
    fun getIsCompiledCodeVisible() = compileOutput.isVisible

    init {
        // Hide this by default. It's added by the menu item.
        split.items.remove(compileOutput)
    }

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
    internal var editorRootPane: Pane = split.items.find { it.id == "editorRootPane" } as Pane

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
    internal var contextPane: Pane = editorRootPane.lookup("#contextPane") as Pane

    /**
     * Context menu used to create new nodes & manage the script.
     */
    private val contextMenu: ContextMenu = menu()
        .item("Add a new node :")
        .separator()

        .item("Element from Web Page") { ctx_createNodeFromDocumentElement() }
        .item("[WIP] Note...") { ctx_createNote() }
        .separator()

        // All other nodes
        .addAll(
            *JsFunctionFactory.asMenus(this::ctx_newFunctionNode).toTypedArray()
        )
        .separator()

        .item("Help") { openURL("https://github.com/jdngray77/HTMLDesigner/wiki/Visual-Scripting") }

        .subMenu("Manage script")
            .item("Save") { requestSave() }
            .item("Validate & Compile") { implValidateAndRecompile() }
            .checkItem("Display the code being generated") { setCompiledCodeVisible(it) }
            .separator()
            .item("Delete everything...") { reset() }
            .item("Unlink everything...") { requestBreakdownAllConnections() }
            .item("Editor looks fucked up...") { requestReloadEditor() }
        .menuDone()

        .toContextMenu()

    /**
     * @return the bounds of the [editorRootPane] within the [root]
     *         of this [VisualScriptEditor].
     */
    fun contextMenuBoundsInEditor() =
        root.screenToLocal(contextMenu.x, contextMenu.y)

    init {
        // Context menu
        contextPane.addContext(contextMenu)
    }

    /**
     * A re-usable line shape used to give feedback to the user
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

            addImportantNode(draggingLine)
        }
    }

    /**
     * Manager for selecting and grouping multiple nodes.
     *
     * Added via [addImportantNode] in [initialize]
     */
    internal val nodeGrouper = JsNodeGrouper(this)

    init {
        addImportantNode(nodeGrouper)
        nodeGrouper.setupListeners()
    }

    /**
     * Adds some FXML component to the editor's [root] that is ***NOT*** a part of the graph.
     *
     * This will be marked as important GUI, and thus will be retained when the editor
     * is cleared via [clearScreen]
     */
    private fun addImportantNode(it: Node) {
        editorRootPane.children.add(it)
        it.styleClass.add("important")
    }

    /**
     * Calculates the bounds of the [editorRootPane] within the [root]
     * of this [VisualScriptEditor]
     */
    fun childToLocal(child: Node): Bounds =
        root.sceneToLocal(child.boundsInScene())


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                         FXML Controls & GUI components.
    //region                                                    Graph data
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Alias for this [Editor]'s [value], for better readability.
     */
    var graph
        set(value) {
            super.value = value
        }
        get() = super.value



//    /**
//     * The data model that this editor is currently editing.
//     *
//     * On [EventType.EDITOR_SELECTED_TAG_CHANGED], when a
//     * script is selected, this is the graph for the selected script
//     * tag.
//     *
//     * When the view is manipulated, this is the model that is modified.
//     * When compiling, this is the model compiled.
//     *
//     * This data model is serializable such that the graph can be
//     * saved, and the JsDesigner view can be re-created from the graph.
//     * TODO make optional
//     * @see implLoadGraph
//     */
//    @Deprecated("Don't set this. Use loadGraph instead.")
//    lateinit var value: JsGraph

    /**
     * List of all graphical representations of nodes in the [graph].
     *
     * These are essentially the controllers for the node panes in the [editorRootPane].
     */
    internal val guiNodes = mutableListOf<JsNode>()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                 Graph Data
    //region                                                  Graph functions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * @returns the GUI [JsNode] that represents the given [JsGraphNode].
     *          or null if there is no [JsNode] for the given [JsGraphNode].
     */
    fun getGUINodeFor(node: JsGraphNode) =
        guiNodes.firstOrNull { it.graphNode == node }

    /**
     * @returns a list of all GUI node groups that contain the given gui node.
     */
    fun getGroupsContaining(node: JsNode) =
        getGroups().filter { it.getNodes().contains(node) }

    /**
     * @returns all GUI node groups in the editor.
     */
    fun getGroups() =
        editorRootPane.children.filterIsInstance<JsNodeGroup>()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                          Graph Functions > Breakdown
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Breaks down all connections on all [guiNodes],
     * if the user consents.
     */
    fun requestBreakdownAllConnections() {
        if (userConfirm("This will destroy ALL connections in the graph.\n\nContinue?"))
            breakdownAllConnections()
    }

    /**
     * Breaks down all connections on all [guiNodes], without
     * asking the user.
     */
    @Deprecated("Prefer request sister method to get consent from the user, as this is destructive.", ReplaceWith("requestBreakdownAllConnections"))
    fun breakdownAllConnections() {
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
    fun requestDeleteNode(node: JsNode) {
        if (userConfirm("This will '${node.getGraphNode().name}',and destroy all connections it.\n\nContinue?"))
            deleteNode(node)
    }

    /**
     * Deletes a given node without questioning the user.
     */
    @Deprecated("Prefer request sister method to get consent from the user, as this is destructive.", ReplaceWith("requestDeleteNode"))
    fun deleteNode(node: JsNode) {

        // Disconnect and delete all connections.
        // This also removes the lines from [emittingLines] and [receivingLines]
        node.breakdownConnections()

        // Remove node from data.
        graph.removeNode(node.getGraphNode())

        // Remove from GUI
        guiNodes.remove(node)

        onUIThread {
            editorRootPane.children.remove(node.root)
        }

        gc()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                        Graph Functions > Breakdown
    //region                                           Graph Functions > Set-up
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Consents the user to [reloadGraph]
     */
    fun requestReloadEditor() {
        if (userConfirm("This will re-load the GUI without modifying the data.\n" +
                        "With any luck, you shouldn't lose any progress.\n\nContinue?"))
            reloadGraph()
    }

    /**
     * Subroutine that reloads the editor from the current [graph].
     *
     * (Also used to create the gui on init.)
     *
     * For ensuring that the editor is in sync with the graph,
     * in exchange for speed.
     *
     * Re-creates the entire GUI from the data, but is slow for big graphs.
     *
     * Best to keep the GUI up-to-date on the fly, but this is
     * a good backup for restoring.
     *
     * Try to just invalidate areas of the graph that have changed
     * if possible, instead of reloading.
     */
    internal fun reloadGraph() {
        clearScreen()

        // Re-create all nodes.
        graph.getNodes().map {
            implCreateGUINode(it)
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
        recompile()

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
     * @throws Exception If [graphNode] does not exist within the [graph] this editor is editing.
     */
    internal fun implCreateGUINode(graphNode: JsGraphNode, x: Double = graphNode.x, y: Double = graphNode.y): JsNode {
        graph.assertExists(graphNode)

        graphNode.x = x
        graphNode.y = y

        // Create GUI from fxml.
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
    override fun save() : Boolean {
        try {
            mvc().Project.saveJsGraph(graph)
        } catch (e: Exception) {
            ExceptionListener.uncaughtException(Thread.currentThread(), e)
            return false
        }
        return true
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
     * @see addImportantNode for adding GUI at runtime.
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
     * Editor has been requested to reset.
     *
     * Drops current graph, and re-loads
     * from disk.
     */
    override fun reset() {
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
     * however when performing large operations this feature only serve
     * to exponentially slow the programme down.
     *
     * Temporarily raise this flag whilst performing large operations
     * to prevent multiple re-compiles during.
     *
     * ***Remember to lower again after!***
     */
    internal var isTransacting = false

    /**
     * Call when performing large operations to prevent multiple re-compiles.
     *
     * Call [endTransaction] when complete.
     */
    fun startTransaction () {
        isTransacting = true
    }

    /**
     * Ends a period of blocking re-compiles.
     *
     * Performs a re-compile.
     */
    fun endTransaction () {
        isTransacting = false
        recompile()
    }

    /**
     * Compiles the entire graph, and in turn evaluates which nodes
     * were touched in the compile, and which were not.
     *
     * After, invokes [invalidateAllNodeTouches] on all nodes to update them
     * with the touch status.
     *
     * Invoke when a connection between two nodes is created or
     * destroyed.
     */
    fun recompile(quietValidation: Boolean = true) {
        if (isTransacting) return

        implValidateAndRecompile(quietValidation)

        // Update the CSS to show nodes that weren't touched.
        invalidateAllNodeTouches()
    }

    /**
     * Compiles the graph and validates the graph.
     *
     * Shows warnings if [quiet] is false. Always shows warnings if the
     * compilatation fails.
     *
     * Updates the [touched] flag on nodes.
     */
    private fun implValidateAndRecompile(quiet: Boolean = true) {
        if (isTransacting) return

        // TODO rename that to validateAndCompile
        var compiled = graph.validateAndCompile()

        println(compiled)

        // Compile and print.
        compileOutput.text = compiled.javascript

        //TODO check if compiled output is different.
        scriptElement.text(compiled.javascript)

        findDocumentEditorByDocument(scriptElement.ownerDocument()!!)
            ?.changed("Updated script : ${scriptElement.id()}")

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

        invalidateAllNodeTouches()
    }

    /**
     * Invokes [JsNode.invalidateTouched] on all nodes in the GUI
     * to notify that touch statuses have changed.
     *
     * Asserts that all GUI nodes correctly represent the touched
     * flag of the corresponding node in the graph.
     */
    private fun invalidateAllNodeTouches() {
        guiNodes.forEach {
            it.invalidateTouched()
        }
    }

    /**
     * Re-evaluates the position of all groups
     *
     * Call when a node that belongs to one or more groups has been moved.
     */
    fun invalidateGroupPositions() {
        getGroups().forEach {
            it.invalidatePosition()
        }
    }

    /**
     * Invalidates the position of all lines.
     */
    fun invalidateAllLinePositions() {
        guiNodes.forEach {
            it.invalidateLinePositions()
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

    /**
     * Context menu item to add a new node that represents an item from the current document.
     */
    fun ctx_createNodeFromDocumentElement() {
        scriptDocument!!.getElementById(
            ItemSelectionDialog<String>(
                scriptDocument!!.allElements
                    .filter(Companion::filterElementIsGraphable)
                    .map { it.id() }
                    .toList()
            ).showAndWait()

        )?.let {
            try {
                val bound = contextMenuBoundsInEditor()

                implCreateGUINode(
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

    /**
     * Creates a new note node.
     */
    fun ctx_createNote() {
        val bound = contextMenuBoundsInEditor()

        implCreateGUINode(
            graph.addNote(
                userInput("Enter the note text")
            ),
            bound.x,
            bound.y
        )
    }

    /**
     * Performs [implCreateGUINode] when a function is selected from the context menu.
     */
    fun ctx_newFunctionNode(it: JsFunction) {
        val bound = contextMenuBoundsInEditor()
        // On user selecting an item, add the function to the graph
        // and create a new node for it.
        implCreateGUINode(
            graph.addFunction(it),
            bound.x - EDITOR.stage.x,
            bound.y - EDITOR.stage.y
        )
    }



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                            menu events
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    // After initalising the rest of the class, load the graph.
    init {
        reloadGraph()

        // listener to solve an issue where lines are not evaluated in the correct position at init.
        root.needsLayoutProperty().addListener(
            object : ChangeListener<Boolean> {
                override fun changed(
                    observable: ObservableValue<out Boolean>,
                    oldValue: Boolean,
                    newValue: Boolean
                ) {
                    if (!newValue) {
                        invalidateGroupPositions()
                        invalidateAllLinePositions()

                        // Remove, so this only happens on the first focus.
                        // It's fixed after the first focus, so it's just overhead.
                        root.needsLayoutProperty().removeListener(this)
                    }
                }
            }
        )
    }

    companion object {

        /**
         * At init time, determines what [JsGraph] instance to use.
         *
         * Is not in class, because it needs to be used before the class is initialised,
         * when initalising the super. The functions within the class are not defined
         * untill after the super class is finished.
         *
         * Will load via project/project cache if already exists,
         * otherwise will create a new graph if it does not exist.
         *
         * @throws ValidationException if [scriptElement] is not a valid script.
         * @see validateJsScriptElement for [scriptElement] validation.
         */
        private fun initDetermineGraph(scriptElement: Element) : JsGraph {

            // Check that the element is actually a valid script.
            scriptElement.validateJsScriptElement()

            val scriptName = scriptElement.id()

            return try {
                // Attempt to load existing graph
                mvc().Project.loadJsGraph(scriptName)

            } catch (e: IOException) {
                // If failed to read file, fail as error
                setAction("Failed to load graph from disk $scriptName")
                throw e

            } catch (e: NoSuchElementException) {
                // If no such graph exists, create a new one.
                mvc().Project.createJsGraph(scriptName)
                setAction("Created a new script graph called $scriptName")
                JsGraph(scriptName)
            }
        }


        /**
         * Tests an element to see if it is a valid script element.
         *
         * - Must be a script tag
         * - Must have an id
         *
         * @throws ValidationException if [scriptElement] does not meet the requirements above.
         */
        private fun Element.validateJsScriptElement() {

            if (tagName() != "script")
                throw ValidationException("Element is not a script tag.")

            if (!hasAttr("id"))
                throw ValidationException("Script element is missing an id attribute.")

        }

        /**
         * Filtering function for use in filtering lists of elements.
         *
         * Returns true if the [element] can be used as a node.
         *
         * - Must have an ID
         * - Must not be a script
         * - Must not be a style
         */
        fun filterElementIsGraphable(element: Element): Boolean =
            element.id().isNotEmpty() && element.tagName().doesNotEqual("style", "script")

        /**
         * When creating a new line, this adds
         * the appropriate css classes to it.
         */
        internal fun Line.configureVisualScriptStyle() {
            styleClass.add("line")
            stroke = Color.WHITE
            strokeWidth = 5.0
        }

        /**
         * Creates a new script, and saves it into the project.
         *
         * @return the script created.
         */
        fun createNewScript() : Element {
            val document = activeDocument()?:
                throw IllegalStateException("No document editor active. Select a document script, so I know where to put the script.")

            val name = userInput("What should the script be called?") {
                if (it.isBlank()) "Enter something" else null
            }

            val e = Element("script")
                .attr("id", name)

            document.body().appendChild(e)

            JsGraph(name).saveObjectToDisk(
                mvc().Project.JS.subFile("$name.jsvg")
            )

            EventNotifier.notifyEvent(EventType.TAG_CREATED)

            return e
        }
    }
}