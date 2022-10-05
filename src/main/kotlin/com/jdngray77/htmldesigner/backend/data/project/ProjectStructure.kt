package com.jdngray77.htmldesigner.backend.data.project

import com.jdngray77.htmldesigner.utility.PartiallySerializable
import com.jdngray77.htmldesigner.utility.hasFile
import com.jdngray77.htmldesigner.utility.subFile
import java.io.File
import java.io.IOException
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty


/*
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *
 *
 *                                         _
 *                                        (_)
 *               __      ____ _ _ __ _ __  _ _ __   __ _
 *               \ \ /\ / / _` | '__| '_ \| | '_ \ / _` |
 *                \ V  V / (_| | |  | | | | | | | | (_| |
 *                 \_/\_/ \__,_|_|  |_| |_|_|_| |_|\__, |
 *                                                  __/ |
 *                                                 |___/
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *                  THIS FILE IS SENSITIVE TO CHANGES.
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *                                   _ _   _
 *                ___  ___ _ __  ___(_) |_(_)_   _____
 *               / __|/ _ \ '_ \/ __| | __| \ \ / / _ \
 *               \__ \  __/ | | \__ \ | |_| |\ V /  __/
 *               |___/\___|_| |_|___/_|\__|_| \_/ \___|
 *
 *                _
 *               | |_ ___
 *               | __/ _ \
 *               | || (_) |
 *                \__\___/
 *
 *                     _
 *                 ___| |__   __ _ _ __   __ _  ___  ___
 *                / __| '_ \ / _` | '_ \ / _` |/ _ \/ __|
 *               | (__| | | | (_| | | | | (_| |  __/\__ \
 *                \___|_| |_|\__,_|_| |_|\__, |\___||___/
 *                                       |___/
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *
 *      TRY TO AVOID CHANGING THE SCHEMA OF THIS CLASS IF POSSIBLE.
 *
 *      IT IMPACTS BACKWARDS COMPATABILITY WITH PROJECT LOADING.
 *
 *      CHANGES TO THE SCHEMA OF THIS CLASS WILL MAKE IT IMPOSSIBLE
 *      FOR A USER TO LOAD THEIR PROJECT WHEN THEY UPDATE.
 *
 *      What's OK :
 *
 *          Altering code within an existing function
 *
 *      What breaks compatability :
 *
 *          Literally anything that changes the schema of the class, i.e
 *
 *          - Changing a functions
 *              - return type
 *              - name
 *              - parameters
 *
 *          - Adding new or removing members from the class
 * @Transient
 *              (functions & variables)
 *
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */




/**
 * Creates and wraps the file structure for a project.
 */
