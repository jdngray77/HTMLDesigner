
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
import com.jdngray77.htmldesigner.utility.IDEEarlyBootListener
import com.sun.javafx.tk.Toolkit
import javafx.application.Platform
import java.lang.System.gc
import java.util.concurrent.*
import kotlin.concurrent.thread
import kotlin.reflect.KFunction

/**
 * ## Executes tasks in the background
 *
 * Utilises a [threadPool] that is responsible for the creation, management and disposal of application [Thread]'s
 * that delegates tasks to a pool of worker threads for execution.
 *
 * @author Dylan Brand
 */
object BackgroundTask : Subscriber, IDEEarlyBootListener {

    init {
        EventNotifier.subscribe(this, EventType.IDE_SHUTDOWN, EventType.IDE_FINISHED_LOADING)
    }

    private lateinit var threadPool : ThreadPoolExecutor

    /**
     * Asserts that the [runnable] is executed on the JavaFX Application Thread.
     *
     * If this is already on the qt thread, the runnable is executed immediately. Otherwise,
     * it's submitted to [Platform.runLater] to be executed on the qt thread later.
     */
    fun onUIThread(runnable: Runnable) {
//        if (Toolkit.getToolkit().isFxUserThread)
        if (Thread.currentThread().name == "JavaFX Application Thread")
            runnable.run()
        else
            Platform.runLater(runnable)
    }

    /**
     * Submits a [runnable] task to the for [threadPool] for execution.
     *
     * If the thread pool has been instructed to or is already shutdown, the task will not be submitted.
     */
    @Synchronized
    fun submit(runnable: Runnable): Future<*>? {
        println(threadPool)

        if (!threadPool.isShutdown && !threadPool.isTerminating)
            return threadPool.submit(runnable)

        return null
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

    override fun onIDEBootEarly() {
        threadPool = Executors.newCachedThreadPool() as ThreadPoolExecutor
        threadPool.prestartAllCoreThreads()
    }

    /**
     * A nice wrapper function that will execute function in the background.
     *
     * e.g :
     *
     * executeInBackground(this::functionName, arg1, arg2, arg3)
     *
     *
     * @param function The function to execute in the background.
     * @param args The arguments to pass to the function, if any.
     * @param applyResult A function that is called when the function has finished executing, with its result
     * @param R the return type of the function
     */
    fun <R> invokeInBackground(function: KFunction<R>, vararg args: Any?, applyResult: ((R) -> Unit)? = null) {
        submit {
            try {
                val r = function.call(*args)
                applyResult?.invoke(r)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun notify(e: EventType) {
        when (e) {
            EventType.IDE_SHUTDOWN -> shutdown()
            EventType.IDE_FINISHED_LOADING ->
                if (threadPool.isTerminated || threadPool.isShutdown || threadPool.isTerminating) {
                    // Thread pool should have been booted by now. Get it started.
                    onIDEBootEarly()

                    assert(!threadPool.isShutdown)
                    assert(!threadPool.isTerminated)
                    assert(!threadPool.isTerminating)

                    logWarning("Thread pool failed to start by the time the ide finished loading. Restarted it.")
                }

            else -> {}
        }
    }
}