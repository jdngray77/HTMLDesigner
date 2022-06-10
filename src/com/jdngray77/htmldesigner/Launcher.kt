package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.backend.ExceptionListener
import com.jdngray77.htmldesigner.frontend.Editor
import javafx.application.Application

// TODO Create a project manager / launcher thing.
fun main() {
    Thread.setDefaultUncaughtExceptionHandler(ExceptionListener)
    Application.launch(Editor::class.java)
}