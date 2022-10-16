package com.jdngray77.htmldesigner.frontend.controls

import javafx.scene.control.Slider
import javafx.scene.control.TextFormatter.Change

typealias ChangeListener = ((Double) -> Unit)?

/**
 * A slider with modified listeners.
 *
 * The event listening for value changed is disambiguated between
 * sliding, and committing a value.
 */
class CommittingSlider(min: Double, max: Double, value: Double) : Slider() {
    constructor() : this(0.0, 100.0, 0.0)

    var onChanged   : ChangeListener = null
    var onCommitted : ChangeListener = null


    init {
        super.valueProperty().addListener { _,_,_ ->
            onChanged?.invoke(value)
        }

        super.setOnMouseReleased {
            onCommitted?.invoke(value)
        }
    }

    fun setOnUncommittedChanged(it : ChangeListener) {
        onChanged = it
    }

    fun setOnChangeCommitted(it : ChangeListener) {
        onCommitted = it
    }

}