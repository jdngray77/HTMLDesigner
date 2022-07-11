
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

package com.jdngray77.htmldesigner.frontend.controls

import javafx.beans.value.ObservableValue
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor
import java.util.*

/**
 * A [PropertySheet.Item] that represents an editor / item that
 * has not yet been added.
 *
 * Displays a label and a disabled text field.
 */
class PlaceholderPropertySheetItem(

    /**
     * The name of the non-existant field.
     */
    val _name: String,

    /**
     * The string used to group properties together in the GUI.
     */
    val _category : String

) : PropertySheet.Item {


    override fun getValue() = description
    override fun setValue(value: Any?) {}
    override fun getType() = String::class.java

    override fun getCategory() = _category
    override fun getName() = _name
    override fun getDescription() = "This field has not yet been implemented."
    override fun isEditable() = false

    override fun getObservableValue(): Optional<ObservableValue<out Any>> =
        Optional.empty()

    override fun getPropertyEditorClass(): Optional<Class<out PropertyEditor<*>>> =
        Optional.empty()
}