package com.jdngray77.htmldesigner.frontend.docks.dockutils

import com.jdngray77.htmldesigner.backend.CamelToSentence
import javafx.beans.value.ObservableValue
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor
import java.util.*
import kotlin.reflect.KMutableProperty

/**
 *  A wrapper around a field of an object that allows you to use that field as a property sheet item
 *
 *  When making A [PropertySheet], provides a way to easily edit any field
 *  in eny object via reflection
 */
open class ReflectivePropertySheetItem<T>(
    val fieldName: String,
    val _description : String,
    val _category : String,
    val obj : Any,
    val _isEditable: Boolean = true
) : PropertySheet.Item {

    // TODO cache this. idm at the sec cause it's not used.
    fun getReflectiveField() = obj::class.members.find { it.name == fieldName } as KMutableProperty<T>?

    override fun getCategory() = _category
    override fun getName() = fieldName.CamelToSentence()
    override fun getDescription() = _description

    override fun getValue() = getReflectiveField()?.getter?.call()

    override fun setValue(value: Any?) {
        getReflectiveField()?.setter?.call(obj, value)
    }

    override fun getType() = obj::class.java.getDeclaredField(fieldName).type

    override fun isEditable() = _isEditable

    override fun getObservableValue(): Optional<ObservableValue<out Any>> =
        Optional.empty()

    override fun getPropertyEditorClass(): Optional<Class<out PropertyEditor<*>>> =
        Optional.empty()
}
