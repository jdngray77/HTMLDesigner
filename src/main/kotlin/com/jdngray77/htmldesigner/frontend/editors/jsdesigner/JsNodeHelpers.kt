package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphEmitter
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNodeProperty
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphReceiver
import com.jdngray77.htmldesigner.utility.addIfAbsent
import com.jdngray77.htmldesigner.utility.setTooltip
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import javafx.scene.text.Text
import java.lang.System.gc

/**
 * Root content for [JsNodeReceiver] and [JsNodeEmitter]'s.
 */
open class JsNodeProperty<T : JsGraphNodeProperty>() {

    /**
     * The [JsGraphNodeProperty] that this represents.
     *
     * As the graph stands currently, this can only be a
     * [JsGraphReceiver] or [JsGraphEmitter].
     */
    private lateinit var property: T

    /**
     * Attempts to return this property casted as an emitter.
     */
    fun emitter() = property as JsGraphEmitter

    /**
     * Attempts to return this property casted as a receiver.
     */
    fun receiver() = property as JsGraphReceiver

    /**
     * Returns the property without casting.
     */
    fun property() = property

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                       FXML Controls & GUI components.
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Text displayed to the user of the property.
     */
    @FXML
    lateinit var lblName: Text

    fun name() = lblName.text

    /**
     * The pane which is used to connect this property to another.
     */
    @FXML
    lateinit var socket: Pane

    @FXML
    private lateinit var root: Pane

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                    FXML Controls & GUI components.
    //region                                       JsDesigner references
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * The node that this property belongs to.
     */
    lateinit var guiNode: JsNode

    /**
     * The editor that this property belongs to.
     */
    lateinit var graphEditor: VisualScriptEditor

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                    JsDesigner references
    //region                                       Construction
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Late initializer.
     *
     * Configures the property that this represents.
     *
     * Is invoked by loading class after the fxml has been loaded.
     */
    fun initProperty(property: T) {
        this.property = property
        this.lblName.text = property.name

        if (property.isTrigger())
            socket.styleClass.addIfAbsent("trigger")

        root.setTooltip("Name : ${property.name}\n" +
                ((if (property() is JsGraphEmitter) "Emits" else "Receives") + " : ${property.type}") + "\n" +
                (if (property() is JsGraphReceiver)
                    ((property as JsGraphReceiver).defaultValue?.let { "Default : $it" }
                        ?: "Has no default value") + "\n"
                else ""))


        assertPopulationCss()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                    Construction
    //region                                       CSS
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Ensures that the population css class
     * matches the state of the [property].
     */
    internal fun assertPopulationCss() {
        if (property is JsGraphReceiver && (property as JsGraphReceiver).hasAdmission())
            return markPopulated()
        else if (property is JsGraphEmitter && (property as JsGraphEmitter).emissions().isNotEmpty())
            return markPopulated()

        markUnpopulated()
    }

    private fun markPopulated() {
        socket.styleClass.addIfAbsent("populated")
    }

    private fun markUnpopulated() {
        socket.styleClass.remove("populated")
    }


    fun breakdown() {
        if (property is JsGraphEmitter && (property as JsGraphEmitter).emissions().isEmpty()) {
            markUnpopulated()
        } else if (property is JsGraphReceiver && !(property as JsGraphReceiver).hasAdmission()) {
            markUnpopulated()
        }


    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     CSS
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    // TODO this should be point 2d not pair
    fun evalLinePosition(): Pair<Double, Double> {
        root.parent.requestLayout()

        return if (guiNode.isCollapsed())
            graphEditor.childToLocal(guiNode.txtElementName).let {
                Pair(
                    if (this is JsNodeReceiver)
                        it.minX + 15
                    else
                        it.maxX - 15,
                    it.centerY
                )
            }
        else
            graphEditor.childToLocal(socket).let { Pair(it.centerX, it.centerY) }
    }

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

    /**
     * The emitter property this line is originating from.
     */
    val emitter: JsNodeEmitter,

    /**
     * The receiver property this line is terminating at.
     */
    val receiver: JsNodeReceiver,

)  {

    /**
     * The line itself.
     */
    val line: Line = Line()

    init {
        evalPosition()

        line.setOnContextMenuRequested {
            breakdown()
            it.consume()
        }

        line.setTooltip("${emitter.property().parent.name}'s ${emitter.name()} sends ${emitter.property().type} to ${receiver.property().parent.name}'s ${receiver.name()}")

        VisualScriptEditor.themeLine(line)
    }

    /**
     * Updates a lines position to match the position of the
     * [JsNodeEmitter] emitting the line, and the [JsNodeReceiver]
     * receiving the line.
     */
    fun evalPosition() {
        with(line) {

            emitter.evalLinePosition().apply {
                startX = first
                startY = second
            }

            receiver.evalLinePosition().apply {
                endX = first
                endY = second
            }

            toFront()
        }
    }

    /**
     * Breaks down the visual line drawn over a connection AND
     * modifies the graph to reflect the change.
     */
    fun breakdown() {

        // Remove the connection in the data.
        receiver.receiver() .admission?.breakdown()

        // Remove references to the graphical line from sender and receiver.
        emitter.guiNode.emittingLines.remove(this)
        receiver.guiNode.receivingLines.remove(this)

        // Notify the emitter and reciever, which updates the 'populated' css class.
        emitter.breakdown()
        receiver.breakdown()

        // Recompile to check for touches.
        emitter.graphEditor.invalidateTouches()

        // Remove from the scene.
        emitter.guiNode.getGraphEditor().editorRootPane.children.remove(line)
        line.isVisible = false

        // We want this fucker gone.
        gc()
    }
}