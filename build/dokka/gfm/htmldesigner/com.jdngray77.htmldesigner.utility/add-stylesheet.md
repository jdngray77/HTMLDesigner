//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.utility](index.md)/[addStylesheet](add-stylesheet.md)

# addStylesheet

[jvm]\
fun Document.[addStylesheet](add-stylesheet.md)(css: [CSSStyleSheet](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.css/-c-s-s-style-sheet/index.html))

Adds a stylesheet to this document.

Creates a style tag in this document's head with the given name, and the serialized content of [css](add-stylesheet.md).

If a style tag with the same ID already exists, it is replaced with [css](add-stylesheet.md).

## See also

jvm

| | |
|---|---|
| [Document.getStylesheet](get-stylesheet.md) | for retrieval. |

[jvm]\
fun Document.[addStylesheet](add-stylesheet.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), initialContent: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;&quot;): [CSSStyleSheet](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.css/-c-s-s-style-sheet/index.html)?
