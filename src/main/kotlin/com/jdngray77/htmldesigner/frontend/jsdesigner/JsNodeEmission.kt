package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphEmitter
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphReceiver
import com.jdngray77.htmldesigner.utility.classEquals
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
class JsNodeReceiver: JsNodeProperty() {

    lateinit var receiver: JsGraphReceiver

    fun initReceiver(receiver: JsGraphReceiver) {
        this.receiver = receiver
        this.name.text = receiver.name
    }


    @FXML
    fun initialize() {

        // Dim when dragging in a new connection
        socket.setOnMouseDragEntered {
            if (!classEquals(emitterBeingDragged.emitter.type, receiver.type))
                return@setOnMouseDragEntered


            socket.effect = socketDim
            it.consume()
        }

        // Commit a new connection. This is the reciever.
        socket.setOnMouseDragReleased {
            if (emitterBeingDragged.emitter.type != receiver.type)
                return@setOnMouseDragReleased

            socket.effect = null

            graphEditor.temporaryLine.isVisible = false

            emitterBeingDragged.guiNode.emitConnection(
                emitterBeingDragged, this
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
class JsNodeEmitter : JsNodeProperty() {

    lateinit var emitter: JsGraphEmitter

    fun initEmitter(emitter: JsGraphEmitter) {
        this.emitter = emitter
        this.name.text = emitter.name
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
                emitterBeingDragged = this@JsNodeEmitter
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

        internal lateinit var emitterBeingDragged: JsNodeEmitter

        val socketDim =  ColorAdjust().also {
            it.brightness = -.3
        }
    }
}