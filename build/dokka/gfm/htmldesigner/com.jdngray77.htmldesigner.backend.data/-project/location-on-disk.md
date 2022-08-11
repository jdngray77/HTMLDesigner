//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend.data](../index.md)/[Project](index.md)/[locationOnDisk](location-on-disk.md)

# locationOnDisk

[jvm]\
val [locationOnDisk](location-on-disk.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)

The location on disk containing this project.

This must be validated as :

- 
   A folder, not a File
- 
   Does not exist already

Then is created automatically when this project is made.

Predicted project folder structure :

```kotlin
myProject
| project.designer (This class serialized)
|
| backups
| | 5-6-2022 13:13:13.meta (Historical copies of the above)
| | 4-6-2022 13:13:13.meta
| | 3-6-2022 13:13:13.meta
|
| export (The latest export)
| | HTML
| | | index.html
| | CSS
| | | styles.css
| | JS
| | | index.js
| | MEDIA
| | | dog.jpg
```
