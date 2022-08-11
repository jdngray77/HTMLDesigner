//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[BackgroundTask](index.md)/[submit](submit.md)

# submit

[jvm]\

@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)

fun [submit](submit.md)(runnable: [Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)): [Future](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html)&lt;*&gt;?

Submits a [runnable](submit.md) task to the for threadPool for execution.

If the thread pool has been instructed to or is already shutdown, the task will not be submitted.
