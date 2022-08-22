package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphEmitter
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphReceiver
import com.jdngray77.htmldesigner.utility.addIfAbsent
import com.jdngray77.htmldesigner.utility.classEqualsOrSubclass
import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.scene.layout.Pane
import javafx.scene.text.Text


/**
 * A property of a [JsNode].
 *
 * Can receive a connection
 */
class JsNodeReceiver: JsNodeProperty() {

    lateinit var receiver: JsGraphReceiver

    fun initReceiver(receiver: JsGraphReceiver) {
        this.receiver = receiver
        this.name.text = receiver.name

        if (receiver.isTrigger())
            socket.styleClass.addIfAbsent("trigger")
    }


    @FXML
    fun initialize() {

        // Dim when dragging in a new connection
        socket.setOnMouseDragEntered {
            if (!classEqualsOrSubclass(receiver.type, emitterBeingDragged.emitter.type) || receiver.hasAdmission())
                return@setOnMouseDragEntered


            socket.styleClass.addIfAbsent("populated")
            it.consume()
        }

        // Commit a new connection. This is the reciever.
        socket.setOnMouseDragReleased {
            if (!classEqualsOrSubclass(receiver.type, emitterBeingDragged.emitter.type) || receiver.hasAdmission())
                return@setOnMouseDragReleased

            socket.styleClass.addIfAbsent("populated")
            emitterBeingDragged.socket.styleClass.addIfAbsent("populated")

            graphEditor.temporaryLine.isVisible = false

            emitterBeingDragged.guiNode.emitConnection(emitterBeingDragged, this)

            graphEditor.invalidateTouches()
            it.consume()
        }

        // Drag did not commit (But also triggered when drag is committed)
        socket.setOnMouseDragExited {
            if (receiver.hasAdmission()) {
                socket.styleClass.addIfAbsent("populated")
            } else
                socket.styleClass.remove("populated")
        }

        socket.setOnContextMenuRequested {
            if (receiver.hasAdmission())
                guiNode.breakdownConnection(receiver.admission!!)

            it.consume()
        }
    }

    /**
     * Breaksdown the data connection AND
     * updates the GUI - including the emitting node.
     */
    fun breakdown() {
        socket.styleClass.remove("populated")
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

        if (emitter.isTrigger())
            socket.styleClass.addIfAbsent("trigger")
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
            assertPopulationCss()

            graphEditor.temporaryLine.isVisible = false
        }

        socket.setOnContextMenuRequested {
            // TODO this isn't being triggered.
            emitter.emissions().forEach {
                guiNode.breakdownConnection(it)
            }
        }
    }

    private fun assertPopulationCss() {
        if (emitter.emissions().isEmpty())
            socket.styleClass.remove("populated")
        else
            socket.styleClass.addIfAbsent("populated")
    }

    fun breakdown() {
        socket.styleClass.remove("populated")
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
    }
}