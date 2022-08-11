//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.html](../index.md)/[Prefab](index.md)

# Prefab

[jvm]\
class [Prefab](index.md)(val locationOnDisk: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), element: Element? = null)

A wrapper to read and write elements to disk.

This creates a way to design an element, then save it for later use.

## Constructors

| | |
|---|---|
| [Prefab](-prefab.md) | [jvm]<br>fun [Prefab](-prefab.md)(subPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), element: Element? = null)<br>Creates [locationOnDisk](location-on-disk.md) relative to Project.PREFABS with subPath |
| [Prefab](-prefab.md) | [jvm]<br>fun [Prefab](-prefab.md)(locationOnDisk: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), element: Element? = null)<br>Saves element to locationOnDisk.     However, If no element is provided, locationOnDisk is used to load, instead. |

## Functions

| Name | Summary |
|---|---|
| [id](id.md) | [jvm]<br>fun [id](id.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [replaceSavedElement](replace-saved-element.md) | [jvm]<br>fun [replaceSavedElement](replace-saved-element.md)(value: Element)<br>This replaces anything already saved to [locationOnDisk](location-on-disk.md) with the new [element](element.md) |

## Properties

| Name | Summary |
|---|---|
| [element](element.md) | [jvm]<br>lateinit var [element](element.md): Element<br>The element in this prefab. |
| [locationOnDisk](location-on-disk.md) | [jvm]<br>val [locationOnDisk](location-on-disk.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The file where this [element](element.md) will be saved |
