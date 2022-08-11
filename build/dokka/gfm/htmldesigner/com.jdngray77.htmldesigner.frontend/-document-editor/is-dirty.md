//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend](../index.md)/[DocumentEditor](index.md)/[isDirty](is-dirty.md)

# isDirty

[jvm]\
var [isDirty](is-dirty.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false

When raised, this flag indicates that this editor has been changed.

Marked high when [documentChanged](document-changed.md) is called, and remains so until [document](document.md) is saved via [save](save.md)
