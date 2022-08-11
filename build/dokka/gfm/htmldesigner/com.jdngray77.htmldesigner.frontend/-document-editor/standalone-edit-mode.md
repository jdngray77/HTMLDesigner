//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend](../index.md)/[DocumentEditor](index.md)/[standaloneEditMode](standalone-edit-mode.md)

# standaloneEditMode

[jvm]\
var [standaloneEditMode](standalone-edit-mode.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false

Standalone editor mode.

When true, [selectedTag](selected-tag.md) becomes the only content to be rendered.

if Configs.STANDALONE_MODE_ALIGN_CENTER_BOOL is true, the content will be rendered in the center of the editor.

Cannot be enabled while [selectedTag](selected-tag.md) is null, and clearing selectedTab will clear this flag.
