package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.Javascript
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import org.jsoup.nodes.Element
import java.io.Serializable


/**
 * A [JsDesigner] data model.
 *
 * Contains [JsGraphNode]s and [JsGraphConnection]s between them.
 *
 * When compiled, creates a javascript that can be placed used on a page.
 */
class JsGraph : Serializable {

    /**
     * Collection of nodes that exist in this graph.
     *
     * There is no hierarchy, all nodes are equal.
     */
    private var nodes: MutableList<JsGraphNode> = mutableListOf()

    /**
     * Returns a non-mutable copy of the nodes contained within this
     * graph.
     *
     * To edit the nodes, use [addNode] and [removeNode].
     */
    fun getNodes() = nodes.toList()

    /**
     * Finds and returns a node in this graph by ID.
     */
    fun getNode(id: String) = nodes.find { it.id == id }

    /**
     * Checks to see if a node with the given ID exists in this graph.
     *
     * @throws IllegalArgumentException if the node does not exist.
     */
    fun assertExists(id: String) {
        if (getNode(id) == null)
            throw IllegalArgumentException("Node with id '$id' does not exist")
    }

    /**
     * Checks to see if a node with the given ID exists in this graph.
     *
     * @throws IllegalArgumentException if the node already exists.
     */
    fun assertDoesNotExist(id: String) {
        if (getNode(id) != null)
            throw IllegalArgumentException("Node with id '$id' already exists in the script graph.")
    }

    /**
     * When creating a new graph,
     * it will represent the current document's scriptable elements.
     */
    init {
        val x = mvc().currentDocument().allElements.filter{ it -> it.tagName() != "style" && it.id().isNotEmpty() }
        x.forEach { addNode(it) }
    }


    //region model manipulation
    /**
     * Creates a new node in the data model.
     */
    fun addNode(e: Element): JsGraphNode {
        if (e.id().isEmpty())
            throw IllegalArgumentException("Element must have an id in order to be scripted.")

        assertDoesNotExist(e.id())

        JsGraphNode(e.id()).apply {
            nodes.add(this)
            return this
        }
    }

    /**
     * Breaks connections a node may have, and deletes the node
     * from the graph.
     */
    fun removeNode(id: String) {
        assertExists(id)
        removeNode(getNode(id)!!)
    }

    /**
     * Breaks connections a node may have, and deletes the node
     * from the graph.
     */
    fun removeNode(node: JsGraphNode) {
        with(node.connections){
            forEach { it.taredown() }
            clear()
        }

        nodes.remove(node)
    }


    /**
     * Creates a new connection between two nodes.
     *
     * TODO this is not invoked, even though connections are being made.
     */
    fun connect(fromID: String, event: String, toID: String, action: String) {
        assertExists(fromID)
        assertExists(toID)
        getNode(fromID)?.connect(event, toID, action)
    }
    //endregion

    /**
     * Compiles the graph into a javascript string.
     */
    override fun toString(): String {
        val js = Javascript()

        nodes.forEach {
            node ->
            node.connections.forEach {
                connection ->
                js.addListener(node.id, connection.localEvent, connection.foriegnProperty.second, connection.foriegnProperty.first)
            }
        }

        return js.toString()
    }
}

/**
 * A representation of an [Element] in a [JsGraph].
 */
data class JsGraphNode(

    /**
     * The id of the [Element] this node represents.
     */
    val id: String,

    /**
     * In the GUI editor, the location of the node.
     *
     * This is used to position the node in the GUI editor when loading
     * the graph.
     */
    var x: Int = 0,

    /**
     * In the GUI editor, the location of the node.
     */
    var y: Int = 0

) : Serializable {

    /**
     * Connections that this node can trigger.
     */
    val connections = mutableListOf<JsGraphConnection>()

    override fun toString() = "JsGraphNode(id='$id', x=$x, y=$y)"

    fun connect(event: String, toID: String, attr: String) {
        connections.add(JsGraphConnection(event, Pair(toID, attr)))
    }
}

/**
 * A link between a [JsGraphNode]s event and another's property.
 *
 * Stored on the triggering [JsNode].
 */
data class JsGraphConnection(

    /**
     * The event on this node that causes this connection to be triggered.
     */
    val localEvent: String,

    /**
     * The property on the other node that is set when this connection is triggered.
     *
     * first : The ID of the node to edit.
     * second : The property to edit.
     */
    val foriegnProperty: Pair<String, String>
) {
    fun taredown() {
        TODO()
    }
}