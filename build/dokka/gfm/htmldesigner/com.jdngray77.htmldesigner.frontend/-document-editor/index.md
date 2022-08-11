//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend](../index.md)/[DocumentEditor](index.md)

# DocumentEditor

[jvm]\
class [DocumentEditor](index.md)

# Central document editor.

This file is the FXML Controller for the 'DocumentEditor.fxml' GUI.

It holds an open document in order to make and save changes, but note that it **Does not handle modifying the** [**document**](document.md)**. For that, see** **MVC****.**

In the IDE's interface, this is a [tab](tab.md) that opens within the IDE displaying a document. The IDE will access and edit the [DocumentEditor](index.md) that is selected.

There is a reference to the [tab](tab.md) used to hold this [DocumentEditor](index.md) for convenience.

All docks will update to display and edit the selected editor via [EventType.EDITOR_DOCUMENT_SWITCH](../../com.jdngray77.htmldesigner.backend/-event-type/-e-d-i-t-o-r_-d-o-c-u-m-e-n-t_-s-w-i-t-c-h/index.md).

The editor is created in MVC.openEditor. The document is also set here.

## Constructors

| | |
|---|---|
| [DocumentEditor](-document-editor.md) | [jvm]<br>fun [DocumentEditor](-document-editor.md)() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [back](back.md) | [jvm]<br>fun [back](back.md)()<br>Navigates back a page. |
| [clean](clean.md) | [jvm]<br>fun [clean](clean.md)() |
| [documentChanged](document-changed.md) | [jvm]<br>fun [documentChanged](document-changed.md)()<br>Notifies this document editor that the document it holds has been modified. |
| [forceClose](force-close.md) | [jvm]<br>~~fun~~ [~~forceClose~~](force-close.md)~~(~~~~)~~<br>Force closes the tab, without saving or asking the user. |
| [forward](forward.md) | [jvm]<br>fun [forward](forward.md)()<br>Navigates forward a page. |
| [initialize](initialize.md) | [jvm]<br>fun [initialize](initialize.md)()<br>Late 'init' called by FXML. |
| [requestClose](request-close.md) | [jvm]<br>fun [requestClose](request-close.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Asks this editor to close. |
| [reRender](re-render.md) | [jvm]<br>fun [reRender](re-render.md)()<br>Updates WebView to display the current state of the [document](document.md) |
| [resetEditor](reset-editor.md) | [jvm]<br>fun [resetEditor](reset-editor.md)()<br>Reset the editor to its default state. |
| [resetZoom](reset-zoom.md) | [jvm]<br>fun [resetZoom](reset-zoom.md)()<br>[btnZoom](btn-zoom.md) |
| [save](save.md) | [jvm]<br>fun [save](save.md)()<br>Saves the document this editor is for. |
| [selectTag](select-tag.md) | [jvm]<br>fun [selectTag](select-tag.md)(treeItem: TreeItem&lt;Element&gt;)<br>Selects the given element, but does and populates the [breadCrumb](bread-crumb.md).<br>[jvm]<br>~~fun~~ [~~selectTag~~](select-tag.md)~~(~~tag: Element?~~)~~<br>Selects the given element, but does not populate the [breadCrumb](bread-crumb.md). |
| [setDocument](set-document.md) | [jvm]<br>~~fun~~ [~~setDocument~~](set-document.md)~~(~~document: Document~~)~~<br>For creation only. |
| [zoomIn](zoom-in.md) | [jvm]<br>fun [zoomIn](zoom-in.md)()<br>Zoom in button |
| [zoomOut](zoom-out.md) | [jvm]<br>fun [zoomOut](zoom-out.md)()<br>Zoom out |

## Properties

| Name | Summary |
|---|---|
| [breadCrumb](bread-crumb.md) | [jvm]<br>lateinit var [breadCrumb](bread-crumb.md): BreadCrumbBar&lt;Element&gt;<br>Breadcrumb bar for the element the user has selected. |
| [btnZoom](btn-zoom.md) | [jvm]<br>lateinit var [btnZoom](btn-zoom.md): Button<br>Button which shows the zoom level to the user, and allows them to reset the zoom when clicked. |
| [contentRenderer](content-renderer.md) | [jvm]<br>lateinit var [contentRenderer](content-renderer.md): WebView<br>A WebView that displays the Document to the user. |
| [document](document.md) | [jvm]<br>lateinit var [document](document.md): Document<br>The document held by this editor. |
| [editorRoot](editor-root.md) | [jvm]<br>lateinit var [editorRoot](editor-root.md): BorderPane<br>The root container of this [DocumentEditor](index.md) |
| [file](file.md) | [jvm]<br>lateinit var [file](file.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The file on disk where the [document](document.md) is located. |
| [isDirty](is-dirty.md) | [jvm]<br>var [isDirty](is-dirty.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>When raised, this flag indicates that this editor has been changed. |
| [selectedTag](selected-tag.md) | [jvm]<br>var [selectedTag](selected-tag.md): Element? = null<br>A tag selected via the TagHierarchy. |
| [standaloneEditMode](standalone-edit-mode.md) | [jvm]<br>var [standaloneEditMode](standalone-edit-mode.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>Standalone editor mode. |
| [tab](tab.md) | [jvm]<br>lateinit var [tab](tab.md): Tab<br>The tab displayed in the IDE, which contains this editor. |
