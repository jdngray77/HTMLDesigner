//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend](../index.md)/[DocumentEditor](index.md)/[initialize](initialize.md)

# initialize

[jvm]\
fun [initialize](initialize.md)()

Late 'init' called by FXML.

Kotlin init is too early. The GUI and @FXML lateinits won't have been created yet.

This automatically the tab in the main GUI, and places this [DocumentEditor](index.md) into it, and configures the teardown logic.

N.B During initalization, there is no document yet.

The editor is created in MVC.openEditor. The document is also set here.
