
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

package com.jdngray77.htmldesigner.backend.extensions

import com.jdngray77.htmldesigner.backend.utility.assertExists
import com.jdngray77.htmldesigner.frontend.Editor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser.xmlParser
import java.io.File
import javax.swing.text.html.parser.Parser

/*
 *
 *      Extension methods for JSoup or HTML related things.
 *
 *
 */





/**
 * Shortcut to open a jsoup document in the editor.
 *
 * Creates or switches to an existing editor in the IDE that
 * holds this document.
 */
fun Document.open() =
    Editor.mvcIfAvail()?.openDocument(this)





//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                                                           Element
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * Writes the given jsoup element to the disk as HTML.
 *
 * Automatically asserts that the file exists.
 */
fun Element.saveToDisk(f: File) {
    f.assertExists()
    f.writeText(toString())
}


/**
 * Injects a child into [this]'s parent prior to [this].
 */
fun Element.injectSiblingBefore(element: Element) = injectRelativeSibling(element)

/**
 * Injects a child into [this]'s parent after to [this].
 */
fun Element.injectSiblingAfter(element: Element) = injectRelativeSibling(element, 1)

/**
 * It's a method that takes an element and inserts it into the parent of the element that called it.
 *
 * The sibling is injected at the offset, where 0 replaces this, and shifts the remainder right.
 *
 * Essentially, 0 = inject left, 1 = inject right.
 */
fun Element.injectRelativeSibling(element: Element, offset: Int = 0) {
    parent()?.let {
        it.insertChildren(it.childNodes().indexOf(this) + offset, element)
    }
}

fun String.asElement(): Element =
    Jsoup.parse(this, "", xmlParser()).let {
        if (it.tagName() == "#root")
            it.child(0)
        else it
    }
