package com.jdngray77.htmldesigner.frontend.controls

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import kotlin.math.floor

/**
 *  A slider with a user selectable unit of css measure.
 */
class CSSUnitSlider(

    min: Double = 0.0,
    max: Double = 100.0,
    step: Double = 1.0,
    defaultUnit: String = CSSUnits[0],
    enableUnits : Boolean = true,

) : HBox() {

    val slider = CommittingSlider(min, max, step)

    val unit = CSSUnitCombo(defaultUnit)

    init {
        children.add(slider)
        showUnits(enableUnits)

        slider.setOnChangeCommitted {
            updateObservableValue()
        }
    }

    fun showUnits(boolean: Boolean) {
        if (boolean)
            if (!children.contains(unit))
                children.add(unit)
        else
           children.remove(unit)
    }

    override fun toString(): String =
        if (slider.value > 0.0)
            floor(slider.value).toString() + unit.value
        else
            ""

    fun setValue(value: String?) {
        if (value == null) {
            slider.value = slider.min
            return
        }


        unit.selectionModel.select(determineCSSUnit(value))
        slider.value = removeCSSUnit(value).toDouble()
    }


    private fun updateObservableValue() {
        observableValue.value = toString()
    }

    /**
     * Registers a function to execute that previews a change to
     * the value of the slider, whilst it is in motion.
     */
    fun setPreviewValueChange(it: (Double) -> Unit) {
        slider.setOnUncommittedChanged(it)
    }

    fun getValue(): String = toString()

    val observableValue = SimpleStringProperty(toString())
}