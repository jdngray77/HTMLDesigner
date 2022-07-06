
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

package com.jdngray77.htmldesigner.backend.utility

import java.io.File
import java.nio.file.Files

/**
 * Returns true if a direct child of this directory
 * has the [_name].
 *
 * Cannot search subdirectories.
 *
 * Returns false if [this] is not a directory
 */
fun File.hasFile(_name : String) =
    this.isDirectory && this.listFiles { _, name -> name == _name }?.isNotEmpty() == true

/**
 * Returns true if the [path] relative to this directory exists.
 *
 * Can be a multi-level path pointing to any sub-directory or file
 * anywhere in the tree in this directory, as long as the [path] provided
 * is suitable to be appended to the location of [this].
 *
 * @return true if "$[this.path]/[path].exists()". Also false if [this] is not a directory.
 **/
fun File.hasChild(path: String) =
    this.isDirectory && File(this.toPath().toString() + "/" + path).exists()

/**
 * Ensures that the file exists on disk, weather it be a
 * file or a directory.
 *
 * @return true if file exists after operation.
 */
fun File.assertExists(): Boolean {
    if (isDirectory)
        mkdirs()

    if (isFile && !exists())
        createNewFile()
    return exists()
}

fun File.subFile(subpath : String) =
    File("$path/$subpath")

/**
 * Requires that the file exists on disk, but does not create it
 * if it doesn't.
 *
 * For automatic file creation,
 * @see File.assertExists
 *
 * @throws NoSuchFileException if it does not exist, but is supposed to.
 */
fun File.requireExists(): File {
    if (!exists())
        throw NoSuchFileException(this, reason = "project document is missing!")

    return this
}

/**
 * Returns a list of every file in the tree
 * of this directory, including sub-directories.
 *
 * Return list is empty if [this] is not a directory.
 */
fun File.flattenTree(): ArrayList<File> {
    val x = ArrayList<File>()
    if (!isDirectory) return x

    Files.walk(this.toPath())
        .filter(Files::isRegularFile)
        .forEach{ x.add(it.toFile()) }
    return x
}