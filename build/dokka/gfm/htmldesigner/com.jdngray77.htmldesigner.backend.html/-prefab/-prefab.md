//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.html](../index.md)/[Prefab](index.md)/[Prefab](-prefab.md)

# Prefab

[jvm]\
fun [Prefab](-prefab.md)(subPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), element: Element? = null)

Creates [locationOnDisk](location-on-disk.md) relative to Project.PREFABS with subPath

[jvm]\
fun [Prefab](-prefab.md)(locationOnDisk: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), element: Element? = null)

Saves element to locationOnDisk.     However, If no element is provided, locationOnDisk is used to load, instead.

Setting new content into element automatically saves prefab to disk.
