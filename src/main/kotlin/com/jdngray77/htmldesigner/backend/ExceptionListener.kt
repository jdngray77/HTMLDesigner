
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

import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvcIfAvail
import java.lang.Thread.UncaughtExceptionHandler
import java.security.PrivilegedActionException

/**
 * Added to the main thread, this listener saves log files
 * and displays notifications for unhandled exceptions.
 */
object ExceptionListener : UncaughtExceptionHandler{

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        e?.let {
            mvcIfAvail()?.Project?.logError(e)
            e.printStackTrace()
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
    }
}

/**
 * Traverses the [cause] to find the top most cause, then returns it.
 */
fun Throwable.rootCause() : Throwable =
    cause?.let { it.rootCause() } ?: this