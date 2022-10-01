package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.backend.JsFunction
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.utility.SerializableColor
import com.jdngray77.htmldesigner.utility.concmod
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
 * TODO function names are not unique. This is a problem.
 */
class JsGraph(

    /**
     * A name that identifies the graph.
     *
     * Used to name the file that this is saved in.
     */
    val scriptName: String

) : Serializable {

    /**
     * Collection of nodes that exist in this graph.
     *
     * There is no hierarchy, all nodes are equal.
     *
     * The data structure is built with links between the nodes.
     */
    private val nodes = mutableListOf<JsGraphNode>()

    /**
     * Collection node groups.
     */
    private val groups = mutableListOf<JsGraphNodeGroup>()

    /**
     * Returns a non-mutable copy of the nodes contained within this
     * graph.
     *
     * To edit the nodes, use [addElement] and [removeNode].
     */
    fun getNodes() = nodes.toList()

    /**
     * Returns all [JsGraphElement]
     */
    fun getElementNodes() = nodes.filterIsInstance<JsGraphElement>()

    /**
     * Returns all [JsGraphFunctions]'s in this graph.
     */
    fun getFunctionNodes() =
        nodes.filterIsInstance<JsGraphFunction>()


    /**
     * Finds any node by ID.
     *
     * @return node by ID, or null if not found.
     */
    fun getNode(name: String) =
        nodes.find { it.name == name }

    /**
     * Finds and returns an element in this graph by ID.
     *
     * @return element by ID
     */
    fun getElement(name: String) =
        getNode(name) as? JsGraphElement

    /**
     * Finds and returns a function in this graph by ID.
     */
    fun getFunction(name: String) =
        getNode(name) as? JsGraphFunction


    /**
     * Checks to see if a node with the given ID exists in this graph.
     *
     * @throws IllegalArgumentException if the node does not exist.
     */
    fun assertElementExists(name: String) {
        if (this.getElement(name) == null)
            throw IllegalArgumentException("Node with id '$name' does not exist")
    }

    /**
     * Checks to see if a node with the given ID exists in this graph.
     *
     * @throws IllegalArgumentException if the node already exists.
     */
    fun assertElementDoesNotExist(name: String) {
        if (this.getElement(name) != null)
            throw IllegalArgumentException("Node with id '$name' already exists in the script graph.")
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
            it.resetTouched()
        }
    }

    /**
     * Deletes all nodes and groups
     */
    fun deleteEverything() {
        nodes.clear()
        groups.clear()
        gc()
    }

    /**
     * Checks the data for integrity, attempts to fix anything that appears invalid
     * if permitted by [Configs.JS_GRAPH_AUTOFIX_MAJOR_BOOL], [Configs.JS_GRAPH_AUTOFIX_MINOR_BOOL]
     * and [Configs.JS_GRAPH_AUTO_DELETE_UNCOMPILED_NODES_BOOL].
     *
     * Compiles the graph and returns the result.
     * // TODO unit test this
     * // TODO Check for dupe element nodes
     * @returns a list of warnings of the problems and fixes, or null if there was none.
     */
    fun validateAndCompile(): JsGraphCompiler.JsGraphCompilationResult {
        val problems = mutableListOf<JsGraphCompiler.JsCompilationResultMessage>()
        val wasEmpty = nodes.isEmpty()

        // Test compile to check for nodes not being touched.
        // Also check if there's actually any output
        val compiled = JsGraphCompiler.compileGraph(this)

        problems.addAll(0, compiled.messages)

        val minor = Config[Configs.JS_GRAPH_AUTOFIX_MINOR_BOOL] as Boolean
        val major = Config[Configs.JS_GRAPH_AUTOFIX_MAJOR_BOOL] as Boolean

        groups.concmod().forEach {
            if (it.isEmpty()) {
                proposeFix(
                    minor,
                    "The group '${it.name}' has no nodes",
                    "so it was deleted"
                ) {
                    deleteGroup(it)
                }
            }

            it.concmod().forEach { node ->
                if (!nodes.contains(node)) {
                    proposeFix(
                        minor,
                        "The group '${it.name}' contains '${node.name}', a node that does not exist within the graph",
                        "so the node was removed from the group"
                    ) {
                        it.remove(node)
                    }
                }
            }

            // Check for nodes that are not in the graph.
        }

        nodes.concmod().forEach {

            // Check for signals being emitted, but not being received
            it.emitters().forEach { emitter ->
                emitter.emissions().forEach { emission ->

                    if (!emission.receiver.hasAdmission()) {
                        proposeFix(
                            minor,
                            "'$emission' was being emitted but the receiver didn't know about the connection",
                            "so the connection was re-attached to the emitter."
                        ) {
                            emission.receiver.receive(emission)
                        }
                    }

                    if (emission.receiver.admission !== emission) {
                        proposeFix(
                            minor,
                            "'$emission' was being emitted but the receiver was accepting a different connection",
                            "so the connection deleted."
                        ) {
                            emission.breakdown()
                        }
                    }
                }
            }


            // Check for signals being received, but not being emitted
            it.receivers().forEach { receiver ->
                receiver.admission?.let {
                    if (!it.emitter.emissions().contains(it)) {
                        proposeFix(
                            minor,
                            "'$it' was being received but not emitted",
                            "so the connection was deleted."
                        ) {
                            it.breakdown()
                        }
                    }
                }
            }

            // Check if included in the output
            if (!it.touched) {
                proposeFix(
                    Config[Configs.JS_GRAPH_AUTO_DELETE_UNCOMPILED_NODES_BOOL] as Boolean,
                    "The node '${it.name}' was not included in the output",
                    "so it was deleted as specified by the auto-delete setting.",
                    "but it was preserved by the auto-delete setting."
                ) {
                    removeNode(it)
                }
            }
        }

        if (!wasEmpty && nodes.isEmpty())
            problems.add(
                JsGraphCompiler.JsCompilationResultMessage(
                    JsGraphCompiler.MessageType.WARNING_VALIDATION_PROBLEM,
                    "Validation deleted all nodes."
                )
            )

        return JsGraphCompiler.JsGraphCompilationResult(compiled.javascript, problems, builder = compiled.builder)
    }

    /**
     * During validation, performs fixes if they are allowed.
     *
     * @param permission the permission level to allow fix
     * @param faultMessage the fault
     * @param fixMessage Message used if action was taken
     * @param noFixAppliedMessage Message used if no action was taken
     * @param correctiveAction the corrective action to perform
     */
    private fun proposeFix(
        permission: Boolean,
        faultMessage: String,
        fixMessage: String,
        noFixAppliedMessage: String = "but auto-fix was disabled",
        correctiveAction: () -> Unit
    ): JsGraphCompiler.JsCompilationResultMessage {
        var m = "$faultMessage, "

        var t = if (permission) {
            correctiveAction()
            m += fixMessage
            JsGraphCompiler.MessageType.WARNING_VALIDATION_PROBLEM_AUTO_FIXED
        } else {
            m += noFixAppliedMessage
            JsGraphCompiler.MessageType.WARNING_VALIDATION_PROBLEM
        }

        return JsGraphCompiler.JsCompilationResultMessage(t, m)
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

    fun addNote(note: String): JsGraphNoteNode {
        if (note.isBlank()) {
            throw IllegalArgumentException("Note cannot be blank.")
        }

        return JsGraphNoteNode(note).apply {
            nodes.add(this)
        }
    }

    fun addValue() {
        TODO()
    }


    /**
     * Breaks connections a node may have, and deletes the node
     * from the graph.
     */
    fun removeNode(id: String) {
        assertElementExists(id)
        removeNode(this.getElement(id)!!)
    }

    /**
     * Deletes a node from the graph.
     *
     * Remove all connections first.
     *
     * Also removes the node from any groups that the node is a member of.
     */
    fun removeNode(node: JsGraphNode) {
        node.removeAllConnections()
        nodes.remove(node)

        // Remove the node from any groups that it may have
        // been in.
        groups.forEach {
            it.remove(node)
        }
    }

    /**
     * Creates a copy of the given node.
     */
    fun cloneNode(node: JsGraphNode) =
        when (node) {

            is JsGraphFunction -> addFunction(node.function.clone())

            is JsGraphElement -> throw java.lang.Exception("Elements can only exist once.")

            else -> throw java.lang.Exception("Not equipped to duplicate a node of type ${node::class.simpleName}")
        }.let {
            it.x = node.x + 10
            it.y = node.y + 10
            it
        }

    /**
     * Clones all nodes in a group, then adds
     * them to a new group.
     *
     * The new group is returned.
     */
    fun cloneGroup(group: JsGraphNodeGroup): JsGraphNodeGroup {
        val newGroup = JsGraphNodeGroup(group.name, group.color)

        group.forEach {
            newGroup.add(cloneNode(it))
        }

        return newGroup
    }

    /**
     * Erradicates a group from the graph, without removing
     * any nodes that may be contained with it.
     * TODO membership assertions
     */
    fun dumpGroup(group: JsGraphNodeGroup) {
        group.clear()
        groups.remove(group)
    }

    /**
     * Deletes a group of nodes from the graph.
     *
     * All nodes are broken down and deleted from the graph.
     *
     * The group is also them removed from the graph.
     *
     * * TODO membership assertions
     */
    fun deleteGroup(group: JsGraphNodeGroup) {
        group.concmod().forEach(this::removeNode)
        dumpGroup(group)
    }

    /**
     * TODO membership assertion
     */
    fun addGroup(group: JsGraphNodeGroup) {
        groups.add(group)
    }

    fun hasGroup(graphGroup: JsGraphNodeGroup): Boolean {
        return groups.contains(graphGroup)
    }

    fun getGroups() = groups.toList()

    /**
     * Compiles the graph into a javascript string.
     */
//    override fun toString() = ""

}


