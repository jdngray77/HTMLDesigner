package com.jdngray77.htmldesigner.backend

/**
 * Runs tasks in the background
 */
object BackgroundTask {

    fun runOnUIThread(runnable: Runnable) {
        TODO()
    }

    fun submit(runnable: Runnable) {
        TODO()
    }

    /**
     * - Disallow new tasks to be submitted,
     * - wait here for all threads to close?
     * - close the thread pool
     */
    fun shutdown() {

    }

}