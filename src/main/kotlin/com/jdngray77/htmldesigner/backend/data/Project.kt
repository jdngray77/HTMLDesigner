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








import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.data.config.ProjectPreference
import com.jdngray77.htmldesigner.backend.data.config.ProjectPreferences
import com.jdngray77.htmldesigner.backend.data.project.ProjectMeta
import com.jdngray77.htmldesigner.backend.data.project.ProjectStructure
import com.jdngray77.htmldesigner.backend.data.project.ProjectStructure.Companion.PROJECT_PATH_META
import com.jdngray77.htmldesigner.backend.html.DefaultDocument
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.frontend.IDE
import com.jdngray77.htmldesigner.frontend.IDE.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.editors.jsdesigner.VisualScriptEditor
import com.jdngray77.htmldesigner.utility.*
import javafx.scene.control.ButtonType
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.io.InvalidClassException
import java.sql.Time
import java.time.Instant

/**
 * A user's HTML Designer project.
 *
 * A medium for files on the disk to be accessed and manipulated in a standardised fashion
 * that can be more easily validated.
 *
 * Creates and manages a directory structure, tracks project files, and handles disk access.
 *
 * When requesting files to be loaded, relative paths are used to identify a file within it's directory.
 * i.e `index.html` or `page2/index.html` are valid pages for [getDocument].
 *
 * Once files are loaded, the data read is kept in a [CachedFile] within the [cache].
 *
 * All paths, once loaded, are absolute to avoid ambiguity.
 *
 * > N.B the lifetime of a [CachedFile] *should* be equal to the lifetime of an [Editor],
 * therefore when an editor managing a file is closed, the [CachedFile] should be removed from the [cache].
 *
 * The initaliser is used to create a project. Loading is only possible via deserialization.
 *
 * This class is created once when the project is, and stored in the root of the project directory as `project.designer`.
 *
 * @see [load] for loading a project from disk
 *
 * @see [ProjectPreferences] for project preferences
 * @see [ProjectStructure] for project directory structure & file access.
 * @see [FileCache] for caching of files.
 * @see [ProjectMeta] for meta data pertaining to projects.
 *
 * @param fileOnDisk The file that the project is to be created in
 * @param name The display name of the project
 * @param author The author / organisation of the project
 *
 * @author Jordan T. Gray.
 *
 * TODO file manifest
 */
