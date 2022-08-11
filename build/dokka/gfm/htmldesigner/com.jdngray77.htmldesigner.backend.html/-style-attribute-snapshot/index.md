//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.html](../index.md)/[StyleAttributeSnapshot](index.md)

# StyleAttributeSnapshot

[jvm]\
class [StyleAttributeSnapshot](index.md)(val element: Element) : [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; 

An editable snapshot for capturing and manipulating the styles within an Element.

**Be aware that this does not continually keep track of changes made to elements from elsewhere in the editor, and as such should not be relied on to access the current state of the element later without re-capturing via** [**capture**](capture.md)**.**

Be aware of race conditions. TODO implement a snapshot lock of some kind?

TODO this can be abstracted to snapshot any attribute.

i.e

```kotlin
<div style="...;">
```

usage

```kotlin
// Create an editable snapshot of the elements styles
val styles = StyleAttribute(element)

// Make changes, or access the snapshot
styles["display"] = "flex"
styles["justify-content"] = "center"

// Commit the changes back to the element.
styles.commit()
```

## See also

jvm

| | |
|---|---|
| [com.jdngray77.htmldesigner.backend.html.StyleAttributeSnapshot](commit.md) |  |

## Constructors

| | |
|---|---|
| [StyleAttributeSnapshot](-style-attribute-snapshot.md) | [jvm]<br>fun [StyleAttributeSnapshot](-style-attribute-snapshot.md)(element: Element) |

## Functions

| Name | Summary |
|---|---|
| [capture](capture.md) | [jvm]<br>fun [capture](capture.md)(): [StyleAttributeSnapshot](index.md)<br>Captures the style properties currently in the [element](element.md). |
| [clear](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-257373230%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [clear](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-257373230%2FFunctions%2F-1216412040)() |
| [clone](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#703051458%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [clone](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#703051458%2FFunctions%2F-1216412040)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [commit](commit.md) | [jvm]<br>fun [commit](commit.md)()<br>Overwrite the element's styles with the contents of this snapshot. |
| [compute](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#30197662%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [compute](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#30197662%2FFunctions%2F-1216412040)(p0: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, out [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [computeIfAbsent](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1886325805%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [computeIfAbsent](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1886325805%2FFunctions%2F-1216412040)(p0: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), out [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [computeIfPresent](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#813458873%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [computeIfPresent](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#813458873%2FFunctions%2F-1216412040)(p0: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), out [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [containsKey](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-894265563%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsKey](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-894265563%2FFunctions%2F-1216412040)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsValue](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1119568119%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsValue](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1119568119%2FFunctions%2F-1216412040)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#208304826%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [equals](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#208304826%2FFunctions%2F-1216412040)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#674821664%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [forEach](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#674821664%2FFunctions%2F-1216412040)(p0: [BiConsumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiConsumer.html)&lt;in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) |
| [get](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#829398075%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [get](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#829398075%2FFunctions%2F-1216412040)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [getOrDefault](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1129959818%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [getOrDefault](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1129959818%2FFunctions%2F-1216412040)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), defaultValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [hashCode](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#612619500%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [hashCode](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#612619500%2FFunctions%2F-1216412040)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#360261660%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [isEmpty](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#360261660%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [merge](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#915730475%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [merge](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#915730475%2FFunctions%2F-1216412040)(p0: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p2: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), out [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [put](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1811651815%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [put](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1811651815%2FFunctions%2F-1216412040)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [putAll](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1902894919%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [putAll](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1902894919%2FFunctions%2F-1216412040)(from: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) |
| [putIfAbsent](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1060937199%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [putIfAbsent](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1060937199%2FFunctions%2F-1216412040)(p0: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [remove](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1413709869%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [remove](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#1413709869%2FFunctions%2F-1216412040)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>open override fun [remove](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#881495634%2FFunctions%2F-1216412040)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replace](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-955131074%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [replace](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-955131074%2FFunctions%2F-1216412040)(p0: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>open override fun [replace](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1657817181%2FFunctions%2F-1216412040)(p0: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p2: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#2105050701%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [replaceAll](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#2105050701%2FFunctions%2F-1216412040)(p0: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), in [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), out [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) |
| [style](style.md) | [jvm]<br>fun [style](style.md)(propertyName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>fun [style](style.md)(propertyName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [toString](to-string.md) | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Properties

| Name | Summary |
|---|---|
| [element](element.md) | [jvm]<br>val [element](element.md): Element |
| [entries](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#2111837807%2FProperties%2F-1216412040) | [jvm]<br>open override val [entries](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#2111837807%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;&gt; |
| [keys](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1479970233%2FProperties%2F-1216412040) | [jvm]<br>open override val [keys](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1479970233%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [size](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-721733414%2FProperties%2F-1216412040) | [jvm]<br>open override val [size](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-721733414%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [values](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#269306713%2FProperties%2F-1216412040) | [jvm]<br>open override val [values](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#269306713%2FProperties%2F-1216412040): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
