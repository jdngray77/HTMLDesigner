package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.utility.setOnDoubleClick
import javafx.scene.Node

/**
 * An event listener that controls things that can be double clicked to change state,
 *
 * On [clickable] being double clicked, [isToggled] is inverted.
 * If it's now true, [onTrue] is ran, otherwise [onFalse] is ran if it's provided.
 *
 * (If [onFalse] is not provided, this [onTrue] is ran, but this is pointless. If you're not using
 * onFalse, just use [setOnDoubleClick] instead.)
 *
 * i.e [onTrue] is called upon first double click, then [onFalse] is called upon second double click.
 *
 * ideal use case : Double click a node to expand it, then double click it again to collapse it.
 *
 * @param clickable The thing that may be double click to trigger the collapse / expand
 * @param onTrue The function to call to perform collapse logic
 * @param onFalse (Optional) The function to call to perform expand logic. If not provided, [onTrue] will be called for both events.
 */
class DoubleClickToggleListener (

    clickable: Node,

    val onTrue: () -> Unit,

    val onFalse: (() -> Unit)?

) {

    var isToggled = false
        private set

    /**
     * Configures the listener
     */
    init {
        clickable.setOnDoubleClick {
            toggle()
        }
    }

    fun toggle() {
        isToggled = !isToggled
        if (!isToggled)
            onFalse?.let { it() } ?: onTrue()
        else
            onTrue()
    }

}