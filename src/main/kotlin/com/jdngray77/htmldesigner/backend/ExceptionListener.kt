
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

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvcIfAvail
import java.lang.Thread.UncaughtExceptionHandler
import java.security.PrivilegedActionException

/**
 * Added to the main thread, this listener saves log files
 * and displays notifications for unhandled exceptions.
 */
object ExceptionListener : UncaughtExceptionHandler {

    /**
     * The number of errors since JVM boot.
     *
     * Not cleared on soft restarts.
     */
    var errCount = 0
        private set

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        e?.let {
            mvcIfAvail()?.Project?.logError(e)
            showErrorNotification(sanitizeException(e))
            checkSpecial(e)
        }
    }

    private fun sanitizeException(e: Throwable) =
        if (e is PrivilegedActionException) e.exception else e.rootCause()

    private fun checkSpecial(e: Throwable) {
        when (e) {
            is IllegalStateException -> {
                if (e.message?.startsWith("Not on FX application thread") == true) {
                    showErrorNotification(e, false)
                    showNotification("Developers : Not on FX thread","Is a subscriber on the wrong thread?\n\nCheck stack trace.")
                }
            }
            else -> {}
        }

        if (Config[Configs.LARGE_ERROR_COUNT_PROMPT_BOOL] as Boolean) {
            errCount ++
            if (errCount % Config[Configs.LARGE_ERROR_COUNT_STEP_THRESHOLD_INT] as Int == 0) {
                showWarningNotification("We suggest restarting the IDE.",
                    "The IDE has encountered $errCount errors (so far...).\n\nTo be safe, please save your work and restart the IDE.\n\nThese prompts can be disabled in the registry.\n(LARGE_ERROR_COUNT_PROMPT_BOOL)")
            }
        }
    }
}

/**
 * Traverses the [cause] to find the top most cause, then returns it.
 */
fun Throwable.rootCause() : Throwable =
    cause?.let { it.rootCause() } ?: this