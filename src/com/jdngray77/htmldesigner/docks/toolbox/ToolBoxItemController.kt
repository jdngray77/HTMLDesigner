package com.jdngray77.htmldesigner.docks.toolbox

import com.jdngray77.htmldesigner.html.dom.Tag
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane


/**
 * Controller for an item in the [ToolBox] dock.
 */
class ToolBoxItemController() {
    @FXML lateinit var name : Label
    @FXML lateinit var tag : Label
}

// TODO don't do this as a class, just use the controller.
class ToolBoxItem(val pane: AnchorPane) {
    lateinit var controller : ToolBoxItemController


    fun initialize(tag: Tag, controller: ToolBoxItemController) {
        this.controller = controller

        controller.name.text = tag.name()
        controller.tag.text  = tag.serializeOpen()
    }
}