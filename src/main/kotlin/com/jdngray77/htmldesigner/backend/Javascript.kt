package com.jdngray77.htmldesigner.backend

internal class Javascript {

    /**
     * A list of elements that will be used throughout the script,
     * referenced by ID.
     *
     * They will be added to the stack at the start of the script.
     */
    var elementIDs = arrayListOf<String>()
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
     * @param functionName The name of the function that will be called when the event is triggered.
     * @param targetID Target of the [functionName], if any. i.e if supplied, [functionName] will be invoked on [targetID].
     */
    fun addListener(elementID: String, eventName: String, functionName: String, targetID: String?) {
        addID(elementID)
        targetID?.let { addID(it) }

        statements.add(
            "$elementID.addEventListener(\"$eventName\", ${targetID?.let { "$it." } ?: ""}$functionName);"
        )

        dirty()
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
            val j = Javascript()
            j.addListener("myButton", "click", "hide", "banner")

            println(j.toString())
        }
    }
}

