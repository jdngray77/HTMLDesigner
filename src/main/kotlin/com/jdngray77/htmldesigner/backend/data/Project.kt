
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

package com.jdngray77.htmldesigner.backend.data

import com.jdngray77.htmldesigner.*
import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.data.config.ProjectPreferences
import com.jdngray77.htmldesigner.backend.html.DefaultDocument
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.jsdesigner.VisualScriptEditor
import com.jdngray77.htmldesigner.utility.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.IOException
import java.io.InvalidClassException
import java.sql.Time
import java.time.Instant
import java.util.*

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
 *              (functions & variables)
 *
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
 * @author Jordan Gray
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
     * Saves meta when altered.
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
     * The project's PREFABS directory
     */
    val PREFABS = File(subPath(PROJECT_PATH_PREFABS))

    /**
     * The project's backup directory
     */
    val BACKUP = File(subPath(PROJECT_PATH_BACKUP))

    /**
     *
     */
    @Transient
    lateinit var PREFERENCES: ProjectPreferences
        private set

    /**
     * Storage of any document file after load.
     *
     * Once a file has been loaded once, the loaded version is returned
     * upon every subsequent request.
     *
     * TODO cache JS, media, etc.
     *

     */
    @Transient
    private lateinit var CACHE : HashMap<String, Any>

    @Deprecated("This is for debug access only. Cache is internal to the project only.")
    fun getCache() = CACHE

    inline fun <reified T> removeFromCache(any: T) {
        getCache().entries.filter { it.value is T && it.value == any }
            .map { getCache().remove(it.key) }
    }

    /**
    * TODO request to reset cache / reload all documents from disk
    *      How to handle dirty files here update open editors
    */
    fun reloadOpenFilesFromDisk() {
        TODO()
    }

    /**
     * Creation of a new project only.
     *
     * Since this file will be serialized to disk
     * as project.designer, it will only be created
     * when the project is.
     */
    init {
        checkPath()
        initDirectoryStructure()
        validate()
        createDocument("index.html")
        logStatus("Created new project '${locationOnDisk.name}'")
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                            Initialisation & Validation
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

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
     * Checks the folder structure on disk.
     *
     * [locationOnDisk] must contain the correct subdirectories.
     *
     * Also initalises [CACHE], if it is not already.
     * since [CACHE] is used by IO operations, this has to be called before any
     * are attempted.
     *
     * If not
     * @throws IllegalStateException
     */
    fun validate() {
        with (locationOnDisk) {
            if (
                !hasChild(PROJECT_PATH_HTML)    ||
                !hasChild(PROJECT_PATH_CSS)     ||
                !hasChild(PROJECT_PATH_MEDIA)   ||
                !hasChild(PROJECT_PATH_BACKUP)  ||
                !hasChild(PROJECT_PATH_PREFABS) ||
                !hasChild(PROJECT_PATH_JS)
            ) throw IllegalStateException("A project folder has gone missing") // TODO create it or prompt to restore backup
        }

        // Handle transient properties

        if (!this::CACHE.isInitialized)
            CACHE = HashMap()
        else
            validateCache()

        if (!this::PREFERENCES.isInitialized)
            PREFERENCES = ProjectPreferences(this)
    }

    /**
     * Removes any files that no-longer exist on disk from the [CACHE]
     */
    fun validateCache() {
        val toRemove = ArrayList<String>()

        CACHE.entries.forEach {
            if (!File(it.key).exists())
                toRemove.add(it.key)
        }

        toRemove.map { CACHE.remove(it) }
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
    private fun initDirectoryStructure() {
        with(locationOnDisk) {
            mkdirs()

            createSubDirectory(PROJECT_PATH_BACKUP)
            createSubDirectory(PROJECT_PATH_LOGS)
            createSubDirectory(PROJECT_PATH_CSS)
            createSubDirectory(PROJECT_PATH_HTML)
            createSubDirectory(PROJECT_PATH_JS)
            createSubDirectory(PROJECT_PATH_MEDIA)
            createSubDirectory(PROJECT_PATH_PREFABS)
        }
    }

    /**
     * Creates a directory within the project structure.
     */
    private fun createSubDirectory(subPath: String) =
        File(subPath(subPath)).mkdirs()

    /**
     * Returns the path of the root of the project,
     * so that it can be used to point to sub dirs and files.
     *
     * i.e `subFile("index.hmtl")
     */
    fun subPath(subpath: String) =
        locationOnDisk.path + "/" + subpath

    /**
     * Returns a file relative to the root of the project,
     * so that it can be used to point to sub dirs and files.
     *
     * i.e `subFile("index.hmtl")
     */
    @Deprecated("Moved to [File.subFile]", ReplaceWith("File.subFile(subpath))"))
    fun subFile(subpath: String) =
        File(subPath(subpath))


    /**
     * Validates the state of an existing HTML document on the
     * disk.
     *
     * @param HTMLPath Path to the document (Root to ./HTML/)
     *
     * @throws NoSuchFileException if it does not exist, but is supposed to.
     */
    private fun assertHTMLFileExists(HTMLPath: String) =
        File(subPath(PROJECT_PATH_HTML + HTMLPath)).requireExists()



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                    Initialisation and Validation
    //region                                                      IO
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

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
    fun renameOrMoveProject(destination: File) =
        locationOnDisk.renameTo(destination)

    /**
     * Returns [file] as it appears in the [CACHE], or
     * null if it has not been loaded.
     */
    private fun <T> getCached(file: File) =
        getCached<T>(file.path)

    /**
     * Returns [file] as it appears in the [CACHE], or
     * null if it has not been loaded.
     */
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
     * Attempts to fetch a file from the [CACHE], but if it doesn't exist
     * invokes a function that provides it instead.
     *
     * @param path The file path to load.
     * @param otherwise The default value provider invoked if the [path] does not exist in the [CACHE].
     */
    private fun <T: Any> tryGetCached(directory: String, subpath: String, otherwise : () -> T) : T = subPath("$directory/$subpath").let {
        path ->
        getCached<T>(path) ?: let {
            CACHE[path] = otherwise()
            return CACHE[path] as T
        }
    }

    /**
     * Finds this document in the [CACHE], and returns the file
     * that it was loaded from.
     *
     * @throws UnloadedDocumentException if the file is not in the cache,
     *         i.e the file has been deleted or the document did not originate from the project.
     */
    fun fileForDocument(d: Document) : File {
        CACHE.entries.find { it.value == d }
            .apply {
                if (this == null)
                    throw UnloadedDocumentException(d)

                return File(key)
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
     * Creates a new HTML document in this project, and saves it to the disk.
     *
     * Document is a clone of [Tag.testDOM].
     *
     * The path is added to [pagePaths].
     *
     * [saveMeta] is called after making the change.
     *
     * Notifies [EventType.PROJECT_CREATED]
     *
     * @return the new document.
     */
    fun createDocument(subpath: String) : Document {
        val doc = DefaultDocument()

        val loc = PROJECT_PATH_HTML + subpath

        File(subPath(loc)).apply {
            if (exists())
                throw FileAlreadyExistsException(this)

            if (!createNewFile())
                throw IOException("Could not create file")

            doc.title(name)
            doc.getElementById("PageTitle")?.text(name)

            doc.addStylesheet(CSS_ID_DOCUMENT_SPECIFIC)
            doc.addStylesheet(CSS_ID_DEBUG, CSS_SHEET_DEBUG)

            saveDocument(doc, path)
            CACHE[path] = doc
        }

        saveMeta()

        EventNotifier.notifyEvent(EventType.PROJECT_CREATED)
        return doc
    }


    /**
     * Overwrites a project document with [d].
     * Notifies [EventType.PROJECT_SAVED]
     */
    fun saveDocument(d: Document) =
        CACHE.filter { it.value == d }.entries.firstOrNull()?.key?.let { saveDocument(d, it) }


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
            requireExists()
            return Jsoup.parse(readText()).also {
                CACHE[file.path] = it
            }
        }
    }


    /**
     * Loads a visual javascript data file from [PROJECT_PATH_JS].
     *
     * These can be edited and compiled with the [VisualScriptEditor]
     */
    fun loadJsGraph(name: String): JsGraph = tryGetCached(PROJECT_PATH_JS, "$name.jvg") {
        loadObjectFromDisk(
            javascripts().find {
                it.name == "$name.jvg"
            }!!
        ) as JsGraph
    }

    fun saveJsGraph(jsGraph: JsGraph) {
        // TODO const val jvg
        // TODO test
        val f = subPath("$PROJECT_PATH_JS${mvc().currentDocument().projectFile().nameWithoutExtension + "/"}${jsGraph.scriptName}.jvg")
        jsGraph.saveObjectToDisk(f)
        CACHE[f] = jsGraph
    }

    fun newJsGraph(name: String) : JsGraph {
        val graph = JsGraph(name)
        saveJsGraph(graph)
        return graph
    }

//    /**
//     * Creates a new stylesheet
//     */
//    fun createStylesheet(name: String) : StyleSheet {
//        File(subPath("$PROJECT_PATH_CSS$name.stylesheet")).apply {
//            createNewFile()
//            return StyleSheet(name).also {
//                saveStylesheet(it, this)
//            }
//        }
//    }
//
//    /**
//     * Saves a stylesheet to [file]
//     */
//    fun saveStylesheet(styleSheet: StyleSheet, file: File) =
//        styleSheet.saveObjectToDisk(file.toString()).also {
//            CACHE[file.path] = it
//        }
//
//    /**
//     * Loads a stylesheet from [file]
//     */
//    fun loadStylesheet(file: File) : StyleSheet {
//        getCached<StyleSheet>(file)?.apply {
//            return this
//        }
//
//        return (loadObjectFromDisk(file) as StyleSheet).also {
//            CACHE[file.path] = it
//        }
//    }

    /**
     * Deletes a file on disk then [validateCache]
     */
    fun deleteFile(projectFile: File) {
        projectFile.delete()
        validateCache()
    }

    /**
     * Takes a copy of the project files and the meta into [PROJECT_PATH_BACKUP].
     *
     * Notifies [EventType.PROJECT_BACKEDUP]
     */
    fun backup() {
//        TODO()
        EventNotifier.notifyEvent(EventType.PROJECT_BACKEDUP)
    }

    /**
     * Saves the stacktrace of [e] in the [PROJECT_PATH_LOGS] folder.
     *
     * The file is titled the date and time of invocation.
     */
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
    fun documents() = HTML.flattenTree()

    /**
     * Returns an array of [File]s containing [CSS] documents
     * in the project.
     */
    fun stylesheets() = CSS.flattenTree()

    /**
     * Returns an array of [File]s containing [JS] documents
     * in the project.
     */
    fun javascripts() = JS.flattenTree()

    /**
     * Returns an array of [File]s containing [MEDIA] documents
     * in the project.
     */
    fun media() = MEDIA.flattenTree()



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
         * Creates a new project at the specified location.
         *
         * @param path The path to the project directory.
         */
        fun create(path : String) = Project(File(path))

        /**
         * Loads a project from disk
         *
         * If the project meta file exists, load it, validate it, and return it.
         *
         * @param path The path to the project folder
         * @return A Project object
         */
        fun load(path : String) : Project? {
            if (!File(path).exists()) {
                throw NoSuchFileException(File(path), reason = "Tried to load a project, but there's nothing there!")
            }

            File("$path/$PROJECT_PATH_META").apply {
                if (!exists())
                    throw NoSuchFileException(this, reason = "\n\nThere is no $PROJECT_PATH_META file in ${parentFile.name}. \nAre you sure this is the right folder?")
                return try {
                        val proj = loadObjectFromDisk(this) as Project
                        proj.validate()
                        logStatus("Loaded Existing Project '${proj.locationOnDisk.name}'")
                        proj
                    } catch (e: InvalidClassException) {
                        showInformationalAlert("This project was made with a different version of the IDE, and is incompatible.\n\n" +
                                "To load this project, you need an editor that supports the following project version : \n\n${Project::class.hashCode()}\n\n" +
                                "To find what editor version you need, visit \n\nhttps://github.com/jdngray77/HTMLDesigner/wiki/IDE-to-Project-Version-Map")
                        null
                   }
            }
        }

        fun Document.projectFile() = mvc().Project.fileForDocument(this)
    }

    class UnloadedDocumentException(val d: Document) : Exception("The file for ${d.title().assertEndsWith(".html")} was required, but the file was not loaded.")
}

