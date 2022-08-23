package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphCompiler
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNode
import com.jdngray77.htmldesigner.backend.showErrorNotification
import com.jdngray77.htmldesigner.backend.showWarningNotification
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.controls.ItemSelectionDialog
import com.jdngray77.htmldesigner.utility.*
import javafx.event.ActionEvent
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

    /**
     * The root pane of all nodes and lines.
     *
     * Sibling to [contextPane], which is placed below
     * on the z axis.
     *
     * Fills the entire IDE, and contains [JsNode]'s
     * that can be dragged around within, and lines displaying
     * the connections.
     */
    lateinit var root: Pane

    /**
     * A pane that fills the [root], used for
     * context menu events, such that context
     * menus are only requestes when not moused
     * over a node.
     *
     * Prior to this, context menu request on the [root]
     * would consume secondary mouse clicks anywhere within the editor.
     */
    lateinit var contextPane: Pane

    /**
     * The data model that this view represents.
     *
     * Manipulated by the view, and compiles to js.
     *
     * This data model is serializable such that the graph can be
     * saved, and the JsDesigner view can be re-created from the graph.
     */
    lateinit var graph: JsGraph

    /**
     * List of all graphical representations of nodes in the [graph].
     */
    internal val nodes = mutableListOf<JsNode>()

    /**
     * Context menu used to create new nodes.
     */
    private val contextMenu: ContextMenu = ContextMenu()

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
     * The HTML Document that this script is targeted towards.
     */
    var document: Document

    init {
        with(mvc()) {
            if (!documentAvail()) {
                showWarningNotification("Unable to open Js Designer", "No document is open.")
                throw Exception("No document is open.")
            } else
                document = currentDocument()
        }
    }

    /**
     * A re-usable line used to give feedback to the user
     * when dragging [JsGraphConnection]s, before they
     * have been created.
     */
    internal val temporaryLine = Line().also {
        it.styleClass.addAll(

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

        it.isVisible = false

        it.visibleProperty().addListener { _, _, _ ->
            it.toFront()
        }
    }

    /**
     * Loads a [JsGraph] into the editor.
     *
     * re-creates the view from the graph.
     */
    fun loadGraph(g: JsGraph) {
        // Breakdown existing
        if (this::graph.isInitialized) {
            nodes.clear()

            // There is some stuff in the root that we don't want to delete.
            // These are marked as 'important'.
            // Filter these out, and delete the rest of the nodes and content.
            root.children.filter {
                !it.styleClass.contains("important")
            }.map {
                root.children.remove(it)
            }
            gc()
        }


        graph = g
        graph.getNodes().map {
            implNewNode(it)
                // JavaFX doesn't calculate the position of the node until sometime later.
                // We need to force it to evaluate now so that the bounds are correct
                // in order to position the lines correctly in the next step.
                .root.layout()
        }

        // For every node
        nodes.forEach { guinode ->
            // For each of it's emitters
            guinode.getEmitters().forEach { emitter ->
                // For each emission coming from it
                emitter.emitter.emissions().forEach { emission ->
                    // Locate the receiver within the gui
                    // TODO this isn't efficient.
                    val receivingNode = nodes.find { it.getGraphNode() === emission.receiver.parent }!!
                    val receiver = receivingNode.getReceivers().find { it.receiver.admission === emission }

                    // Create a line.
                    guinode.emitConnectionLine(emitter, receiver!!)
                }
            }
        }

        invalidateTouches()
    }

    /**
     * Deletes every node from the graph.
     */
    fun reset() {
        nodes.concmod().map {
            it.delete()
        }
    }

    /**
     * Breaks down all connections on the entire graph.
     */
    fun resetConnections() {
        nodes.map {
            it.breakdownConnections()
        }
    }


    /**
     * Creates a new GUI Node for the view.
     *
     * Does not modify the model.
     */
    internal fun implNewNode(e: JsGraphNode, x: Double = e.x, y: Double = e.y): JsNode {
        e.x = x
        e.y = y

        loadFXMLComponent<AnchorPane>("JsNode.fxml", javaClass).apply {
            root.children.add(first)
            with((second as JsNode)) {
                nodes.add(this)
                init(e, this@JsDesigner)
                return this
            }
        }
    }


    /**
     * Initializes with a blank graph.
     */
    @FXML
    fun initialize() {
        loadGraph(JsGraph())
        root.children.add(temporaryLine)

        contextPane.setOnContextMenuRequested {
            invalidateTouches()
            contextMenu.show(root, it.screenX, it.screenY)
        }

        contextPane.addEventHandler(MouseEvent.MOUSE_PRESSED) { contextMenu.hide() }
    }

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
        nodes.forEach {
            it.invalidateTouched()
        }
    }

    //region menu
    fun menu_save() {
        graph.saveObjectToDisk("./test.jvg")
    }

    fun menu_load() {
        loadGraph(loadObjectFromDisk(File("./test.jvg")) as JsGraph)
    }

    fun menu_close() {
        EDITOR.stage.scene = EDITOR.scene.first
    }


    fun menu_help() {
//        openURL("")
    }

    fun menu_add_node() {
        contextMenu.show(root, EDITOR.stage.x + 50.0, EDITOR.stage.y + 60.0)
    }

    //endregion menu

    companion object {
        fun themeLine(line: Line) {
            line.styleClass.add("line")

            line.stroke = Color.WHITE
            line.strokeWidth = 5.0
        }
    }
}