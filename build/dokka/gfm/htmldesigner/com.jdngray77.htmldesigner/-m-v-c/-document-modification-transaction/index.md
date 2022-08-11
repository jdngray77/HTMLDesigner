//[htmldesigner](../../../../index.md)/[com.jdngray77.htmldesigner](../../index.md)/[MVC](../index.md)/[DocumentModificationTransaction](index.md)

# DocumentModificationTransaction

[jvm]\
inner class [DocumentModificationTransaction](index.md) : [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;Document&gt; 

When making many changes at once, this can be used to delay the change notifications to after completing all changes.

Make an instance then use `add` or `modified` to queue documents that have been changed (even if they're the same / duplicates).

Once all changes are made, call `finishedModifying`

## Constructors

| | |
|---|---|
| [DocumentModificationTransaction](-document-modification-transaction.md) | [jvm]<br>fun [DocumentModificationTransaction](-document-modification-transaction.md)() |

## Functions

| Name | Summary |
|---|---|
| [add](index.md#-1971985914%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [add](index.md#-1971985914%2FFunctions%2F-1216412040)(element: Document): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>open override fun [add](index.md#-1909757155%2FFunctions%2F-1216412040)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), element: Document) |
| [addAll](index.md#95454663%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [addAll](index.md#95454663%2FFunctions%2F-1216412040)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Document&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>open override fun [addAll](index.md#1753667662%2FFunctions%2F-1216412040)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Document&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [clear](index.md#-24309431%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [clear](index.md#-24309431%2FFunctions%2F-1216412040)() |
| [clone](index.md#936115257%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [clone](index.md#936115257%2FFunctions%2F-1216412040)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [contains](index.md#291792630%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [contains](index.md#291792630%2FFunctions%2F-1216412040)(element: Document): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#-1763718622%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsAll](index.md#-1763718622%2FFunctions%2F-1216412040)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Document&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [ensureCapacity](index.md#-1361101014%2FFunctions%2F-1216412040) | [jvm]<br>open fun [ensureCapacity](index.md#-1361101014%2FFunctions%2F-1216412040)(p0: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [equals](index.md#346489373%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [equals](index.md#346489373%2FFunctions%2F-1216412040)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [finishedModifying](finished-modifying.md) | [jvm]<br>fun [finishedModifying](finished-modifying.md)() |
| [forEach](index.md#1512390743%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [forEach](index.md#1512390743%2FFunctions%2F-1216412040)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in Document&gt;) |
| [get](index.md#1986850878%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [get](index.md#1986850878%2FFunctions%2F-1216412040)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Document |
| [hashCode](index.md#1992679977%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [hashCode](index.md#1992679977%2FFunctions%2F-1216412040)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [indexOf](index.md#-713567010%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [indexOf](index.md#-713567010%2FFunctions%2F-1216412040)(element: Document): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](index.md#996273107%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [isEmpty](index.md#996273107%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.md#204273974%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [iterator](index.md#204273974%2FFunctions%2F-1216412040)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;Document&gt; |
| [lastIndexOf](index.md#-1317163756%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [lastIndexOf](index.md#-1317163756%2FFunctions%2F-1216412040)(element: Document): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [listIterator](index.md#-1172936520%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [listIterator](index.md#-1172936520%2FFunctions%2F-1216412040)(): [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;Document&gt;<br>open override fun [listIterator](index.md#1119542358%2FFunctions%2F-1216412040)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;Document&gt; |
| [modified](modified.md) | [jvm]<br>fun [modified](modified.md)(document: Document): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [parallelStream](index.md#-1592339412%2FFunctions%2F-1216412040) | [jvm]<br>open fun [parallelStream](index.md#-1592339412%2FFunctions%2F-1216412040)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Document&gt; |
| [remove](index.md#374721777%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [remove](index.md#374721777%2FFunctions%2F-1216412040)(element: Document): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](index.md#1645082098%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [removeAll](index.md#1645082098%2FFunctions%2F-1216412040)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Document&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAt](index.md#-2012790965%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [removeAt](index.md#-2012790965%2FFunctions%2F-1216412040)(p0: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Document |
| [removeIf](index.md#1275057519%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [removeIf](index.md#1275057519%2FFunctions%2F-1216412040)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in Document&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](index.md#-1808076569%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [replaceAll](index.md#-1808076569%2FFunctions%2F-1216412040)(p0: [UnaryOperator](https://docs.oracle.com/javase/8/docs/api/java/util/function/UnaryOperator.html)&lt;Document&gt;) |
| [retainAll](index.md#-907637807%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [retainAll](index.md#-907637807%2FFunctions%2F-1216412040)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Document&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [set](index.md#-371511458%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [set](index.md#-371511458%2FFunctions%2F-1216412040)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), element: Document): Document |
| [sort](index.md#-1366046179%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [sort](index.md#-1366046179%2FFunctions%2F-1216412040)(p0: [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;in Document&gt;) |
| [spliterator](index.md#1642634169%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [spliterator](index.md#1642634169%2FFunctions%2F-1216412040)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;Document&gt; |
| [stream](index.md#135225651%2FFunctions%2F-1216412040) | [jvm]<br>open fun [stream](index.md#135225651%2FFunctions%2F-1216412040)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Document&gt; |
| [subList](index.md#-1861460857%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [subList](index.md#-1861460857%2FFunctions%2F-1216412040)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Document&gt; |
| [toArray](index.md#776741336%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [toArray](index.md#776741336%2FFunctions%2F-1216412040)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;<br>open override fun &lt;[T](index.md#-268358819%2FFunctions%2F-1216412040) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [toArray](index.md#-268358819%2FFunctions%2F-1216412040)(p0: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-268358819%2FFunctions%2F-1216412040)&gt;): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-268358819%2FFunctions%2F-1216412040)&gt;<br>~~open~~ ~~fun~~ ~~&lt;~~[T](index.md#-1215154575%2FFunctions%2F-1216412040) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](index.md#-1215154575%2FFunctions%2F-1216412040)~~(~~p0: [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F-1216412040)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F-1216412040)&gt; |
| [toString](index.md#-42557405%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [toString](index.md#-42557405%2FFunctions%2F-1216412040)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [trimToSize](index.md#130278918%2FFunctions%2F-1216412040) | [jvm]<br>open fun [trimToSize](index.md#130278918%2FFunctions%2F-1216412040)() |

## Properties

| Name | Summary |
|---|---|
| [size](index.md#1363994755%2FProperties%2F-1216412040) | [jvm]<br>open override val [size](index.md#1363994755%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
