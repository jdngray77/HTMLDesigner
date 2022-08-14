package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import java.io.*
import java.lang.System.gc
import java.util.*

/**
 * Represents the current state of some kind of object.
 * Typically a document, when used in an editing programme.
 *
 * Uses serialization to take a copy of the state of the object
 * given to store.
 *
 * This makes it possible to push the same object to the history
 * time and time again.
 *
 * Each time, we'll take a snapshot of the object.
 *
 * If we didn't each state in the history would be the same.
 *
 * The timeline is made of two sections
 *
 * @author Jordan Gray
 */
class DocumentState<T : Serializable>(_state: T, val actionName: String) {

    var data: ByteArray

    fun getState(): T {
        ByteArrayInputStream(data).use {
            ObjectInputStream(it).use {
                return it.readObject() as T
            }
        }
    }

    init {
        val baos = ByteArrayOutputStream()
        val os = ObjectOutputStream(baos)

        os.writeObject(_state)
        os.close()

        data = baos.toByteArray()
        baos.close()
    }

    override fun toString() = actionName
}

/**
 * A stack of document states.
 */
internal typealias Timeline<T> = Stack<DocumentState<T>>

typealias DocumentChangeListener <T> = (after: T) -> Unit

// TODO handle change of limit.

/**
 *
 */
class DocumentUndoRedo<T : Serializable>(initialState: T) {

    /**
     * The history of changes that have been made.
     */
    private val pastStates: Timeline<T> = Timeline()

    /**
     * Changes removed by an undo, that may be re-applied on a redo.
     */
    private val futureStates: Timeline<T> = Timeline()

    fun pastStateCount() = pastStates.size - 1

    fun futureStateCount() = futureStates.size

    var historyLimit = Config[Configs.UNDO_HISTORY_MAX_INT] as Int
        set(value) {
            assert(value >= 1)

            if (value < 1)
                throw IllegalArgumentException("History limit must be >= 1")

            if (value < field)
                for (i in pastStateCount() - value downTo 1)
                    pastStates.removeFirst()

            field = value
        }

    private var onChangeListener : DocumentChangeListener<T>? = null

    init {
        pastStates.push(DocumentState(initialState, "Initial state"))
    }

    /**
     * Adds a new state to the history.
     *
     * Before :
     * ```
     * History     Future
     * [1,2,3,4] | [6,7]
     * ```
     * After :
     * ```
     * History
     * [1,2,3,4,8] |
     * ```
     *
     * Clears any ability to [redo].
     *
     *
     * If [pastStates] is larger than [Configs.UNDO_HISTORY_MAX_INT], then the
     * oldest state is deleted from the timeline.
     */
    fun push(doc: T, actionName: String = "Unknown change") {
        // Prevent duplicates.
        if (pastStates.peek() == doc) return

        futureStates.clear()
        gc()

        pastStates.push(DocumentState(doc, actionName))

        if (pastStates.size > historyLimit + 1) {
            pastStates.removeFirst()
            gc()
        }

        changed(doc)
    }

    /**
     * Removes the last state from the history,
     * and moves it to the future.
     *
     * States stores in the future can be re-applied with [redo],
     * until the next [push] ocours.
     */
    fun undo(): T {
        if (pastStates.size == 1) return getDocument()

        pastStates.pop().apply {
            futureStates.push(this)
//            println(this@DocumentUndoRedo.toString())

            return pastStates.peek().getState().also {
                changed(it)
            }
        }
    }

    /**
     * Re-applies the last state removed by [undo], if any.
     *
     * Cannot [redo] [undo]'s that ococured before the last [push]
     *
     * @return the current state of the document
     */
    fun redo(): T {
        if (futureStates.isEmpty()) return getDocument()

        futureStates.pop().apply {
            pastStates.push(this)
//            println(this@DocumentUndoRedo.toString())
            return getState().also {
                changed(it);
            }
        }


    }

    /**
     * Peeks the current state of the document.
     */
    fun getDocument(): T =
        currentState().getState()

    /**
     * Returns the [DocumentState] in the timeline
     * holding the current document.
     *
     * Use [getDocument] to fetch the current
     * document.
     */
    fun currentState(): DocumentState<T> =
        pastStates.peek()

    /**
     * Returns the entire timeline of previous and future states.
     *
     * The current state can be found using [currentState]
     */
    fun timeline() = pastStates + futureStates.reversed()

    /**
     * @return true if [state] appears anywhere in the the [timeline], future or past.
     */
    fun stateInTimeline(state: DocumentState<T>) =
        timeline().contains(state)


    /**
     * Performs undo until the current state
     *
     * @throws NoSuchFileException if the [state] does not exist in the [timeline]. You can check for this prior with [stateInTimeline].
     */
    fun jumpTo(state: DocumentState<T>) {
        if (!stateInTimeline(state))
            _throwJumpNoSuchElement()

        // For redundant safety, we'll check them again anyway.

        if (futureStates.contains(state))
            redoTo(state)
        else if (pastStates.contains(state))
            undoTo(state)
        else
            _throwJumpNoSuchElement()
    }

    /**
     * Performs [undo] until the [currentState] is the first
     * entry in the timeline.
     */
    fun undoAll() =
        undoTo(pastStates.first())

    /**
     * Performs [redo] until the [currentState] is the last
     * entry in the timeline.
     */
    fun redoAll() =
        redoTo(futureStates.first())

    /**
     * Performs [undo] until the [currentState] is [state]
     *
     * @throws NoSuchElementException If [state] is not in timeline
     */
    fun undoTo(state: DocumentState<T>) {
        if (!pastStates.contains(state))
            _throwJumpNoSuchElement()

        while (
            currentState() != state &&
            pastStates.size > 1   // For safety. Prevents infinite loops, just in case.
        )
            undo()

    }

    /**
     * Performs [redo] until the [currentState] is [state]
     *
     * @throws NoSuchElementException If [state] is not in timeline
     */
    fun redoTo(state: DocumentState<T>) {
        if (!futureStates.contains(state))
            _throwJumpNoSuchElement()

        while (
            currentState() != state &&
            futureStates.size > 0   // For safety. Prevents infinite loops, just in case.
        )
            redo()
    }


    private fun _throwJumpNoSuchElement() {
        throw NoSuchElementException("Tried to jump to a document state, but the state does not exist in the timeline!")
    }

    override fun toString() = "History : $pastStates\nFuture : $futureStates"

    fun setOnChange(listener: DocumentChangeListener<T>) {
        onChangeListener = listener
    }

    private fun changed(after: T) {
        onChangeListener?.invoke(after)
    }
}
