package com.jdngray77.htmldesigner.frontend.editors.jsdesigner

import com.jdngray77.htmldesigner.utility.setOnDoubleClick
import javafx.scene.Node

/**
 * An event manager that controls things that can be collapsed and expanded
 *
 * @param clickable The thing that may be double click to trigger the collapse / expand
 * @param onCollapse The function to call to perform collapse logic
 * @param onExpand (Optional) The function to call to perform expand logic. If not provided, [onCollapse] will be called for both events.
 */
class DoubleClickCollapseManager (

    clickable: Node,

    val onCollapse: () -> Unit,

    val onExpand: (() -> Unit)?

) {

    var isCollapsed = false
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
        isCollapsed = !isCollapsed
        if (!isCollapsed)
            onExpand?.let { it() } ?: onCollapse()
        else
            onCollapse()
    }

}