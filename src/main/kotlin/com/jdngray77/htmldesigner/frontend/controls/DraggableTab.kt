import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.geometry.Rectangle2D
import javafx.geometry.Side
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Control
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.stage.StageStyle

/**
 * A draggable tab that can optionally be detached from its tab pane and shown
 * in a separate window. This can be added to any normal TabPane, however a
 * TabPane with draggable tabs must *only* have DraggableTabs, normal tabs and
 * DrragableTabs mixed will cause issues!
 *
 *
 * @author Michael Berry
 */
class DraggableTab(text: String?, content: Node) : Tab() {

    /**
     * Label within the tab's header.
     */
    private val label: Label = Label(text)

    /**
     * Text that is dragged around.
     */
    private val dragText: Text = Text(text)

    /**
     * A window that is used to show the tab as it's being dragged.
     *
     * TODO only use one?
     */
    private val dragStage: Stage = Stage(StageStyle.UNDECORATED)

    /**
     * Allows this tab to be removed from the tab pane.
     */
    private var detachable: Boolean = true

    /**
     * Create a new draggable tab. This can be added to any normal TabPane,
     * however a TabPane with draggable tabs must *only* have DraggableTabs,
     * normal tabs and DrragableTabs mixed will cause issues!
     *
     *
     * @param text the text to appear on the tag label.
     */
    init {
        // Deffer until after added to tab pane.
        tabPaneProperty().addListener { _, _, _ ->

            graphic = label
            setContent(content)

            label.style += "-fx-rotate: ${
                if (tabPane.side == Side.RIGHT)
                    -90.0
                else if (tabPane.side == Side.LEFT)
                    90.0
                else
                    0.0
            };"


            val dragStagePane = StackPane()
            dragStagePane.style = "-fx-background-color:#DDDDDD;"
            StackPane.setAlignment(dragText, Pos.CENTER)
            dragStagePane.children.add(dragText)
            dragStage.scene = Scene(dragStagePane)


            label.onMouseDragged = EventHandler { t ->

                // Resize the window that will attach to the mouse.
                dragStage.width = label.width + 10
                dragStage.height = label.height + 10
                dragStage.x = t.screenX
                dragStage.y = t.screenY
                dragStage.show()

                // Get current location on screen
                val screenPoint = Point2D(t.screenX, t.screenY)

                // Not sure
                tabPanes.add(tabPane)

                // Get information about what's under the mouse
                val data = getInsertData(screenPoint)


                if (data == null || data.insertPane.tabs.isEmpty()) {
                    markerStage.hide()
                } else {
                    var index = data.index
                    var end = false
                    if (index == data.insertPane.tabs.size) {
                        end = true
                        index--
                    }
                    val rect = getAbsoluteRect(data.insertPane.tabs[index])

                    // Position a marker on screen to indicate where the
                    // tab will be dropped.
                    // This will only work horizontally.
                    if (end) {
                        markerStage!!.x = rect.maxX + 13
                    } else {
                        markerStage!!.x = rect.minX
                    }
                    markerStage.y = rect.maxY + 10
                    markerStage.show()
                }
            }


            label.onMouseReleased = EventHandler { t ->

                // Hide the window and marker
                markerStage.hide()
                dragStage.hide()

                // If the mouse didn't move during drag,
                // do nothing.
                if (t.isStillSincePress) return@EventHandler

                // Get current location on screen
                val screenPoint = Point2D(t.screenX, t.screenY)

                val oldTabPane = tabPane
                val oldIndex = oldTabPane.tabs.indexOf(this@DraggableTab)

                tabPanes.add(oldTabPane)

                // Get data about the tab pane we're inserting to.
                val insertData = getInsertData(screenPoint)

                if (insertData != null) {

                    // Not sure
                    if (oldTabPane === insertData.insertPane && oldTabPane.tabs.size == 1)
                        return@EventHandler

                    // Remove from old tab pane
                    oldTabPane.tabs.remove(this@DraggableTab)

                    // Figure out where to add the tab in the new pane.
                    var addIndex = insertData.index
                    if (oldIndex < addIndex && oldTabPane === insertData.insertPane)
                        addIndex--

                    if (addIndex > insertData.insertPane.tabs.size)
                        addIndex = insertData.insertPane.tabs.size

                    // Add to new pane and select it.
                    insertData.insertPane.tabs.add(addIndex, this@DraggableTab)
                    insertData.insertPane.selectionModelProperty().get().select(addIndex)

                    // Done
                    return@EventHandler
                }

                if (!detachable) {
                    return@EventHandler
                }

                // We're not over a tab pane, detach the tab.
                val newStage = Stage()
                val pane = TabPane()
                tabPanes.add(pane)
                newStage.onHiding = EventHandler { tabPanes.remove(pane) }
                tabPane.tabs.remove(this@DraggableTab)

                pane.tabs.add(this@DraggableTab)
                pane.tabs.addListener(ListChangeListener {
                    if (pane.tabs.isEmpty()) {
                        newStage.hide()
                    }
                })

                newStage.scene = Scene(pane)
                newStage.initStyle(StageStyle.UTILITY)
                newStage.x = t.screenX
                newStage.y = t.screenY
                newStage.show()
                pane.requestLayout()
                pane.requestFocus()
            }
        }
    }


    /**
     * Set the label text on this draggable tab. This must be used instead of
     * setText() to set the label, otherwise weird side effects will result!
     *
     *
     * @param text the label text for this tab.
     */
    fun setLabelText(text: String?) {
        label.text = text
        dragText.text = text
    }

    /**
     * Gets data about the tab pane we're inserting to.
     *
     * Determines what tab pane is under the mouse, and gets info.
     */
    private fun getInsertData(screenPoint: Point2D): InsertData? {

        // For every tab pane that contains DraggableTabs
        for (tabPane in tabPanes) {

            // Get the bounds of the tab pane
            val tabAbsolute = getAbsoluteRect(tabPane)
            if (tabAbsolute.contains(screenPoint)) {

                // Figure out what index we're mousing over.
                var tabInsertIndex = 0

                // If there's no tabs, then 0.
                if (tabPane.tabs.isEmpty())
                    return InsertData(tabInsertIndex, tabPane)

                // If off screen?
                val firstTabRect = getAbsoluteRect(tabPane.tabs[0])
                if (firstTabRect.maxY + 60 < screenPoint.y || firstTabRect.minY > screenPoint.y) {
                    return null
                }


                val lastTabRect = getAbsoluteRect(tabPane.tabs[tabPane.tabs.size - 1])

                // If mouse is before first tab, then 0.
                if (screenPoint.x < (firstTabRect.minX + firstTabRect.width / 2)) {
                    tabInsertIndex = 0

                    // If mouse is after last tab, then last index.
                } else if (screenPoint.x > (lastTabRect.maxX - lastTabRect.width / 2)) {
                    tabInsertIndex = tabPane.tabs.size

                    // Else dig through all other tabs.
                } else {
                    for (i in 0 until tabPane.tabs.size - 1) {
                        val leftTab = tabPane.tabs[i]
                        val rightTab = tabPane.tabs[i + 1]


                        if (leftTab is DraggableTab && rightTab is DraggableTab) {
                            val leftTabRect = getAbsoluteRect(leftTab)
                            val rightTabRect = getAbsoluteRect(rightTab)
                            if (betweenX(leftTabRect, rightTabRect, screenPoint.x)) {
                                tabInsertIndex = i + 1
                                break
                            }
                        }
                    }
                }
                return InsertData(tabInsertIndex, tabPane)
            }
        }
        return null
    }

    private fun getAbsoluteRect(node: Control): Rectangle2D {
        return Rectangle2D(
            node.localToScene(node.layoutBounds.minX, node.layoutBounds.minY).x + node.scene.window.x,
            node.localToScene(node.layoutBounds.minX, node.layoutBounds.minY).y + node.scene.window.y,
            node.width,
            node.height
        )
    }

    private fun getAbsoluteRect(tab: Tab): Rectangle2D {
        val node: Control = (tab as DraggableTab).label
        return getAbsoluteRect(node)
    }

    private fun betweenX(r1: Rectangle2D, r2: Rectangle2D, xPoint: Double): Boolean {
        val lowerBound = r1.minX + r1.width / 2
        val upperBound = r2.maxX - r2.width / 2
        return xPoint in lowerBound..upperBound
    }

    private class InsertData(val index: Int, val insertPane: TabPane)
    companion object {

        /**
         * A collection of all tab panes that contain DraggableTabs.
         *
         * Used to look at all tab panes to calculate what tab pane
         * we may be dropping a tab into.
         */
        private val tabPanes: MutableSet<TabPane> = HashSet()

        /**
         * A window dragged around with the mouse containing the name only.
         */
        private var markerStage: Stage = Stage()

        init {
            markerStage.initStyle(StageStyle.UNDECORATED)
            val dummy = Rectangle(3.0, 10.0, Color.web("#555555"))
            val markerStack = StackPane()
            markerStack.children.add(dummy)
            markerStage.scene = Scene(markerStack)
        }
    }
}