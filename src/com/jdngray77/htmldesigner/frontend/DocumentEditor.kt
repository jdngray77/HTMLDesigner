package com.jdngray77.htmldesigner.frontend

import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.web.WebView
import org.jsoup.nodes.Document

class DocumentEditor {

    @FXML lateinit var contentRenderer : WebView

    lateinit var document : Document
        private set

    lateinit var tab: Tab

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
        if (!tab.text.endsWith(UNSAVED_SUFFIX))
            tab.text += UNSAVED_SUFFIX
    }

    fun clean() {
        tab.text = document.title()
    }

    companion object {
        private const val UNSAVED_SUFFIX = " *"
    }
}