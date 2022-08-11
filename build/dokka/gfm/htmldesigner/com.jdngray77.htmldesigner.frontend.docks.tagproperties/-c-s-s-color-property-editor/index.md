//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend.docks.tagproperties](../index.md)/[CSSColorPropertyEditor](index.md)

# CSSColorPropertyEditor

[jvm]\
class [CSSColorPropertyEditor](index.md)(property: [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md)) : [CSSPropertyEditor](../-c-s-s-property-editor/index.md)&lt;Color&gt; 

Editor for any color based property.

i.e. color, background-color, border-color, etc.

Uses a ColorPicker.

## Constructors

| | |
|---|---|
| [CSSColorPropertyEditor](-c-s-s-color-property-editor.md) | [jvm]<br>fun [CSSColorPropertyEditor](-c-s-s-color-property-editor.md)(property: [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [getEditor](../-c-s-s-property-editor/get-editor.md) | [jvm]<br>open override fun [getEditor](../-c-s-s-property-editor/get-editor.md)(): Node<br>Gets the GUI editor used to edit the item. |
| [getValue](get-value.md) | [jvm]<br>open override fun [getValue](get-value.md)(): Color |
| [setValue](set-value.md) | [jvm]<br>open override fun [setValue](set-value.md)(value: Color?) |

## Properties

| Name | Summary |
|---|---|
| [_editor](../-c-s-s-property-editor/_editor.md) | [jvm]<br>val [_editor](../-c-s-s-property-editor/_editor.md): Node<br>The actual GUI editor to provide to the user. |
| [_item](../-c-s-s-property-editor/_item.md) | [jvm]<br>val [_item](../-c-s-s-property-editor/_item.md): [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md)<br>The item to edit. |
| [property](../-c-s-s-quad-range-editor/index.md#-813299166%2FProperties%2F-1216412040) | [jvm]<br>val [property](../-c-s-s-quad-range-editor/index.md#-813299166%2FProperties%2F-1216412040): PropertySheet.Item |
