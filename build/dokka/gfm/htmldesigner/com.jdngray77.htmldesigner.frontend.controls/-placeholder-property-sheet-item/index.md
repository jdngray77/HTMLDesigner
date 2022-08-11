//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend.controls](../index.md)/[PlaceholderPropertySheetItem](index.md)

# PlaceholderPropertySheetItem

[jvm]\
class [PlaceholderPropertySheetItem](index.md)(val _name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : PropertySheet.Item

A PropertySheet.Item that represents an editor / item that has not yet been added.

Displays a label and a disabled text field.

## Constructors

| | |
|---|---|
| [PlaceholderPropertySheetItem](-placeholder-property-sheet-item.md) | [jvm]<br>fun [PlaceholderPropertySheetItem](-placeholder-property-sheet-item.md)(_name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [getCategory](get-category.md) | [jvm]<br>open override fun [getCategory](get-category.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getDescription](get-description.md) | [jvm]<br>open override fun [getDescription](get-description.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getName](get-name.md) | [jvm]<br>open override fun [getName](get-name.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getObservableValue](get-observable-value.md) | [jvm]<br>open override fun [getObservableValue](get-observable-value.md)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;ObservableValue&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;&gt; |
| [getPropertyEditorClass](get-property-editor-class.md) | [jvm]<br>open override fun [getPropertyEditorClass](get-property-editor-class.md)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out PropertyEditor&lt;*&gt;&gt;&gt; |
| [getType](get-type.md) | [jvm]<br>open override fun [getType](get-type.md)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [getValue](get-value.md) | [jvm]<br>open override fun [getValue](get-value.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [isEditable](is-editable.md) | [jvm]<br>open override fun [isEditable](is-editable.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setValue](set-value.md) | [jvm]<br>open override fun [setValue](set-value.md)(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [_category](_category.md) | [jvm]<br>val [_category](_category.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The string used to group properties together in the GUI. |
| [_name](_name.md) | [jvm]<br>val [_name](_name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the non-existant field. |
