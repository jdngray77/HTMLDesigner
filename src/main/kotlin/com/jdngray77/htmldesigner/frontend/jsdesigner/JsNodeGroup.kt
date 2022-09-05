package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.BackgroundTask.executeInBackground
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraph
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphNodeGroup
import com.jdngray77.htmldesigner.backend.userInput
import com.jdngray77.htmldesigner.utility.SerializableColor
import com.jdngray77.htmldesigner.utility.addIfAbsent
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.lang.System.gc

/**
 * An object that handles drag selection of multiple nodes within [JsDesigner],
 * and grouping them together.
 */
class JsNodeGrouper(

    val editor: JsDesigner

) : Rectangle() {

    init {
        styleClass.add("drag-selection")
        fill = Color.rgb(72, 179, 255, 0.45)
    }

    @Volatile
    var isSelecting = false

    var originX = 0.0
        private set

    var originY = 0.0
        private set

    var lastCreatedGroup: JsNodeGroup? = null

    fun startSelection(x: Double, y: Double) {
        println("Selection Start")

        // If the last selection was not committed, get rid. Replace with new selection.
        deleteIfUncommitted()

        relocate(x, y)
        originX = x
        originY = y

        width = 0.0
        height = 0.0
        isVisible = true
    }

    // Not perfect. seems to drift. Not sure why. cba to fix.
    fun updateSelection(x: Double, y: Double) {
        width = x - originX
        height = y - originY

        if (width < 0) {
            width = -width
            layoutX = originX - width
        }

        if (height < 0) {
            height = -height
            layoutY = originY - height
        }
        //println(boundsInParent)
    }

    fun endSelection() {
        if (!isVisible) return
        println("selection end")
        isVisible = false

        val selectedNodes = evalSelected()

        if (selectedNodes.isEmpty()) return

        lastCreatedGroup = JsNodeGroup(editor, *selectedNodes.toTypedArray()).also {
            println("Selected ${it.graphGroup.size} nodes")
        }
    }

    /**
     * Returns every node
     */
    fun evalSelected() =
        editor.guiNodes.filter {
            boundsInParent.contains(it.root.boundsInParent)
        }

    fun deleteIfUncommitted() {
        lastCreatedGroup?.deleteIfUncommitted()
        lastCreatedGroup = null
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
 */
class JsNodeGroup (

    val editor: JsDesigner,

    vararg nodes: JsNode

) : Pane() {

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
     * The graph group this represents
     */
    val graphGroup: JsGraphNodeGroup

    /**
     * Label used to display the group's name
     */
    private val lblHeader: Label = Label("Group")

    init {
        if (nodes.isEmpty())
            throw IllegalArgumentException("Cannot create a group with no nodes")

        isManaged = true
        styleClass.add("node-group")

        // TODO save and load color from the graph.
        background = Background(BackgroundFill(Color.rgb(72, 179, 255, 0.85), null, null))

        graphGroup = JsGraphNodeGroup("", SerializableColor(1.0,1.0,1.0,0.5), *nodes.map { it.graphNode }.toTypedArray())

        children.add(lblHeader)
        setName("Right click to save group.")

        setOnMouseClicked {
            setName(userInput("Enter a name for this group"))
            commitToGraph(nodes.first().getGraphEditor().graph)
            commitConfirmed()
        }


        addToEditor()
        invalidatePosition()
        println(boundsInParent)
    }

    constructor(editor: JsDesigner, group: JsGraphNodeGroup) : this(editor, *group.map { editor.getGUINodeFor(it)!! }.toTypedArray()) {
        // If already committed, skip commit phase of the group.
        if (isCommitted()) {
            commitConfirmed()
            setName(group.name)
        }
    }


    /**
     * Sets the name of the group, and displays
     * it in the label.
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
    }

    /**
     * Removes a node from the group.
     * // TODO listener for drag out
     */
    fun removeNode(node: JsNode) {
        checkDisposeMod()

        nodes.remove(node)
        graphGroup.remove(node.graphNode)
    }


    /**
     * Deletes the group, and all nodes within it.
     *
     * Also deletes the graph from the screen.
     */
    fun deleteGroup() {
        checkDisposeMod()


        // Remove from data graph.
        editor.graph.deleteGroup(graphGroup)

        removeFromEditor()

        // Delete all nodes via the GUI node.
        for (node in nodes) {
            node.delete()
        }
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

    /**
     * Removes this from the [editor]
     */
    private fun removeFromEditor() {
        editor.root.children.remove(this)
    }

    /**
     * Adds this to the [editor], if it isn't already on screen.
     */
    private fun addToEditor() {
        editor.root.children.addIfAbsent(this)
        invalidatePosition()
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
                it.dupeNode()
            }.toTypedArray()
        )


    // TODO show name
    // TODO edit name
    // TODO selection class on nodes
}