//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.utility](index.md)/[readPrivateProperty](read-private-property.md)

# readPrivateProperty

[jvm]\
fun &lt;[T](read-private-property.md)&gt; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html).[readPrivateProperty](read-private-property.md)(superClass: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;, propertyName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](read-private-property.md)

Returns the value of a property in this object's supers, that you wouldn't otherwise be allowed to access.

Sometimes there's a perfectly good reason to read a value from a class you're extending, but you can't because it's private. That's what this is for.

## Parameters

jvm

| | |
|---|---|
| superClass | The class of the super you want to retrieve the value from.     Must be the KClass of a class in your extension hierarchy.     This may just be the class you're extending, or it may be higher,     like the super's super. |
