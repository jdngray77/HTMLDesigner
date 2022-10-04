package com.jdngray77.htmldesigner.backend.data

import com.jdngray77.htmldesigner.RequiresEditorGUI
import com.jdngray77.htmldesigner.backend.ExceptionListener
import com.jdngray77.htmldesigner.backend.data.Project.Companion.PROJECT_PATH_LOGS
import com.jdngray77.htmldesigner.backend.data.Project.Companion.PROJECT_PATH_META
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
        const val TEST_PROJECT_PATH = "./testProject"
    }

    @BeforeEach
    internal fun setUp() {
        if (File(TEST_PROJECT_PATH).exists()) {
            File(TEST_PROJECT_PATH).deleteRecursively()
        }

        project = Project.create(TEST_PROJECT_PATH)
    }

    @AfterEach
    internal fun tearDown() {
        project.deleteProject()
    }

    @Test
    fun testPath() {
        assertEquals(File(TEST_PROJECT_PATH).absolutePath, project.locationOnDisk.absolutePath)
    }

    @Test
    fun getAuthor() {
        project.author = "My Name Here"
        assertEquals("My Name Here", project.author)
    }

    @Test
    fun hasDefaultHTMLDocument() {
        assertEquals(1, project.HTML.listFiles()?.size ?: 1)
        val file = project.HTML.listFiles()!!.first()

        assertDoesNotThrow {
            project.loadDocument(file)
        }

        val defaultDocument = project.loadDocument(file)

        assertEquals(file.name, defaultDocument.title())
    }

    @Test
    fun jsGraph() {
        // No files to start
        assertEquals(0, project.JS.listFiles()?.size ?: 0)

        project.invalidateCache()
        assertEquals(0, project.getCache().size)

        // Create file
        val graph = project.createJsGraph("myscript")

        assertEquals(0, graph.getNodes().size)

        assertTrue(project.getCache().containsValue(graph))

        // there is one file, and the graph is cached
        assertEquals(1, project.JS.listFiles()?.size ?: 0)

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

        assertEquals(0, project.JS.listFiles()?.size ?: null)

        assertEquals(0, project.getCache().size)

        // Delete no such file

        assertThrowsExactly(NoSuchFileException::class.java) { project.deleteJsGraph("i don't exist") }


        // save modification

        graph.addFunction(JsRandomColorFunction())
        project.saveJsGraph(graph)

        project.invalidateCache()
        val graph1 = project.loadJsGraph("myscript")
        assertEquals(1, graph1.getNodes().size)
        assertEquals(JsRandomColorFunction().name, graph1.getFunctionNodes().first()!!.function.name)

    }

    @Test
    fun document() {
        // No files to start
        assertEquals(0, project.HTML.listFiles()?.size ?: 0)

        project.invalidateCache()
        assertEquals(0, project.getCache().size)

        // Create file
        val doc = project.createDocument("hello")
        val file = project.fileForDocument(doc)

        assertTrue(file.exists())

        // configured document
        assertEquals("hello", doc.title())
        assertEquals("hello", doc.getElementById("PageTitle")!!.text())

        assertNotNull(doc.getStylesheet(CSS_ID_DEBUG))
        assertNotNull(doc.getStylesheet(CSS_SHEET_DEBUG))
        assertNotNull(doc.getStylesheet(CSS_ID_DOCUMENT_SPECIFIC))

        assertTrue(project.getCache().containsValue(doc))

        // Create dupe
        assertThrowsExactly(FileAlreadyExistsException::class.java) { project.createDocument("hello") }


        // there is one file, and the graph is cached
        assertEquals(1, project.HTML.listFiles()?.size ?: 0)

        assertEquals(1, project.getCache().size)

        // load from cache
        assertDoesNotThrow { project.loadDocument(file) }

        assertEquals(1, project.getCache().size)

        // load without cache
        project.invalidateCache()

        assertEquals(0, project.getCache().size)

        assertDoesNotThrow { project.loadDocument(file) }


        // Load non-existant

        assertThrowsExactly(NoSuchFileException::class.java) { project.loadDocument(File("i don't exist")) }

        // Delete
        project.deleteFile(file)

        assertEquals(0, project.JS.listFiles()?.size)

        assertEquals(0, project.getCache().size)



        // Modify, save, reload

        doc.getElementById("PageTitle")!!.text("Test")

        project.saveDocument(doc)
        project.saveDocument(doc, project.HTML.subFile("test.html"))

        // load changes from disk
        project.invalidateCache()

        val doc2 = project.loadDocument(file)
        assertEquals("Test", doc2.getElementById("PageTitle")!!.text())

        val doc3 = project.loadDocument(project.HTML.subFile("test.html"))
        assertEquals("Test", doc3.getElementById("PageTitle")!!.text())

    }

    @Test
    fun testMissingFolder() {
        assertDoesNotThrow { project.validate() }

        project.HTML.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.validate() }
        tearDown()
        setUp()

        project.JS.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.validate() }
        tearDown()
        setUp()

        project.CSS.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.validate() }
        tearDown()
        setUp()

        project.MEDIA.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.validate() }
        tearDown()
        setUp()

        project.BACKUP.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.validate() }
        tearDown()
        setUp()

        project.PREFABS.deleteRecursively()
        assertThrowsExactly(IllegalStateException::class.java) { project.validate() }
        tearDown()
        setUp()
    }

    @Test
    fun testCheckPath() {
        // Project already exists.
        assertThrowsExactly(IllegalArgumentException::class.java) { project.checkPath() }

        // No project, but folder is not empty.
        project.locationOnDisk.subFile(PROJECT_PATH_META).delete()
        assertThrowsExactly(IllegalArgumentException::class.java) { project.checkPath() }
    }

    @Test
    fun validateCache() {
        project.invalidateCache()
        val doc = project.createDocument("hello")
        val file = project.fileForDocument(doc)
        assertEquals(1, project.getCache().size)

        assertEquals(2, project.HTML.listFiles()!!.size)

        file.delete()
        project.validateCache()
        assertEquals(0, project.getCache().size)

    }

    @Test
    fun assertCached() {
        project.invalidateCache()
        val doc = project.createDocument("hello")
        val file = project.fileForDocument(doc)

        assertEquals(1, project.getCache().size)
        project.assertCached(file,doc)
        assertEquals(1, project.getCache().size)

        project.invalidateCache()
        assertEquals(0, project.getCache().size)
        project.assertCached(file,doc)
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
        assertEquals(0, project.locationOnDisk.subFile(PROJECT_PATH_LOGS).listFiles()!!.size)
        ExceptionListener.uncaughtException(Thread.currentThread(), Exception("Test"))
        assertEquals(1, project.locationOnDisk.subFile(PROJECT_PATH_LOGS).listFiles()!!.size)
        project.deleteLogs()
        assertEquals(0, project.locationOnDisk.subFile(PROJECT_PATH_LOGS).listFiles()!!.size)
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