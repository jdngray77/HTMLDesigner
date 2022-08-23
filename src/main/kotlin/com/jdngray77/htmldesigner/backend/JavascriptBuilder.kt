package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphDataType
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphDataTypeOf
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphFunction
import com.jdngray77.htmldesigner.frontend.jsdesigner.JsFunctionFactory
import com.jdngray77.htmldesigner.utility.addIfAbsent
import com.jdngray77.htmldesigner.utility.classEquals
import com.jdngray77.htmldesigner.utility.classEqualsOrSubclass
import com.jdngray77.htmldesigner.utility.toCamel
import java.io.Serializable

/**
 * A non-comprehansive string builder for simple javascripts.
 *
 * Add functions, html elements, listeners, etc.
 *
 * Then, the all attributes will be built into a single
 * javascript string on [toString]
 *
 * @author Jordan T Gray.
 */
internal class JavascriptBuilder {

    /**
     * A list of elements that will be used throughout the script,
     * referenced by ID.
     *
     * They will be added to the stack at the start of the script.
     */
    var elementIDs = arrayListOf<String>()
        private set

    /**
     * Functions that will be added to the script.
     */
    var jsFunctions = arrayListOf<JsFunction>()
        private set

    /**
     * Constructed statements that can be directly placed in
     * the script.
     */
    val statements = arrayListOf<String>()

    private var cachedCompilation = ""

    fun dirty() {
        cachedCompilation = ""
    }

    /**
     * Adds an event listener to an element.
     *
     * @param elementID the element, by id, to add the listener to.
     * @param eventName the type of event listener.
     * @param functionInvocation The name of the function that will be called when the event is triggered.
     * @param functionLocation Target of the [functionInvocation], if any. i.e if supplied, [functionInvocation] will be invoked on [functionLocation].
     */
    fun addListener(elementID: String, eventName: String, functionInvocation: String, functionLocation: String? = null): String {
        addID(elementID)
        functionLocation?.let { addID(it) }

        val toAdd = "$elementID.addEventListener(\"$eventName\", ${functionLocation?.let { "$it." } ?: ""}$functionInvocation);"

        statements.add(toAdd)

        dirty()

        return toAdd
    }

    /**
     * Adds a function.
     */
    fun addFunction (jsFunction: JsFunction) {
        jsFunctions.addIfAbsent(jsFunction)
    }

    /**
     * Adds an element ID to [elementIDs]
     */
    private fun addID(id: String) {
        elementIDs.add(id)
        elementIDs = arrayListOf(*elementIDs.distinct().toTypedArray())
        dirty()
    }

    /**
     * Constructs the script.
     */
    override fun toString(): String {
        if (cachedCompilation.isNotEmpty())
            return cachedCompilation


        with (StringBuilder()) {

            appendLine("// Document Objects")
            elementIDs.forEach{
                appendLine(buildVariable("document.getElementById(\"$it\")", it))
            }

            appendLine("\n\n// Functions")
            jsFunctions.forEach {
                appendLine("let ${it.name.toCamel()} = (${it.args.joinToString(", ") { it.first.toCamel() }}) => { \n\t${it.javascript} \n}" )
            }


            appendLine("\n\n// Event listeners")
            statements.forEach {
                appendLine(it)
            }

            cachedCompilation = this.toString()
            return cachedCompilation
        }
    }

    companion object {


        /**
         * Constructs a ES6 let variable.
         */
        fun buildVariable(value: String, vararg names: String) =
            "let ${ if (names.size == 1) names.first() else "{ ${names.joinToString((""))} }" } = $value;"



        @JvmStatic
        fun main(args: Array<String>) {
            val j = JavascriptBuilder()
            j.addListener("myButton", "click", "hide", "banner")

            println(j.toString())
        }
    }
}

/**
 * Simple wrapper for a javascript function.
 */
open class JsFunction (

    /**
     * The name of the function
     */
    val name: String,

    /**
     * The function's return type.
     *
     * Cannot be [Unit]
     *
     * Can be retrieved from
     *
     * ```
     * emitters().first().type
     * ```
     */
    val returnType: JsGraphDataType,

    /**
     * The function's arguments.
     *
     * @param [String] The name of the argument.
     * @param [Class<*>] The type of the argument.
     * @param [Any] The default value of the argument. Must match the arg type.
     */
    vararg args: Triple<String, JsGraphDataType, Serializable?>,

    val javascript: String
) : Serializable {
    /**
     * The function's arguments.
     *
     * @param [String] The name of the argument.
     * @param [Class<*>] The type of the argument.
     * @param [Any] The default value of the argument. Must match the arg type.
     */
    val args: List<Triple<String, JsGraphDataType, Serializable?>>

    init {
        this.args = args.toList()

        args.forEach {
            if (it.second != JsGraphDataTypeOf(it.third))
                throw IllegalArgumentException("Default value for argument '${it.first}' is '${it.third!!::class.simpleName}', but must be must be of type '${it.second.name}' in function '$name'")
        }

    }

    fun invoke(vararg args: Any): String {
        if (args.size != this.args.size)
            throw IllegalArgumentException("Argument count mismatch. $name takes ${this.args.size}, but ${args.size} were supplied.")


        var i = 0
        args.forEach {
            classEquals(it::class.java, this.args[i]::class.java)
            i++
        }

        return "${name.toCamel()}(${args.joinToString(", ")})"
    }

    /**
     * For finding and comparing within lists.
     */
    override fun equals(other: Any?): Boolean {
        return other is JsFunction && other.name == name
    }

    /**
     * Creates and returns a new [JsGraphFunction] of the same class.
     */
    fun clone() = JsFunctionFactory.byName(name)


    /**
     * Auto generated hashcode for efficient use of [equals]
     * in complex [Collection]s
     */
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + returnType.hashCode()
        result = 31 * result + javascript.hashCode()
        result = 31 * result + args.hashCode()
        return result
    }


}

