//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend.docks.tagproperties](../index.md)/[CSSPropertyEditor](index.md)

# CSSPropertyEditor

[jvm]\
abstract class [CSSPropertyEditor](index.md)&lt;[T](index.md)&gt;(val _item: [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md), val _editor: Node) : AbstractPropertyEditor&lt;[T](index.md), Node&gt; 

The base editor for editing the above [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md).

This is an editor that can be added to a PropertySheet.

Since HTML Element's have to be edited in special ways to apply the CSS, default property cannot be directly used.

Default editors, like a TextPropertyEditor, could still be used to accept an input from the user, but it must be wrapped with [CSSPropertyEditor](index.md) to apply the user's changes to the style attribute in the Element.

This editor Listens to the given editor, and when updated by the user the [_item](_item.md) is automatically updated.

If using the single parameter constructor, the editor to be used will be automatically inferred by type using the DefaultPropertyEditorFactory.

Alternately, an editor may be provided.

## Parameters

jvm

| | |
|---|---|
| T | the type of data provided by and to the editor. The result will always be stored as a string. |

## Constructors

| | |
|---|---|
| [CSSPropertyEditor](-c-s-s-property-editor.md) | [jvm]<br>fun [CSSPropertyEditor](-c-s-s-property-editor.md)(property: [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md))<br>Alternate constructor which infers what GUI editor to use by data type by using the DefaultPropertyEditorFactory. |
| [CSSPropertyEditor](-c-s-s-property-editor.md) | [jvm]<br>fun [CSSPropertyEditor](-c-s-s-property-editor.md)(_item: [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md), _editor: Node) |

## Functions

| Name | Summary |
|---|---|
| [getEditor](get-editor.md) | [jvm]<br>open override fun [getEditor](get-editor.md)(): Node<br>Gets the GUI editor used to edit the item. |
| [getValue](index.md#-1716486404%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [getValue](index.md#-1716486404%2FFunctions%2F-1216412040)(): [T](index.md) |
| [setValue](index.md#1138338168%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [setValue](index.md#1138338168%2FFunctions%2F-1216412040)(p0: [T](index.md)) |

## Properties

| Name | Summary |
|---|---|
| [_editor](_editor.md) | [jvm]<br>val [_editor](_editor.md): Node<br>The actual GUI editor to provide to the user. |
| [_item](_item.md) | [jvm]<br>val [_item](_item.md): [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md)<br>The item to edit. |
| [property](../-c-s-s-quad-range-editor/index.md#-813299166%2FProperties%2F-1216412040) | [jvm]<br>val [property](../-c-s-s-quad-range-editor/index.md#-813299166%2FProperties%2F-1216412040): PropertySheet.Item |

## Inheritors

| Name |
|---|
| [CSSAlignmentPropertyEditor](../-c-s-s-alignment-property-editor/index.md) |
| [CSSColorPropertyEditor](../-c-s-s-color-property-editor/index.md) |
| [CSSDropdownEditor](../-c-s-s-dropdown-editor/index.md) |
| [CSSRangeEditor](../-c-s-s-range-editor/index.md) |
| [CSSQuadRangeEditor](../-c-s-s-quad-range-editor/index.md) |
