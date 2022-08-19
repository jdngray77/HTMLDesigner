package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.RequiresEditorGUI
import com.jdngray77.htmldesigner.frontend.jsdesigner.IncompatibleEmissionException
import com.jdngray77.htmldesigner.frontend.jsdesigner.JsGraph
import org.jsoup.nodes.Element
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RequiresEditorGUI::class)
internal class JsGraphNodeTest {

    lateinit var data: JsGraph

    @BeforeEach
    fun setUp() {
        data = JsGraph()

        val h1 = data.addElement(Element("h1").id("h1"))
        val clickEmitter = h1.emitters().first()

        val a = data.addElement(Element("a").id("a"))
        val visibilityReceiver = a.recievers().last()

        clickEmitter.emit(visibilityReceiver)
    }

    @Test
    internal fun IncompatTypes() {
        val h1 = data.getElementNode("h1")
        assertNotNull(h1)

        val clickEmitter = h1!!.emitters().first()

        val a = data.getElementNode("a")
        assertNotNull(a)

        val colorReceiver = a!!.recievers().first()

        assertThrows(IncompatibleEmissionException::class.java) { clickEmitter.emit(colorReceiver) }
    }

    @Test
    fun removeAllConnections() {
        val h1 = data.getNodes().first()
        val a = data.getNodes().last()

        val emissions = h1.emitters().first().emissions()
        val admissions = a.recievers().last().admissions()
        assertEquals(1, emissions.size)
        assertEquals(1, admissions.size)

        h1.removeAllConnections()

        assertEquals(0, h1.emitters().first().emissions().size)
        assertEquals(0, a.recievers().first().admissions().size)
    }
}