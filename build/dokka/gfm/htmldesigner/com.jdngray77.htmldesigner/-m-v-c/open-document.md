//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner](../index.md)/[MVC](index.md)/[openDocument](open-document.md)

# openDocument

[jvm]\
fun [openDocument](open-document.md)(document: Document)

Creates and opens a new document editor for the given [document](open-document.md)

Create a new document editor, set the document, add the editor to the list of open editors, and switch to the new editor

## Parameters

jvm

| | |
|---|---|
| document | Document - The document to open |

[jvm]\
fun [openDocument](open-document.md)(document: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html))

Loads a project document from disk and opens it.

If there's already an editor, it's switched to. Else, one is created.
