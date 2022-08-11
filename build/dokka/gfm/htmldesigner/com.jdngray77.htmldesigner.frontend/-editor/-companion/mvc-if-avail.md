//[htmldesigner](../../../../index.md)/[com.jdngray77.htmldesigner.frontend](../../index.md)/[Editor](../index.md)/[Companion](index.md)/[mvcIfAvail](mvc-if-avail.md)

# mvcIfAvail

[jvm]\
fun [mvcIfAvail](mvc-if-avail.md)(): [MVC](../../../com.jdngray77.htmldesigner/-m-v-c/index.md)?

Returns the [mvc](mvc.md), if it is available.

Slower, but adds null safety where it's required.

For use in places where the access is optional, and calls may be made early - but it doesn't matter if the mvc does not exist yet.
