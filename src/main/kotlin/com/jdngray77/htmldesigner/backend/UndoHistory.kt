package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import java.io.*
import java.lang.System.gc
import java.util.Stack

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
internal class DocumentState<T : Serializable>( _state: T) {

    var data: ByteArray

    fun getState() : T {
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

    override fun toString() = getState().toString()
}

/**
 * A stack of document states.
 */
internal typealias Timeline<T> = Stack<DocumentState<T>>

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

    init {
        pastStates.push(DocumentState(initialState))
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
    fun push(doc: T) {
        // Prevent duplicates.
        if (pastStates.peek() == doc) return

        futureStates.clear()
        gc()

        pastStates.push(DocumentState(doc))

        if (pastStates.size > historyLimit + 1) {
            pastStates.removeFirst()
            gc()
        }
    }

    /**
     * Removes the last state from the history,
     * and moves it to the future.
     *
     * States stores in the future can be re-applied with [redo],
     * until the next [push] ocours.
     */
    fun undo(): T {
        if (pastStates.size == 1) return currentState()

        pastStates.pop().apply {
            futureStates.push(this)
//            println(this@DocumentUndoRedo.toString())

            return pastStates.peek().getState()
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
        if (futureStates.isEmpty()) return currentState()

        futureStates.pop().apply {
            pastStates.push(this)
//            println(this@DocumentUndoRedo.toString())
            return getState()
        }
    }

    /**
     * Peeks the current state of the document.
     *
     * May be null, if no states have been added.
     */
    fun currentState(): T =
            pastStates.peek().getState()


    override fun toString() = "History : $pastStates\nFuture : $futureStates"
}
