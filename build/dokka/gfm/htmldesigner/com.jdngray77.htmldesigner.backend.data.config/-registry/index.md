//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.data.config](../index.md)/[Registry](index.md)

# Registry

[jvm]\
open class [Registry](index.md)&lt;[T](index.md)&gt;(val saveLocation: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) : [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[T](index.md), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; 

A HashMap for storing configurable values that are automatically saved to and loaded from disk.

All keys must be entered at initalisation. Keys can be modified later, but not created.

Keys can be anything, but Strings or Enum's are suggested. If using an object as a key, it must have a compatable toString.

#  Key Format

The key format is a SNAKE_CASE breadcrumb, suffixed with the expected rudimentary data type. This is to help keep track of what data is expected.

The breadcrumb can be anything you desire, but use the breadcumb well to organise together configurations relevant to the same thing.

Valid data types are :

- 
   BOOL
- 
   INT
- 
   FLOAT
- 
   DOUBLE
- 
   SHORT
- 
   DOC
- 
   STRING

More data types can be added by adding them to [keyType](-companion/key-type.md), but they must be serializable.

## See also

jvm

| | |
|---|---|
| [com.jdngray77.htmldesigner.backend.data.config.Registry.Companion](-companion/key-type.md) | Examples :<br>PROJECT_AUTOEXPORT_BOOL<br>PROJECT_SKIPVALIDATION_BOOL,<br>PROJECT_BACKUP_DEPTH_INT |
| [com.jdngray77.htmldesigner.backend.data.config.Registry](validate.md) | TODO validation is removed. |

## Parameters

jvm

| | |
|---|---|
| T | The type used for the KEY. |

## Constructors

| | |
|---|---|
| [Registry](-registry.md) | [jvm]<br>fun [Registry](-registry.md)(saveLocation: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [MismatchedTypeException](-mismatched-type-exception/index.md) | [jvm]<br>class [MismatchedTypeException](-mismatched-type-exception/index.md)(key: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)) : [IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html) |
| [MissingEntryException](-missing-entry-exception/index.md) | [jvm]<br>class [MissingEntryException](-missing-entry-exception/index.md)(key: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html) |

## Functions

| Name | Summary |
|---|---|
| [clear](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-257373230%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [clear](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-257373230%2FFunctions%2F-1216412040)() |
| [clone](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#703051458%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [clone](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#703051458%2FFunctions%2F-1216412040)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [compute](index.md#-2029840974%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [compute](index.md#-2029840974%2FFunctions%2F-1216412040)(p0: [T](index.md), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [T](index.md), in [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |
| [computeIfAbsent](index.md#-767883898%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [computeIfAbsent](index.md#-767883898%2FFunctions%2F-1216412040)(p0: [T](index.md), p1: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in [T](index.md), out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [computeIfPresent](index.md#-1985834511%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [computeIfPresent](index.md#-1985834511%2FFunctions%2F-1216412040)(p0: [T](index.md), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [T](index.md), in [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |
| [containsKey](index.md#1942535647%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsKey](index.md#1942535647%2FFunctions%2F-1216412040)(key: [T](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsValue](index.md#-419153938%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsValue](index.md#-419153938%2FFunctions%2F-1216412040)(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [dirty](dirty.md) | [jvm]<br>fun [dirty](dirty.md)() |
| [equals](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#208304826%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [equals](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#208304826%2FFunctions%2F-1216412040)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [flush](flush.md) | [jvm]<br>fun [flush](flush.md)()<br>Saves the current state of the registry to disk in the prior provided [saveLocation](save-location.md) if there are changes to save. |
| [forceFlush](force-flush.md) | [jvm]<br>fun [forceFlush](force-flush.md)()<br>Ignores the [dirty](dirty.md) flag, and saves the registry. |
| [forEach](index.md#-1539625953%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [forEach](index.md#-1539625953%2FFunctions%2F-1216412040)(p0: [BiConsumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiConsumer.html)&lt;in [T](index.md), in [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;) |
| [get](get.md) | [jvm]<br>operator override fun [get](get.md)(key: [T](index.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [getOrDefault](get-or-default.md) | [jvm]<br>~~override~~ ~~fun~~ [~~getOrDefault~~](get-or-default.md)~~(~~key: [T](index.md), defaultValue: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~)~~~~:~~ [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [hashCode](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#612619500%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [hashCode](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#612619500%2FFunctions%2F-1216412040)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#360261660%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [isEmpty](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#360261660%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [load](load.md) | [jvm]<br>fun [load](load.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>(re)loads from disk. |
| [merge](index.md#234919763%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [merge](index.md#234919763%2FFunctions%2F-1216412040)(p0: [T](index.md), p1: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), p2: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), in [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |
| [put](put.md) | [jvm]<br>override fun [put](put.md)(key: [T](index.md), value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html))<br>Enters a value into the registry, if the type matches |
| [putAll](put-all.md) | [jvm]<br>override fun [putAll](put-all.md)(from: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [T](index.md), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;) |
| [putIfAbsent](index.md#-1493944772%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [putIfAbsent](index.md#-1493944772%2FFunctions%2F-1216412040)(p0: [T](index.md), p1: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |
| [remove](index.md#967079143%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [remove](index.md#967079143%2FFunctions%2F-1216412040)(key: [T](index.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?<br>open override fun [remove](index.md#-915336327%2FFunctions%2F-1216412040)(key: [T](index.md), value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replace](index.md#945869069%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [replace](index.md#945869069%2FFunctions%2F-1216412040)(p0: [T](index.md), p1: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?<br>open override fun [replace](index.md#2030158739%2FFunctions%2F-1216412040)(p0: [T](index.md), p1: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), p2: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](index.md#-2131673521%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [replaceAll](index.md#-2131673521%2FFunctions%2F-1216412040)(p0: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [T](index.md), in [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;) |
| [reset](reset.md) | [jvm]<br>fun [reset](reset.md)()<br>Clears the registry, resets all values to defaults, then saves the changes to the disk. |
| [toString](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#434453435%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [toString](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#434453435%2FFunctions%2F-1216412040)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [validate](validate.md) | [jvm]<br>open fun [validate](validate.md)()<br>Invoked so you can check the data within the registry is as you'd expect. |

## Properties

| Name | Summary |
|---|---|
| [autosave](autosave.md) | [jvm]<br>var [autosave](autosave.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>When this flag is high, then the registry is automatically saved when [dirty](dirty.md) is called. |
| [dirty](dirty.md) | [jvm]<br>var [dirty](dirty.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [entries](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#2111837807%2FProperties%2F-1216412040) | [jvm]<br>open override val [entries](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#2111837807%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[T](index.md), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;&gt; |
| [keys](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1479970233%2FProperties%2F-1216412040) | [jvm]<br>open override val [keys](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-1479970233%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[T](index.md)&gt; |
| [saveLocation](save-location.md) | [jvm]<br>val [saveLocation](save-location.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) |
| [size](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-721733414%2FProperties%2F-1216412040) | [jvm]<br>open override val [size](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#-721733414%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [values](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#269306713%2FProperties%2F-1216412040) | [jvm]<br>open override val [values](../../com.jdngray77.htmldesigner.frontend.controls/-flex-justify/index.md#269306713%2FProperties%2F-1216412040): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |

## Inheritors

| Name |
|---|
| [Config](../-config/index.md) |
| [ProjectPreferences](../-project-preferences/index.md) |
