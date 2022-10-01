package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.backend.JavascriptBuilder
import com.jdngray77.htmldesigner.backend.JavascriptBuilder.Companion.wrapAnonFunction
import com.jdngray77.htmldesigner.backend.logWarning
import com.jdngray77.htmldesigner.utility.toCamel

/**
 * A non-comprehensive compiler for the [JsGraph].
 *
 * Accepts a [JsGraph] and generates a javascript string
 * that can be placed into the web page using a [JavascriptBuilder]
 *
 * TODO coallate auto fixes with different levels & a setting
 * TODO warning levels + prefix
 *
 * TODO CHECK THAT NODES FOUND VIA CONNECTIONS ARE IN THE GRAPH
 */
object JsGraphCompiler {


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                  Result tracking
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * The result of compiling a graph.
     *
     * Wraps the javascript and any error or warnings messages
     * produced during generation.
     */
    class JsGraphCompilationResult(

        /**
         * The generated output javascript.
         *
         * May be empty if the graph failed to compile.
         */
        val javascript: String,

        /**
         * A list of warning messages generated, if any.
         */
        val messages: List<JsCompilationResultMessage>,

        /**
         * The overall result of this compilation.
         *
         * If not provided, will [autoType]
         */
        val type: ResultType = autoType(javascript, messages),

        /**
         * The javascript builder that was used to build the [javascript].
         *
         * Can be used to poll for state of the javascript, such as presence of functions
         * or variables.
         */
        val builder: JavascriptBuilder

    ) {

        /**
         * The type of compilation result.
         */
        enum class ResultType {

            /**
             * The graph was comiled with zero errors or warnings.
             */
            SUCCESS,

            /**
             * The graph was compiled successfully, but there were some warnings.
             */
            SUCCESS_WITH_WARNINGS,

            /**
             * The graph could not be compiled.
             */
            ERROR,
        }

        /**
         * toString is a printable visual report of the compilation result.
         */
        override fun toString(): String {
            return """
Compilation Result Report :
$type

${
                if (javascript.isEmpty())
                    "No Javascript Generated"
                else
                    "Javascript:\n\n$javascript"
            }



${
                if (messages.isEmpty())
                    "No Warnings"
                else
                    "Messages:\n\n${messages.joinToString("\n")}"
            }
            """
        }

        companion object {

            /**
             * Automatically determines the result type based on the state of the warnings.
             *
             * Only used if no type is explicitly specified.
             */
            private fun autoType(js: String, warns: List<JsCompilationResultMessage>): ResultType {
                return when {
                    warns.isEmpty() -> ResultType.SUCCESS
                    else -> if (js.isBlank() || warns.any { it.level == MessageLevel.ERROR })
                        ResultType.ERROR
                    else
                        ResultType.SUCCESS_WITH_WARNINGS
                }
            }

        }
    }

    /**
     * A message that can be provided during compilation
     * via the [JsGraphCompilationResult].
     *
     * Provides warnings and errors.
     */
    class JsCompilationResultMessage(

        /**
         * The type of message
         */
        val type: MessageType,

        /**
         * The message
         */
        val message: String?
    ) {

        val level: MessageLevel = try {
            MessageLevel.valueOf(type.name.split("_").first())
        } catch (e : IllegalArgumentException) {
            logWarning("Compiler message did not begin with a message level : $type")
            MessageLevel.WARNING
        }

        override fun toString() =
            if (message == null)
                type.name
            else
                "$message ($type)"
    }

    enum class MessageLevel() {
        INFO,
        WARNING,
        ERROR
    }

    /**
     * Types of [JsCompilationResultMessage] that can appear in the [JsGraphCompilationResult].
     */
    enum class MessageType {

        /**
         * Informational message.
         */
        INFO,

        /**
         * Whilst validating the graph, problem was found but not fixed.
         */
        WARNING_VALIDATION_PROBLEM,
        // TODO unit test these values begin with message level.

        /**
         * Whilst validating the graph, problem was found and automatically was fixed.
         */
        WARNING_VALIDATION_PROBLEM_AUTO_FIXED,

        /**
         * After compiling the graph, there was no javascript.
         */
        WARNING_NO_JAVASCRIPT_GENERATED,

        /**
         * The graph contains no nodes to compile.
         */
        WARNING_NO_NODES,

        /**
         * A function has been triggered, but the output was unused.
         */
        WARNING_TRIGGERED_FUNCTION_UNUSED,

