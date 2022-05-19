package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import javafx.application.Application
import java.io.File

// TODO Create a project manager / launcher thing.
fun main() {
    Application.launch(Editor::class.java)
    EDITOR.start(Project(File ("")))
}