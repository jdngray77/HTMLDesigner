package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.backend.JavascriptBuilder
import com.jdngray77.htmldesigner.backend.JavascriptBuilder.Companion.wrapAnonFunction

/**
 * A non-comprehensive compiler for the [JsGraph].
 *
 * Accepts a [JsGraph] and generates a javascript string
 * that can be placed into the web page using a [JavascriptBuilder]
 */
object JsGraphCompiler {

    /**
     * The result of compiling a graph.
     *
     * Wraps the javascript and any warning messages
     * generated during compilation.
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
        val warnings: List<String>,

        /**
         * The overall result of this compilation.
         */
        val type : ResultType = autoType(javascript, warnings)

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

        companion object {

            /**
             * Automatically determines the result type based on the
             * state of the warnings.
             */
            private fun autoType(js: String, warns: List<String>) : ResultType {
                return when {
                    warns.isEmpty() -> ResultType.SUCCESS
                    else -> if (js.isBlank() || warns.any { it.startsWith(ERROR_PREFACE) })
                                ResultType.ERROR
                            else
                                ResultType.SUCCESS_WITH_WARNINGS
                }
            }

        }
    }

    /**
     * String placed at the beginning of fatal warning messages.
     */
    const val ERROR_PREFACE = " !! ERROR !! - "

    /**
     * A log of all warnings generated during compilation.
     */
    private val warnings = mutableListOf<String>()

    /**
     * Returns the warnings generated during the last compilation.
     */
    fun getWarnings() : List<String> {
        return warnings.toList()
    }

    /**
     * Creates a script from a [JsGraph].
     *
     * @param [graph] The graph to compile.
     * @return [String] The javascript string.
     */
    fun compileGraph(graph: JsGraph): JsGraphCompilationResult {
        warnings.clear()

        graph.resetTouched()

        val js = JavascriptBuilder()

        try {

            with(graph) {

                // For every HTML element,
                getNodes().filterIsInstance<JsGraphElement>().forEach {
                    // check each emitter
                    it.emitters().filter { it.isTrigger() }.forEach {
                        // For every event it triggers
                        it.emissions().forEach {
                            compileFunctionTrigger(js, it)
                        }
                    }
                }
            }
        } catch (e: JsGraphCompilationException) {
            warnings.add(0, ERROR_PREFACE + e.message)
            return JsGraphCompilationResult("", warnings)
        }

        return JsGraphCompilationResult(js.toString(), warnings)
    }

    /**
     * Builds a javascript event listener that will invoke a function.
     *
     * i.e
     *
     * ```
     * MainContent.addEventListener("Click", () => {
     *   ...
     * });
     * ```
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [connection] The [JsGraphConnection] being emitted by the trigger. This determines the event, and what's inside it.
     * @throws IllegalArgumentException If [connection] is not being emitted by a [JsGraphElement], or is not an event trigger.
     * @throws IllegalArgumentException If the node being triggered is not a [JsGraphFunction]
     */
    private fun compileFunctionTrigger(js: JavascriptBuilder, connection: JsGraphEmission): String {
        if (!connection.emitter.isTrigger())
            throw IllegalArgumentException("Compiler is trying to create a trigger from a connection that's not emanating from a trigger.")

        if (connection.emitter.parent !is JsGraphElement)
            throw IllegalArgumentException("A event trigger can only be emitted by a html Element.")

        if (connection.receiver.parent !is JsGraphFunction)
            throw IllegalArgumentException("A event can only trigger a function.")

        connection.touch()

        return js.addListener(
            // From this element
            connection.emitter.parent.name,

            // On this event
            connection.emitter.name,

            // Call this function
            wrapAnonFunction(
                compileFunctionAndOutput(js, connection.receiver.parent)
            )
        )
    }


    /**
     * Performs [compileFunctionInvocation], and applies it's output as defined in the graph.
     *
     * If the function's output is not being used, then a warning is logged.
     */
    private fun compileFunctionAndOutput(js: JavascriptBuilder, function: JsGraphFunction) : String {

        val outputEmitter = function.emitters().find { it.name == "output" }
            ?: throw IllegalStateException("Function ${function.name} has no output emitter. Functions must emit an output.")

        if (outputEmitter.emissions().isEmpty()) {
            warnings.add("Function ${function.name} is being triggered, but it's output is not being used. Trigger will be ignored.")
            return ""
        }

        // Check the nodes that the output is connected to

        outputEmitter.emissions().forEach {
            when (it.receiver.parent) {
                is JsGraphFunction -> {
                    warnings.add("'${function.name}' is being triggered, but it's output is supplying '${it.receiver.parent.name}'. You should trigger ${it.receiver.parent.name} instead. This trigger will be ignored.")
                    return ""
                }

                is JsGraphElement -> {
                    addElement(js, it.receiver.parent)
                }
            }
        }


        // Compile the function invocation
        val invocation = compileFunctionInvocation(js, function)

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
     * Builds a lambda function that calls another function.
     *
     * Function being called will be added to the [js] in a variable.
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [connection] An [JsGraphEmission] where the reciever is a [JsGraphFunction] that needs to be invoked.
     */


    /**
     * Builds javascript code that invokes a [JsGraphFunction], including all of
     * its arguments.
     *
     * Arguments are retrieved from the [JsGraphFunction]'s recievers
     * which either recurse with the output of other functions, if any are connected in the [JsGraph],
     * or accept the default value.
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [function] The [JsGraphFunction] to invoke.
     */
    private fun compileFunctionInvocation(js: JavascriptBuilder, function: JsGraphFunction): String {
        addFunction(js, function)


        // Check the inputs

        function.recievers().forEach {
            if (!it.isTrigger() && it.defaultValue == null && !it.hasAdmission()) {
                throw JsGraphCompilationException("$it requires a value, but none is provided.")
            }
        }

        if (function.recievers().none { !it.isTrigger() && it.hasAdmission() }) {
            warnings.add("All inputs of ${function.name} are blank. It will just only be able to supply it's default values.")
        }

        // Evaluate the function's JS with arguments
        return function.function.invoke(

                // For every argument,
                *function.function.args.map { arg ->

                    // Locate the corresponding receiver and compile it
                    compileReceiver(
                        js,
                        function.recievers().find { it.name == arg.first }!!
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
    private fun compileReceiver(js: JavascriptBuilder, receiver: JsGraphReceiver): String {

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
                    compileFunctionInvocation(js, dataSource)
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

    /**
     * Includes and touches a function.
     */
    private fun addFunction(js: JavascriptBuilder, function: JsGraphFunction) {
        js.addFunction(function.function)
        function.touch()
    }

    /**
     * Includes and touches an element.
     */
    private fun addElement(js: JavascriptBuilder, element: JsGraphElement) {
        js.addElementID(element.name)
        element.touch()
    }
}



class JsGraphCompilationException(msg: String) : Exception(msg)