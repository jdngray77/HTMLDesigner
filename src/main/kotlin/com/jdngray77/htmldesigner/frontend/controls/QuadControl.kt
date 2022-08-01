package com.jdngray77.htmldesigner.frontend.controls

/*░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
 ░                                                                                                ░
 ░ Jordan T. Gray's                                                                               ░
 ░                                                                                                ░
 ░          HTML Designer                                                                         ░
 ░                                                                                                ░
 ░ FOSS 2022.                                                                                     ░
 ░ License decision pending.                                                                      ░
 ░                                                                                                ░
 ░ https://www.github.com/jdngray77/HTMLDesigner/                                                 ░
 ░ https://www.jordantgray.uk                                                                     ░
 ░                                                                                                ░
 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

import com.jdngray77.htmldesigner.utility.growH
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.layout.VBox
import javafx.scene.text.TextAlignment
import org.controlsfx.control.SegmentedButton

/**
 * A control which can hold a many CSS unit providing controls.
 *
 * It can display a single control to supply CSS values where all sides are the same,
 * or it can display a grid of controls to supply CSS values where each side is different.
 *
 * i.e, two controls for top-bottom, left-right CSS (1px, 1px), or four controls for top-bottom-left-right CSS (1px, 1px, 1px, 1px)
 *
 * Values will always be cleared when mode is switched.
 *
 * @param T The type of data.
 * @param R The type of control.
 * @param controlFactory The factory to create controls to place in the view.
 *
 * @author Jordan Gray
 */
class QuadControl<T,R : Node>(val controlFactory : () -> R, val controlGetter: (R) -> T, val controlSetter: (R, T) -> Unit) : VBox(){

    companion object {

        /**
         * Strings to display above the controls, depending on the selected mode.
         */
        val modeTexts = hashMapOf(
            QuadControlMode.Same to arrayOf("All sides"),
            QuadControlMode.Cardinal to arrayOf("Top and Bottom", "Left and Right"),
            QuadControlMode.All to arrayOf("Top", "Right", "Bottom", "Left")
        )

    }

    /**
     * The possible modes of the control.
     * TODO probably rename them.
     */
    enum class QuadControlMode {

        // All sides are the same.
        Same,

        // Top = Bottom && Left = Right
        Cardinal,

        // All sides are different.
        All
    }

    /**
     * Buttons that allow the user to switch between modes.
     */
    private val modeButtons: SegmentedButton = SegmentedButton(
        ToggleButton("Same"),
        ToggleButton("Cardinal"),
        ToggleButton("All")
    ).also {
        it.toggleGroup.selectedToggleProperty().addListener {
            _,old,new  -> if (old != new ) reconstruct()
        }
    }

    fun selectedMode() = QuadControlMode.valueOf((modeButtons.toggleGroup.selectedToggle as ToggleButton).text)

    val controlContainer: VBox = VBox()


    init {

        modeButtons.growH()
        controlContainer.growH()
        growH()


        children.add(modeButtons)
//        modeButtons.
//        modeButtons.toggleGroup.selectToggle(modeButtons.toggleGroup.toggles.first())

        children.add(controlContainer)
//        reconstruct()
    }

    /**
     * Update the view to reflect the current mode.
     */
    private fun reconstruct() {
        // Clear the control container
        controlContainer.children.clear()

        // Create the GUI based on the selected mode
        when (selectedMode()) {
            QuadControlMode.Same -> { createControl(1) }
            QuadControlMode.Cardinal -> { createControl(2) }
            QuadControlMode.All -> { createControl(4) }
        }
    }

    /**
     * Create a control for the given number of sides.
     *
     * Controls are labelled depending on thier mode and index.
     *
     * @param quantity The number of controls to create.
     * @see getControlString
     * @see modeTexts
     */
    private fun createControl(quantity : Int) {
        for (i in 0 until quantity) {
            with (controlContainer.children) {
                add(createText(i))
                add(controlFactory())
            }
        }
    }

    /**
     * Creates a label for the control, containing the [getControlString] of the control.
     *
     * @param index The index of the control.
     * @return The label.
     */
    private fun createText(i: Int) = Label(getControlString(i)).also {
        it.growH()
        it.textAlignment = TextAlignment.CENTER
    }

    /**
     * Gets the string to display for the control at the given index,
     * depending on the selected mode.
     */
    private fun getControlString(index: Int) =
        modeTexts[selectedMode()]?.get(index) ?: "-Unknown-"

    /**
     * Returns a CSS string containing the value obtained from each control
     * currently present.
     */
    fun getValue(): String =
        getAllControls().joinToString(" ", transform = { controlGetter(it as R).toString()} )


    /**
     * Gets a list of all controls currently present.
     */
    fun getAllControls(): List<R> {
        val cachedType = controlFactory()::class
        return controlContainer.children.filter { it::class == cachedType } as List<R>
    }

    /**
     * Blankly sets all controls to the provided value.
     */
    fun setValue(value: T) {
        getAllControls().forEach {
            controlSetter(it, value)
        }
    }
}