        /**
         * A function is being triggered when it's not supposed to be.
         */
        WARNING_INVALID_TRIGGER_POSITION,

        /**
         * A function does not *require* inputs, but none are being supplied.
         *
         * The function will always use it's default values.
         */
        WARNING_FUNCTION_HAS_NO_INPUT,

        /**
         * A function's parameter requires a value, but none is provided.
         */
        ERROR_FUNCTION_MISSING_REQUIRED_INPUTS,

        /**
         * A function was triggered by an emitter that is not emitting a trigger
         * signal.
         */
        ERROR_FUNCTION_TRIGGERED_BY_NON_TRIGGER,

        /**
         * Came across a node that does not exist within the graph.
         *
         * Perhaps it remained connected to another node after the node was deleted
         * from the graph.
         */
        ERROR_NON_EXISTANT_NODE,

        /**
         * A function was triggered by something that wasn't an element node.
         */
        ERROR_TRIGGERED_BY_NON_ELEMENT,

        /**
         * Something is being triggered that is not a function.
         */
        ERROR_TRIGGERING_NON_FUNCTION,

        /**
         * Function has no output emitter?
         */
        ERROR_FUNCTION_HAS_NO_OUTPUT,

        /**
         * Came across something that this compiler version knows it
         * cannot handle, such as some kind of node with no ability to
         * compile it.
         */
        ERROR_COMPILER_NOT_EQUIPT_TO_HANDLE,

        /**
         * Any other kind of exception thrown during compilation.
         */
        ERROR_OTHER
    }

    /**
     * Called during compilation to report a message that is not a problem.
     *
     * @param message The message to report.
     */
    private fun INFO(message: String) =
        messages.add(JsCompilationResultMessage(MessageType.INFO, message))

    /**
     * Called during compilation to report a warning.
     *
     * @param MessageType The type of warning
     * @param message Optional. The message to report
     */
    private fun WARN(MessageType: MessageType, message: String? = null) =
        messages.add(JsCompilationResultMessage(MessageType, message))

    /**
     * Called during compilation to report an error.
     *
     * @param MessageType The type of error
     * @throws JsGraphCompilationException (as long as not [suppressException]) to abort compilation.
     * @throws IllegalArgumentException if [MessageType] is not an error. Should never happen. Indicates a programming error.
     */
    private fun ERROR(MessageType: MessageType, message: String? = null, suppressException: Boolean = false) {
        if (!MessageType.name.startsWith("ERROR"))
            throw IllegalArgumentException("Threw an error with a message.")

        messages.add(0, JsCompilationResultMessage(MessageType, message))

        if (!suppressException)
            throw JsGraphCompilationException(message ?: "No message provided.")
    }

    /**
     * A log of all warnings generated during compilation.
     */
    private val messages = mutableListOf<JsCompilationResultMessage>()

    /**
     * Returns all messages generated during the last compilation.
     */
    fun getMessages(): List<JsCompilationResultMessage> {
        return messages.toList()
    }

    /**
     * Returns the errors generated during the last compilation.
     */
    fun getErrors(): List<JsCompilationResultMessage> {
        return messages.filter { it.type.name.startsWith("ERROR") }
    }

    /**
     * Returns the warnings generated during the last compilation.
     */
    fun getWarnings(): List<JsCompilationResultMessage> {
        return messages.filter { !it.type.name.startsWith("WARNING") }
    }

    /**
     * Returns the info messages generated during the last compilation.
     */
    fun getInfo(): List<JsCompilationResultMessage> =
        messages.filter { it.type.name.startsWith("INFO") }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                         Result tracking
    //region                                            Graph tracking
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * The graph last compiled, or being worked on.
     */
    private var graph: JsGraph? = null

    private lateinit var js: JavascriptBuilder

    /**
     * Checks that nodes within the given connection are still within
     * the [graph] currently being compiled.
     */
    private fun checkNodesExist(connection: JsGraphEmission) {
        checkNodeExists(connection.receiver.parent)
        checkNodeExists(connection.emitter.parent)
    }

