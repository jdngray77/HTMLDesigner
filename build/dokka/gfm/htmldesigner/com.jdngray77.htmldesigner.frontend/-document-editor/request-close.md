//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend](../index.md)/[DocumentEditor](index.md)/[requestClose](request-close.md)

# requestClose

[jvm]\
fun [requestClose](request-close.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Asks this editor to close.

Identical to the user clicking the 'x'.

Performs tab.onCloseRequest, and if the user is happy to close, [forceClose](force-close.md) is used to close the tab.

#### Return

true if tab closed, false if request was refused.
