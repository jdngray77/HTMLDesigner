package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphCompiler
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNode
import com.jdngray77.htmldesigner.backend.showErrorNotification
import com.jdngray77.htmldesigner.backend.showWarningNotification
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.controls.ItemSelectionDialog
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import com.jdngray77.htmldesigner.utility.loadFXMLScene
import javafx.application.Application
import javafx.application.Application.launch
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.stage.Stage
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
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
     * The root pane of this editor.
     *
     * Fills the entire IDE, and contains [JsNode]'s
     * that can be dragged around within, and lines displaying
     * the connections.
     */
    lateinit var root: Pane

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
    val contextMenu: ContextMenu = ContextMenu()

    init {
        contextMenu.items.add(
            MenuItem("From Document").also {
                it.setOnAction {
                    action ->

                    // Fuck this shit, man. Why can't i just put this in the brackets?
                    val filter: (Element) -> Boolean = {
                        it.tagName()!="style" && it.id().isNotEmpty()
                    }

                    document.getElementById(ItemSelectionDialog<String>(
                        document.allElements.filter(filter).map { it.id() }.toList()
                    ).showAndWait())?.let {
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
    var document: Document = mvc().currentDocument()

    /**
     * A re-usable line used to give feedback to the user
     * when dragging [JsGraphConnection]s, before they
     * have been created.
     */
    internal val temporaryLine = Line().also {
        themeLine(it)
        it.styleClass.add("dragging")
        it.isVisible = false

        it.visibleProperty().addListener {
            _,_,_ ->
            it.toFront()
        }
    }

    /**
     * Loads a [JsGraph] into the editor.
     *
     * re-creates the view from the graph.
     */
    fun loadGraph(g: JsGraph) {
        if (this::graph.isInitialized)  {
            nodes.clear()
            root.children.clear()
            gc()
//                graph.unload()
            //  gc()
        }


        graph = g
        graph.getNodes().map { implNewNode(it) }
    }


    /**
     * Creates a new GUI Node for the view.
     *
     * Does not modify the model.
     */
    private fun implNewNode(e: JsGraphNode, x: Double = 0.0, y: Double = 0.0): JsNode {
        e.x=x
        e.y=y

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

        root.setOnContextMenuRequested {
            invalidateTouches()
            contextMenu.show(root, it.screenX, it.screenY)
        }

        root.addEventHandler(MouseEvent.MOUSE_PRESSED) { contextMenu.hide() }
    }

    /**
     * Invoke when a connection between two nodes is created or
     * destroyed.
     *
     * Compiles the entire graph, and invokes [invalidateTouched] on all nodes.
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
     * Invokes [JsNode.invalidateTouched] on all nodes in the GUI.
     */
    private fun invalidateTouched() {
        nodes.forEach {
            it.invalidateTouched()
        }
    }

    companion object {
        fun themeLine(line: Line) {
            line.styleClass.add("line")

            line.stroke = Color.WHITE
            line.strokeWidth = 5.0
        }
    }
}