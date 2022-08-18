package com.jdngray77.htmldesigner.frontend.jsdesigner

import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.scene.effect.ColorAdjust
import javafx.scene.layout.Pane
import javafx.scene.text.Text


/**
 * A property of a [JsNode].
 *
 * Can recieve a connection
 */
class JsNodeAttr: JsNodeProperty() {

    val attr: String
        get() = name.text

    fun setAttr(name: String) {
        this.name.text = name
    }

    @FXML
    fun initialize() {

        // Dim when dragging in a new connection
        socket.setOnMouseDragEntered {
            socket.effect = socketDim
            it.consume()
        }

        // Commit a new connection. This is the reciever.
        socket.setOnMouseDragReleased {
            socket.effect = null

            graphEditor.temporaryLine.isVisible = false

            eventBeingDragged.guiNode.emitConnection(
                eventBeingDragged, this
            )

            it.consume()
        }

        // Drag did not commit.
        socket.setOnMouseDragExited {
            socket.effect = null
            it.consume()
        }
    }
}


/**
 * A property of a [JsNode].
 *
 * Can be used by the user to create [JsGraphConnection]'s.
 *
 * Emits events.
 */
class JsNodeEvent : JsNodeProperty() {

    val event: String
        get() = name.text

    fun setEvent(name: String) {
        this.name.text = name
    }

    @FXML
    fun initialize() {

        // Start a new emission.
        socket.setOnMousePressed {
            with(graphEditor.temporaryLine) {
                val screenBounds: Bounds = socket.localToScene(socket.boundsInLocal)

                startX = screenBounds.centerX
                startY = screenBounds.centerY

                endX = it.sceneX
                endY = it.sceneY

                isVisible = true
                eventBeingDragged = this@JsNodeEvent
            }

            socket.effect = socketDim
        }

        // Configure drag, so that drag events occour on
        // recieving nodes.
        socket.setOnDragDetected {
            socket.startFullDrag()
            socket.effect = null
        }

        // TODO check which of these is triggered.
        socket.setOnMouseReleased {
            graphEditor.temporaryLine.isVisible = false
            it.consume()
        }


        // Provide feedback when moving the mouse around.
        socket.setOnMouseDragged {
            with(graphEditor.temporaryLine) {
                endX = it.sceneX - 5
                endY = it.sceneY - 5
            }
        }

        socket.setOnMouseReleased {
            socket.effect = null
            graphEditor.temporaryLine.isVisible = false
            it.consume()
        }


    }
}

open class JsNodeProperty() {
    @FXML
    lateinit var name: Text

    /**
     * The pane which is used to connect this property to another.
     */
    @FXML
    lateinit var socket: Pane

    lateinit var guiNode: JsNode

    lateinit var graphEditor: JsDesigner


    companion object {

        internal lateinit var eventBeingDragged: JsNodeEvent

        val socketDim =  ColorAdjust().also {
            it.brightness = -.3
        }
    }
}