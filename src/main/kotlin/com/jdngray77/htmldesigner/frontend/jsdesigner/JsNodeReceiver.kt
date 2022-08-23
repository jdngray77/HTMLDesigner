package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphReceiver
import com.jdngray77.htmldesigner.utility.addIfAbsent
import javafx.fxml.FXML
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

        assertPopulationCss()
    }

    /**
     * Ensures that the population css class
     * matches the state of the receiver..
     */
    fun assertPopulationCss() {
        if (receiver.hasAdmission())
            socket.styleClass.addIfAbsent("populated")
        else
            socket.styleClass.remove("populated")
    }



    @FXML
    fun initialize() {

        // Dim when dragging in a new connection
        socket.setOnMouseDragEntered {
            if (receiver.hasAdmission() || receiver.type != emitterBeingDragged.emitter.emits)
                return@setOnMouseDragEntered


            socket.styleClass.addIfAbsent("populated")
            it.consume()
        }

        // Commit a new connection. This is the reciever.
        socket.setOnMouseDragReleased {
            if (receiver.hasAdmission() || receiver.type != emitterBeingDragged.emitter.emits)
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



