//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.data](../index.md)/[Project](index.md)/[validate](validate.md)

# validate

[jvm]\
fun [validate](validate.md)()

Checks the folder structure on disk.

[locationOnDisk](location-on-disk.md) must contain the correct subdirectories.

Also initalises CACHE, if it is not already. since CACHE is used by IO operations, this has to be called before any are attempted.

If not

## Throws

| | |
|---|---|
| [kotlin.IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) |  |
