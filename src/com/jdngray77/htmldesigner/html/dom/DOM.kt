package com.jdngray77.htmldesigner.html.dom

/**
 * A loaded representation of a HTML document.
 */
class DOM (
    var body: html
) : SerializableHTML {

    /**
     * Creates a text representation of the entire document model.
     */
    override fun serialize(): String = body.toString()

    override fun toString(): String = serialize()

}