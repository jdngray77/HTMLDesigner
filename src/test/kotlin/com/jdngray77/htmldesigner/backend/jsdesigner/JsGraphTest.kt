package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.RequiresEditorGUI
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(RequiresEditorGUI::class)
internal class JsGraphTest {

    lateinit var data: JsGraph

    @BeforeEach
    internal fun setUp() {
        data = JsGraph()

        data.addElement(Element("h1").id("h1"))
        data.addElement(Element("a").id("a"))
    }

    @Test
    fun getNodes() {
        assertEquals(2, data.getNodes().size)

        data.addElement(Element("h2").id("h2"))
        assertEquals(3, data.getNodes().size)

        data.removeNode("h2")
        assertEquals(2, data.getNodes().size)
    }

    @Test
    fun getElementNode() {
        assertNotNull(data.getElementNode("h1"))

        data.removeNode("h1")

        assertNull(data.getElementNode("h1"))
    }

    @Test
    fun assertElementExists() {
        assertDoesNotThrow { data.assertElementExists("h1") }
        data.removeNode("h1")
        assertThrows(IllegalArgumentException::class.java) { data.assertElementExists("h1") }
    }

    @Test
    fun assertElementDoesNotExist() {
        assertThrows(IllegalArgumentException::class.java) { data.assertElementDoesNotExist("h1") }
        data.removeNode("h1")
        assertDoesNotThrow { data.assertElementDoesNotExist("h1") }
    }

    @Test
    fun addElement() {

        assertThrows(IllegalArgumentException::class.java) { data.addElement(Element("h1").id("h1")) }
        data.addElement(Element("h2").id("test"))

        assertNotNull(data.getElementNode("test"))
    }

    @Test
    fun removeNode() {
        assertThrows(IllegalArgumentException::class.java) { data.removeNode("test") }
        data.addElement(Element("h2").id("test"))

        assertNotNull(data.getElementNode("test"))
    }

    @Test
    fun testRemoveNode() {
        val first = data.getNodes().first()

        assertDoesNotThrow { data.removeNode(first) }

        assertFalse(data.getNodes().contains(first))
    }
}