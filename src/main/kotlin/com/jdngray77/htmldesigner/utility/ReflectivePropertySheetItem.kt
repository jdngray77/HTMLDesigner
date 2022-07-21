


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

package com.jdngray77.htmldesigner.utility

import javafx.beans.value.ObservableValue
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor
import java.util.*

/**
 *  A wrapper around a field of an object that allows you to use that field as a property sheet item
 *
 *  When making A [PropertySheet], provides a way to easily edit any field
 *  in eny object via reflection
 */
class ReflectivePropertySheetItem<T>(

    /**
     * The name of the property (variable) within [obj]
     * that will be modified. This is also the
     * name displayed to the user.
     */
    val fieldName: String,

    /**
     * More information that is shown to the user, when they hover
     * the cursor.
     */
    val _description: String,

    /**
     * The string used to group properties together in the GUI.
     */
    val _category: String,

    /**
     * The object being modified
     */
    val obj: Any,

    /**
     * Determines if this property is read-only
     */
    val _isEditable: Boolean = true

) : PropertySheet.Item {

    val javaGetter = obj::class.java.getDeclaredMethod(fieldName)

    val javaSetter = obj::class.java.getDeclaredMethod(fieldName, getType())


    override fun getValue() =
        javaGetter.invoke(obj) as T


    override fun setValue(value: Any?) {
        javaSetter.invoke(obj, value)
    }

    override fun getType() = javaGetter.returnType

    override fun getCategory() = _category

    override fun getName() = fieldName.camelToSentence()

    override fun getDescription() = _description

    override fun isEditable() = _isEditable

    override fun getObservableValue(): Optional<ObservableValue<out Any>> =
        Optional.empty()

    override fun getPropertyEditorClass(): Optional<Class<out PropertyEditor<*>>> =
        Optional.empty()

}