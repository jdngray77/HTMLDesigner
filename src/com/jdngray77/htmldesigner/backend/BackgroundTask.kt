package com.jdngray77.htmldesigner.backend

import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * ## Executes tasks in the background
 *
 * Utilises a [threadPool] that is responsible for the creation, management and disposal of application [Thread]'s
 * that delegates tasks to a pool of worker threads for execution.
 *
 * @author Dylan Brand
 */
object BackgroundTask : Subscriber {

    private val threadPool : ThreadPoolExecutor = Executors.newCachedThreadPool()

    init {
        EventNotifier.subscribe(this, EventType.USER_EXIT)
    }


    /**
     * ### Executes a [runnable] task.
     *
     * Executes the given task on a new thread, a pooled thread or the calling thread, at the discretion of the
     * [threadPool]. There are no guarantees that the task will be executed immediately.
     */
    fun execute(runnable: Runnable) {
        threadPool.execute(runnable)
    }


    @Deprecated("Currently not implemented.", ReplaceWith("TODO()"))
    fun runOnUIThread(runnable: Runnable) {
        TODO()
    }


    /**
     * Submits a [runnable] task to the for [threadPool] for execution.
     *
     * If the thread pool has been instructed to or is already shutdown, the task will not be submitted.
     */
    @Synchronized
    fun submit(runnable: Runnable) {
        if (!threadPool.isShutdown && threadPool.isTerminating)
            threadPool.submit(runnable)
    }


    /**
     * Returns all tasks waiting for execution on a worker [Thread] in the [threadPool].
     *
     * @return Tasks waiting in the [threadPool] queue for execution.
     */
    fun scheduledTasks() = threadPool.queue


    /**
     * Shutdowns the application's [threadPool].
     *
     * Rejects the submission of new tasks and executes the [threadPool]'s remaining unfinished tasks - shutting
     * it down on completion.
     */
    private fun shutdown() {
        threadPool.shutdown()

        try {
            if (!threadPool.awaitTermination(10L, TimeUnit.SECONDS))
                threadPool.shutdownNow() // terminates remaining tasks after 10 seconds
        } catch (e: InterruptedException) {
            threadPool.shutdownNow() // if await termination's calling thread is interrupted, shutdown the thread pool
            // TODO - dialog functionality for user in the event of an error
    }

    /**
     * Handles a failure to close the thread pool.
     */
    private fun onShutdownInterrupted() {
        // TODO - dialog functionality for user in the event of an error
        threadPool.shutdownNow()
    }

    override fun notify(e: EventType) {
        // TODO - remove when necessary
        if (e == EventType.USER_EXIT)
            shutdown()
    }
}