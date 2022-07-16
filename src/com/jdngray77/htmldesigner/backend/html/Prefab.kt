
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

import com.jdngray77.htmldesigner.utility.asElement
import com.jdngray77.htmldesigner.utility.assertEndsWith
import com.jdngray77.htmldesigner.utility.assertExists
import com.jdngray77.htmldesigner.utility.subFile
import com.jdngray77.htmldesigner.frontend.Editor
import org.jsoup.nodes.Element
import java.io.File

/**
 * A wrapper to read and write elements to disk.
 *
 * This creates a way to design an element, then save it for
 * later use.
 *
 *
 * @constructor Saves [element] to [locationOnDisk].
 *              However, If no [element] is provided, [locationOnDisk] is used to load, instead.
 *
 * Setting new content into [element] automatically saves prefab to disk.
 */
class Prefab (

    /**
     * The file where this [element] will be saved
     */
    val locationOnDisk : File,

    /**
     * The element to save.
     *
     * If Provided then it's assumed that you're saving a new
     * prefab.
     *
     * If NOT provided, then it is loaded from disk.
     *
     * If NOT provided, but there is no prefab on disk to load,
     * a [NoSuchFileException] is thrown.
     */
    element: Element? = null

) {

    /**
     * Creates [locationOnDisk] relative to [Project.PREFABS] with [subPath]
     */
    constructor(subPath: String, element: Element? = null) : this(Editor.mvc().Project.PREFABS.subFile(subPath.assertEndsWith(".html")), element)

    /**
     * The element in this prefab.
     *
     * Either provided in constructor OR loaded from disk.
     */
    lateinit var element: Element
        private set

    fun id() =
        element.attr("prefab-id")

    /**
     * This replaces anything already saved to [locationOnDisk]
     * with the new [element]
     *
     * Sets the element used in this prefab,
     * and saves it to disk.
     *

     */
    fun replaceSavedElement(value: Element) {
        element = value
        doSave()
    }

    /**
     * Loads [locationOnDisk] to [element], replacing [element]
     *
     * @throws NoSuchFileException if [locationOnDisk] does not exist.
     */
    private fun doLoad() {
        if (!locationOnDisk.exists())
            throw NoSuchFileException(locationOnDisk, reason = "Tried to load a prefab from disk, but it wasn't there.")

        element = locationOnDisk.readText().asElement()
    }

    /**
     * Saves [element] to [locationOnDisk]
     */
    private fun doSave() {
        element.removeClass("debug-outline")
        element.attr("prefab-id", locationOnDisk.nameWithoutExtension)
        locationOnDisk.assertExists()
        locationOnDisk.writeText(element!!.outerHtml())
    }

    init {
        if (element == null)
            if (locationOnDisk.exists())
                doLoad()
            else
                throw NoSuchFileException(locationOnDisk, reason = "Prefab loading constructor was used, but there was no file to load.")
        else
            replaceSavedElement(element)
    }
}