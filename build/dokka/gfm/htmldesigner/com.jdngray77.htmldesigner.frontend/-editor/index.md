//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.frontend](../index.md)/[Editor](index.md)

# Editor

[jvm]\
class [Editor](index.md) : Application

Main entry point to the front end.

Launches the FXML, configures and stores references to the [EDITOR](-companion/-e-d-i-t-o-r.md) and the [mvc](mvc.md).

## Constructors

| | |
|---|---|
| [Editor](-editor.md) | [jvm]<br>fun [Editor](-editor.md)() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [closeProject](close-project.md) | [jvm]<br>fun [closeProject](close-project.md)()<br>Closes the current project & restarts fresh. |
| [exit](exit.md) | [jvm]<br>fun [exit](exit.md)()<br>Requests the application to close. |
| [getParameters](../-splash-screen/index.md#-807279243%2FFunctions%2F-1216412040) | [jvm]<br>fun [getParameters](../-splash-screen/index.md#-807279243%2FFunctions%2F-1216412040)(): Application.Parameters |
| [init](../-splash-screen/index.md#-1813461483%2FFunctions%2F-1216412040) | [jvm]<br>open fun [init](../-splash-screen/index.md#-1813461483%2FFunctions%2F-1216412040)() |
| [notifyPreloader](../-splash-screen/index.md#-1908879305%2FFunctions%2F-1216412040) | [jvm]<br>fun [notifyPreloader](../-splash-screen/index.md#-1908879305%2FFunctions%2F-1216412040)(p0: Preloader.PreloaderNotification) |
| [restart](restart.md) | [jvm]<br>fun [restart](restart.md)()<br>Performs the shutdown routine, then the startup routine. |
| [start](start.md) | [jvm]<br>open override fun [start](start.md)(stage: Stage)<br>Loads and initalises the GUI. |
| [stop](stop.md) | [jvm]<br>~~open~~ ~~override~~ ~~fun~~ [~~stop~~](stop.md)~~(~~~~)~~<br>Interruptable shutdown routine. |

## Properties

| Name | Summary |
|---|---|
| [hostServices](../-splash-screen/index.md#296601023%2FProperties%2F-1216412040) | [jvm]<br>val [hostServices](../-splash-screen/index.md#296601023%2FProperties%2F-1216412040): HostServices |
| [mvc](mvc.md) | [jvm]<br>var [mvc](mvc.md): [MVC](../../com.jdngray77.htmldesigner/-m-v-c/index.md)? = null<br>The model view controller for the IDE. |
| [scene](scene.md) | [jvm]<br>lateinit var [scene](scene.md): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;Scene, [MainViewController](../-main-view-controller/index.md)&gt;<br>A tuple of the JavaFX Scene which hosts the MainView, and it's [MainViewController](../-main-view-controller/index.md). |
| [stage](stage.md) | [jvm]<br>lateinit var [stage](stage.md): Stage<br>The stage that is used to show scenes. |
