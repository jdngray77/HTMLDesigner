//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[ExceptionListener](index.md)

# ExceptionListener

[jvm]\
object [ExceptionListener](index.md) : [Thread.UncaughtExceptionHandler](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.UncaughtExceptionHandler.html)

Added to the main thread, this listener saves log files and displays notifications for unhandled exceptions.

## Functions

| Name | Summary |
|---|---|
| [uncaughtException](uncaught-exception.md) | [jvm]<br>open override fun [uncaughtException](uncaught-exception.md)(t: [Thread](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html)?, e: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?) |
