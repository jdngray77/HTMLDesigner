//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.data](../index.md)/[Project](index.md)/[createDocument](create-document.md)

# createDocument

[jvm]\
fun [createDocument](create-document.md)(subpath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): Document

Creates a new HTML document in this project, and saves it to the disk.

Document is a clone of Tag.testDOM.

The path is added to pagePaths.

[saveMeta](save-meta.md) is called after making the change.

Notifies [EventType.PROJECT_NEW_DOCUMENT_CREATED](../../com.jdngray77.htmldesigner.backend/-event-type/-p-r-o-j-e-c-t_-n-e-w_-d-o-c-u-m-e-n-t_-c-r-e-a-t-e-d/index.md)

#### Return

the new document.
