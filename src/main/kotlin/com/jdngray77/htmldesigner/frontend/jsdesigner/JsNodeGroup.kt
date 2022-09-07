package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.BackgroundTask.invokeInBackground
import com.jdngray77.htmldesigner.backend.BackgroundTask.submitToUI
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNodeGroup
import com.jdngray77.htmldesigner.backend.setAction
import com.jdngray77.htmldesigner.backend.showWarningNotification
import com.jdngray77.htmldesigner.backend.userConfirm
import com.jdngray77.htmldesigner.backend.userInput
import com.jdngray77.htmldesigner.utility.SerializableColor
import com.jdngray77.htmldesigner.utility.addIfAbsent
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.lang.System.gc

/**
 * An advanced drag listener that uses a drag selection to group multiple
 * nodes into a [JsNodeGroup].
 *
 * Create and added to the [JsDesigner]
 *
 * // TODO fun to config listeners.
 * //TODO move listeners here
 * // TODO logs
 */
class JsNodeGrouper(

    /**
     * Reference to the editor on which this grouper is active.
     */
    val editor: JsDesigner

) : Rectangle() {

    companion object {
        val GROUPER_FILL: Color = Color.rgb(72, 179, 255, 0.45)
    }

    init {
        styleClass.add("drag-selection")
        fill = GROUPER_FILL
    }

    /**
     * The initial mouse down position of the drag.
     *
     * Determines the position the rectangle is drawn from.
     */
    var originX = 0.0
        private set

    /**
     * The initial mouse down position of the drag.
     *
     * Determines the position the rectangle is drawn from.
     */
    var originY = 0.0
        private set

    /**
     * The last group that was created on [endSelection]
     *
     * Used to remove temporary groups when the user clicks off the selection without
     * committing.
     *
     * This is cleared to null when [deleteIfUncommitted] is called.
     *
     * @see [deleteIfUncommitted]
     */
    var lastCreatedGroup: JsNodeGroup? = null
        set(value) {
            field?.deleteIfUncommitted()
            field = value
        }

    /**
     * Begins dragging.
     *
     * Occours on 'DRAG_DETECTED' event.
     *
     * Cancels any existing temp group with [deleteIfUncommitted]
     *
     * Stores the origin in [originX] & [originY], and relocates to this position.
     *
     * Resets the size to 0, then finally makes the rectangle visible.
     */
    fun startSelection(x: Double, y: Double) {
        // If the last selection was not committed, get rid. Replace with new selection.
        deleteIfUncommitted()

        relocate(x, y)
        originX = x
        originY = y

        width = 0.0
        height = 0.0
        isVisible = true
    }

    /**
     * Updates the size of the selection rectangle.
     *
     * Rejected if [startSelection] was not called first
     *
     * Called many times between [startSelection] and [endSelection] by the 'MOUSE_DRAGGED' event.
     *
     * Updates the size of the rectangle to match the current position of the mouse.
     */
    // Not perfect. seems to drift. Not sure why. cba to fix.
    fun updateSelection(mousex: Double, mousey: Double) {
        if (!isVisible) return

        // Determine the size of the rectangle from how far the mouse
        // has travelled from the origin.
        width = mousex - originX
        height = mousey - originY

        // Handle negative width/height by relocating the rectangle.

        // Typically, the rectagles origin matches the mouse origin, however
        // when the mouse goes negavtive to the origin, the rectangle origin
        // follows the mouse and is resized to have the opposite corner
        // be positioned in the origin instead.

        // Negative x
        if (width < 0) {
            width = -width
            layoutX = originX - width
        }

        // Negative y
        if (height < 0) {
            height = -height
            layoutY = originY - height
        }
    }

    /**
     * Ends the selection.
     *
     * Called by the 'MOUSE_RELEASED' event.
     *
     * Rejected if [startSelection] was not called first.
     *
     * Hides the rectangle and creates a group with all nodes that are inside of it.
     *
     * Does not attempt to create a new group if no nodes were selected
     *
     * @see [evalSelected]
     */
    fun endSelection() {
        if (!isVisible) return

        isVisible = false

        // Determine which nodes are inside the rectangle.
        val selectedNodes = evalSelected()

        if (selectedNodes.isEmpty()) return

        submitToUI {
            lastCreatedGroup = JsNodeGroup(editor, *selectedNodes.toTypedArray()).also {
                setAction("Selected ${it.graphGroup.size} nodes")
            }
        }
    }

    /**
     * Returns every node that is ***entirely*** within the bounds of the selection rectangle.
     */
    fun evalSelected() =
        editor.guiNodes.filter {
            boundsInParent.contains(it.root.boundsInParent)
        }

    /**
     * Deletes the [lastCreatedGroup], if it has not been committed to the graph.
     *
     * Always clears the [lastCreatedGroup].
     *
     * @see [JsNodeGroup.deleteIfUncommitted]
     */
    fun deleteIfUncommitted() {
        lastCreatedGroup = null
        setAction("Cancelled selection")
    }

    /**
     * Sets mouse listeners of the [editor]'s context pane
     * to trigger the appropriate functions above.
     */
    fun setupListeners() {
        //TODO Perform these functions in the background.
        val layout = editor.contextPane

        // TODO transfer these to adds, not sets.
        layout.addEventHandler(MouseEvent.MOUSE_PRESSED) {
            if (it.isPrimaryButtonDown ) {
                invokeInBackground(this::deleteIfUncommitted)
                it.consume()
            }
        }

        layout.addEventHandler(MouseEvent.DRAG_DETECTED) {
            if (!it.isPrimaryButtonDown) return@addEventHandler
            invokeInBackground(this::startSelection, it.sceneX, it.sceneY)
            it.consume()
        }

        layout.addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            invokeInBackground(this::updateSelection, it.sceneX, it.sceneY)
            it.consume()
        }

        layout.addEventHandler(MouseEvent.MOUSE_RELEASED) {
            invokeInBackground(this::endSelection)
            it.consume()
        }
    }
}

