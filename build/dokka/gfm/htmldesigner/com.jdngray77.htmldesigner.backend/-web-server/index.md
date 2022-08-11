//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[WebServer](index.md)

# WebServer

[jvm]\
object [WebServer](index.md) : [Subscriber](../-subscriber/index.md), [Restartable](../../com.jdngray77.htmldesigner.utility/-restartable/index.md)

A (Live-ish) web server for development and debugging.

Hosts the current document, so it can be viewed in a real-world web browser.

Generally the only user-feedback of this system is some notifications show at [start](start.md) and [stop](stop.md).

#### Author

Jordan Gray

## Functions

| Name | Summary |
|---|---|
| [notify](notify.md) | [jvm]<br>open override fun [notify](notify.md)(e: [EventType](../-event-type/index.md))<br>Updates the page being served. |
| [restart](restart.md) | [jvm]<br>open override fun [restart](restart.md)()<br>Shuts down the server when the IDE is restarted. |
| [serve](serve.md) | [jvm]<br>fun [serve](serve.md)(e: Document?)<br>Changes the document that is being served. |
| [start](start.md) | [jvm]<br>fun [start](start.md)()<br>Boots the server. |
| [stop](stop.md) | [jvm]<br>fun [stop](stop.md)()<br>Stops the server. |
| [toggleAutoRefresh](toggle-auto-refresh.md) | [jvm]<br>~~fun~~ [~~toggleAutoRefresh~~](toggle-auto-refresh.md)~~(~~~~)~~<br>Toggles [autoRefresh](auto-refresh.md) |

## Properties

| Name | Summary |
|---|---|
| [autoRefresh](auto-refresh.md) | [jvm]<br>~~var~~ [~~autoRefresh~~](auto-refresh.md)~~:~~ [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) ~~=~~ ~~true~~<br>Determines whether the browser should automatically refresh the page. |
| [DOCUMENT_SERVING](-d-o-c-u-m-e-n-t_-s-e-r-v-i-n-g.md) | [jvm]<br>var [DOCUMENT_SERVING](-d-o-c-u-m-e-n-t_-s-e-r-v-i-n-g.md): Document? = null<br>The document that is being served, if any. |
