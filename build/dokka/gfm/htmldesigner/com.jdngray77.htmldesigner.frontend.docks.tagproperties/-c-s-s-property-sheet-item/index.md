//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend.docks.tagproperties](../index.md)/[CSSPropertySheetItem](index.md)

# CSSPropertySheetItem

[jvm]\
open class [CSSPropertySheetItem](index.md)(val _name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val element: Element, val property: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _type: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt; = String::class.java, val caster: ([String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) -&gt; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) = { it }) : PropertySheet.Item

Abstract representation of a css property.

This is an item that can be added to a PropertySheet to represent an editable CSS property for a given html element.

When it receives the input from a [CSSPropertyEditor](../-c-s-s-property-editor/index.md), it handles updating the value in the Element's style attribute.

i.e

Edited with a [CSSPropertyEditor](../-c-s-s-property-editor/index.md) of some kind.

Can be used alone for simple properties that, but can be extended for CSS entries that require custom implementations or requirements.

## Constructors

| | |
|---|---|
| [CSSPropertySheetItem](-c-s-s-property-sheet-item.md) | [jvm]<br>fun [CSSPropertySheetItem](-c-s-s-property-sheet-item.md)(_name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), element: Element, property: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _type: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt; = String::class.java, caster: ([String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) -&gt; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) = { it }) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [getCategory](get-category.md) | [jvm]<br>open override fun [getCategory](get-category.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getDescription](get-description.md) | [jvm]<br>open override fun [getDescription](get-description.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getName](get-name.md) | [jvm]<br>open override fun [getName](get-name.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getObservableValue](get-observable-value.md) | [jvm]<br>override fun [getObservableValue](get-observable-value.md)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;ObservableValue&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;&gt;<br>Provides a read-only listenable property for this item. |
| [getPropertyEditorClass](../-c-s-s-quad-range-item/index.md#216077875%2FFunctions%2F-1216412040) | [jvm]<br>open fun [getPropertyEditorClass](../-c-s-s-quad-range-item/index.md#216077875%2FFunctions%2F-1216412040)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out PropertyEditor&lt;*&gt;&gt;&gt; |
| [getType](get-type.md) | [jvm]<br>override fun [getType](get-type.md)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt;<br>Tells the PropertySheet the data type of the item. |
| [getValue](get-value.md) | [jvm]<br>open override fun [getValue](get-value.md)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?<br>Returns the current value of [property](property.md) in the [element](element.md)'s [styles](styles.md). |
| [isEditable](../-c-s-s-quad-range-item/index.md#1697750277%2FFunctions%2F-1216412040) | [jvm]<br>open fun [isEditable](../-c-s-s-quad-range-item/index.md#1697750277%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setValue](set-value.md) | [jvm]<br>open override fun [setValue](set-value.md)(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [_category](_category.md) | [jvm]<br>val [_category](_category.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Used by the PropertySheet to group items together in the GUI. |
| [_description](_description.md) | [jvm]<br>val [_description](_description.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Used by the PropertySheet to display a tooltip when the user hovers over the item. |
| [_name](_name.md) | [jvm]<br>val [_name](_name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the property displayed to the user. |
| [_type](_type.md) | [jvm]<br>val [_type](_type.md): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt;<br>Determines what type of editor to use for this property. |
| [caster](caster.md) | [jvm]<br>val [caster](caster.md): ([String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) -&gt; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>A function to cast the string found on the tag to the data type required by the GUI editor. |
| [element](element.md) | [jvm]<br>val [element](element.md): Element<br>The element that is being edited |
| [property](property.md) | [jvm]<br>val [property](property.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the CSS property that is being edited on the [element](element.md) |
| [styles](styles.md) | [jvm]<br>val [styles](styles.md): [StyleAttributeSnapshot](../../com.jdngray77.htmldesigner.backend.html/-style-attribute-snapshot/index.md)<br>The styles obtained from the [element](element.md) at time of creation of this item |

## Inheritors

| Name |
|---|
| [CSSAlignmentPropertySheetItem](../-c-s-s-alignment-property-sheet-item/index.md) |
| [CSSDropdownItem](../-c-s-s-dropdown-item/index.md) |
| [CSSRangeItem](../-c-s-s-range-item/index.md) |
| [CSSQuadRangeItem](../-c-s-s-quad-range-item/index.md) |
