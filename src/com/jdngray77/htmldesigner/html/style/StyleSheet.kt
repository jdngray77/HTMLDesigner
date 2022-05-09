package com.jdngray77.htmldesigner.html.style


typealias StyleArray = ArrayList<Style>


class Style(val property: String, val value: String) {
    override fun toString() = property + ": " + value + ";"
}


class StyleClass(val selectors: ArrayList<String>) : StyleArray() {
    constructor(vararg selectors : String) : this(arrayListOf(*selectors))

    override fun toString(): String =
        selectors.joinToString(",\n") { it } + " {\n" +
        joinToString("\n") { "\t" + it.toString() } + "\n}"

    fun styles (vararg styles: Style) = this.apply {
        addAll(styles)
    }

}


class StyleSheet : ArrayList<StyleClass>() {
    override fun toString(): String =
        joinToString("\n\n") { it.toString() }

    fun classes(vararg clazz: StyleClass) = this.apply {
        addAll(clazz)
    }

    companion object {
        fun test() {
            println(
                StyleSheet().classes(
                    StyleClass("button").styles(
                        Style("background", "blue"),
                        Style("color", "yellow")
                    )
                )
            )
        }
    }
}
