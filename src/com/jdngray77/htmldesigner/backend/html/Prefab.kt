
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

import com.jdngray77.htmldesigner.backend.extensions.asElement
import com.jdngray77.htmldesigner.backend.utility.assertEndsWith
import com.jdngray77.htmldesigner.backend.utility.assertExists
import com.jdngray77.htmldesigner.backend.utility.subFile
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
    val locationOnDisk : File,
    element: Element? = null
) {
    /**
     * For creating a new prefab only.
     *
     * Creates [locationOnDisk] relative to [Project.PREFABS] with [subPath]
     */
    constructor(subPath: String) : this(Editor.mvc().Project.PREFABS.subFile(subPath.assertEndsWith(".html")))

    /**
     * The element in this prefab.
     */
    private lateinit var element: Element

    /**
     * Sets the element used in this prefab,
     * and saves it to disk.
     *
     * This replaces anything already saved to [locationOnDisk]
     */
    fun setElement(value: Element) {
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
            setElement(element)
    }
}