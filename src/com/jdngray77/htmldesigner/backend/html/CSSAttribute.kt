/*░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
 ░                                                                                                ░
 ░ Jordan T. Gray's                                                                               ░
 ░                                                                                                ░
 ░          HTML Designer                                                                         ░
 ░                                                                                                ░
 ░ FOSS 2022.                                                                                     ░
 ░ License decision pending.                                                                      ░
 ░                                                                                                ░
 ░ https://www.github.com/jdngray77/HTMLDesigner/                                                 ░
 ░ https://www.jordantgray.uk                                                                     ░
 ░                                                                                                ░
 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

package com.jdngray77.htmldesigner.backend.html

/**
 * A container for CSS properties that can be added directly
 * to an element as an attribute.
 *
 * i.e
 * ```
 * <div style="...;">
 * ```
 *
 * usage
 * ```
 * val styles = StyleAttribute()
 *
 * styles["display"] = "flex"
 * styles["justify-content"] = "center"
 *
 * element.attr("style", styles)
 * ```
 */
class StyleAttribute : HashMap<String, String>() {

    fun style(propertyName : String, value : String) =
        put(propertyName, value)

    fun style(propertyName : String) =
        get(propertyName)


    override fun toString()=
        entries.joinToString("") { it.key + ": " + it.value + "; " }

}