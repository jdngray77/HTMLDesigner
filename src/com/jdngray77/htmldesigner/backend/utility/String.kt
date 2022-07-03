
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

package com.jdngray77.htmldesigner.backend.utility

import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import java.util.*

/**
 * Asserts that the string ends with the [suffix].
 *
 * If it does, the string is returns unmodified, else the result is string + [suffix]
 **/
fun String.assertEndsWith(that: String) =
    if (this.endsWith(that)) this else this + that

/**
 * Converts a camel cased string to a sentence,
 * with spaces between capitlized words and
 * capitalizations on the first character.
 */
fun String.camelToSentence() : String =
    this
        .map { if (it.isUpperCase()) " " + it.lowercase() else it }
        .joinToString("")
        .capitaliseEveryWord()

/**
 * Splits string by spaces, then capitalises each substring.
 *
 * Returns the joining of each substring.
 */
fun String.capitaliseEveryWord() =
    this.split(" ").joinToString(" ") {
        // For some reason, [String.capitalize] says to use replace it with this. I hate it.
        it.replaceFirstChar { (if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString())}
    }


// TODO CLIPBOARD FILE.
/**
 * Copies the string to the system clipboard.
 */
fun String.copyToClipboard() =
    CopyToClipboard(this)

/**
 * It takes a string, puts it on the clipboard, and returns nothing
 *
 * @param string The string to copy to the clipboard.
 */
fun CopyToClipboard(string: String) {
    Clipboard.getSystemClipboard().setContent(
        ClipboardContent().apply {
            putString(string)
        }
    )
}







/**
 * Use to post data to clipboard.
 *
 * (copy)
 */
fun clipboard(callback : (ClipboardContent) -> Any ) {
    clipboard().setContent(
        ClipboardContent().apply {
            callback(this)
        }
    )
}

/**
 * Use to retrieve data.
 *
 * (paste)
 */
fun clipboard() =
    Clipboard.getSystemClipboard()