/**
 * A [JsGraphNode] for other code utilities..
 */
class JsGraphFunction(

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
                    null,
                    it.second,
                    it.first,
                    this
                )
            )
        }
    }

    /**
     * returns the trigger receiver.
     */
    fun trigger() = receiver("trigger")

    /**
     * returns the output emitter.
     */
    fun output() = emitter("output")
}

class JsGraphNoteNode(note: String) : JsGraphNode(note) {
    override fun resetTouched() = touch()

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
        private set

    open fun resetTouched() {
        touched = false
    }

    open fun touch() {
        touched = true
    }

    /**
     * Data that this node can provide.
     */
    @Deprecated("Directly modifies the data without validation. Connections will not be broken down.")
    val emitters = mutableListOf<JsGraphEmitter>()

    fun emitters() = emitters.toList()

    /**
     * Finds an emitter by name.
     *
     * If there's more than one with the same name, only the first will be returned.
     *
     * If none match, will return null
     */
    fun emitter(name: String) =
        emitters.find { it.name == name } ?: throw NoSuchElementException("No emitter with name $name")

    /**
     * Sockets to recieve data from emitters on other nodes.
     */
    @Deprecated("Directly modifies the data without validation. Connections will not be broken down.")
    val receivers = mutableListOf<JsGraphReceiver>()

