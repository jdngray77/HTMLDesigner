package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.backend.JsFunction
import com.jdngray77.htmldesigner.frontend.jsdesigner.JsNodeProperty
import com.jdngray77.htmldesigner.utility.SerializableColor
import com.jdngray77.htmldesigner.utility.classEqualsOrSubclass
import com.jdngray77.htmldesigner.utility.toSerializable
import javafx.scene.paint.Color
import org.jsoup.nodes.Element
import java.io.Serializable
import java.lang.System.gc
import kotlin.reflect.KClass


/**
 * The data model for a javascript graph.
 *
 * TODO variables
 * TODO value providers
 */
class JsGraph : Serializable {

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

    fun assertExists(node: JsGraphNode) {
        if (!nodes.contains(node))
            throw Exception("Node does not exist in this graph : $node")
    }


    /**
     * Clears the [JsGraphNode.touch] flag on all [nodes].
     *
     * Performed prior to compilation.
     */
    internal fun resetTouched() {
        nodes.forEach {
            it.touched = false
        }
    }

    /**
     * When creating a new graph,
     * it will represent the current document's scriptable elements.
     */
    init {
//        val x = mvc().currentDocument().allElements.filter{ it -> it.tagName() != "style" && it.id().isNotEmpty() }
//        x.forEach { addElement(it) }
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

    fun addFunction(jsFunction: JsFunction): JsGraphFunction {
//        assertElementDoesNotExist(name)
        JsGraphFunction(jsFunction).apply {
            nodes.add(this)
            return this
        }
    }

    fun addValue() {
        TODO()
    }


    fun getNode(name: String) =
        nodes.find { it.name == name }


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


    /**
     * Compiles the graph into a javascript string.
     */
//    override fun toString() = ""

}


/**
 * A [JsGraphNode] for other code utilities..
 */
class JsGraphFunction (

    val function: JsFunction

) : JsGraphNode(function.name) {
    init {

        emitters.add(
            JsGraphEmitter(
                function.returnType,
                "output",
                this
            )
        )

        receivers.add(
            JsGraphReceiver(
                null,
                JsGraphDataType.Trigger,
                "trigger",
                this
            )
        )



        function.args.forEach {
            receivers.add(
                JsGraphReceiver(
                    it.third,
                    it.second,
                    it.first,
                    this
                )
            )
        }
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
    val name: String,

    /**
     * Position of the node in the GUI.
     */
    var x: Double = 0.0,

    /**
     * Position of the node in the GUI.
     */
    var y: Double = 0.0

) : Serializable {

    /**
     * True if this node was touched on the last compile.
     *
     * If not, then the node is not included in the output, and may be redundant.
     */
    var touched: Boolean = false

    fun touch() {
        touched = true
    }

    /**
     * Data that this node can provide.
     */
    @Deprecated("Directly modifies the data without validation. Connections will not be broken down.")
    val emitters = mutableListOf<JsGraphEmitter>()

    fun emitters() = emitters.toList()

    /**
     * Sockets to recieve data from emitters on other nodes.
     */
    @Deprecated("Directly modifies the data without validation. Connections will not be broken down.")
    val receivers = mutableListOf<JsGraphReceiver>()

    fun recievers() = receivers.toList()

    fun removeAllConnections() {
        emitters.forEach {
            it.emissions().forEach {
                it.breakdown()
            }
        }
        receivers.forEach {
            it.admission?.breakdown()
        }

        gc()
    }

}

// TODO abstract these similiarities
//      in the  emitter and receiver.

/**
 * A data emitter that
 * can provide data to recievers of the
 * same type on other nodes.
 */
class JsGraphEmitter(

    /**
     * Minimal example of data that may be
     * transmitted from this emitter.
     *
     * Used to determine the data type of the emitter.
     *
     * Specifically, the [KClass] of this value determines
     * the type constraints of the emitter.
     *
     * @see getType
     */
    emits: JsGraphDataType,

    name: String,

    parent: JsGraphNode

) : JsGraphProperty(emits, name, parent), Serializable {

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
    fun emit(reciever: JsGraphReceiver) {
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
class JsGraphReceiver(

    // TODO this can't be validated. Create a map?
    val defaultValue: Serializable?,

    type: JsGraphDataType,

    name: String,

    parent: JsGraphNode


) : JsGraphProperty(type, name, parent), Serializable {


    /**
     * Receiving connections.
     */
    var admission: JsGraphEmission? = null
        private set

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
        if (hasAdmission())
            admission?.breakdown()

        admission = emission
    }

    @Deprecated("Directly manipulates the data, without validation.")
    fun revoke(jsGraphEmission: JsGraphEmission) {
        if (jsGraphEmission == admission)
            admission = null
    }

    /**
     * returns true if this receiver is connected to any emitters.
     */
    fun hasAdmission() = admission != null
}

/**
 * Abstraction of [JsGraphReceiver] and [JsGraphEmitter].
 */
abstract class JsGraphProperty(

    val type: JsGraphDataType,

    val name: String,

    val parent: JsGraphNode

) : Serializable {
    override fun toString() = name


    /**
     * Returns true if this receiver is connected to the given emitter.
     */
    fun isTrigger(): Boolean = type == JsGraphDataType.Trigger

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
    val receiver: JsGraphReceiver
) : Serializable {

//    init {
//        if (!classEqualsOrSubclass(receiver.getType(), JsNodeProperty.emitterBeingDragged.emitter.getType()) || receiver.hasAdmission())
//            throw IncompatibleEmissionException(this)
//    }

    /**
     * Revokes the connection.
     */
    fun breakdown() {
        emitter.revoke(this)
        receiver.revoke(this)
    }

    fun touch() {
        emitter.parent.touched = true
        receiver.parent.touched = true
    }
}

/**
 * A [JsGraphNode] for a HTML document element.
 */
class JsGraphElement(id: String) : JsGraphNode(id) {

    init {
        emitters.add(
            JsGraphEmitter(
                JsGraphDataType.Trigger,
                "Click",
                this
            )
        )

        emitters.add(
            JsGraphEmitter(
                JsGraphDataType.Trigger,
                "Hover",
                this
            )
        )

        receivers.add(
            JsGraphReceiver(
                Color.BLACK.toSerializable(),
                JsGraphDataType.Color,
                "Back Color",
                this
            )
        )

        receivers.add(
            JsGraphReceiver(
                Color.BLACK.toSerializable(),
                JsGraphDataType.Color,
                "Text Color",
                this
            )
        )

        receivers.add(
            JsGraphReceiver(
                false,
                JsGraphDataType.Boolean,
                "Visible",
                this
            )
        )
    }

}

enum class JsGraphDataType {
    Number,
    String,
    Boolean,
    Color,
    Trigger,

    void
}

fun JsGraphDataTypeOf(value: Serializable?) =
    when (value) {
        is Number ->  JsGraphDataType.Number
        is SerializableColor -> JsGraphDataType.Color
        is String -> JsGraphDataType.String
        is Boolean -> JsGraphDataType.Boolean
        is JsGraphDataType -> value
        else -> JsGraphDataType.void
    }


class IncompatibleEmissionException(emission: JsGraphEmission) : Exception(
    "Receiver accepts ${emission.receiver.type.name}, but emitter provides ${emission.emitter.emits.name}.\n" +
    "Both must be of the same type to create a connection between them."
)

