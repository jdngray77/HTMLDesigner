package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.web.WebView
import org.jsoup.nodes.Document

class DocumentEditor {

    @FXML lateinit var contentRenderer : WebView

    lateinit var document : Document
        private set

    lateinit var tab: Tab

    var isDirty = false
        private set

    fun setDocument(document: Document, tab: Tab) {
        if (this::document.isInitialized) return

        this.document = document
        this.tab = tab

        clean()
        reRender()
    }

    fun reRender() {
        contentRenderer.engine.loadContent(document.toString())
    }

    fun dirty() {
        if (isDirty) return

        tab.text += UNSAVED_SUFFIX
        isDirty = true
    }

    fun clean() {
        tab.text = document.title()
        isDirty = false
    }

    fun save() =
        mvc().Project.saveDocument(document)

    companion object {
        private const val UNSAVED_SUFFIX = " *"
    }
}