    fun receivers() = receivers.toList()

    /**
     * Finds an reciever by name.
     *
     * If there's more than one with the same name, only the first will be returned.
     *
     * If none match, will return null
     */
    fun receiver(name: String) =
        receivers.find { it.name == name } ?: throw NoSuchElementException("No receiver with name $name")

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

) : JsGraphNodeProperty(emits, name, parent), Serializable {

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

    fun breakdownAllEmissions() {
        emissions.concmod().forEach {
            it.breakdown()
        }
    }
}

class JsGraphReceiver(

    /**
     * When being evaluated (especially on functions) but has no incoming data
     * provided from an [admission], this value is used
     * instead.
     *
     * Optional.
     *
     * If not provided, an incoming connection will be required.
     */
    val defaultValue: Serializable?,

    /**
     * When being evaluated (especially on Elements) this determines
     * how incoming data is handled.
     *
     * for example, on an element incomming data may be set into the background color :
     *
     * ```
     * { "$parent.name.style.backgroundColor = $it" },
     * ```
     */
    val setter: ((String) -> String)?,

    type: JsGraphDataType,

    name: String,

    parent: JsGraphNode


) : JsGraphNodeProperty(type, name, parent), Serializable {


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

    override fun toString() = "${parent.name}.$name"
}

/**
 * Abstraction of [JsGraphReceiver] and [JsGraphEmitter].
 */
abstract class JsGraphNodeProperty(

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
class JsGraphEmission(

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
        emitter.parent.touch()
        receiver.parent.touch()
    }

    override fun toString() = "${emitter.parent.name}#${emitter.name} -> ${receiver.parent.name}#${receiver.name}"
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
                "Mouse Over",
                this
            )
        )

        receivers.add(
            JsGraphReceiver(
                Color.BLACK.toSerializable(),
                { "$name.style.backgroundColor = $it" },
                JsGraphDataType.Color,
                "Back Color",
                this
            )
        )

        receivers.add(
            JsGraphReceiver(
                Color.BLACK.toSerializable(),
                { "$name.style.color = $it" },
                JsGraphDataType.Color,
                "Text Color",
                this
            )
        )

        receivers.add(
            JsGraphReceiver(
                false,
                {
                    "$name.style.visibility = ${
                        if (it.toBoolean())
                            "visible"
                        else
                            "hidden"
                    }"
                },
                JsGraphDataType.Boolean,
                "Visible",
                this
            )
        )
    }

}

/**
 * Specifies a group of [JsGraphNode]s.
 *
 * These nodes may be operated on as a group,
 * such as dragged, deleted, or copied.
 *
 * This also labels the group, as to create a
 * better visualisation.
 *
 * The GUI places a labeled box around the nodes.
 */
class JsGraphNodeGroup(

    var name: String,

    /**
     * A personalised color used in the GUI, if the user chooses to use it.
     */
    var color: SerializableColor = SerializableColor(1.0, 1.0, 1.0, 0.5),

    vararg nodes: JsGraphNode

) : ArrayList<JsGraphNode>(nodes.size), Serializable {

    /**
     * Constructor to create a group without having to specify the color.
     */
    constructor(name: String, vararg _nodes: JsGraphNode) : this(name, nodes = _nodes)


    init {
        addAll(nodes)
    }

    /**
     * Groups are not considered to be the same
     * if they contain the same set of nodes.
     *
     * Equality is only true if both groups are the exact
     * same group instance.
     */
    override fun equals(other: Any?) = this === other
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
        is Number -> JsGraphDataType.Number
        is SerializableColor -> JsGraphDataType.Color
        is String -> JsGraphDataType.String
        is Boolean -> JsGraphDataType.Boolean
        is JsGraphDataType -> value
        else -> JsGraphDataType.void
    }


class IncompatibleEmissionException(emission: JsGraphEmission) : Exception(
    "Receiver accepts ${emission.receiver.type.name}, but emitter provides ${emission.emitter.type.name}.\n" +
            "Both must be of the same type to create a connection between them."
)

