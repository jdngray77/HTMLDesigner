package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphEmitter
import com.jdngray77.htmldesigner.utility.addIfAbsent
import javafx.fxml.FXML
import javafx.geometry.Bounds


/**
 * A property of a [JsNode].
 *
 * Can be used by the user to create [JsGraphConnection]'s.
 *
 * Emits events.
 */
class JsNodeEmitter : JsNodeProperty() {

    lateinit var emitter: JsGraphEmitter

    fun initEmitter(emitter: JsGraphEmitter) {
        this.emitter = emitter
        this.name.text = emitter.name

        if (emitter.isTrigger())
            socket.styleClass.addIfAbsent("trigger")

        assertPopulationCss()
    }

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
        // recieving nodes.
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
            emitter.emissions().forEach {
                guiNode.breakdownConnection(it)
            }
        }
    }

    internal fun assertPopulationCss() {
        if (emitter.emissions().isEmpty())
            socket.styleClass.remove("populated")
        else
            socket.styleClass.addIfAbsent("populated")
    }

    fun breakdown() {
        socket.styleClass.remove("populated")
    }
}