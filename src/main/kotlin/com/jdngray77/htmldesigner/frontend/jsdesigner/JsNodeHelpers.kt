package com.jdngray77.htmldesigner.frontend.jsdesigner

import javafx.fxml.FXML
import javafx.geometry.Bounds
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import javafx.scene.text.Text
import java.lang.System.gc

/**
 * Root content for [JsNodeReceiver] and [JsNodeEmitter]'s.
 */
open class JsNodeProperty() {

    /**
     * Text displayed to the user of the property.
     */
    @FXML
    lateinit var name: Text

    /**
     * The pane which is used to connect this property to another.
     */
    @FXML
    lateinit var socket: Pane

    /**
     * The node that this property belongs to.
     */
    lateinit var guiNode: JsNode

    /**
     * The editor that this property belongs to.
     */
    lateinit var graphEditor: JsDesigner

    companion object {

        /**
         * Global storage of the last emitter to be dragged.
         *
         * When the user begins dragging a new connection, it's stored here.
         *
         * Then, when the connection is committed, this is used in the
         * creation of the new connection.
         */
        internal lateinit var emitterBeingDragged: JsNodeEmitter
    }
}


/**
 * The gui line between two [JsNodeProperty]'s.
 *
 * @param emitter The emitter property this line is originating from.
 * @param receiver The receiver property this line is terminating at.
 * @param line The line itself.
 */
class EmissionLine(

    val emitter: JsNodeEmitter,

    val receiver: JsNodeReceiver,

    val line: Line

)  {

    /**
     * Updates a lines position to match the position of the
     * [JsNodeEmitter] emitting the line, and the [JsNodeReceiver]
     * receiving the line.
     */
    fun evalPosition() {
        with(line) {
            val startBounds: Bounds = emitter.socket.localToScene(emitter.socket.boundsInLocal)
            startX = startBounds.centerX
            startY = startBounds.centerY

            val endBounds: Bounds = receiver.socket.localToScene(receiver.socket.boundsInLocal)
            endX = endBounds.centerX
            endY = endBounds.centerY

            toFront()
        }
    }

    /**
     * Breaks down the visual line drawn over a connection AND
     * modifies the graph to reflect the change.
     */
    fun breakdown() {

        // Remove the connection in the data.
        receiver.receiver.admission?.breakdown()

        // Remove references to the graphical line from sender and receiver.
        emitter.guiNode.emittingLines.remove(this)
        receiver.guiNode.receivingLines.remove(this)

        // Notify the emitter and reciever, which updates the 'populated' css class.
        emitter.breakdown()
        receiver.breakdown()

        // Recompile to check for touches.
        emitter.graphEditor.invalidateTouches()

        // Remove from the scene.
        emitter.guiNode.getGraphEditor().root.children.remove(line)
        line.isVisible = false

        // We want this fucker gone.
        gc()
    }
}