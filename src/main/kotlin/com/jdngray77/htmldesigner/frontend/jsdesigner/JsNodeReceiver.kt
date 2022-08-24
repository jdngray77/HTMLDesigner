package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphProperty
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphReceiver
import com.jdngray77.htmldesigner.utility.addIfAbsent
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.text.Text


/**
 * A gui [JsNodeProperty] of a [JsNode] that can receive a connection.
 *
 * Represents a [JsGraphReceiver].
 */
class JsNodeReceiver: JsNodeProperty<JsGraphReceiver>() {

    /**
     * FXML init.
     *
     * Configures listeners for drag events,
     * handling dragging of connections.
     *
     * Committing of new connections occurs here.
     */
    @FXML
    fun initialize() {

        // Dim when dragging in a new connection
        socket.setOnMouseDragEntered {
            if (receiver().hasAdmission() || receiver().type != emitterBeingDragged.property().type)
                return@setOnMouseDragEntered


            socket.styleClass.addIfAbsent("populated")
            it.consume()
        }

        // Commit a new connection. This is the reciever.
        socket.setOnMouseDragReleased {
            if (receiver().hasAdmission() || receiver().type != emitterBeingDragged.property().type)
                return@setOnMouseDragReleased

            graphEditor.uncommittedLine.isVisible = false

            emitterBeingDragged.guiNode.emitConnection(emitterBeingDragged, this)

            assertPopulationCss()
            emitterBeingDragged.assertPopulationCss()


            graphEditor.invalidateTouches()
            it.consume()
        }

        // Drag did not commit (But also triggered when drag is committed)
        socket.setOnMouseDragExited {
            if (receiver().hasAdmission()) {
                socket.styleClass.addIfAbsent("populated")
            } else
                socket.styleClass.remove("populated")
        }

        socket.setOnContextMenuRequested {
            if (receiver().hasAdmission())
                guiNode.breakdownConnection(receiver().admission!!)

            it.consume()
        }
    }
}