class ProjectStructure (

    /**
     * The location on disk containing this project.
     *
     * This must be validated as :
     *  - A folder, not a File
     *  - Does not exist already
     *
     * Then is created automatically when this project is made.
     *
     * Predicted project folder structure :
     *
     * ```
     * myProject
     * | project.designer (This class serialized)
     * |
     * | backups
     * | | 5-6-2022 13:13:13.meta (Historical copies of the above)
     * | | 4-6-2022 13:13:13.meta
     * | | 3-6-2022 13:13:13.meta
     * |
     * | export (The latest export)
     * | | HTML
     * | | | index.html
     * | | CSS
     * | | | styles.css
     * | | JS
     * | | | index.js
     * | | MEDIA
     * | | | dog.jpg
     * ```
     */
    val locationOnDisk: File

) : PartiallySerializable {

    /**
     * @returns a file with a path relative to the root of the project.
     */
    fun subFile(relativePath: String) = locationOnDisk.subFile(relativePath)

    // TODO unit test that there is an instance for every path

    /**
     * The project's HTML directory
     */
    @Transient
    var HTML: File = File("")

    /**
     * The project's javascript directory
     */
    @Transient
    var JS: File = File("")

    /*
     * The project's CSS directory
     */
    @Transient
    var CSS: File = File("")

    /**
     * The project's MEDIA directory
     */
    @Transient
    var MEDIA: File = File("")

    /**
     * The project's PREFABS directory
     */
    @Transient
    var PREFABS: File = File("")

    /**
     * The project's backup directory
     */
    @Transient
    var BACKUP: File = File("")

    /**
     * Directory used to store error and activity logs.
     */
    @Transient
    var LOGS: File = File("")

    /**
     * Checks that are performed on [locationOnDisk]
     * regardless of before creation or after creation.
     *
     * @throws IllegalArgumentException if the [locationOnDisk] is not absolute, or if the [locationOnDisk] is not a directory
     */
    private fun baseLocationChecks() {
        if (!locationOnDisk.isAbsolute)
            throw IllegalArgumentException("Project location cannot be relative. Be precise.")

//        if (!locationOnDisk.isDirectory)
//            throw IllegalArgumentException("Project location is not a directory.")
    }

    /**
     * For after the project has been created,
     * especially with loading.
     *
     * Checks that the file exists, and is a project.
     * If not, we know that the project has been moved.
     *
     * @throws IllegalArgumentException if [locationOnDisk] does not exist, or the meta is missing.
     */
    internal fun validateLocationOnDisk() {
        baseLocationChecks()

        if (!locationOnDisk.exists())
            throw IllegalArgumentException("Project location does not exist. Has it been moved?")

        if (!locationOnDisk.hasFile(PROJECT_PATH_META))
            throw IllegalArgumentException("Project location is missing project meta data. Restore it from a backup, or re-import the project.")


        // Check directories are present
        forEachRootDirectory { property, file ->
            if (!file.exists() || !locationOnDisk.hasFile(file.name))
                throw IllegalArgumentException("${file.name} directory does is missing from the project!")

            file
        }

    }

    /**
     * Checks a desired location for creating a project.
     *
     * Occours before creating a project, to test if the
     * selected location is a good place to create a project.
     *
     * @throws IllegalArgumentException If meta data is missing, or if the location is not an empty directory.
     */
    internal fun checkValidCreationTarget() {
        baseLocationChecks()

        if (locationOnDisk.hasFile(PROJECT_PATH_META))
            throw IllegalArgumentException("There is already a project in this location. Delete it first.")

        if (locationOnDisk.exists() && locationOnDisk.listFiles()?.isNotEmpty() == true)
            throw IllegalArgumentException("Directory must be empty in order to create a new project here.")
    }

    /**
     * Populates file properties in this class after loading from the disk.
     *
     * Ensures that the directory references are relevant to the project's current location
     *
     * i.e if the project has been moved since last load, or loaded on a different computer.
     */
    override fun recreateTransientProperties() {
        forEachRootDirectory { prop, value ->
            locationOnDisk.subFile(
                (Companion::class.members.find {
                    it.name.endsWith(prop.name)
                } as KProperty<String>)
                    .getter.call(this)
            )
        }
    }

    /**
     * Applies a function to every directory val in this class.
     *
     * @param payload The function that accepts the current value, and supplies a new value
     *                  i.e `x = payload(x)`. New value can be old value.
     */
    private fun forEachRootDirectory(payload: (KMutableProperty<File>, File) -> File) {
        Companion::class.members.forEach {
            companionconst ->
            this::class.members
                .filterIsInstance<KMutableProperty<File>>()
                .forEach {
                    it.setter.call(this@ProjectStructure, payload(it, it.getter.call(this@ProjectStructure)))
                }
        }
    }

    /**
     * Creates all root directories for the project.
     *
     * If [locationOnDisk] does not exist, it is created.
     *
     * @throws IOException if unable to write to disk.
     */
    fun createProjectStructure() {
        forEachRootDirectory { kMutableProperty, file ->
            if (!file.exists())
                file.mkdir()
            file
        }
    }

    /**
     * Deletes the project directory and every file & directory and within
     */
    fun deleteEverything() {
        locationOnDisk.deleteRecursively()
        locationOnDisk.delete()
    }

    /**
     * Creates the project structure at project init time.
     */
    init {
        checkValidCreationTarget()
        locationOnDisk.mkdirs()
        recreateTransientProperties()
        createProjectStructure()
    }

    companion object {

        /**
         * The location of the project meta data, relative to the project root.
         */
        const val PROJECT_PATH_META: String = "project.designer"

        /**
         * The location of the project backups, relative to the project root.
         */
        const val PROJECT_PATH_BACKUP: String = ".backup/"

        /**
         * The location of the logs, relative to the project root.
         */
        const val PROJECT_PATH_LOGS: String = ".logs/"

        /**
         * The location of the project css, relative to the project root.
         */
        const val PROJECT_PATH_CSS: String = "CSS/"

        /**
         * The location of the project HTML, relative to the project root.
         */
        const val PROJECT_PATH_HTML: String = "HTML/"

        /**
         * The location of the project javascript, relative to the project root.
         */
        const val PROJECT_PATH_JS: String = "JS/"

        /**
         * The location of the project media, relative to the project root.
         */
        const val PROJECT_PATH_MEDIA: String = "MEDIA/"

        /**
         * The location of prefabricated elements, relative to the root.
         */
        const val PROJECT_PATH_PREFABS: String = "PREFABS/"

        /**
         * Location of the preferences registry.
         */
        const val PROJECT_PATH_PREFERENCES: String = "preferences.registry"

    }
}