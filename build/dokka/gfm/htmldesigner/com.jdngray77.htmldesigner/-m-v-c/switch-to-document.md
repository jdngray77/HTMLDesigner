//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner](../index.md)/[MVC](index.md)/[switchToDocument](switch-to-document.md)

# switchToDocument

[jvm]\
fun [switchToDocument](switch-to-document.md)(document: Document): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)

&quot;If there's an editor for the given document, switch to it, otherwise create a new editor.&quot;

The first line of the function is a call to the function findEditorFor, which returns an Editor?. If it's not null, the apply function is called on it. The apply function takes a lambda as its argument, and the lambda is executed with the Editor as its receiver. The lambda in this case is a call to the function switchToEditor, which takes an Editor as its argument

## Parameters

jvm

| | |
|---|---|
| document | The document to switch to. |
