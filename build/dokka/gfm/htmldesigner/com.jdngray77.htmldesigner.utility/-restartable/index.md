//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.utility](../index.md)/[Restartable](index.md)

# Restartable

[jvm]\
interface [Restartable](index.md)

When the IDE restarts, static systems are not unloaded, as the JVM is still running.

Only instances of objects created within the Editor are unloaded and re-created.

This is for the cases where static systems or objects that need to clean up when the IDE:

- 
   unloads a project,
- 
   shuts down
- 
   or restarts.

When implemented, [restart](restart.md) will automatically be invoked when the IDE's Editor.stop is invoked.

Instances are located and invoked using reflection, so there no need to invoke or subscribe.

#### Author

Jordan T. Gray

## Functions

| Name | Summary |
|---|---|
| [restart](restart.md) | [jvm]<br>abstract fun [restart](restart.md)() |

## Inheritors

| Name |
|---|
| [BackgroundTask](../../com.jdngray77.htmldesigner.backend/-background-task/index.md) |
| [EventNotifier](../../com.jdngray77.htmldesigner.backend/-event-notifier/index.md) |
| [WebServer](../../com.jdngray77.htmldesigner.backend/-web-server/index.md) |
