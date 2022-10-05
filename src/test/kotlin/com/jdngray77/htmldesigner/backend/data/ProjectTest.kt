package com.jdngray77.htmldesigner.backend.data

import com.jdngray77.htmldesigner.RequiresEditorGUI
import com.jdngray77.htmldesigner.backend.ExceptionListener
import com.jdngray77.htmldesigner.backend.data.project.ProjectStructure.Companion.PROJECT_PATH_LOGS
import com.jdngray77.htmldesigner.backend.data.project.ProjectStructure.Companion.PROJECT_PATH_META
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.editors.jsdesigner.JsRandomColorFunction
import com.jdngray77.htmldesigner.utility.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File
import java.lang.IllegalArgumentException

@ExtendWith(RequiresEditorGUI::class)
internal class ProjectTest {

    lateinit var project: Project

    companion object {
        val TEST_PROJECT_PATH = File("./testProject")
    }

    @BeforeEach
    internal fun setUp() {
        if (TEST_PROJECT_PATH.exists()) {
            TEST_PROJECT_PATH.deleteRecursively()
        }

        project = Project(TEST_PROJECT_PATH)
    }

    @AfterEach
    internal fun tearDown() {
        project.deleteProject()
    }

    @Test
    fun testPath() {
        assertEquals(TEST_PROJECT_PATH.absolutePath, project.fileStructure.locationOnDisk.absolutePath)
    }

    @Test
    fun getAuthor() {
        project.meta.author = "My Name Here"
        assertEquals("My Name Here", project.meta.author)
    }

    @Test
    fun hasDefaultHTMLDocument() {
        assertEquals(1, project.documents().size)
        val file = project.documents().first()

        assertDoesNotThrow {
            project.getDocument(file)
        }

        val defaultDocument = project.getDocument(file)

        assertEquals(file.name, defaultDocument.data.title())
    }

    @Test
    fun jsGraph() {
        // No files to start
        assertEquals(0, project.javascripts().size)

        project
        assertEquals(0, project.getCache().entries.size)

        // Create file
        val graphFile = project.createJsGraph("myscript")
        val graph = graphFile.data

        assertEquals(0, graph.getNodes().size)

        assertTrue(project.getCache().containsValue(graphFile))

        // there is one file, and the graph is cached
        assertEquals(1, project.javascripts().size)

        assertEquals(1, project.getCache().size)

        // load from cache
        assertDoesNotThrow { project.loadJsGraph("myscript") }

        assertEquals(1, project.getCache().size)

        // load without cache
        project.invalidateCache()

        assertEquals(0, project.getCache().size)

        assertDoesNotThrow { project.loadJsGraph("myscript") }

        assertThrowsExactly(NoSuchElementException::class.java) { project.loadJsGraph("notascript") }

        // Delete
        project.deleteJsGraph("myscript")

        assertEquals(0, project.javascripts().size)

        assertEquals(0, project.getCache().size)

        // Delete no such file

        assertThrowsExactly(NoSuchFileException::class.java) { project.deleteJsGraph("i don't exist") }


        // save modification

        graph.addFunction(JsRandomColorFunction())
        project.saveJsGraph(graph)

        project.invalidateCache()
        val graph1 = project.loadJsGraph("myscript").data
        assertEquals(1, graph1.getNodes().size)
        assertEquals(JsRandomColorFunction().name, graph1.getFunctionNodes().first()!!.function.name)

    }

    @Test
    fun document() {
        // Start with no files.
        project.fileStructure.HTML.deleteRecursively()
        project.fileStructure.HTML.mkdir()

        assertEquals(0, project.documents().size)

        // Nothing should be loaded
        project.invalidateCache()
        assertEquals(0, project.getCache().size)

        // Create file
        var cachedfile = project.createDocument("hello")
        val doc = cachedfile.data
        val file = cachedfile.file


        assertTrue(file.exists())

        // configured document
        assertEquals(file.name, doc.title())
        assertEquals(file.name, doc.getElementById("PageTitle")!!.text())

        assertNotNull(doc.getStylesheet(CSS_ID_DEBUG))
        assertNotNull(doc.getStylesheet(CSS_SHEET_DEBUG))
        assertNotNull(doc.getStylesheet(CSS_ID_DOCUMENT_SPECIFIC))

        assertTrue(project.getCache().containsValue(cachedfile))

        // Create dupe
        assertThrowsExactly(FileAlreadyExistsException::class.java) { project.createDocument("hello") }


        // there is one file, and the graph is cached
        assertEquals(1, project.documents().size)

        assertEquals(1, project.getCache().size)

        // load from cache
        assertDoesNotThrow { project.getDocument(file) }

        assertEquals(1, project.getCache().size)

        // load without cache
        project.invalidateCache()

        assertEquals(0, project.getCache().size)

        assertDoesNotThrow { project.getDocument(file) }


        // Load non-existant

        assertThrowsExactly(NoSuchFileException::class.java) { project.getDocument(File("i don't exist")) }

        // Delete
        project.deleteFile(cachedfile)

        assertEquals(0, project.javascripts().size)

        assertEquals(0, project.getCache().size)



        // Modify, save, reload

        doc.getElementById("PageTitle")!!.text("Test")

        project.saveDocument(cachedfile)
        project.saveDocument(doc, project.fileStructure.HTML.subFile("test.html"))

        // load changes from disk
        project.invalidateCache()

        val doc2 = project.getDocument(file).data
        assertEquals("Test", doc2.getElementById("PageTitle")!!.text())

        val doc3 = project.getDocument(project.fileStructure.HTML.subFile("test.html")).data
        assertEquals("Test", doc3.getElementById("PageTitle")!!.text())

    }

