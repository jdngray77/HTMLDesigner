//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend](../index.md)/[Editor](index.md)/[restart](restart.md)

# restart

[jvm]\
fun [restart](restart.md)()

Performs the shutdown routine, then the startup routine.

Just be aware that this is a soft restart, so only non-static things are reloaded.

Static classes in the JVM persist. For cases where this matters, see [Restartable](../../com.jdngray77.htmldesigner.utility/-restartable/index.md).
