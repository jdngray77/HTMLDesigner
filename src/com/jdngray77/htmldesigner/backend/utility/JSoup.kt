
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

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.html.Prefab
import com.jdngray77.htmldesigner.backend.userInput
import com.jdngray77.htmldesigner.backend.utility.assertExists
import com.jdngray77.htmldesigner.frontend.Editor
import com.steadystate.css.dom.CSSStyleSheetImpl
import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import com.sun.org.apache.xerces.internal.dom.DocumentImpl
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser.xmlParser
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.CSSStyleSheet
import java.io.File
import java.io.StringReader

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


// TODO replace these.
/**
 * Injects a child into [this]'s parent prior to [this].
 */
@Deprecated("There's functions in Element for this.", ReplaceWith("Element.before()"))
fun Element.injectSiblingBefore(element: Element) = injectRelativeSibling(element)

/**
 * Injects a child into [this]'s parent after to [this].
 */
@Deprecated("There's functions in Element for this.", ReplaceWith("After"))
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

fun Element.createPrefab(): Prefab? {
    var ret: Prefab? = null

    userInput("Enter a name / path for this prefab") {
        try {
            // Validation.
            // This should not exist, and throw an error. If not, then
            // reject input.
            Prefab(it)
            return@userInput "$it already exists!"
        } catch (e: NoSuchFileException) {
            // Create it.
            ret = Prefab(e.file, this)
            EventNotifier.notifyEvent(EventType.PROJECT_PREFAB_CREATED)
            return@userInput null
        }
    }
    return ret
}

fun String.asElement(): Element =
    Jsoup.parse(this, "", xmlParser()).let {
        if (it.tagName() == "#root")
            it.child(0)
        else it
    }

/**
 * Finds a stylesheet within the head of the document.
 *
 * @return a style tag with the matching ID, found within this document
 *         head, if there is one.
 */
fun Document.getStylesheet(id : String) = this.head().getElementById(id)?.asStyleSheet()


/**
 * Adds a stylesheet to this document.
 *
 * Creates a style tag in this document's head with the given name,
 * and the serialized content of [css].
 *
 * If a style tag with the same ID already exists, it is replaced with [css].
 *
 * @see Document.getStylesheet for retrieval.
 */
fun Document.addStylesheet(css: CSSStyleSheet) {
    this.head().getElementById(css.title)?.let {
        if (it.tagName() == "style") // Just to be safe.
            it.replaceWith(css.asStyleTag())
    } ?: run {
            head().appendChild(css.asStyleTag())
    }
}

fun Document.addStylesheet(id: String, initialContent: String = ""): CSSStyleSheet? {
    head().appendChild(newStyleTag(id, initialContent))
    return getStylesheet(id)
}

fun CSSStyleSheet.asStyleTag() =
    newStyleTag(title, toString())

fun newStyleTag(id: String, initialContent: String) =
    Element("style").text(initialContent).attr("title", id).id(id)


fun Element.asStyleSheet(): CSSStyleSheet? {
    val source = InputSource(StringReader(text()))


//    val owner =
//        DocumentImpl().let {
//            it.createElement("style").apply {
//                setAttribute("id", id())
//                setAttribute("title", id())
//            }
//        }

    val x = CSSOMParser(SACParserCSS3()).parseStyleSheet(source, null, null)
    (x as CSSStyleSheetImpl).title = id()

    return x
}


const val CSS_ID_DOCUMENT_SPECIFIC = "document-css"

const val CSS_ID_DEBUG = "ide-debug-css"

const val CSS_SHEET_DEBUG =
"""
   .debug-outline { 
        outline : 1px solid red;
   } 
"""