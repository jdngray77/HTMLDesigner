package com.jdngray77.htmldesigner.backend

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Runs tasks in the background
 */
object BackgroundTask : Subscriber {

    private val threadPool = Executors.newCachedThreadPool()

    init {
        EventNotifier.subscribe(this, EventType.USER_EXIT)
    }


    fun runOnUIThread(runnable: Runnable) {
        TODO()
    }

    
    /**
     * Submits a [runnable] task to the for [threadPool] for execution.
     */
    fun submit(runnable: Runnable) {
        threadPool.submit(runnable)
    }


    /**
     * Shutdowns the application's [threadPool].
     *
     * Executes the [threadPool]'s remaining unfinished tasks and shuts it down on completion.
     */
    private fun shutdown() {
        threadPool.shutdown()

        try {
            if (!threadPool.awaitTermination(10L, TimeUnit.SECONDS))
                threadPool.shutdownNow() // terminates remaining tasks after 10 seconds
        } catch (e: InterruptedException) {
            return
        }

        threadPool.shutdownNow() // shutdown thread pool upon task(s) completion
    }

    override fun notify(e: EventType) {
        shutdown()
    }
}