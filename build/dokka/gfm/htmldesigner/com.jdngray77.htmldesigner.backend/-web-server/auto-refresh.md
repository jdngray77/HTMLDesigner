//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[WebServer](index.md)/[autoRefresh](auto-refresh.md)

# autoRefresh

[jvm]\
~~var~~ [~~autoRefresh~~](auto-refresh.md)~~:~~ [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) ~~=~~ ~~true~~

Determines whether the browser should automatically refresh the page.

When true, REFRESH is injected into the document when it's served.

N.B There's a CheckMenuItem in the menu bar whose isSelected will fall out of sync with [autoRefresh](auto-refresh.md) if this is used. This isn't crutial, but the check mark shown to the user will be made unreliable.
