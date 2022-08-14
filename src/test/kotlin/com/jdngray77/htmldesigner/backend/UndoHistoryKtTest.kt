package com.jdngray77.htmldesigner.backend

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue


internal class UndoHistoryKtTest {

    private lateinit var history: DocumentUndoRedo<String>

    @Test
    fun testOvershoot() {

        // Current
        assertEquals(0, history.futureStateCount(), "There should be 0 future states, since we've not undone anything yet")
        assertEquals("Hello World", history.getDocument(), "The present value should be 'hello world'")

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

    @Test
    fun lowerHistoryLimit2() {
        // If we lower the limit, it should remove the oldest states leaving us
        // with the newest two states only.
        history.historyLimit = 2
        assertEquals(2, history.pastStateCount(), "All but the two newest states should've been removed")
    }

    @Test
    fun lowerHistoryLimit4() {
        // If we lower the limit, it should remove the oldest states leaving us
        // with the newest two states only.
        history.historyLimit = 4
        assertEquals(4, history.pastStateCount(), "All but the four newest states should've been removed")
    }

    @Test
    internal fun jumpForward() {
        // Pick a state to jump to
        val desiredState = history.timeline().let {
            it[ it.size-2 ]
        }

        history.undo()
        history.undo()
        history.undo()
        history.undo()
        history.undo()
        history.undo()
        assertEquals("Hello", history.getDocument(), "Document should be 'Hello' after undoing ' world'")


        // Jump to state
        history.jumpTo(desiredState)
        assertEquals(desiredState, history.currentState(), "History should have jumped to the second to last state.")
    }

    @Test
    internal fun jumpBackward() {
        // Pick a state to jump to. // Second in the timeline.
        val desiredState = history.timeline()[1]

        assertEquals("Hello World", history.getDocument())


        // Jump to state
        history.jumpTo(desiredState)
        assertEquals(desiredState, history.currentState(), "History should have jumped to the second state.")
    }

    @Test
    internal fun jumpInvalid() {
        // Pick a state to jump to. // Second in the timeline.
        val desiredState = DocumentState<String>("I'm not in the timeline!", "")

        assertEquals("Hello World", history.getDocument())

        assertThrows<java.util.NoSuchElementException> {
            history.jumpTo(desiredState)
        }
    }

    @Test
    internal fun undoAll() {
        history.undoAll()
        assertEquals("empty", history.getDocument())
        assertEquals(history.timeline().first(), history.currentState())
    }

    @Test
    internal fun redoAll() {
        history.undoAll()
        history.redoAll()
        assertEquals("Hello World", history.getDocument())
        assertEquals(history.timeline().last(), history.currentState())
    }

    @Test
    internal fun inTimeline() {
        assertTrue(history.stateInTimeline(history.currentState()))
        assertTrue(history.stateInTimeline(history.timeline().first()))
        assertTrue(history.stateInTimeline(history.timeline().last()))
        assertFalse(history.stateInTimeline(DocumentState("", "")))
    }

    @Test
    internal fun pushClearsFuture() {
        assertEquals(0, history.futureStateCount())
        undoAll()
        assertNotEquals(0, history.futureStateCount())
        history.push("Hello World!")

        assertEquals(0, history.futureStateCount())
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