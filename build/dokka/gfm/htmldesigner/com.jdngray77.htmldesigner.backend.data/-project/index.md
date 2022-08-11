//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.data](../index.md)/[Project](index.md)

# Project

[jvm]\
class [Project](index.md)(val locationOnDisk: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), _author: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) : [Serializable](https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html)

A HTML Designer project.

A midpoint between the files on the disk and the data.

Stores paths to files which *should* exist on the disk, and fetches them when requested.

This object is serialized to disk as `project.designer`.

#### Author

Jordan Gray

## See also

jvm

| | |
|---|---|
| [com.jdngray77.htmldesigner.backend.data.Project](location-on-disk.md) | for project file structure. |

## Constructors

| | |
|---|---|
| [Project](-project.md) | [jvm]<br>fun [Project](-project.md)(locationOnDisk: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), _author: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |
| [UnloadedDocumentException](-unloaded-document-exception/index.md) | [jvm]<br>class [UnloadedDocumentException](-unloaded-document-exception/index.md)(val d: Document) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html) |

## Functions

| Name | Summary |
|---|---|
| [backup](backup.md) | [jvm]<br>fun [backup](backup.md)()<br>Takes a copy of the project files and the meta into [PROJECT_PATH_BACKUP](-companion/-p-r-o-j-e-c-t_-p-a-t-h_-b-a-c-k-u-p.md). |
| [createDocument](create-document.md) | [jvm]<br>fun [createDocument](create-document.md)(subpath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): Document<br>Creates a new HTML document in this project, and saves it to the disk. |
| [deleteFile](delete-file.md) | [jvm]<br>fun [deleteFile](delete-file.md)(projectFile: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html))<br>Deletes a file on disk then [validateCache](validate-cache.md) |
| [documents](documents.md) | [jvm]<br>fun [documents](documents.md)(): [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)&gt;<br>Returns an array of [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)s containing [HTML](-h-t-m-l.md) documents in the project. |
| [fileForDocument](file-for-document.md) | [jvm]<br>fun [fileForDocument](file-for-document.md)(d: Document): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>Finds this document in the CACHE, and returns the file that it was loaded from. |
| [getCache](get-cache.md) | [jvm]<br>~~fun~~ [~~getCache~~](get-cache.md)~~(~~~~)~~~~:~~ [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
| [javascripts](javascripts.md) | [jvm]<br>fun [javascripts](javascripts.md)(): [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)&gt;<br>Returns an array of [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)s containing [JS](-j-s.md) documents in the project. |
| [loadDocument](load-document.md) | [jvm]<br>fun [loadDocument](load-document.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)): Document<br>Fetches an existing document from the disk |
| [logError](log-error.md) | [jvm]<br>fun [logError](log-error.md)(e: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html))<br>Saves the stacktrace of [e](log-error.md) in the [PROJECT_PATH_LOGS](-companion/-p-r-o-j-e-c-t_-p-a-t-h_-l-o-g-s.md) folder. |
| [media](media.md) | [jvm]<br>fun [media](media.md)(): [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)&gt;<br>Returns an array of [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)s containing [MEDIA](-m-e-d-i-a.md) documents in the project. |
| [projectName](project-name.md) | [jvm]<br>fun [projectName](project-name.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this project, as inherited by the project's directory name. |
| [reloadOpenFilesFromDisk](reload-open-files-from-disk.md) | [jvm]<br>fun [reloadOpenFilesFromDisk](reload-open-files-from-disk.md)()<br>TODO request to reset cache / reload all documents from disk     How to handle dirty files here update open editors |
| [removeFromCache](remove-from-cache.md) | [jvm]<br>inline fun &lt;[T](remove-from-cache.md)&gt; [removeFromCache](remove-from-cache.md)(any: [T](remove-from-cache.md)) |
| [renameOrMoveProject](rename-or-move-project.md) | [jvm]<br>~~fun~~ [~~renameOrMoveProject~~](rename-or-move-project.md)~~(~~destination: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)~~)~~~~:~~ [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>renames [locationOnDisk](location-on-disk.md) |
| [saveDocument](save-document.md) | [jvm]<br>fun [saveDocument](save-document.md)(d: Document)<br>Overwrites a project document with [d](save-document.md).<br>[jvm]<br>fun [saveDocument](save-document.md)(d: Document, path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [saveMeta](save-meta.md) | [jvm]<br>@[Synchronized](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-synchronized/index.html)<br>fun [saveMeta](save-meta.md)()<br>Saves the 'project.designer' file containing meta data about the project. |
| [stylesheets](stylesheets.md) | [jvm]<br>fun [stylesheets](stylesheets.md)(): [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)&gt;<br>Returns an array of [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)s containing [CSS](-c-s-s.md) documents in the project. |
| [subFile](sub-file.md) | [jvm]<br>~~fun~~ [~~subFile~~](sub-file.md)~~(~~subpath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~~~:~~ [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>Returns a file relative to the root of the project, so that it can be used to point to sub dirs and files. |
| [subPath](sub-path.md) | [jvm]<br>fun [subPath](sub-path.md)(subpath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns the path of the root of the project, so that it can be used to point to sub dirs and files. |
| [validate](validate.md) | [jvm]<br>fun [validate](validate.md)()<br>Checks the folder structure on disk. |
| [validateCache](validate-cache.md) | [jvm]<br>fun [validateCache](validate-cache.md)()<br>Removes any files that no-longer exist on disk from the CACHE |

## Properties

| Name | Summary |
|---|---|
| [author](author.md) | [jvm]<br>var [author](author.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>Name of the person or organisation that created this project. |
| [BACKUP](-b-a-c-k-u-p.md) | [jvm]<br>val [BACKUP](-b-a-c-k-u-p.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The project's backup directory |
| [createdOn](created-on.md) | [jvm]<br>val [createdOn](created-on.md): [Date](https://docs.oracle.com/javase/8/docs/api/java/util/Date.html)<br>The date this project was created. |
| [CSS](-c-s-s.md) | [jvm]<br>val [CSS](-c-s-s.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The project's CSS directory |
| [HTML](-h-t-m-l.md) | [jvm]<br>val [HTML](-h-t-m-l.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The project's HTML directory |
| [JS](-j-s.md) | [jvm]<br>val [JS](-j-s.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The project's javascript directory |
| [locationOnDisk](location-on-disk.md) | [jvm]<br>val [locationOnDisk](location-on-disk.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The location on disk containing this project. |
| [MEDIA](-m-e-d-i-a.md) | [jvm]<br>val [MEDIA](-m-e-d-i-a.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The project's MEDIA directory |
| [PREFABS](-p-r-e-f-a-b-s.md) | [jvm]<br>val [PREFABS](-p-r-e-f-a-b-s.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)<br>The project's PREFABS directory |
| [PREFERENCES](-p-r-e-f-e-r-e-n-c-e-s.md) | [jvm]<br>@[Transient](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-transient/index.html)<br>lateinit var [PREFERENCES](-p-r-e-f-e-r-e-n-c-e-s.md): [ProjectPreferences](../../com.jdngray77.htmldesigner.backend.data.config/-project-preferences/index.md) |
