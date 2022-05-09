package com.jdngray77.htmldesigner.html.dom

/**
 * Some kind of element which can be turned
 * into text for the HTML output.
 */
interface SerializableHTML {
    fun serialize() : String
}