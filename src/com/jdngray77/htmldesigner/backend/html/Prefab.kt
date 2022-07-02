
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

import com.jdngray77.htmldesigner.backend.html.dom.Tag
import java.io.IOException

class Prefab(

    val ID: String,

    /**
     * Determines if the prefab is stored in the application
     * or the project.
     */
    val project : Boolean = true

) {

    /**
     * The HTML tag of this prefab.
     *
     * Only loaded from disk when requested,
     * and only on the first request.
     */
    var tag: Tag? = null
        get() {
            if (field == null) {
                TODO("Load the tag from disk")
            }

            if (field == null) throw IOException("Unable to load Prefab!")
            return field!!
        }
}