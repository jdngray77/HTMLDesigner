package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

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
        it.isVisible = false
    }

    /**
     * Loads a [JsGraph] into the editor.
     *
     * re-creates the view from the graph.
     */
    fun loadGraph(g: JsGraph) {
        this::graph.isInitialized.let {
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
    private fun implNewNode(e: JsGraphNode) {
        loadFXMLComponent<AnchorPane>("JsNode.fxml", javaClass).apply {
            root.children.add(first)
            with((second as JsNode)) {
                init(e, this@JsDesigner)
            }
        }
    }


    //region MVC
    /**
     * Creates a new node in the model.
     *
     * Also creates a new GUI node in the view
     * to represent it.
     */
    fun newNode(e: Element) {
        implNewNode(graph.addElement(e))
    }
    //endregion


    @FXML
    fun initialize() {
        loadGraph(JsGraph())
        root.children.add(temporaryLine)

        root.setOnContextMenuRequested {
            println(JsGraphCompiler.compileGraph(graph))
        }
    }

    companion object {
        fun themeLine (line: Line) {
            line.stroke = Color.WHITE
            line.strokeWidth = 5.0
        }
    }
}