class Project(

    /**
     * The file that the project is to be created in
     */
    fileOnDisk: File,

    /**
     * The display name of the project
     */
    name: String = fileOnDisk.nameWithoutExtension,

    /**
     * The author / organisation of the project
     */
    author: String? = null

) : PartiallySerializable {


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                         properties
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * The file structure of the project.
     *
     * Pertains to disk access and directory structure management.
     *
     * Handles creation of the directory structure at init time, and validates at load time.
     *
     * All disk file access starts here.
     */
    val fileStructure: ProjectStructure = ProjectStructure(fileOnDisk)

    /**
     * Metadata pertaining to the project.
     *
     * Simple data class that is serialized within the `project.designer`.
     */
    val meta: ProjectMeta = ProjectMeta(
        name,
        author
    )

    /**
     * Project specific preferences that are stored alongside the project
     * in `preferences.registry`.
     */
    @Transient
    lateinit var PREFERENCES: ProjectPreferences
        private set


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion
    //region                         file cache
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * A store of all files and project data that is actively open
     * within the editor.
     *
     * If a file is open in the editor, it can be found here.
     */
    @Transient
    private lateinit var cache: FileCache

    /**
     * @return a non-mutable copy of the file cache, containing all files that are loaded.
     */
    fun getCache() = cache.copy() as HashMap<String, CachedFile<*>>

    /**
     * Removes any files from the cache that do not exist
     * // TODO don't keep files that are not loaded, either.
     */
    fun validateCache() = cache.validate()


    /**
     * Deletes everything from the project cache.
     */
    @Deprecated("Used for forceful and debug operations only. Cached files should be removed when thier corresponding editor is closed.")
    fun invalidateCache() = cache.clear()

    /**
     * @returns a [CachedFile] containing the [file], or null of that file is not in the cache.
     */
    fun findCachedFile(file: File) =
        getCache().entries.find { it.key == file.absolutePath || it.value.file == file }?.value


    /**
     * Removes a specific item from the cache.
     *
     * Use this only if you don't have access to the [CachedFile] itself.
     * @see [CachedFile.removeCache]
     */
    fun removeTFromCache(it: Any): Boolean {
        return cache.findT(it)?.let {
            it.removeCache()
            true
        } ?: false
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion
    //region                         initalisation
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Project creation time initaliser.
     *
     * Creates the project for the first time.
     */
    init {
        // Create project
        recreateTransientProperties()
        saveDesignerFile()

        // Check
        fileStructure.validateLocationOnDisk()

        // Report success
        createDocument("index")
        logStatus("Created new project '$name'")
        EventNotifier.notifyEvent(EventType.PROJECT_CREATED)
    }


    /**
     * Project load time initaliser.
     *
     * Re-creates properties that are not serialized.
     */
    override fun recreateTransientProperties() {
        fileStructure.recreateTransientProperties()
        cache = FileCache()
        PREFERENCES = ProjectPreferences(this)
    }

    /**
     * Saves the 'project.designer' file containing metadata about the project.
     *
     * Notifies [EventType.PROJECT_META_SAVED]
     */
    fun saveDesignerFile() {
        saveObjectToDisk(fileStructure.locationOnDisk.subFile(PROJECT_PATH_META))
        EventNotifier.notifyEvent(EventType.PROJECT_META_SAVED)
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion
    //region                         project util functions
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    fun renameOrMoveProject(newLocation: File) {
        TODO()
    }

    /**
     * Reveals the project root in the system explorer.
     */
    fun showInExplorer() {
        //Desktop.getDesktop().open(fileStructure.locationOnDisk);
        fileStructure.locationOnDisk.openFileInSystem()
    }


    /**
     * Deletes all files on disk, and closes the project within the editor
     * if the editor has this project loaded.
     */
    fun deleteProject() {
        fileStructure.deleteEverything()

        if (IDE.mvc().Project === this)
            IDE.EDITOR.closeProject()
    }


    /**
     * Deletes all files in the [PROJECT_PATH_LOGS] directory.
     */
    fun deleteLogs() {
        fileStructure.LOGS.deleteRecursively()
        fileStructure.LOGS.mkdir()
    }

    /**
     * Deletes one or many files on disk then [validateCache]
     */
    fun deleteFile(vararg projectFile: CachedFile<*>) {
        projectFile.forEach {
            it.file.delete()
            cache.remove(it.file)
        }
        cache.validate()
    }

    /**
     * Saves the stacktrace of [e] in the [PROJECT_PATH_LOGS] folder.
     *
     * The file is titled the date and time of invocation.
     *
     * @return the log file that was created.
     */
    fun logError(e: Throwable) =
        newLogFile(e.stackTraceToString(), "ERR")

    /**
     * Creates a new log file with the given contents.
     * @return the file created.
     */
    fun newLogFile(content: String, prefix: String = "") : File {
        fileStructure.LOGS.subFile(
            (if (prefix.isNotBlank()) " [$prefix] " else "") +
                    "" +
                    "${Time.from(Instant.now())}.log"
        ).apply {

            createNewFile()
            printWriter().apply {
                write(content)
                flush()
            }

            return this
        }
    }

    /**
     * Takes a copy of the project files and the meta into [PROJECT_PATH_BACKUP].
     *
     * Notifies [EventType.PROJECT_BACKEDUP]
     */
    fun backup() {
        TODO()
        EventNotifier.notifyEvent(EventType.PROJECT_BACKEDUP)
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion
    //region                         Documents
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


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
    fun createDocument(subpath: String): CachedFile<Document> {
        val doc = DefaultDocument()

        val file = fileStructure.HTML.subFile(subpath.assertEndsWith(".html"))


        file.apply {
            if (exists())
                throw FileAlreadyExistsException(this)

            if (!createNewFile())
                throw IOException("Could not create file")

            doc.title(name)
            doc.getElementById("PageTitle")?.text(name)

            doc.addStylesheet(CSS_ID_DOCUMENT_SPECIFIC)
            doc.addStylesheet(CSS_ID_DEBUG, CSS_SHEET_DEBUG)

            saveDocument(
                cache.put(file, doc)
            )
        }

        saveDesignerFile()

        EventNotifier.notifyEvent(EventType.PROJECT_CREATED)
        return cache[file]!! as CachedFile<Document>
    }

    /**
     * Overwrites a project document with [d].
     * Notifies [EventType.PROJECT_META_SAVED]
     */
    fun saveDocument(d: CachedFile<Document>) =
        saveDocument(d.data, d.file)

    /**
     * Overwrites a project document with [d].
     * Notifies [EventType.PROJECT_META_SAVED]
     */
    fun saveDocument(document: Document, file: File) {
        file.apply {
            assertExists()
            writeText(document.toString())
        }
        EventNotifier.notifyEvent(EventType.PROJECT_DOCUMENT_SAVED)
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
    fun getDocument(subpath: String): CachedFile<Document> =
        getDocument(fileStructure.HTML.subFile(subpath))


    /**
     * Fetches an existing document from the disk
     *
     * If has been loaded previously, the existing object is
     * returned.
     *
     * @param path the location, must be from [pagePaths]
     * @return the document
     */
    fun getDocument(file: File): CachedFile<Document> {
        return cache.getOrLoad(file) {
            Jsoup.parse(it)
        }
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion
    //region                         Javascript graphs
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Loads a visual javascript data file from [PROJECT_PATH_JS].
     *
     * These can be edited and compiled with the [VisualScriptEditor]
     * @return Cached file containting the [JsGraph]
     * @throws NoSuchFileException if no script with that name can be found.
     */
    fun loadJsGraph(subpath: String): CachedFile<JsGraph> =
        loadJsGraph(fileStructure.JS.subFile(subpath.assertEndsWith(JsGraph.FILE_EXTENSION)))


    /**
     * Loads a visual javascript data file from [PROJECT_PATH_JS].
     *
     * These can be edited and compiled with the [VisualScriptEditor]
     * @return Cached file containing [JsGraph]
     * @throws NoSuchFileException if no script with that name can be found.
     */
    fun loadJsGraph(file: File): CachedFile<JsGraph> = cache.getOrLoad(file) {
        loadObjectFromDisk(file) as JsGraph
    }

    /**
     * Saves a visual javascript data file to it's file.
     *
     * @param jsGraph the graph to save
     */
    fun saveJsGraph(jsGraph: CachedFile<JsGraph>): CachedFile<JsGraph> {
        jsGraph.data.saveObjectToDisk(jsGraph.file)
        return jsGraph
    }

    /**
     * Saves a visual javascript data file to it's file.
     *
     * Use this if you have the file object, but not the cached file.
     *
     * If you have access to the cached file, preffer sister method.
     */
    fun saveJsGraph(jsGraph: JsGraph): CachedFile<JsGraph> =
        cache.findT(jsGraph)!!.also {
            saveJsGraph(it)
        }

    /**
     * Creates a new graph.
     */
    fun createJsGraph(subpath: String): CachedFile<JsGraph> = saveJsGraph(JsGraph(subpath))

    /**
     * Deletes a graph
     */
    fun deleteJsGraph(subpath: String) {
        val f = fileStructure.locationOnDisk.subFile(subpath.assertEndsWith(JsGraph.FILE_EXTENSION))

        if (!f.exists())
            throw NoSuchFileException(f)

        f.delete()
        cache.remove(f)
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion
    //region                         file getters
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * @return a list of all documents, loaded or not, that are in the project directory.
     *
     * Load via [getDocument]
     */
    fun documents() = fileStructure.HTML.flattenTree()

    /**
     * @return a list of all stylesheets files, loaded or not, that are in the project directory.
     */
    fun stylesheets() = fileStructure.CSS.flattenTree()

    /**
     * @return a list of all javascript files, loaded or not, that are in the project directory.
     *
     * Load via [loadJsGraph]
     */
    fun javascripts() = fileStructure.JS.flattenTree()

    /**
     * @return a list of all image files, loaded or not, that are in the project directory.
     */
    fun media() = fileStructure.MEDIA.flattenTree()

    /**
     * @return a list of all files, loaded or not, that are in the project directory.
     */
    fun prefabs() = fileStructure.PREFABS.flattenTree()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    companion object {

        /**
         * Loads a project from disk
         *
         * If the project meta file exists, load it, validate it, and return it.
         *
         * @param absolutePath The path to the project folder
         * @return A Project object
         */
        fun load(absolutePath: String): Project? {
            if (!File(absolutePath).exists())
                throw NoSuchFileException(
                    File(absolutePath),
                    reason = "Tried to load a project, but there's nothing there!"
                )

            File("$absolutePath/$PROJECT_PATH_META").apply {

                if (!exists())
                    throw NoSuchFileException(
                        this,
                        reason = "\n\nThere is no $PROJECT_PATH_META file in ${parentFile.name}. \nAre you sure this is the right folder?"
                    )

                return try {

                    val proj = loadObjectFromDisk(this) as Project

                    proj.fileStructure.validateLocationOnDisk(this.parentFile)
                    proj.recreateTransientProperties()

                    if (
                        !(proj.PREFERENCES[ProjectPreference.SUPPRESS_PROJECT_NAME_MISMATCH_WARNING_BOOL] as Boolean)
                        && proj.fileStructure.locationOnDisk.nameWithoutExtension != proj.meta.name
                    ) {

                        val curr = proj.fileStructure.locationOnDisk.nameWithoutExtension
                        val orig = proj.meta.name

                        val updDirectory = ButtonType("Change folder back to '$orig'")
                        val updProject = ButtonType("Update project to '$curr'")
                        val updNeither = ButtonType("Nothing")
                        val updNeitherDontAsk = ButtonType("Nothing & don't ask again")

                        val response = userConfirm(
                            "This project is titled '$orig', but the folder has been renamed to" +
                                    " '$curr'.\n\n" +
                                    "What would you like to do?",
                            updDirectory, updProject, updNeither, updNeitherDontAsk
                        )

                        when (response) {
                            updDirectory -> {
                                // rename the directory
                                val dest = File(proj.fileStructure.locationOnDisk.parentFile, orig)

                                proj.fileStructure.locationOnDisk.renameTo(dest)
                                proj.fileStructure.validateLocationOnDisk(dest)

                                // Restart to reload
                                Config[Configs.LAST_PROJECT_PATH_STRING] =
                                    proj.fileStructure.locationOnDisk.absolutePath
                                EDITOR.restart()
                            }

                            updProject -> {
                                proj.meta.name = proj.fileStructure.locationOnDisk.nameWithoutExtension
                                proj.saveDesignerFile()
                            }

                            updNeitherDontAsk -> {
                                proj.PREFERENCES[ProjectPreference.SUPPRESS_PROJECT_NAME_MISMATCH_WARNING_BOOL] = true
                                proj.saveDesignerFile()
                            }
                        }

                    }




                    logStatus("Loaded Existing Project '${proj.fileStructure.locationOnDisk.name}'")
                    proj

                } catch (e: InvalidClassException) {
                    showInformationalAlert(
                        "This project was made with a different version of the IDE, and is incompatible.\n\n" +
                                "To load this project, you need an editor that supports the following project version : \n\n${Project::class.hashCode()}\n\n" +
                                "To find what editor version you need, visit \n\nhttps://github.com/jdngray77/HTMLDesigner/wiki/IDE-to-Project-Version-Map"
                    )
                    null
                }
            }
        }

        fun Document.projectFile() = IDE.mvc().Project.cache.findT(this)!!
    }

    class UnloadedDocumentException(val d: Document) :
        Exception("The file for ${d.title().assertEndsWith(".html")} was required, but the file was not loaded.")
}