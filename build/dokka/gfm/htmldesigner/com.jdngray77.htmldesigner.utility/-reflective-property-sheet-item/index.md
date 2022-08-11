//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.utility](../index.md)/[ReflectivePropertySheetItem](index.md)

# ReflectivePropertySheetItem

[jvm]\
class [ReflectivePropertySheetItem](index.md)&lt;[T](index.md)&gt;(val fieldName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val obj: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), val _isEditable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true) : PropertySheet.Item

A wrapper around a field of an object that allows you to use that field as a property sheet item

When making A PropertySheet, provides a way to easily edit any field in eny object via reflection

## Constructors

| | |
|---|---|
| [ReflectivePropertySheetItem](-reflective-property-sheet-item.md) | [jvm]<br>fun [ReflectivePropertySheetItem](-reflective-property-sheet-item.md)(fieldName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), obj: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), _isEditable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true) |

## Functions

| Name | Summary |
|---|---|
| [getCategory](get-category.md) | [jvm]<br>open override fun [getCategory](get-category.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getDescription](get-description.md) | [jvm]<br>open override fun [getDescription](get-description.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getName](get-name.md) | [jvm]<br>open override fun [getName](get-name.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getObservableValue](get-observable-value.md) | [jvm]<br>open override fun [getObservableValue](get-observable-value.md)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;ObservableValue&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;&gt; |
| [getPropertyEditorClass](get-property-editor-class.md) | [jvm]<br>open override fun [getPropertyEditorClass](get-property-editor-class.md)(): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out PropertyEditor&lt;*&gt;&gt;&gt; |
| [getType](get-type.md) | [jvm]<br>open override fun [getType](get-type.md)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt; |
| [getValue](get-value.md) | [jvm]<br>open override fun [getValue](get-value.md)(): [T](index.md) |
| [isEditable](is-editable.md) | [jvm]<br>open override fun [isEditable](is-editable.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setValue](set-value.md) | [jvm]<br>open override fun [setValue](set-value.md)(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [_category](_category.md) | [jvm]<br>val [_category](_category.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The string used to group properties together in the GUI. |
| [_description](_description.md) | [jvm]<br>val [_description](_description.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>More information that is shown to the user, when they hover the cursor. |
| [_isEditable](_is-editable.md) | [jvm]<br>val [_isEditable](_is-editable.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true<br>Determines if this property is read-only |
| [fieldName](field-name.md) | [jvm]<br>val [fieldName](field-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the property (variable) within [obj](obj.md) that will be modified. This is also the name displayed to the user. |
| [javaGetter](java-getter.md) | [jvm]<br>val [javaGetter](java-getter.md): [Method](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Method.html) |
| [javaSetter](java-setter.md) | [jvm]<br>val [javaSetter](java-setter.md): [Method](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Method.html) |
| [obj](obj.md) | [jvm]<br>val [obj](obj.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>The object being modified |
