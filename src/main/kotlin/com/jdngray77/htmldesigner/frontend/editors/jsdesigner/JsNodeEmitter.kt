package com.jdngray77.htmldesigner.frontend.editors.jsdesigner

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphEmitter
import com.jdngray77.htmldesigner.backend.userConfirm
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
            // Reject right clicks, otherwise the context request is not invoked.
            if (!it.isPrimaryButtonDown) return@setOnMousePressed

            // Move the feedback line
            with(graphEditor.draggingLine) {
                val localToEditor: Bounds = graphEditor.childToLocal(socket)

                startX = localToEditor.centerX
                startY = localToEditor.centerY

                endX = startX
                endY = startY

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
            graphEditor.draggingLine.isVisible = false
            it.consume()
        }

        // Provide feedback when moving the mouse around.
        socket.setOnMouseDragged {
            with(graphEditor.draggingLine) {
                graphEditor.root.sceneToLocal(it.sceneX, it.sceneY).let {
                    endX = it.x - 5
                    endY = it.y - 5
                }
            }
        }

        socket.setOnMouseReleased {
            assertPopulationCss()
            graphEditor.draggingLine.isVisible = false
        }

        socket.setOnContextMenuRequested {
            breakdownAllEmissions()
        }
    }

    /**
     * Breaks down all emissions from the underlying emitter.
     *
     * If the unerlying emitter contains more emissions than [JSDESIGNER_EMITTER_BREAKDOWN_CONFIRM_THRESHOLD],
     * the user is prompted to confirm the breakdown.
     *
     */
    fun breakdownAllEmissions() {
        if (
            emitter().emissions().size > Config[Configs.JSDESIGNER_EMITTER_BREAKDOWN_CONFIRM_THRESHOLD_INT] as Int &&
            !userConfirm("You're about to delete many connections. Are you sure?")
        )
            return

        emitter().emissions().forEach {
            guiNode.breakdownConnection(it)
        }
    }
}