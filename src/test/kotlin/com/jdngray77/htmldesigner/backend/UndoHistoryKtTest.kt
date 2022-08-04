package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.RequiresEditorGUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals


internal class UndoHistoryKtTest {

    private lateinit var history: DocumentUndoRedo<String>

    @org.junit.jupiter.api.Test
    fun testOvershoot() {

        // Current
        assertEquals(0, history.futureStateCount(), "There should be 0 future states, since we've not undone anything yet")
        assertEquals("Hello World", history.currentState(), "The present value should be 'hello world'")

        // Undo
        assertEquals("Hello Worl", history.undo())
        assertEquals("Hello Wor", history.undo())
        assertEquals("Hello Wo", history.undo())
        assertEquals("Hello W", history.undo())
        assertEquals("Hello ", history.undo())
        assertEquals("Hello", history.undo())
        assertEquals("Hell", history.undo())
        assertEquals("Hel", history.undo())
        assertEquals("He", history.undo())
        assertEquals("H", history.undo())

        // Overshoot undo
        assertEquals("empty", history.undo())
        assertEquals("empty", history.undo())
        assertEquals("empty", history.undo())

        // Redo
        assertEquals("H", history.redo())
        assertEquals("He", history.redo())
        assertEquals("Hel", history.redo())
        assertEquals("Hell", history.redo())
        assertEquals("Hello", history.redo())
        assertEquals("Hello ", history.redo())
        assertEquals("Hello W", history.redo())
        assertEquals("Hello Wo", history.redo())
        assertEquals("Hello Wor", history.redo())
        assertEquals("Hello Worl", history.redo())

        // Overshoot redo
        assertEquals("Hello World", history.redo())
        assertEquals("Hello World", history.redo())
        assertEquals("Hello World", history.redo())
    }

    @org.junit.jupiter.api.Test
    fun lowerHistoryLimit2() {
        // If we lower the limit, it should remove the oldest states leaving us
        // with the newest two states only.
        history.historyLimit = 2
        assertEquals(2, history.pastStateCount(), "All but the two newest states should've been removed")
    }

    @org.junit.jupiter.api.Test
    fun lowerHistoryLimit4() {
        // If we lower the limit, it should remove the oldest states leaving us
        // with the newest two states only.
        history.historyLimit = 4
        assertEquals(4, history.pastStateCount(), "All but the four newest states should've been removed")
    }





    @BeforeEach
    internal fun setUp() {
        history = DocumentUndoRedo("empty")

        // The limit is linked to the config, so
        // We'll override it for the tests for consistency.
        history.historyLimit = 20

        history.push("H")
        history.push("He")
        history.push("Hel")
        history.push("Hell")
        history.push("Hello")
        history.push("Hello ")
        history.push("Hello W")
        history.push("Hello Wo")
        history.push("Hello Wor")
        history.push("Hello Worl")
        history.push("Hello World")
    }
}