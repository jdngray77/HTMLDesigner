package com.jdngray77.htmldesigner.backend.html.style

import com.jdngray77.htmldesigner.backend.removeDuplicates
import com.jdngray77.htmldesigner.backend.html.dom.SerializableHTML

/**
 * An array of styles.
 */
typealias StyleArray = ArrayList<Style>

/**
 * A single CSS style attribute.
 *
 * `property: value;`
 *
 * i.e
 *
 * `background: black;`
 */
class Style(val property: String, val value: String) : SerializableHTML {
    override fun toString(): String = serialize()
    override fun serialize() = "$property: $value;"
}


/**
 * A CSS class.
 *
 * i.e
 * ```
 * selector {
 *  property: value;
 * }
 * ```
 */
class StyleClass(val selectors: ArrayList<String>) : StyleArray(), SerializableHTML {
    constructor(vararg selectors : String) : this(arrayListOf(*selectors))

    override fun toString(): String = serialize()

    override fun serialize(): String =
        selectors.joinToString(",\n") { it } + " {\n" +
        joinToString("\n") { "\t" + it.toString() } + "\n}"

    /**
     * Adds a style to this class.
     */
    fun styles (vararg styles: Style) = this.apply {
        addAll(styles)
        removeDuplicates()
    }
}

/**
 * A collection of [StyleClass]'s.
 *
 * TODO keyframe, media queries, extends, fonts, imports?
 *
 * @author [Jordan T. Gray](https://www.jordantgray.uk) on 9/6/2022
 */
class StyleSheet(val name: String) : ArrayList<StyleClass>(), SerializableHTML {
    override fun toString(): String = serialize()
    override fun serialize(): String =
        joinToString("\n\n") { it.toString() }


    /**
     * Adds classes to this stylesheet.
     */
    fun classes(vararg clazz: StyleClass) = this.apply {
        addAll(clazz)
    }

    companion object {
        fun test() {
            println(
                StyleSheet("test").classes(
                    StyleClass("button").styles(
                        Style("background", "blue"),
                        Style("color", "yellow")
                    )
                )
            )
        }


        const val IDE_DEBUG_SHEET = "IDE_DEBUG_SHEET"
        val DEBUG = StyleSheet(IDE_DEBUG_SHEET).classes(
            StyleClass(".debug-outline")
                .styles(
                    Style("outline", "1px solid red")
                )
        )
    }
}