    /**
     * Checks that the given node is still within the [graph] currently being compiled.
     */
    private fun checkNodeExists(node: JsGraphNode) {
        try {
            graph!!.assertExists(node)
        } catch (e: Exception) {
            ERROR(MessageType.ERROR_NON_EXISTANT_NODE, "Node ${node.name} does not exist in the graph.")
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                      Graph tracking
    //region                                         Compilation
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Creates a script from a [JsGraph].
     *
     * syncronized to block threads into one compilation at any given time
     * since global [messages] and [graph] are shared.
     *
     * TODO this could be fixed if this was a class instead of an object.
     *
     * @param [graph] The graph to compile.
     * @return [String] The javascript string.
     */
    @Synchronized
    fun compileGraph(graph: JsGraph): JsGraphCompilationResult {

        // Update tracking
        messages.clear()
        this.graph = graph
        js = JavascriptBuilder()

        // Check the graph, first.

        if (graph.getNodes().isEmpty())
            WARN(MessageType.WARNING_NO_NODES)


        graph.resetTouched()

        try {

            with(graph) {

                // For every HTML element,
                getNodes().filterIsInstance<JsGraphElement>().forEach {
                    // check each emitter
                    it.emitters().filter { it.isTrigger() }.forEach {
                        // For every event it triggers
                        it.emissions().forEach {
                            compileFunctionTrigger(it)
                        }
                    }
                }
            }

        } catch (e: Exception) {
            if (e !is JsGraphCompilationException)
                ERROR(MessageType.ERROR_OTHER, e.message ?: "No more information provided.", true)

            return JsGraphCompilationResult("", messages, builder = js)
        }

        val javascript = js.toString()

        if (javascript.isBlank())
            WARN(MessageType.WARNING_NO_JAVASCRIPT_GENERATED)

        return JsGraphCompilationResult(javascript, messages, builder = js)
    }

    /**
     * Builds a javascript event listener that will invoke a function.
     *
     * i.e
     *
     * ```
     * MainContent.addEventListener("click", () => {
     *   ...
     * });
     * ```
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [connection] The [JsGraphConnection] being emitted by the trigger. This determines the event, and what's inside it.
     * @throws IllegalArgumentException If [connection] is not being emitted by a [JsGraphElement], or is not an event trigger.
     * @throws IllegalArgumentException If the node being triggered is not a [JsGraphFunction]
     */
    private fun compileFunctionTrigger(connection: JsGraphEmission): String {
        checkNodesExist(connection)

        if (!connection.emitter.isTrigger())
            ERROR(
                MessageType.ERROR_FUNCTION_TRIGGERED_BY_NON_TRIGGER,
                "${connection.emitter.name} is not a trigger, but it's trying to trigger ${connection.receiver.name}"
            )

        if (connection.emitter.parent !is JsGraphElement)
            ERROR(
                MessageType.ERROR_TRIGGERED_BY_NON_ELEMENT,
                "${connection.emitter.parent.name} is not an element. Only elements can cause triggers."
            )

        if (connection.receiver.parent !is JsGraphFunction) {
            ERROR(
                MessageType.ERROR_TRIGGERING_NON_FUNCTION,
                "${connection.receiver.parent.name} is not a function. Only functions can be triggered."
            )
            return ""
        }

        // Check that the function compiles well
        val compiledFunc = compileFunctionAndOutput(connection.receiver.parent)

        if (compiledFunc.isBlank())
            return ""

        connection.touch()

        return js.addListener(
            // From this element
            connection.emitter.parent.name,

            // On this event
            connection.emitter.name.toCamel().lowercase(),

            // Call this function
            wrapAnonFunction(
                compiledFunc
            )
        )
    }


    /**
     * Performs [compileFunctionInvocation], and applies it's output as defined in the graph.
     *
     * i.e
     *
     * ```
     * element.property = function1(x,y,z)
     * ```
     *
     * If the function's output is not being used, then a warning is logged.
     */
    private fun compileFunctionAndOutput(function: JsGraphFunction): String {

        val outputEmitter = try {
            function.output()
        } catch (e: Exception) {
            ERROR(MessageType.ERROR_FUNCTION_HAS_NO_OUTPUT, "Function ${function.name} has no output emitter.")
            return ""
        }


        if (outputEmitter.emissions().isEmpty()) {
            WARN(MessageType.WARNING_TRIGGERED_FUNCTION_UNUSED, "Function ${function.name} is being triggered, but it's output is not being used.")
            return ""
        }

        // Check the nodes that the output is connected to

        outputEmitter.emissions().forEach {
            when (it.receiver.parent) {
                is JsGraphFunction -> {
                    WARN(MessageType.WARNING_INVALID_TRIGGER_POSITION, "Function ${function.name} is being triggered, but it's output is being connected to another function. You should trigger ${it.receiver.parent.name} instead. This trigger will be ignored.")
                    return ""
                }

                is JsGraphElement -> {
                    addElement(it.receiver.parent)
                }

                else -> {
                    ERROR(MessageType.ERROR_COMPILER_NOT_EQUIPT_TO_HANDLE, "Function ${function.name} is supplying ${it.receiver.parent.name}, but the compiler does not know what to do here.")
                    return ""
                }
            }
        }


        // Compile the function invocation
        val invocation = compileFunctionInvocation(function)

        // Apply the output of the function to one, none, or many connections
        return if (outputEmitter.emissions().size == 1) {
            outputEmitter.emissions().first().receiver.let {
                it.parent.touch()
                it.setter!!(invocation)
            }
        } else {
            "let result = $invocation" +
                    outputEmitter.emissions().joinToString("\n") {
                        it.receiver.parent.touch()
                        it.receiver.setter!!("result")
                    }
        }
    }


    /**
     * Builds javascript code that invokes a [JsGraphFunction], including all of
     * its arguments.
     *
     * Arguments are retrieved from the [JsGraphFunction]'s recievers
     * which either recurse with the output of other functions, if any are connected in the [JsGraph],
     * or accept the default value.
     *
     * ```
     * function(x,y(),z)
     * ```
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [function] The [JsGraphFunction] to invoke.
     */
    private fun compileFunctionInvocation(function: JsGraphFunction): String {
        // Check the inputs
        function.receivers().forEach {
            if (!it.isTrigger() && it.defaultValue == null && !it.hasAdmission()) {
                ERROR(MessageType.ERROR_FUNCTION_MISSING_REQUIRED_INPUTS, "$it requires a value, but none is provided.")
                return ""
            }
        }

        addFunction(function)

        if (function.receivers().none { it.isTrigger() || it.hasAdmission() }) {
            WARN(MessageType.WARNING_FUNCTION_HAS_NO_INPUT, "${function.name}'s inputs are blank. It will only ever use it's default values.")
        }

        // Evaluate the function's JS with arguments
        return function.function.invoke(

            // For every argument,
            *function.function.args.map { arg ->

                // Locate the corresponding receiver and compile it
                compileReceiver(
                    function.receivers().find { it.name == arg.first }!!
                )

            }.toTypedArray()
        )
    }


    /**
     * Evaluates a receiver.
     *
     * A receiver provides a value of some kind.
     *
     * Thus this evaluates the value that this receiver stands for,
     * or the javascript code that would obtain it.
     *
     * This evaluates either a value provider, a default value,
     * or invokes a connected function that provides the value.
     *
     * i.e if the receiver is connected to a function node in the [JsGraph],
     * that function will be compiled. If not, this will just return the default.
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [receiver] The [JsGraphReceiver] to evaluate.
     * @throws JsGraphCompilationException if the receiver does not have a default value, and is not connected to anything that provides it a value.
     */
    private fun compileReceiver(receiver: JsGraphReceiver): String {

        // If there's no connections to provide a value, find a default value.
        return if (!receiver.hasAdmission())
        // Use the default.
            receiver.defaultValue?.toString()
            // There's no value being provided, and no default. Compilation error.
                ?: run {
                    throw JsGraphCompilationException("$receiver must be supplied a value."); "";
                }


        // Otherwise, the value is being provided by something else.
        // So we need to compile that.


        else {
            val dataSource = receiver.admission!!.emitter.parent
            when (dataSource) {

                // Functions provide data by being called
                is JsGraphFunction -> {
                    compileFunctionInvocation(dataSource)
                }

                //            is JsValue -> {
                //                  dataSource.value.toString()
                //            }

                else -> {
                    throw JsGraphCompilationException("$receiver is is receiving data from something that I don't know how to compile (${dataSource.javaClass.name})")
                }
            }
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                         Compilation
    //region                                            Javascript tracking
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Includes and touches a function.
     */
    private fun addFunction(function: JsGraphFunction) {
        js.addFunction(function.function)
        function.touch()
    }

    /**
     * Includes and touches an element.
     */
    private fun addElement(element: JsGraphElement) {
        js.addElementID(element.name)
        element.touch()
    }
}


class JsGraphCompilationException(msg: String) : Exception(msg)