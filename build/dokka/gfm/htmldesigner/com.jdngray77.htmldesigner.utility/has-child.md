//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.utility](index.md)/[hasChild](has-child.md)

# hasChild

[jvm]\
fun [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html).[hasChild](has-child.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Returns true if the [path](has-child.md) relative to this directory exists.

Can be a multi-level path pointing to any sub-directory or file anywhere in the tree in this directory, as long as the [path](has-child.md) provided is suitable to be appended to the location of this.

#### Return

true if &quot;$[this.path](../[root]/index.md)/[path](has-child.md).exists()&quot;. Also false if this is not a directory.
