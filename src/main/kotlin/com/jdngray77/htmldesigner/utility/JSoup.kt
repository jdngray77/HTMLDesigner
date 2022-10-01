

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


package com.jdngray77.htmldesigner.utility

import com.jdngray77.htmldesigner.backend.data.Project.Companion.projectFile
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvcIfAvail
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.findDocumentEditorByDocument
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.openDocument
import com.steadystate.css.dom.CSSStyleSheetImpl
import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser.xmlParser
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.CSSStyleSheet
import java.io.File
import java.io.Serializable
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
    openDocument(this)

/**
 * Finds an editor in the IDE that is editing this document,
 * if there is one.
 */
fun Document.editor() =
    findDocumentEditorByDocument(this)

/**
 * If the document is open in an editor, notifies
 * the editor that the document has changed.
 */
fun Document.changed(reason: String)
    = editor()?.changed(reason)




//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                                                           Element
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * Writes the given jsoup element to the disk as HTML.
 *
 * Automatically asserts that the file exists.
 */
@Deprecated("Elements should be saved as prefabs, but there are exceptions.")
fun Element.saveToDisk(f: File) {
    f.assertExists()
    f.writeText(toString())
}

/**
 * Applies some clean-up operations to the element.
 *
 * Intended for when it is copied to the clipboard, or saved to the disk.
 *
 * Removes remnants of the editor, such as the debug / editing css classes.
 */
fun Element.prepareForExport() {
    removeClass("debug-outline")
}

fun Element.userString() = tagName() + " " + id()

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

fun Document.delete() {
    mvcIfAvail()?.deleteProjectFile(projectFile())
}



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
    body().parent()!!.prependChild(newStyleTag(id, initialContent))
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

/**
 * The [DocumentUndoRedo] system relies on serialization.
 *
 * JSoup [Document]'s aren't serializable.
 *
 *
 * Luclily, since it's just HTML, it's really easy to fix this.
 *
 * We can store the document as it's HTML, which is serializable, then use JSoup
 * to interpret it back into a [Document].
 */
class SerializableDocument(document: Document) : Serializable {

    // Store the document as a string, which can be serialized.
    private val document = document.toString()

    fun get() = Jsoup.parse(document)
}

fun Document.equalsDocument(d: Document) : Boolean {
    if (this == d)
        return true

    else if (title() == d.title())
        if (toString() == d.toString())
            return true

    return true
//    // Compare by pointer first (fastest)
//    this === d ||
//            (
//                    // If not the same pointer, compare titles. If titles don't match, not same document.
//                    title() == d.title() &&
//                            // If titles are the same, then check the content matches (Slowest part. Trying to avoid this unless it's likely to be a match.)
//                            toString() == d.toString()
//                    )
}


const val CSS_ID_DOCUMENT_SPECIFIC = "document-css"

const val CSS_ID_DEBUG = "ide-debug-css"

const val CSS_SHEET_DEBUG =
"""
   .debug-outline { 
        outline : 1px solid red;
   } 
"""