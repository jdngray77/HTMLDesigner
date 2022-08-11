//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.data.config](../index.md)/[Registry](index.md)/[autosave](autosave.md)

# autosave

[jvm]\
var [autosave](autosave.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false

When this flag is high, then the registry is automatically saved when [dirty](dirty.md) is called.

This is low for initalisation and loading, but automatically raised once the registry is ready for use.
