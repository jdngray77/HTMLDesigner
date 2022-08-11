//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[BackgroundTask](index.md)/[scheduledTasks](scheduled-tasks.md)

# scheduledTasks

[jvm]\
fun [scheduledTasks](scheduled-tasks.md)(): [BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)&lt;[Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)&gt;

Returns all tasks waiting for execution on a worker [Thread](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html) in the threadPool.

#### Return

Tasks waiting in the threadPool queue for execution.
