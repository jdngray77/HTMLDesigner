package com.jdngray77.htmldesigner.backend.data

import com.jdngray77.htmldesigner.*
import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.html.dom.Tag
import com.jdngray77.htmldesigner.backend.html.style.StyleSheet
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.IOException
import java.io.InvalidClassException
import java.sql.Time
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap

/*
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *      TRY TO AVOID CHANGING THE SCHEMA OF THIS CLASS IF POSSIBLE.
 *
 *      IT IMPACTS BACKWARDS COMPATABILITY WITH PROJECT LOADING.
 *
 *      CHANGES TO THE SCHEMA OF THIS CLASS WILL MAKE IT IMPOSSIBLE
 *      FOR THE EDITOR TO LOAD PROJECTS CREATED BEFORE THE CHANGE.
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */






/**
 * A HTML Designer project.
 *
 * A midpoint between the files on the disk
 * and the data.
 *
 * Stores paths to files which *should* exist on the disk,
 * and fetches them when requested.
 *
 * This object is serialized to disk as `project.designer`.
 *
 * @see locationOnDisk for project file structure.
 */
class Project(

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
    val locationOnDisk: File,

    _author: String? = null

) : java.io.Serializable {

    /**
     * Name of the person or organisation
     * that created this project.
     *
     * Save
     */
    var author: String? = _author
        set(value) {
            field = value
            saveMeta()
        }

    /**
     * The date this project was created.
     */
    val createdOn = Date.from(Instant.now())

    /**
     * The project's HTML directory
     */
    val HTML = File(subPath(PROJECT_PATH_HTML))

    /**
     * The project's javascript directory
     */
    val JS = File(subPath(PROJECT_PATH_JS))

    /**
     * The project's CSS directory
     */
    val CSS = File(subPath(PROJECT_PATH_CSS))

    /**
     * The project's MEDIA directory
     */
    val MEDIA = File(subPath(PROJECT_PATH_MEDIA))

    /**
     * The project's backup directory
     */
    val BACKUP = File(subPath(PROJECT_PATH_BACKUP))

    /**
     * Storage of any document file after load.
     *
     * TODO cache JS, media, etc.
     */
    @Transient
    private lateinit var CACHE : HashMap<String, Any>

    @Deprecated("This is for debug access only. Cache is internal to the project only.")
    fun getCache() = CACHE

    init {
        checkPath()
        createSkeleton()
        createDocument("index.html")
        validate()
        logStatus("Created new project '${locationOnDisk.name}'")
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                IO
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Checks the folder structure,
     */
    fun validate() {
        with (locationOnDisk) {
            if (
                !hasChild(PROJECT_PATH_HTML)    ||
                !hasChild(PROJECT_PATH_CSS)     ||
                !hasChild(PROJECT_PATH_MEDIA)   ||
                !hasChild(PROJECT_PATH_BACKUP)  ||
                !hasChild(PROJECT_PATH_JS)
            ) throw IllegalStateException("A project folder has gone missing") // TODO create it or prompt to restore backup

//            pagePaths.forEach {
//                if (!File(it).exists())
//                    throw IllegalStateException("$it has gone missing!")
//            }
        }

        if (!this::CACHE.isInitialized)
            CACHE = HashMap()
        else validateCache()
    }

    fun validateCache() {
        val toRemove = ArrayList<String>()

        CACHE.entries.forEach {
            if (!File(it.key).exists())
                toRemove.add(it.key)
        }

        toRemove.map { CACHE.remove(it) }
    }


    /**
     * Checks to see if [locationOnDisk]
     * is a valid path to create a project.
     *
     * If [locationOnDisk] exists and is empty,
     * then the check passes.
     *
     * @throws IllegalArgumentException if directory is not empty / a project already exists.
     */
    private fun checkPath() {
        with(locationOnDisk) {
            if (exists()) {
                if (hasFile(PROJECT_PATH_META))
                    throw IllegalArgumentException("A project already exists here. Load it instead, or delete it if you want to overwrite it.")
                else
                    if (listFiles()?.isNotEmpty() == true)
                        throw IllegalArgumentException("Directory must be empty in order to create a new project here.")
            }
        }
    }

    /**
     * Creates the folder structure for a
     * new project on the disk under [locationOnDisk]
     *
     * If [locationOnDisk] does not exist,
     * it is created.
     *
     * @throws IOException if unable to write to disk.
     */
    private fun createSkeleton() {
        with(locationOnDisk) {
            mkdirs()

            createSubDirectory(PROJECT_PATH_BACKUP)
            createSubDirectory(PROJECT_PATH_LOGS)
            createSubDirectory(PROJECT_PATH_CSS)
            createSubDirectory(PROJECT_PATH_HTML)
            createSubDirectory(PROJECT_PATH_JS)
            createSubDirectory(PROJECT_PATH_MEDIA)
        }
    }

    /**
     * Creates a directory within the project
     * structure.
     */
    private fun createSubDirectory(name: String) =
        File(subPath(name)).mkdirs()

    /**
     * Returns the path of the root of the project,
     * so that it can be used to point to sub dirs and files.
     *
     * i.e `subFile("index.hmtl")
     */
    private fun subPath(subpath: String) =
        locationOnDisk.path + "/" + subpath

    /**
     * Returns the path of the root of the project,
     * so that it can be used to point to sub dirs and files.
     *
     * i.e `subFile("index.hmtl")
     */
    private fun subFile(subpath: String) =
        File(subPath(subpath))

    /**
     * Validates the existance of a file on the disk.
     * @throws NoSuchFileException if it does not exist, but is supposed to.
     */
    private fun assertProjectFileExists(f: File): File {
        if (!f.exists())
            throw NoSuchFileException(f, reason = "project document is missing!")

        return f
    }

    /**
     * Validates the state of an existing HTML document on the
     * disk.
     *
     * @param HTMLPath Path to the document (Root to ./HTML/)
     *
     * @throws NoSuchFileException if it does not exist, but is supposed to.
     */
    private fun assertHTMLFileExists(HTMLPath: String) =
        assertProjectFileExists(File(subPath(PROJECT_PATH_HTML + HTMLPath)))

    /**
     * The name of this project, as inherited by
     * the project's directory name.
     */
    fun projectName() =
        locationOnDisk.name

    /**
     * renames [locationOnDisk]
     */
    @Deprecated("Incomplete and untested. This will likely break the ability to locate files.")
    fun renameOrMoveProject(destination: File) {
        locationOnDisk.renameTo(destination)
    }




    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                             Project Creation
    //region                                                  Save / Load
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    private fun <T> getCached(path: File) =
        getCached<T>(path.path)

    private fun <T> getCached(path: String) : T?{
        try {
            return CACHE[path]?.let {
                it  as T
            }
        } catch (e: ClassCastException) {
            throw ClassCastException("The loaded file for `$path` was not the expected file type.")
        }
    }

    /**
     * Saves the 'project.designer' file containing meta data about the project.
     *
     * Called automatically when changes are made to this class.
     *
     * TODO Automatically creates backups prior to saving.
     *
     * Notifies [EventType.PROJECT_SAVED]
     */
    @Synchronized
    fun saveMeta() {
        backup()

        saveObjectToDisk(subPath(PROJECT_PATH_META))
        EventNotifier.notifyEvent(EventType.PROJECT_SAVED)
    }

    /**
     * Creates a new document in this project, and saves it to the disk.
     *
     * Document is a clone of [Tag.testDOM].
     *
     * The path is added to [pagePaths].
     *
     * [saveMeta] is called after making the change.
     *
     * Notifies [EventType.PROJECT_NEW_DOCUMENT_CREATED]
     *
     * @return the new document.
     */
    fun createDocument(subpath: String) : Document {
        val doc = Tag.testDOM.clone()

        val loc = PROJECT_PATH_HTML + subpath

        File(subPath(loc)).apply {
            if (exists())
                throw FileAlreadyExistsException(this)

            if (!createNewFile())
                throw IOException("Could not create file")


            doc.title(name)
            doc.getElementById("PageTitle")?.text(name)


            saveDocument(doc, path)
        }

        saveMeta()

        EventNotifier.notifyEvent(EventType.PROJECT_NEW_DOCUMENT_CREATED)
        return doc
    }

    /**
     * Overwrites a project document with [d].
     *
     * Notifies [EventType.PROJECT_SAVED]
     */
    fun saveDocument(d: Document) =
        saveDocument(d, CACHE.filter { it.value == d }.entries.first().key)


    fun saveDocument(d: Document, path: String) {
        File(path).apply {
            assertExists()
            writeText(d.toString())
        }
        EventNotifier.notifyEvent(EventType.PROJECT_SAVED)
    }

    /**
     * Fetches an existing document from the disk
     *
     * If has been loaded previously, the existing object is
     * returned.
     *
     * @param path the location, must be from [pagePaths]
     * @return the document
     */
    fun loadDocument(file: File): Document {
        getCached<Document>(file)?.apply {
            return this
        }

        with(file) {
            assertProjectFileExists(this)
            return Jsoup.parse(readText()).also {
                CACHE[file.path] = it
            }
        }
    }

    /**
     * Creates a new stylesheet
     */
    fun createStylesheet(name: String) : StyleSheet {
        File(subPath("$PROJECT_PATH_CSS$name.stylesheet")).apply {
            createNewFile()
            return StyleSheet(name).also {
                saveStylesheet(it, this)
            }
        }
    }

    /**
     * Saves a stylesheet to [file]
     */
    fun saveStylesheet(styleSheet: StyleSheet, file: File) =
        styleSheet.saveObjectToDisk(file.toString()).also {
            CACHE[file.path] = it
        }

    /**
     * Loads a stylesheet from [file]
     */
    fun loadStylesheet(file: File) : StyleSheet {
        getCached<StyleSheet>(file)?.apply {
            return this
        }

        return (loadObjectFromDisk(file) as StyleSheet).also {
            CACHE[file.path] = it
        }
    }

    fun deleteFile(projectFile: File) {
        projectFile.delete()
        validateCache()
    }


    /**
     * Takes a copy of the HTML files and the meta into [PROJECT_PATH_BACKUP].
     *
     * Notifies [EventType.PROJECT_BACKEDUP]
     */
    fun backup() {
//        TODO()
        EventNotifier.notifyEvent(EventType.PROJECT_BACKEDUP)
    }

    fun logError(e : Throwable) {
        subFile("$PROJECT_PATH_LOGS [ERR] ${Time.from(Instant.now())}.log").apply {
            createNewFile()
            printWriter().apply {
                e.printStackTrace(this)
                flush()
            }
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                             Save / Load
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Returns an array of [File]s containing [HTML] documents
     * in the project.
     */
    fun documents() = HTML.listTree()

    /**
     * Returns an array of [File]s containing [CSS] documents
     * in the project.
     */
    fun stylesheets() = CSS.listTree()

    /**
     * Returns an array of [File]s containing [JS] documents
     * in the project.
     */
    fun javascripts() = JS.listTree()

    /**
     * Returns an array of [File]s containing [MEDIA] documents
     * in the project.
     */
    fun media() = MEDIA.listTree()

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
         * Attempts to load a project from disk, if it exists.
         *
         * If there is no project at the [path], then one is created.
         *
         * @return the existing or new project.
         * @throws InvalidClassException if a project exists, but was made by a different version of the editor and cannot be loaded.
         */
        fun loadOrCreate(path: String): Project? {
            File("$path/$PROJECT_PATH_META").apply {
                if (exists()) {
                    return try {
                        val proj = loadObjectFromDisk(this) as Project
                        proj.validate()
                        logStatus("Loaded Existing Project '${proj.locationOnDisk.name}'")
                        proj
                    } catch (e: InvalidClassException) {
                        AlertUser("This project was made with a different version of the IDE, and is incompatible.\n\n" +
                                "To load this project, you need an editor that supports the following project version : \n\n${Project::class.hashCode()}\n\n" +
                                "To find what editor version you need, visit \n\nhttps://github.com/jdngray77/HTMLDesigner/wiki/IDE-to-Project-Version-Map")
                        null
                    }
                }
                return Project(File(path))
            }
        }
    }
}