/**
 * Simple object-oriented wrapper for Configs.AUTO_LOAD stuff,
 * for convenience.
 */
internal object AutoLoad {

    /**
     * @return true if [Configs.AUTO_LOAD_PROJECT_BOOL] permits auto-loading, and the there is a project to load
     *         within [Configs.AUTO_LOAD_PROJECT_PATH]
     */
    fun isAvailable() =
        (Config[Configs.AUTO_LOAD_PROJECT_BOOL] as Boolean)
        && Config[Configs.LAST_PROJECT_PATH_STRING] != ""

    /**
     * Raises [Configs.AUTO_LOAD_PROJECT_BOOL]
     */
    fun enable() {
        Config[Configs.AUTO_LOAD_PROJECT_BOOL] = true
    }

    /**
     * Lowers [Configs.AUTO_LOAD_PROJECT_BOOL]
     */
    fun disable() {
        Config[Configs.AUTO_LOAD_PROJECT_BOOL] = false
    }

    /**
     * @return the value of [Configs.AUTO_LOAD_PROJECT_PATH]
     */
    fun getLastProjectLoaded() =
        Config[Configs.LAST_PROJECT_PATH_STRING] as String

    /**
     * Sets [Configs.AUTO_LOAD_PROJECT_PATH] to nothing.
     *
     * Makes [AutoLoad] not [isAvailable]
     */
    fun clearLastProjectLoaded() {
        Config[Configs.LAST_PROJECT_PATH_STRING] = ""
    }

    /**
     * Stores a project that could be loaded at next boot.
     *
     * Typically set when project is loaded.
     *
     * [Configs.AUTO_LOAD_PROJECT_PATH] to the specified value.
     */
    fun storeLastProjectLoaded(path: String) {
        Config[Configs.LAST_PROJECT_PATH_STRING] = path
    }
}