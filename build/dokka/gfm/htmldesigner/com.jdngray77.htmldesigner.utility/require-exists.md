//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.utility](index.md)/[requireExists](require-exists.md)

# requireExists

[jvm]\
fun [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html).[requireExists](require-exists.md)(): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)

Requires that the file exists on disk, but does not create it if it doesn't.

For automatic file creation,

## See also

jvm

| | |
|---|---|
| [File.assertExists](assert-exists.md) |  |

## Throws

| | |
|---|---|
| [kotlin.io.NoSuchFileException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/-no-such-file-exception/index.html) | if it does not exist, but is supposed to. |
