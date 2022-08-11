//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner](../index.md)/[MVC](index.md)

# MVC

[jvm]\
class [MVC](index.md)(var Project: [Project](../../com.jdngray77.htmldesigner.backend.data/-project/index.md), var MainView: [MainViewController](../../com.jdngray77.htmldesigner.frontend/-main-view-controller/index.md)) : [Subscriber](../../com.jdngray77.htmldesigner.backend/-subscriber/index.md)

# Model View Controller.

Central location for reaching :

- 
   GUI
- 
   Project
- 
   Current [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md).

The entire IDE uses the data and API here to access and mutate the model.

## Constructors

| | |
|---|---|
| [MVC](-m-v-c.md) | [jvm]<br>fun [MVC](-m-v-c.md)(Project: [Project](../../com.jdngray77.htmldesigner.backend.data/-project/index.md), MainView: [MainViewController](../../com.jdngray77.htmldesigner.frontend/-main-view-controller/index.md)) |

## Types

| Name | Summary |
|---|---|
| [DocumentModificationTransaction](-document-modification-transaction/index.md) | [jvm]<br>inner class [DocumentModificationTransaction](-document-modification-transaction/index.md) : [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;Document&gt; <br>When making many changes at once, this can be used to delay the change notifications to after completing all changes. |

## Functions

| Name | Summary |
|---|---|
| [currentDocument](current-document.md) | [jvm]<br>fun [currentDocument](current-document.md)(): Document<br>Returns the document of the current editor |
| [currentDocumentModified](current-document-modified.md) | [jvm]<br>fun [currentDocumentModified](current-document-modified.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>Marks the current document as dirty, and updates the GUI with the changes. |
| [currentEditor](current-editor.md) | [jvm]<br>fun [currentEditor](current-editor.md)(): [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md)<br>It returns the current editor |
| [delete](delete.md) | [jvm]<br>fun [delete](delete.md)(projectFile: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) |
| [deleteTag](delete-tag.md) | [jvm]<br>fun [deleteTag](delete-tag.md)(vararg tag: Element)<br>Deletes [tag](delete-tag.md) after confirming with the user. |
| [editorAvail](editor-avail.md) | [jvm]<br>fun [editorAvail](editor-avail.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [findEditorFor](find-editor-for.md) | [jvm]<br>fun [findEditorFor](find-editor-for.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)): [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md)?<br>Finds the editor for the given File<br>[jvm]<br>fun [findEditorFor](find-editor-for.md)(tab: Tab): [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md)?<br>Finds the editor for the given tab.<br>[jvm]<br>fun [findEditorFor](find-editor-for.md)(document: Document): [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md)?<br>Finds the editor for the given document. |
| [Focus](-focus.md) | [jvm]<br>fun [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md).[Focus](-focus.md)()<br>This function switches to the editor tab that is passed in as a parameter |
| [getOpenEditors](get-open-editors.md) | [jvm]<br>fun [getOpenEditors](get-open-editors.md)(): [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md)&gt; |
| [implDeleteTag](impl-delete-tag.md) | [jvm]<br>fun [implDeleteTag](impl-delete-tag.md)(vararg tag: Element) |
| [notify](notify.md) | [jvm]<br>open override fun [notify](notify.md)(e: [EventType](../../com.jdngray77.htmldesigner.backend/-event-type/index.md)) |
| [onEditorClosed](on-editor-closed.md) | [jvm]<br>fun [onEditorClosed](on-editor-closed.md)(documentEditor: [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md))<br>Event invoked when an editor tab is closed. |
| [openDocument](open-document.md) | [jvm]<br>fun [openDocument](open-document.md)(document: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html))<br>Loads a project document from disk and opens it.<br>[jvm]<br>fun [openDocument](open-document.md)(document: Document)<br>Creates and opens a new document editor for the given [document](open-document.md) |
| [selectedTag](selected-tag.md) | [jvm]<br>fun [selectedTag](selected-tag.md)(): Element? |
| [switchToDocument](switch-to-document.md) | [jvm]<br>fun [switchToDocument](switch-to-document.md)(document: Document): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>&quot;If there's an editor for the given document, switch to it, otherwise create a new editor.&quot; |
| [switchToEditor](switch-to-editor.md) | [jvm]<br>fun [switchToEditor](switch-to-editor.md)(editor: [DocumentEditor](../../com.jdngray77.htmldesigner.frontend/-document-editor/index.md))<br>This function switches to the editor tab that is passed in as a parameter |
| [validateEditors](validate-editors.md) | [jvm]<br>fun [validateEditors](validate-editors.md)() |

## Properties

| Name | Summary |
|---|---|
| [MainView](-main-view.md) | [jvm]<br>var [MainView](-main-view.md): [MainViewController](../../com.jdngray77.htmldesigner.frontend/-main-view-controller/index.md)<br>The IDE's main FXML GUI controller. |
| [Project](-project.md) | [jvm]<br>var [Project](-project.md): [Project](../../com.jdngray77.htmldesigner.backend.data/-project/index.md)<br>The project the IDE is working on. |
