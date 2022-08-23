
/*░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
 ░                                                                                                ░
 ░ Jordan T. Gray's                                                                               ░
 ░                                                                                                ░
 ░          HTML Designer                                                                         ░
 ░                                                                                                ░
 ░ FOSS 2022.                                                                                     ░
 ░ License decision pending.                                                                      ░
 ░                                                                                                ░
 ░ https://www.github.com/jdngray77/HTMLDesigner/                                                 ░
 ░ https://www.jordantgray.uk                                                                     ░
 ░                                                                                                ░
 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

package com.jdngray77.htmldesigner.utility

import javafx.scene.paint.Color
import java.io.*

/**
 * Saves [this] to the given path via Java's
 * object serialization.
 */
fun Serializable.saveObjectToDisk(path: String) =
    saveObjectToDisk(File(path))

/**
 * Saves [this] to the given file via Java's
 * object serialization.
 */
fun Serializable.saveObjectToDisk(file: File) {
    file.createNewFile()

    val fos = FileOutputStream(file)
    val os = ObjectOutputStream(fos)

    os.writeObject(this)

    os.close()
    fos.close()
}

/**
 * Loads an object serialized with Java's object serialization
 * from disk.
 */
fun loadObjectFromDisk(f: File): Any? {
    val fos = FileInputStream(f)
    val os = ObjectInputStream(fos)
    return os.readObject().also {
        os.close()
        fos.close()
    }
}

/**
 * JavaFX Colors aren't serializable, so this wraps the
 * rgba to serialize and re-create them.
 */
class SerializableColor (

    val red: Double,

    val green: Double,

    val blue: Double,

    val opacity: Double = 1.0

) : Serializable {

    @Transient
    private var color: Color? = null

    fun get() : Color {
        if (color == null)
            color = Color(red, green, blue, opacity)

        return color!!
    }

}

fun Color.toSerializable() = SerializableColor(red, green, blue, opacity)