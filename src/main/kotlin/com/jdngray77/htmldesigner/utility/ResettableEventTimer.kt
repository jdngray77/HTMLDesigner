package com.jdngray77.htmldesigner.utility

/**
 * A threaded timer that lumps multiple event triggers together,
 * theoretically limiting the number of events that are fired as a result.
 *
 * On the first [trigger], the timer will start elapsing [delay] milliseconds.
 *
 * If more [trigger]s occour whilst the timer is running, the timer is reset, and we
 * continue waiting.
 *
 * However, when the [delay] elapses without any more [trigger]s,
 * the [onElapsed] function is called, and the timer
 */
class ResettableEventTimer(

    /**
     * The number of milliseconds to wait before firing the event.
     */
    val delay: Long,

    /**
     * Function to run to commit all changes.
     */
    val onElapsed: () -> Unit

) {

    /**
     * The time that has elapsed since the last trigger.
     */
    @Volatile
    @get:Synchronized
    @set:Synchronized
    private var timeElapsed = 0L

    /**
     * The time of the last trigger.
     *
     * Used to calculate [timerElapsed].
     */
    @Volatile
    @get:Synchronized
    @set:Synchronized
    private var lastTrigger = System.currentTimeMillis()

    /**
     * The thread that is running the timer.
     *
     * Only usable for one trigger of [onElapsed].
     *
     * Once the timer has been triggered, the timer thread will be killed.
     *
     * A new thread will be created the next time the timer is triggered.
     */
    private var timerThread : Thread = Thread(CommitTimerTask())
        set(value) {

            // For safety, make sure old threads are disposed.
            field.interrupt()
            field.stop()

            field = value
        }


    /**
     * Starts a new [timerThread]
     */
    private fun newThread() {
        timerThread = Thread(null, CommitTimerTask(), "Commit timer thread")
        timerThread.isDaemon = true
        timerThread.start()
    }

    /**
     * Resets the timer.
     */
    private fun reset() {
        timeElapsed = 0L
        lastTrigger = System.currentTimeMillis()
    }

    /**
     * Triggers the timer to start / restart.
     *
     * Call on every event that should be grouped together.
     */
    fun trigger() {
        if (!timerThread.isAlive) {
            newThread()
        } else reset()
    }

    /**
     * The timer thread
     */
    inner class CommitTimerTask : Runnable {

        override fun run() {

            reset() // Ensure that the timer is cleared at start.

            while (!Thread.interrupted() && timeElapsed < delay) {
                // To help with the volatile caching.
                // Constant reads jams up the jvm sometimes.
                Thread.sleep(1)

                timeElapsed = (System.currentTimeMillis() - lastTrigger)
            }

            if (!Thread.interrupted())
                onElapsed()
        }
    }

    fun finalize() {
        timerThread.interrupt()
    }
}