package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import javafx.scene.paint.Color
import org.jsoup.nodes.Element
import java.io.Serializable
import java.lang.System.gc

/**
 * The data model for a javascript graph.
 */
class JsGraph : Serializable{

    /**
     * Collection of nodes that exist in this graph.
     *
     * There is no hierarchy, all nodes are equal.
     *
     * The data structure is built with links between the nodes.
     */
    private val nodes = mutableListOf<JsGraphNode>()

    /**
     * Returns a non-mutable copy of the nodes contained within this
     * graph.
     *
     * To edit the nodes, use [addElement] and [removeNode].
     */
    fun getNodes() = nodes.toList()

    /**
     * Finds and returns an element in this graph by ID.
     */
    fun getElementNode(id: String) = nodes.filterIsInstance<JsGraphElement>().find { it.name == id }

    /**
     * Checks to see if a node with the given ID exists in this graph.
     *
     * @throws IllegalArgumentException if the node does not exist.
     */
    fun assertElementExists(id: String) {
        if (getElementNode(id) == null)
            throw IllegalArgumentException("Node with id '$id' does not exist")
    }

    /**
     * Checks to see if a node with the given ID exists in this graph.
     *
     * @throws IllegalArgumentException if the node already exists.
     */
    fun assertElementDoesNotExist(id: String) {
        if (getElementNode(id) != null)
            throw IllegalArgumentException("Node with id '$id' already exists in the script graph.")
    }

    /**
     * When creating a new graph,
     * it will represent the current document's scriptable elements.
     */
    init {
        val x = mvc().currentDocument().allElements.filter{ it -> it.tagName() != "style" && it.id().isNotEmpty() }
        x.forEach { addElement(it) }
    }
    

    //region model manipulation
    /**
     * Creates a new node in the data model.
     */
    fun addElement(e: Element): JsGraphNode {
        if (e.id().isEmpty())
            throw IllegalArgumentException("Element must have an id in order to be scripted.")

        assertElementDoesNotExist(e.id())

        JsGraphElement(e.id()).apply {
            nodes.add(this)
            return this
        }
    }

    /**
     * Breaks connections a node may have, and deletes the node
     * from the graph.
     */
    fun removeNode(id: String) {
        assertElementExists(id)
        removeNode(getElementNode(id)!!)
    }

    /**
     * Breaks connections a node may have, and deletes the node
     * from the graph.
     */
    fun removeNode(node: JsGraphNode) {
        node.removeAllConnections()
        nodes.remove(node)
    }
}

/**
 * Root of all nodes within the graph.
 *
 * May be document elements, functions, or other
 * types of nodes.
 */
abstract class JsGraphNode(

    /**
     * Name displayed in the GUI.
     */
    val name: String

) {



    /**
     * Data that this node can provide.
     */
    protected val emitters = mutableListOf<JsGraphEmitter>()

    fun emitters() = emitters.toList()

    /**
     * Sockets to recieve data from emitters on other nodes.
     */
    protected val recievers = mutableListOf<JsGraphReciever>()

    fun recievers() = recievers.toList()

    fun removeAllConnections() {
        emitters.forEach {
            it.emissions().forEach {
                it.breakdown()
            }
        }
        recievers.forEach {
            it.admissions().forEach {
                it.breakdown()
            }
        }

        gc()
    }

}

/**
 * A data emitter that
 * can provide data to recievers of the
 * same type on other nodes.
 */
class JsGraphEmitter(

    /**
     * The type of data being emitted
     */
    val type: Class<*>,

    val name: String

) {

    /**
     * List of connections emitting from
     * this emitter.
     */
    private val emissions: MutableList<JsGraphEmission> = mutableListOf()

    fun emissions() = emissions.toList()

    /**
     * Creates a link between this emitter and a receiver.
     *
     * A reference to the connection is stored in both.
     */
    fun emit(reciever: JsGraphReciever) {
        val it = JsGraphEmission(
            this,
            reciever
        )

        emit(it)
        reciever.receive(it)
    }


    @Deprecated("Directly manipulates the data, without validation.")
    fun emit(emission: JsGraphEmission) {
        emissions.add(emission)
    }

    @Deprecated("Directly manipulates the data, without validation.")
    fun revoke(jsGraphEmission: JsGraphEmission) {
        emissions.remove(jsGraphEmission)
    }

}
class JsGraphReciever(

    /**
     * The type of data being received
     */
    val type: Class<*>,

    val name: String

) {

    /**
     * Receiving connections.
     */
    private val admissions: MutableList<JsGraphEmission> = mutableListOf()

    fun admissions() = admissions.toList()

    /**
     * Creates a data connection.
     */
    fun connectTo(emitter: JsGraphEmitter) {
        val it = JsGraphEmission(
            emitter,
            this
        )

        emitter.emit(it)
        receive(it)
    }

    fun receive(emission: JsGraphEmission) {
        admissions.add(emission)
    }

    @Deprecated("Directly manipulates the data, without validation.")
    fun revoke(jsGraphEmission: JsGraphEmission) {
        admissions.remove(jsGraphEmission)
    }

}

/**
 * A data connection between an emitter and a reciever.
 *
 * @throws IncompatibleEmissionException if the emitter and receiver are not of the same type.
 */
class JsGraphEmission (

    /**
     * The emitter that is emitting data.
     */
    val emitter: JsGraphEmitter,

    /**
     * The reciever that is receiving data.
     */
    val receiver: JsGraphReciever
) {

    init {
        if (receiver.type != emitter.type)
            throw IncompatibleEmissionException(this)
    }

    /**
     * Revokes the connection.
     */
    fun breakdown() {
        emitter.revoke(this)
        receiver.revoke(this)
    }
}

/**
 * A [JsGraphNode] for a HTML document element.
 */
class JsGraphElement(id: String) : JsGraphNode(id) {

    init {
        emitters.add(
            JsGraphEmitter(
                Trigger::class.java,
                "Click"
            )
        )

        emitters.add(
            JsGraphEmitter(
                Trigger::class.java,
                "Hover"
            )
        )

        recievers.add(
            JsGraphReciever(
                Color::class.java,
                "Back Color"
            )
        )

        recievers.add(
            JsGraphReciever(
                Color::class.java,
                "Text Color"
            )
        )

        recievers.add(
            JsGraphReciever(
                Trigger::class.java,
                "Visible"
            )
        )
    }

}

/**
 * A [JsGraphNode] for other code utilities..
 */
abstract class JsGraphFunction : JsGraphNode("Unknown Function.")

class Trigger

class IncompatibleEmissionException(emission: JsGraphEmission) : Exception(
    "Receiver accepts ${emission.receiver.type.simpleName}, but emitter provides ${emission.emitter.type.simpleName}.\n" +
    "Both must be of the same type to create a connection between them."
)

