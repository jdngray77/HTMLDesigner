
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

package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.BackgroundTask.threadPool
import com.jdngray77.htmldesigner.backend.utility.Restartable
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
object BackgroundTask : Subscriber, Restartable {

    private val threadPool : ThreadPoolExecutor = Executors.newCachedThreadPool() as ThreadPoolExecutor

    init {
        EventNotifier.subscribe(this, EventType.USER_EXIT)
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
            // Politely wait for current tasks to close.
            if (!threadPool.awaitTermination(10L, TimeUnit.SECONDS))
                // If there are still tasks running after timeout, force close.
                onShutdownInterrupted()
        } catch (e: InterruptedException) {
            // if this thread was interrupted whilst waiting for tasks to close
            onShutdownInterrupted()
        }
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

    override fun onIDERestart() {

    }
}