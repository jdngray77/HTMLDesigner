//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[BackgroundTask](index.md)

# BackgroundTask

[jvm]\
object [BackgroundTask](index.md) : [Subscriber](../-subscriber/index.md), [Restartable](../../com.jdngray77.htmldesigner.utility/-restartable/index.md)

##  Executes tasks in the background

Utilises a threadPool that is responsible for the creation, management and disposal of application [Thread](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html)'s that delegates tasks to a pool of worker threads for execution.

#### Author

Dylan Brand

## Functions

| Name | Summary |
|---|---|
| [notify](notify.md) | [jvm]<br>open override fun [notify](notify.md)(e: [EventType](../-event-type/index.md)) |
| [restart](restart.md) | [jvm]<br>open override fun [restart](restart.md)() |
| [scheduledTasks](scheduled-tasks.md) | [jvm]<br>fun [scheduledTasks](scheduled-tasks.md)(): [BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)&lt;[Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)&gt;<br>Returns all tasks waiting for execution on a worker [Thread](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html) in the threadPool. |
| [submit](submit.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [submit](submit.md)(runnable: [Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)): [Future](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html)&lt;*&gt;?<br>Submits a [runnable](submit.md) task to the for threadPool for execution. |
| [submitToUI](submit-to-u-i.md) | [jvm]<br>fun [submitToUI](submit-to-u-i.md)(runnable: [Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)) |