    @Test
    fun testMissingFolder() {
        assertDoesNotThrow { project.fileStructure.validateLocationOnDisk() }

        project.fileStructure.HTML.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.fileStructure.validateLocationOnDisk() }
        tearDown()
        setUp()

        project.fileStructure.JS.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.fileStructure.validateLocationOnDisk() }
        tearDown()
        setUp()

        project.fileStructure.CSS.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.fileStructure.validateLocationOnDisk() }
        tearDown()
        setUp()

        project.fileStructure.MEDIA.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.fileStructure.validateLocationOnDisk() }
        tearDown()
        setUp()

        project.fileStructure.BACKUP.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.fileStructure.validateLocationOnDisk() }
        tearDown()
        setUp()

        project.fileStructure.PREFABS.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.fileStructure.validateLocationOnDisk() }
        tearDown()
        setUp()
    }

    @Test
    fun testCheckPath() {
        // Project already exists.
        assertThrowsExactly(IllegalArgumentException::class.java) { project.fileStructure.checkValidCreationTarget() }

        // No project, but folder is not empty.
        project.fileStructure.locationOnDisk.subFile(PROJECT_PATH_META).delete()
        assertThrowsExactly(IllegalArgumentException::class.java) { project.fileStructure.checkValidCreationTarget() }
    }

    @Test
    fun validateCache() {
        project.invalidateCache()

        val doc = project.createDocument("hello")
        val file = doc.file

        assertEquals(1, project.getCache().size)

        assertEquals(2, project.documents().size)

        file.delete()
        project.validateCache()
        assertEquals(0, project.getCache().size)

    }

    @Test
    fun assertCached() {
        project.invalidateCache()
        val doc = project.createDocument("hello")
        val file = doc.file

        assertEquals(1, project.getCache().size)
        project.getDocument(file)
        assertEquals(1, project.getCache().size)

        project.invalidateCache()
        assertEquals(0, project.getCache().size)
        project.getDocument(file)
        assertEquals(1, project.getCache().size)
    }

    @Test
    fun getCSS() {
        TODO()
    }

    @Test
    fun getMEDIA() {
        TODO()
    }

    @Test
    fun getPREFABS() {
        TODO()
    }

    @Test
    fun getBACKUP() {
        TODO()
    }

    @Test
    fun getPREFERENCES() {
        TODO()
    }

    @Test
    fun getCache() {
        TODO()
    }

    @Test
    fun reloadOpenFilesFromDisk() {
        TODO()
    }

    @Test
    fun subPath() {
        TODO()
    }

    @Test
    fun subFile() {
        TODO()
    }

    @Test
    fun projectName() {
        TODO()
    }

    @Test
    fun renameOrMoveProject() {
        TODO()
    }

    @Test
    fun fileForDocument() {
        TODO()
    }

    @Test
    fun saveMeta() {
        TODO()
    }

    @Test
    fun createDocument() {
        TODO()
    }

    @Test
    fun saveDocument() {
        TODO()
    }

    @Test
    fun testSaveDocument() {
        TODO()
    }

    @Test
    fun loadDocument() {
        TODO()
    }

    @Test
    fun deleteFile() {
        TODO()
    }

    @Test
    fun backup() {
        TODO()
    }


    @Test
    fun logError() {
        // Can't forward to unloaded project, so test using the loaded project.
        val project = mvc().Project

        project.deleteLogs()
        assertEquals(0, project.fileStructure.locationOnDisk.subFile(PROJECT_PATH_LOGS).listFiles()!!.size)
        ExceptionListener.uncaughtException(Thread.currentThread(), Exception("Test"))
        assertEquals(1, project.fileStructure.locationOnDisk.subFile(PROJECT_PATH_LOGS).listFiles()!!.size)
        project.deleteLogs()
        assertEquals(0, project.fileStructure.locationOnDisk.subFile(PROJECT_PATH_LOGS).listFiles()!!.size)
    }

    @Test
    fun documents() {
        TODO()
    }

    @Test
    fun stylesheets() {
        TODO()
    }

    @Test
    fun javascripts() {
        TODO()
    }

    @Test
    fun media() {
        TODO()
    }

    @Test
    fun getLocationOnDisk() {
        TODO()
    }
}