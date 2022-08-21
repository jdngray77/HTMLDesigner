package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.backend.JavascriptBuilder
import com.jdngray77.htmldesigner.utility.classEquals

/**
 * A non-comprehensive compiler for the [JsGraph].
 *
 * Accepts a [JsGraph] and generates a javascript string
 * that can be placed into the web page using a [JavascriptBuilder]
 */
object JsGraphCompiler {

    /**
     * Creates a script from a [JsGraph].
     *
     * @param [graph] The graph to compile.
     * @return [String] The javascript string.
     */
    fun compileGraph(graph: JsGraph): String {

        val js = JavascriptBuilder()

        with(graph) {

            // For every HTML element,
            getNodes().filterIsInstance<JsGraphElement>().forEach {
                // check each emitter
                it.emitters().forEach {
                    // For each link being emitted,
                    it.emissions().forEach { connection ->
                        if (classEquals(connection.emitter.type.java, Trigger::class.java)) {
                            compileTrigger(js, connection)
                        }
                    }
                }
            }
        }

        return js.toString()
    }

    /**
     * Builds a javascript event listener that will trigger something else.
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [connection] The [JsGraphConnection] being emitted by the trigger to compile.
     */
    private fun compileTrigger(js: JavascriptBuilder, connection: JsGraphEmission): String {
        if (!classEquals(connection.emitter.type.java, Trigger::class.java))
            throw IllegalArgumentException("Compiler is trying to create a trigger from a connection that's not emanating from a trigger.")

        if (connection.emitter.parent !is JsGraphElement)
            throw IllegalArgumentException("A event trigger can only be emitted by a html Element.")

        connection.touch()

        return js.addListener(
            // From this element
            connection.emitter.parent.name,

            // On this event
            connection.emitter.name,

            // Call this function
            compileCallback(js, connection)
        )
    }

    /**
     * Builds a lambda function that calls another function.
     *
     * Function being called will be added to the [js] in a variable.
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [connection] An [JsGraphEmission] where the reciever is a [JsGraphFunction] that needs to be invoked.
     */
    private fun compileCallback(js: JavascriptBuilder, connection: JsGraphEmission): String {

        if (connection.receiver.parent !is JsGraphFunction)
            throw IllegalArgumentException("Connection does not lead to a function.")

        connection.touch()

        js.addFunction(connection.receiver.parent.function)

        return "() => {\n\t${
            compileFunctionInvocation(js, connection.receiver.parent)
        }\n}"
    }

    /**
     * Builds javascript code that invokes a [JsGraphFunction], including all of
     * its arguments.
     *
     * Arguments are either sourced by recursing with other functions,
     * if any are connected in the [JsGraph], otherwise the default value
     * of the function's argument when there is no link.
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [function] The [JsGraphFunction] to invoke.
     */
    private fun compileFunctionInvocation(js: JavascriptBuilder, function: JsGraphFunction): String {
        js.addFunction(function.function)
        function.touch()

        // Evaluate the function's JS with argument
        return function.function.invoke(

                // For every argument,
                *function.function.args.map { arg ->

                    // Locate the corresponding receiver and compile it
                    compileReceiver(js, function.recievers().find { it.name == arg.first }!!)
                }.toTypedArray()
            )
    }


    /**
     * Evaluates a receiver.
     *
     * A receiver provides a value of some kind.
     * This evaluates either a value provider, or a function invocation
     * in order to obtain a value.
     *
     * i.e if the receiver is connected to a function node in the [JsGraph],
     * that function will be compiled. If not, this will just return the default.
     *
     * @param [js] The [JavascriptBuilder] being used to generate the script.
     * @param [receiver] The [JsGraphReceiver] to evaluate.
     */
    private fun compileReceiver(js: JavascriptBuilder, receiver: JsGraphReceiver): String {
        // If there's no connections to provide a value, find a default value.
        return if (!receiver.hasAdmission())
            when (receiver.parent) {
                // TODO default values for all receivers.
                is JsGraphFunction -> receiver.parent.function.args.find { it.first == receiver.name }!!.third.toString()
                else -> throw IllegalArgumentException("Could not determine a default value for ${receiver.name}")
            }

        // Otherwise evaluate the incoming connection.
        else when (receiver.admission!!.emitter.parent) {
//            is JsGraphValueProvider -> {
//                js.addValue(receiver.value)
//            }
//            is JsGraphVariable -> {
//                js.addValue(receiver.value)
//            }
            is JsGraphFunction -> {
                compileFunctionInvocation(js, receiver.admission!!.emitter.parent as JsGraphFunction)
            }

            else -> {
                throw JsGraphCompilationException("Unsupported receiver type: ${receiver.admission!!.emitter.parent.javaClass.name}")
            }
        }
    }
}


class JsGraphCompilationException(msg: String) : Exception(msg)