package com.jdngray77.htmldesigner.backend.html.dom

import com.jdngray77.htmldesigner.backend.html.style.StyleSheet

/**
 * Representation of an entire HTML document and it's stylesheets.
 *
 * @author [Jordan T. Gray](https://www.jordantgray.uk) on 9/6/2022
 */
class Document (

    /**
     * The HTML Tag DOM.
     *
     * This is the root of the Tag linked-tree
     * structure.
     */
    var TagModel: html

) : SerializableHTML {

    /**
     * The stylesheets used by this document
     */
    // TODO impl notes  :  Later, store stylesheets elsewhere first. Keep them in the project,
    //                  and just store a reference here so there isn't copies and we can just
    //                  keep reusing the same stylesheets.
    val stylesheets = ArrayList<StyleSheet>()

    /**
     * Parses and validates the [TagModel]
     */
    override fun serialize(): String = TagModel.toString()

    /**
     * Parses and validates the [TagModel]
     */
    override fun toString(): String = serialize()

}