/**
 * Front end to represent a [JsGraphNodeGroup] in [JsDesigner]
 *
 * A group may or may not have been commited to a graph.
 *
 * This way, a group may be used temporarily to perform a some actions,
 * without having to modify the graph.
 *
 * A group may be commited to a graph by calling [commitToGraph], which
 * will make the group permanent
 * TODO re-create on loading
 *
 * @constructor primary constructor used to create a brand new group in the data. See alternate constructor for loading existing groups.
 */
class JsNodeGroup (

    /**
     * The editor in which this group will be displayed
     */
    val editor: JsDesigner,

    val graphGroup: JsGraphNodeGroup,

    vararg nodes: JsNode

) : Pane() {

    /**
     * @constructor used to load an existing group from the graph.
     *
     * Skips committable phase, if the existing group is already in the [editor's graph.]
     */
    constructor(editor: JsDesigner, vararg nodes: JsNode) :
            this(editor, JsGraphNodeGroup("", SerializableColor(1.0,1.0,1.0,0.5), *nodes.map { it.graphNode }.toTypedArray()), *nodes) {
    }

    /**
     * For safety, raised when the user has disposed of this group of nodes.
     *
     * Prevents calls that attempt to modify the group of data.
     *
     * @see [dispose] and [checkDisposeMod]
     */
    var disposed = false
        private set

    /**
     * The GUI nodes contained within this group
     */
    private val nodes = nodes.toMutableList()

    /**
     * The GUI nodes contained within this group.
     *
     * TODO when a node is removed from the graph, remove it from any groups.
     */
    fun getNodes() = nodes.toList()

    /**
     * Label used to display the group's name
     */
    private val lblHeader: Label = Label("Group")

    private val contextMenu = ContextMenu().also {
        it.items.addAll(
            MenuItem("Rename").also { item ->
                item.setOnAction {
                    requireIsCommitted()
                    setName(userInput("Enter a new name for this group"))
                }
            },

            MenuItem("[inop] Change Color").also { item ->
                item.setOnAction {
                    requireIsCommitted()
                    TODO()
                }
            },

            SeparatorMenuItem(),

            MenuItem("Delete group...").also { item ->
                item.setOnAction {
                    if (!isCommitted()){
                        deleteIfUncommitted()
                        return@setOnAction
                    }

                    if (userConfirm("This will delete the group, but not it's nodes.\n\nContinue?")) {
                        invokeInBackground(this::dumpGroup)
                    }
                }
            },

            MenuItem("Delete group and nodes...").also { item ->
                item.setOnAction {
                    if (userConfirm("This will delete the group and all ${nodes.size} nodes within.\n\nContinue?")) {
                        deleteGroup()
                    }
                }
            },

            SeparatorMenuItem(),

            MenuItem("Clone").also { item ->
                item.setOnAction {
                    clone()
                    deleteIfUncommitted()
                }
            },

            MenuItem("Commit to graph").also { item ->
                item.setOnAction {
                    requireIsNotCommitted()
                    invokeInBackground(this::commitToGraph)
                }
            },

            MenuItem("Send group to back").also { item ->
                item.setOnAction {
                    toBack()
                }
            },
        )
    }


    init {
        if (nodes.isEmpty())
            throw IllegalArgumentException("Cannot create a group with no nodes")

        // Own control over placement of the rectangle.
        isManaged = true

        cursor = Cursor.HAND
        styleClass.add("node-group")

        lblHeader.padding = Insets(20.0)
        children.add(lblHeader)



        // If already committed, skip commit phase of the group.
        if (isCommitted()) {
            commitConfirmed()
            setName(graphGroup.name)
        } else {
            // TODO save and load color from the graph.
            background = Background(BackgroundFill(Color.rgb(72, 179, 255, 0.85), null, null))

            setName("Click to save group.")
        }


        setOnMousePressed {
            if (!it.isPrimaryButtonDown) return@setOnMousePressed
            invalidatePosition()
            dragOriginX = it.screenX
            dragOriginY = it.screenY
            cursor = Cursor.CLOSED_HAND
            it.consume()
        }

        setOnMouseDragged {
            updateDrag(it)
            cursor = Cursor.CROSSHAIR
            it.consume()
        }

        setOnMouseReleased {
            finalizeDrag(it)

            if (onMouseClicked == null)
                cursor = Cursor.OPEN_HAND
            it.consume()
        }

        setOnMouseClicked {

            it.consume()

            // If cursor was closed, then we have been dragging
            // In this case, we reject the click.
            if (cursor == Cursor.CROSSHAIR) {
                cursor = Cursor.OPEN_HAND
                return@setOnMouseClicked
            }


            println(it.eventType)
            println(dragOriginX)
            println(dragOriginY)
            println(translateX)
            println(translateY)
            if (it.button != MouseButton.PRIMARY) {
                return@setOnMouseClicked
            }

            if (contextMenu.isShowing) {
                contextMenu.hide()
                return@setOnMouseClicked
            }


            setName(userInput("Enter a name for this group"))
            commitToGraph(nodes.first().getGraphEditor().graph)
            commitConfirmed()
        }

        setOnContextMenuRequested {
            contextMenu.show(this, it.screenX, it.screenY)
            it.consume()
        }

        addToEditor()
        invalidatePosition()
    }



    // position of the mouse at drag start.
    private var dragOriginX = 0.0
    private var dragOriginY = 0.0

    private fun updateDrag(mouseEvent: MouseEvent) {
        val dx = mouseEvent.screenX - dragOriginX
        val dy = mouseEvent.screenY - dragOriginY
        translateX = dx
        translateY = dy

        nodes.forEach {
            with (it.root) {
                translateX = dx
                translateY = dy
            }
        }
    }

    private fun finalizeDrag(mouseEvent: MouseEvent) {
        if (translateX == 0.0 && translateY == 0.0) {
            return
        }

        layoutX += translateX
        layoutY += translateY
        translateX = 0.0
        translateY = 0.0

        nodes.forEach {
            // TODO it.relocate
            with (it.root) {
                layoutX += translateX
                layoutY += translateY
                translateX = 0.0
                translateY = 0.0

                it.relocate(layoutX, layoutY)
            }
        }

        editor.invalidateGroupPositions()
    }




    /**
     * Sets the name of the group, and displays it in the label.
     */
    fun setName(name: String) {
        graphGroup.name = name
        lblHeader.text = name
    }

    /**
     * Updates this group to match the backend data.
     *
     * If the graph has been modified, i.e if some nodes have been deleted
     * or removed from the group without us knowing, then this will
     * update the group.
     *
     * Recommend doing this in a background thread.
     */
    fun invalidateData() {
        nodes.clear()

        graphGroup.forEach {
            nodes.add(editor.getGUINodeFor(it)!!)
        }

        gc()
    }

    /**
     * Calculates graphical bounds of the group.
     *
     * A box is expanded to fit all members of the group,
     * and a padding is added.
     */
    fun invalidatePosition() {
        // TODO test this
        // TODO would this be better as just adding the nodes as children?
        with (nodes.first()) {
            super.relocate(root.layoutX, root.layoutY)
        }

        toFront()

        for (node in nodes) {
            // Not required, but it's good to make sure that the
            // node is in the right place first.
            node.invalidatePosition()

//            super.relocate(
//                layoutX.coerceAtLeast(node.root.layoutX),
//                layoutY.coerceAtLeast(node.root.layoutY)
//            )
//
//            if (!boundsInParent.contains(node.root.boundsInParent)) {
//                prefWidth = prefWidth.coerceAtLeast(node.root.boundsInParent.maxX - layoutX)
//                prefHeight = prefHeight.coerceAtLeast(node.root.boundsInParent.maxY - layoutY)
//            }

            node.toFront()
        }


        val minx = nodes.minBy { it.root.layoutX }.root.layoutX
        val miny = nodes.minBy { it.root.layoutY }.root.layoutY
        val maxx = nodes.maxBy { it.root.boundsInParent.maxX }.root.boundsInParent.maxX
        val maxy = nodes.maxBy { it.root.boundsInParent.maxY }.root.boundsInParent.maxY

        super.relocate(minx, miny)
        prefWidth = maxx - minx
        prefHeight = maxy - miny

        // Padding
        val tempPaddingVal = 50
        prefWidth += tempPaddingVal * 2
        prefHeight += tempPaddingVal * 2
        layoutX -= tempPaddingVal
        layoutY -= tempPaddingVal

    }

    /**
     * Groups made in the GUI are temporary until the user commits them.
     *
     * this allows the user to quickly drag a group of nodes and something with them,
     * i.e drag them, without having to commit a group into the data.
     */
    fun commitToGraph(graph: JsGraph) {
        checkDisposeMod()
        graph.addGroup(graphGroup)
        setAction("Group '${graphGroup.name}' created with ${graphGroup.size} nodes")
    }

    /**
     * Removes this group from the [editor], iff the graph does not exist in the
     * graph.
     *
     * i.e this will remove temporary groups iff [commitToGraph] has not been called.
     *
     * // TODO listener for click off?
     */
    fun deleteIfUncommitted() {
        if (!isCommitted())
            removeFromEditor()
    }

    /**
     * @return true if the [editor]'s graph contains this group.
     */
    fun isCommitted() =
        editor.graph.hasGroup(graphGroup)

    /**
     * Exits the 'commit' phase of the group.
     *
     * Alters appearance and removes commit click listener.
     */
    private fun commitConfirmed() {
        background = Background(BackgroundFill(Color.rgb(72, 130, 200, 0.6), null, null))
        onMouseClicked = null
    }

    /**
     * Adds a new node to the group.
     * // TODO listener for drag in
     */
    fun addNode(node: JsNode) {
        checkDisposeMod()

        nodes.add(node)
        graphGroup.add(node.graphNode)

        setAction("Added node '${node.graphNode.name}' to group '${graphGroup.name}'")
    }

    /**
     * Removes a node from the group.
     * // TODO listener for drag out
     */
    fun removeNode(node: JsNode) {
        checkDisposeMod()

        nodes.remove(node)
        graphGroup.remove(node.graphNode)

        setAction("Removed node '${node.graphNode.name}' from group '${graphGroup.name}'")
    }


    /**
     * Deletes the group, and all nodes within it.
     *
     * Also deletes the graph from the screen.
     */
    fun deleteGroup() {
        checkDisposeMod()

        // Prevent deleting nodes from re-compiling the graph.
        editor.disableCompile = true

        // Delete all nodes via the GUI node.
        for (node in nodes) {
            node.delete()
        }

        // Remove the group
        removeFromEditor()

        // All nodes are set to be deleted, so clear the group.
        graphGroup.clear()
        editor.graph.dumpGroup(graphGroup)

        editor.disableCompile = false
        setAction("Deleted group '${graphGroup.name}', and all of it's nodes.")
    }

    /**
     * Drops the group without deleting the nodes
     */
    fun dumpGroup() {
        dispose()
    }

    /**
     * Dumps the group without deleting the nodes, [removeFromEditor], then
     * raises [dispose]d flag to prevent further access.
     */
    private fun dispose() {
        if (editor.graph.hasGroup(graphGroup)) {
            editor.graph.dumpGroup(graphGroup)
        }

        removeFromEditor()
        disposed = true
    }

    /**
     * @throws [IllegalStateException] If [dispose] via invokation of [dispose]
     */
    private fun checkDisposeMod () {
        if (disposed)
            throw IllegalStateException("Cannot add modify a disposed group")
    }

    private fun requireIsCommitted() {
        if (!isCommitted()) {
            showWarningNotification("Cannot perform this action on an uncommitted group.\n\nSave the group first.")
            throw IllegalStateException("Tried to perform an action on an uncommitted group that required it to be committed")
        }
    }

    private fun requireIsNotCommitted() {
        if (isCommitted()) {
            showWarningNotification("Cannot perform this action on a group that has already been committed.")
            throw IllegalStateException("Tried to perform an action on a committed group that required it to be uncommitted")
        }
    }

    /**
     * Removes this from the [editor]
     */
    private fun removeFromEditor() {
        submitToUI {
            editor.root.children.remove(this)
        }
    }

    /**
     * Adds this to the [editor], if it isn't already on screen.
     */
    private fun addToEditor() {
        submitToUI {
            editor.root.children.addIfAbsent(this)
            invalidatePosition()
        }
    }

    /**
     * Relocates the group, and adjusts the position of all nodes within it.
     */
    override fun relocate(x: Double, y: Double) {

        val dx = x - layoutX
        val dy = y - layoutY

        nodes.forEach {
            with(it.root) {
                super.relocate(
                    layoutX + dx,
                    layoutY + dy
                )
            }
        }

        invalidatePosition()
    }

    /**
     * Creates and returns a new [JsNodeGroup], with the same name and nodes
     */
    fun clone() =
        JsNodeGroup(
            editor,
            *nodes.map {
                it.dupeNode() // FIXME i don't want this to crash if the group contains a non-dupeable node.
            }.toTypedArray()
        ).also {
            editor.grouper.lastCreatedGroup = it
            setAction("Cloned group '${graphGroup.name}', and duplicated all nodes contained within.")
        }


    // TODO show name
    // TODO edit name
    // TODO selection class on nodes
}