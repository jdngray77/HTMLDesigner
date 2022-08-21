package com.jdngray77.htmldesigner.frontend.controls

import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style

class ItemSelectionDialog<T>(

    items: Iterable<T>

) : SearchableList<T>(items) {

    val dialog = Dialog<ButtonType>().also {
        JMetro(Style.LIGHT).scene = it.dialogPane.scene
        it.dialogPane.setPrefSize(800.0, 500.0)
        it.title = "Select an item"
        it.dialogPane.content = this
        it.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)
    }

    fun showAndWait(): T {

        dialog.showAndWait()

        return selectedItem()
    }

    override fun onAction(item: T) {
        dialog.hide()
    }

}