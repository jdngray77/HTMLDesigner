package com.jdngray77.htmldesigner.docks.toolbox

import com.jdngray77.htmldesigner.FXMLUtility
import com.jdngray77.htmldesigner.html.dom.ALLTAGS
import com.jdngray77.htmldesigner.html.dom.Tag
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox

/**
 * A list of all tags that for the user to add to the dom.
 */
class ToolboxDock : VBox() {

    private fun <T : Tag> addItem(t : T) {
        children.add(
            FXMLUtility.loadFXMLComponent<AnchorPane>("docks/toolbox/ToolBoxItem.fxml").let {
                ToolBoxItem(it.first).also { item -> item.initialize(t, it.second as ToolBoxItemController) }.pane
            }
        )
    }

    init {
        ALLTAGS.forEach {
            addItem(it)
        }
    }
}

