//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend.docks.tagproperties](../index.md)/[CSSDropdownItem](index.md)

# CSSDropdownItem

[jvm]\
class [CSSDropdownItem](index.md)(val _name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val element: Element, val property: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _possibleValues: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md)

A [CSSPropertySheetItem](../-c-s-s-property-sheet-item/index.md) with a list of possible values to select from.

Edited with a [CSSDropdownEditor](../-c-s-s-dropdown-editor/index.md)

## Constructors

| | |
|---|---|
| [CSSDropdownItem](-c-s-s-dropdown-item.md) | [jvm]<br>fun [CSSDropdownItem](-c-s-s-dropdown-item.md)(_name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), element: Element, property: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg _possibleValues: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [getCategory](../-c-s-s-property-sheet-item/get-category.md) | [jvm]<br>open override fun [getCategory](../-c-s-s-property-sheet-item/get-category.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getDescription](../-c-s-s-property-sheet-item/get-description.md) | [jvm]<br>open override fun [getDescription](../-c-s-s-property-sheet-item/get-description.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getName](../-c-s-s-property-sheet-item/get-name.md) | [jvm]<br>open override fun [getName](../-c-s-s-property-sheet-item/get-name.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getObservableValue](../-c-s-s-property-sheet-item/get-observable-value.md) | [jvm]<br>override fun [getObservableValue](../-c-s-s-property-sheet-item/get-observable-value.md)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;ObservableValue&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;&gt;<br>Provides a read-only listenable property for this item. |
| [getPropertyEditorClass](../-c-s-s-quad-range-item/index.md#216077875%2FFunctions%2F-1216412040) | [jvm]<br>open fun [getPropertyEditorClass](../-c-s-s-quad-range-item/index.md#216077875%2FFunctions%2F-1216412040)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out PropertyEditor&lt;*&gt;&gt;&gt; |
| [getType](../-c-s-s-property-sheet-item/get-type.md) | [jvm]<br>override fun [getType](../-c-s-s-property-sheet-item/get-type.md)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt;<br>Tells the PropertySheet the data type of the item. |
| [getValue](../-c-s-s-property-sheet-item/get-value.md) | [jvm]<br>open override fun [getValue](../-c-s-s-property-sheet-item/get-value.md)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?<br>Returns the current value of [property](../-c-s-s-property-sheet-item/property.md) in the [element](../-c-s-s-property-sheet-item/element.md)'s [styles](../-c-s-s-property-sheet-item/styles.md). |
| [isEditable](../-c-s-s-quad-range-item/index.md#1697750277%2FFunctions%2F-1216412040) | [jvm]<br>open fun [isEditable](../-c-s-s-quad-range-item/index.md#1697750277%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setValue](../-c-s-s-property-sheet-item/set-value.md) | [jvm]<br>open override fun [setValue](../-c-s-s-property-sheet-item/set-value.md)(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [_category](../-c-s-s-property-sheet-item/_category.md) | [jvm]<br>val [_category](../-c-s-s-property-sheet-item/_category.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Used by the PropertySheet to group items together in the GUI. |
| [_description](../-c-s-s-property-sheet-item/_description.md) | [jvm]<br>val [_description](../-c-s-s-property-sheet-item/_description.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Used by the PropertySheet to display a tooltip when the user hovers over the item. |
| [_name](../-c-s-s-property-sheet-item/_name.md) | [jvm]<br>val [_name](../-c-s-s-property-sheet-item/_name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the property displayed to the user. |
| [_type](../-c-s-s-property-sheet-item/_type.md) | [jvm]<br>val [_type](../-c-s-s-property-sheet-item/_type.md): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt;<br>Determines what type of editor to use for this property. |
| [caster](../-c-s-s-property-sheet-item/caster.md) | [jvm]<br>val [caster](../-c-s-s-property-sheet-item/caster.md): ([String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) -&gt; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>A function to cast the string found on the tag to the data type required by the GUI editor. |
| [element](../-c-s-s-property-sheet-item/element.md) | [jvm]<br>val [element](../-c-s-s-property-sheet-item/element.md): Element<br>The element that is being edited |
| [possibleValues](possible-values.md) | [jvm]<br>val [possibleValues](possible-values.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [property](../-c-s-s-property-sheet-item/property.md) | [jvm]<br>val [property](../-c-s-s-property-sheet-item/property.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the CSS property that is being edited on the [element](../-c-s-s-property-sheet-item/element.md) |
| [styles](../-c-s-s-property-sheet-item/styles.md) | [jvm]<br>val [styles](../-c-s-s-property-sheet-item/styles.md): [StyleAttributeSnapshot](../../com.jdngray77.htmldesigner.backend.html/-style-attribute-snapshot/index.md)<br>The styles obtained from the [element](../-c-s-s-property-sheet-item/element.md) at time of creation of this item |
