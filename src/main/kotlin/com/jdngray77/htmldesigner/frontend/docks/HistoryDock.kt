package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.DocumentState
import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeEditor
import com.jdngray77.htmldesigner.utility.SerializableDocument
import javafx.collections.FXCollections
import javafx.scene.control.ListView


class HistoryDock : Dock(), Subscriber {

    init {
        center = ListView<DocumentState<*>>().also {
            it.setOnMouseClicked { _ ->
                activeEditor()?.apply {

                    val selected = it.selectionModel.selectedItem

                    if (selected::class.java == this::jumpTo.parameters.first().type.javaClass)
                        return@setOnMouseClicked

                    this::jumpTo.call(
                        selected
                    )
                }
            }
        }

        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_EDITED, EventType.EDITOR_DOCUMENT_SWITCH)
    }

    override fun notify(e: EventType) {
        val history = activeEditor()?.history?.timeline()

        (center as ListView<DocumentState<*>>).apply {
            items = FXCollections.observableList(history)

            selectionModel.select(activeEditor()?.history?.currentState())
        }
    }
}