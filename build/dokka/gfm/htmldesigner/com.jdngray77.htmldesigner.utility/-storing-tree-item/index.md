//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.utility](../index.md)/[StoringTreeItem](index.md)

# StoringTreeItem

[jvm]\
class [StoringTreeItem](index.md)&lt;[T](index.md)&gt;(val data: [T](index.md)?) : TreeItem&lt;[T](index.md)&gt; 

A TreeItem with exposed and retrievable value.

## Constructors

| | |
|---|---|
| [StoringTreeItem](-storing-tree-item.md) | [jvm]<br>fun &lt;[T](index.md)&gt; [StoringTreeItem](-storing-tree-item.md)(data: [T](index.md)?) |

## Functions

| Name | Summary |
|---|---|
| [addEventHandler](index.md#1673795831%2FFunctions%2F-1216412040) | [jvm]<br>open fun &lt;[E](index.md#1673795831%2FFunctions%2F-1216412040) : Event&gt; [addEventHandler](index.md#1673795831%2FFunctions%2F-1216412040)(p0: EventType&lt;[E](index.md#1673795831%2FFunctions%2F-1216412040)&gt;, p1: EventHandler&lt;[E](index.md#1673795831%2FFunctions%2F-1216412040)&gt;) |
| [buildEventDispatchChain](index.md#852116629%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [buildEventDispatchChain](index.md#852116629%2FFunctions%2F-1216412040)(p0: EventDispatchChain): EventDispatchChain |
| [expandedProperty](index.md#-1655774721%2FFunctions%2F-1216412040) | [jvm]<br>fun [expandedProperty](index.md#-1655774721%2FFunctions%2F-1216412040)(): BooleanProperty |
| [getGraphic](index.md#769943931%2FFunctions%2F-1216412040) | [jvm]<br>fun [getGraphic](index.md#769943931%2FFunctions%2F-1216412040)(): Node |
| [getParent](index.md#-1980375571%2FFunctions%2F-1216412040) | [jvm]<br>fun [getParent](index.md#-1980375571%2FFunctions%2F-1216412040)(): TreeItem&lt;[T](index.md)&gt; |
| [getValue](index.md#-1700693870%2FFunctions%2F-1216412040) | [jvm]<br>fun [getValue](index.md#-1700693870%2FFunctions%2F-1216412040)(): [T](index.md) |
| [graphicProperty](index.md#95342416%2FFunctions%2F-1216412040) | [jvm]<br>fun [graphicProperty](index.md#95342416%2FFunctions%2F-1216412040)(): ObjectProperty&lt;Node&gt; |
| [isExpanded](index.md#651358538%2FFunctions%2F-1216412040) | [jvm]<br>fun [isExpanded](index.md#651358538%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isLeaf](index.md#612947269%2FFunctions%2F-1216412040) | [jvm]<br>open fun [isLeaf](index.md#612947269%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [leafProperty](index.md#424751098%2FFunctions%2F-1216412040) | [jvm]<br>fun [leafProperty](index.md#424751098%2FFunctions%2F-1216412040)(): ReadOnlyBooleanProperty |
| [nextSibling](index.md#1387404318%2FFunctions%2F-1216412040) | [jvm]<br>open fun [nextSibling](index.md#1387404318%2FFunctions%2F-1216412040)(): TreeItem&lt;[T](index.md)&gt;<br>open fun [nextSibling](index.md#-442516312%2FFunctions%2F-1216412040)(p0: TreeItem&lt;[T](index.md)&gt;): TreeItem&lt;[T](index.md)&gt; |
| [parentProperty](index.md#-1687038930%2FFunctions%2F-1216412040) | [jvm]<br>fun [parentProperty](index.md#-1687038930%2FFunctions%2F-1216412040)(): ReadOnlyObjectProperty&lt;TreeItem&lt;[T](index.md)&gt;&gt; |
| [previousSibling](index.md#-1695689694%2FFunctions%2F-1216412040) | [jvm]<br>open fun [previousSibling](index.md#-1695689694%2FFunctions%2F-1216412040)(): TreeItem&lt;[T](index.md)&gt;<br>open fun [previousSibling](index.md#-1645771348%2FFunctions%2F-1216412040)(p0: TreeItem&lt;[T](index.md)&gt;): TreeItem&lt;[T](index.md)&gt; |
| [removeEventHandler](index.md#-336622252%2FFunctions%2F-1216412040) | [jvm]<br>open fun &lt;[E](index.md#-336622252%2FFunctions%2F-1216412040) : Event&gt; [removeEventHandler](index.md#-336622252%2FFunctions%2F-1216412040)(p0: EventType&lt;[E](index.md#-336622252%2FFunctions%2F-1216412040)&gt;, p1: EventHandler&lt;[E](index.md#-336622252%2FFunctions%2F-1216412040)&gt;) |
| [setExpanded](index.md#309986935%2FFunctions%2F-1216412040) | [jvm]<br>fun [setExpanded](index.md#309986935%2FFunctions%2F-1216412040)(p0: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setGraphic](index.md#-2010655587%2FFunctions%2F-1216412040) | [jvm]<br>fun [setGraphic](index.md#-2010655587%2FFunctions%2F-1216412040)(p0: Node) |
| [setValue](index.md#-1898888800%2FFunctions%2F-1216412040) | [jvm]<br>fun [setValue](index.md#-1898888800%2FFunctions%2F-1216412040)(p0: [T](index.md)) |
| [toString](index.md#285808385%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [toString](index.md#285808385%2FFunctions%2F-1216412040)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [valueProperty](index.md#-1139877401%2FFunctions%2F-1216412040) | [jvm]<br>fun [valueProperty](index.md#-1139877401%2FFunctions%2F-1216412040)(): ObjectProperty&lt;[T](index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [children](index.md#17966798%2FProperties%2F-1216412040) | [jvm]<br>val [children](index.md#17966798%2FProperties%2F-1216412040): ObservableList&lt;TreeItem&lt;[T](index.md)&gt;&gt; |
| [data](data.md) | [jvm]<br>val [data](data.md): [T](index.md)? |
