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

import org.jsoup.nodes.Element

/**
 * An editable snapshot for capturing and manipulating the styles within an [Element].
 *
 * **Be aware that this does not continually keep track of changes made to elements from elsewhere in the editor,
 * and as such should not be relied on to access the current state of the element later without re-capturing via [capture].**
 *
 * Be aware of race conditions.
 * TODO implement a snapshot lock of some kind?
 *
 * i.e
 * ```
 * <div style="...;">
 * ```
 *
 * usage
 * ```
 * // Create an editable snapshot of the elements styles
 * val styles = StyleAttribute(element)
 *
 * // Make changes, or access the snapshot
 * styles["display"] = "flex"
 * styles["justify-content"] = "center"
 *
 * // Commit the changes back to the element.
 * styles.commit()
 * ```
 *
 * @see capture
 * @see commit
 */
class StyleAttributeSnapshot(
    val element: Element
) : HashMap<String, String>() {

    init { capture() }

    /**
     * Captures the style properties currently in the [element].
     *
     * This will reset any uncommitted changes.
     */
    fun capture() {
        // TODO test this
        if (element.hasAttr("style"))
            element.attr("style")
                .split(";")
                .filter { it.isNotBlank() }
                .forEach {
                    it.split(":").apply {
                        style(get(0).trim(), get(1).trim())
                    }
            }
        else
            clear()
    }

    /**
     * Overwrite the element's styles with the contents of this snapshot.
     *
     * Be aware of race conditions.
     */
    fun commit() {
        element.attr("style", toString())
    }

    fun style(propertyName : String, value : String) =
        put(propertyName, value)

    fun style(propertyName : String) =
        get(propertyName)


    override fun toString()=
        entries.joinToString("") { it.key + ": " + it.value + "; " }

}