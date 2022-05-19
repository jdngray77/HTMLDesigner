package com.jdngray77.htmldesigner.backend.html.dom

/**
 * Some kind of element which can be turned
 * into text for the HTML output.
 * @author [Jordan T. Gray](https://www.jordantgray.uk) on 9/6/2022
 */
interface SerializableHTML : java.io.Serializable{
    fun serialize() : String
}