package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphEmitter
import com.jdngray77.htmldesigner.utility.addIfAbsent
import javafx.fxml.FXML
import javafx.geometry.Bounds


/**
 * A gui [JsNodeProperty] of a [JsNode] that can emit connections.
 *
 * Represents a [JsGraphEmitter].
 */
class JsNodeEmitter : JsNodeProperty<JsGraphEmitter>() {

    /**
     * FXML init.
     *
     * Configures listeners for drag events,
     * handling dragging of connections.
     *
     * This begins the dragging of connections,
     * but does not handle the creation of the connections.
     */
    @FXML
    fun initialize() {

        // Start a new emission.
        socket.setOnMousePressed {
            with(graphEditor.uncommittedLine) {
                val screenBounds: Bounds = socket.localToScene(socket.boundsInLocal)

                startX = screenBounds.centerX
                startY = screenBounds.centerY

                endX = it.sceneX
                endY = it.sceneY

                isVisible = true
                emitterBeingDragged = this@JsNodeEmitter
            }

            socket.styleClass.addIfAbsent("populated")
        }

        // Configure drag, so that drag events occour on
        // receiving nodes.
        socket.setOnDragDetected {
            socket.startFullDrag()
            assertPopulationCss()
        }

        // TODO check which of these is triggered.
        socket.setOnMouseReleased {
            graphEditor.uncommittedLine.isVisible = false
            it.consume()
        }


        // Provide feedback when moving the mouse around.
        socket.setOnMouseDragged {
            with(graphEditor.uncommittedLine) {
                endX = it.sceneX - 5
                endY = it.sceneY - 5
            }
        }

        socket.setOnMouseReleased {
            assertPopulationCss()

            graphEditor.uncommittedLine.isVisible = false
        }

        socket.setOnContextMenuRequested {
            // TODO this isn't being triggered.
            emitter().emissions().forEach {
                guiNode.breakdownConnection(it)
            }
        }
    }
}