//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.frontend.controls](index.md)

# Package com.jdngray77.htmldesigner.frontend.controls

## Types

| Name | Summary |
|---|---|
| [AlignControl](-align-control/index.md) | [jvm]<br>class [AlignControl](-align-control/index.md) : SegmentedButton |
| [CSSUnitCombo](-c-s-s-unit-combo/index.md) | [jvm]<br>class [CSSUnitCombo](-c-s-s-unit-combo/index.md)(defaultValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = CSSUnits[0]) : ComboBox&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; <br>A combo box that displays CSS Units. |
| [CSSUnitSlider](-c-s-s-unit-slider/index.md) | [jvm]<br>class [CSSUnitSlider](-c-s-s-unit-slider/index.md)(min: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = 0.0, max: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = 100.0, step: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = 1.0, defaultUnit: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = CSSUnits[0], enableUnits: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true) : HBox<br>A slider with a user selectable unit of css measure. |
| [FlexJustify](-flex-justify/index.md) | [jvm]<br>object [FlexJustify](-flex-justify/index.md) : [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [PlaceholderPropertySheetItem](-placeholder-property-sheet-item/index.md) | [jvm]<br>class [PlaceholderPropertySheetItem](-placeholder-property-sheet-item/index.md)(val _name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val _category: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : PropertySheet.Item<br>A PropertySheet.Item that represents an editor / item that has not yet been added. |
| [QuadControl](-quad-control/index.md) | [jvm]<br>class [QuadControl](-quad-control/index.md)&lt;[T](-quad-control/index.md), [R](-quad-control/index.md) : Node&gt;(val controlFactory: () -&gt; [R](-quad-control/index.md), val controlGetter: ([R](-quad-control/index.md)) -&gt; [T](-quad-control/index.md), val controlSetter: ([R](-quad-control/index.md), [T](-quad-control/index.md)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) : VBox<br>A control which can hold a many CSS unit providing controls. |
| [RegistryEditor](-registry-editor/index.md) | [jvm]<br>class [RegistryEditor](-registry-editor/index.md)&lt;[T](-registry-editor/index.md)&gt;(val registry: [Registry](../com.jdngray77.htmldesigner.backend.data.config/-registry/index.md)&lt;[T](-registry-editor/index.md)&gt;) : PropertySheet<br>A property sheet control for displaying and editing a [registry](-registry-editor/registry.md). |
| [RunAnything](-run-anything/index.md) | [jvm]<br>object [RunAnything](-run-anything/index.md) : [SearchableList](-searchable-list/index.md)&lt;[Task](-task/index.md)&gt; |
| [SearchableList](-searchable-list/index.md) | [jvm]<br>open class [SearchableList](-searchable-list/index.md)&lt;[T](-searchable-list/index.md)&gt;(var items: [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[T](-searchable-list/index.md)&gt; = arrayListOf()) : VBox |
| [Task](-task/index.md) | [jvm]<br>class [Task](-task/index.md)(val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val script: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) : [Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html) |

## Functions

| Name | Summary |
|---|---|
| [determineCSSUnit](determine-c-s-s-unit.md) | [jvm]<br>fun [determineCSSUnit](determine-c-s-s-unit.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>It takes a string and returns the CSS unit if it exists in the string |
| [removeCSSUnit](remove-c-s-s-unit.md) | [jvm]<br>fun [removeCSSUnit](remove-c-s-s-unit.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>When provided a valid CSS size, removes all the units from a CSS size value. |

## Properties

| Name | Summary |
|---|---|
| [CSSUnits](-c-s-s-units.md) | [jvm]<br>val [CSSUnits](-c-s-s-units.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
