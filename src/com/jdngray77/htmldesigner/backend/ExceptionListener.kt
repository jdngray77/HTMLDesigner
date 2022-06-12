package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvcIfAvail
import java.lang.Thread.UncaughtExceptionHandler
import java.security.PrivilegedActionException

object ExceptionListener : UncaughtExceptionHandler{
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        e?.let {
            mvcIfAvail()?.Project?.logError(e)
            e.printStackTrace()
            NotifyOfError(sanitizeException(e))
        }
    }

    private fun sanitizeException(e: Throwable) =
        if (e is PrivilegedActionException) e.exception else e.rootCause()
}


fun Throwable.rootCause() : Throwable =
    cause?.let { it.rootCause() } ?: this