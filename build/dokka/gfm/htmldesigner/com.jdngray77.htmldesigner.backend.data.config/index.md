//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.backend.data.config](index.md)

# Package com.jdngray77.htmldesigner.backend.data.config

## Types

| Name | Summary |
|---|---|
| [Config](-config/index.md) | [jvm]<br>object [Config](-config/index.md) : [Registry](-registry/index.md)&lt;[Configs](-configs/index.md)&gt; <br>The IDE specific registry. |
| [Configs](-configs/index.md) | [jvm]<br>enum [Configs](-configs/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Configs](-configs/index.md)&gt; <br>Keys for the config registry. |
| [ProjectPreference](-project-preference/index.md) | [jvm]<br>enum [ProjectPreference](-project-preference/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ProjectPreference](-project-preference/index.md)&gt; <br>Keys for the [ProjectPreferences](-project-preferences/index.md) registry. |
| [ProjectPreferences](-project-preferences/index.md) | [jvm]<br>class [ProjectPreferences](-project-preferences/index.md)(project: [Project](../com.jdngray77.htmldesigner.backend.data/-project/index.md)) : [Registry](-registry/index.md)&lt;[ProjectPreference](-project-preference/index.md)&gt; <br>A registry used to store project-specific preferences. |
| [Registry](-registry/index.md) | [jvm]<br>open class [Registry](-registry/index.md)&lt;[T](-registry/index.md)&gt;(val saveLocation: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) : [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[T](-registry/index.md), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; <br>A HashMap for storing configurable values that are automatically saved to and loaded from disk. |
