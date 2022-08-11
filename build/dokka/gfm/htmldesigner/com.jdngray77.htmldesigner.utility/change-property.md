//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.utility](index.md)/[changeProperty](change-property.md)

# changeProperty

[jvm]\
fun &lt;[R](change-property.md), [T](change-property.md)&gt; [changeProperty](change-property.md)(prop: [KProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)&lt;*&gt;, instance: [R](change-property.md), newValue: [T](change-property.md))

Uses Kotlin reflection to mutate a variable in an object.

## Parameters

jvm

| | |
|---|---|
| prop | the property to mutate. KProperties can be obtained from the list of an object's properties - `X::class.memberProperties` |
