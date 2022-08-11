//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.data](../index.md)/[Project](index.md)/[fileForDocument](file-for-document.md)

# fileForDocument

[jvm]\
fun [fileForDocument](file-for-document.md)(d: Document): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)

Finds this document in the CACHE, and returns the file that it was loaded from.

## Throws

| | |
|---|---|
| [com.jdngray77.htmldesigner.backend.data.Project.UnloadedDocumentException](-unloaded-document-exception/index.md) | if the file is not in the cache,     i.e the file has been deleted or the document did not originate from the project. |
