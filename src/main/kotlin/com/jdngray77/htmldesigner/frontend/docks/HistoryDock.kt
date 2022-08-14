package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.DocumentState
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.utility.SerializableDocument
import javafx.collections.FXCollections
import javafx.scene.control.ListView
import org.jsoup.nodes.Document


class HistoryDock : Dock(), Subscriber {

    init {
        center = ListView<DocumentState<SerializableDocument>>().also {
            it.setOnMouseClicked {e ->
                mvc().currentEditor().apply {
                    jumpTo(it.selectionModel.selectedItem)
                    reRender()
                }
            }
        }

        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_EDITED, EventType.EDITOR_DOCUMENT_SWITCH)
    }

    override fun notify(e: EventType) {
        val history = mvc().currentEditor().documentHistory.timeline()
        (center as ListView<DocumentState<SerializableDocument>>).apply {
            items = FXCollections.observableList(history)

            selectionModel.select(mvc().currentEditor().documentHistory.currentState())
        }
    }
}