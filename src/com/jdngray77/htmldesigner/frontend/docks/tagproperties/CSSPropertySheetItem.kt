package com.jdngray77.htmldesigner.frontend.docks.tagproperties

import com.jdngray77.htmldesigner.frontend.controls.AlignControl
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor
import org.jsoup.nodes.Element
import org.w3c.dom.css.CSSStyleSheet
import java.util.*


open class CSSPropertySheetItem(
    val _name: String,
    val element : Element,
    val property : String,
    val _category: String,
    val _description: String
) : PropertySheet.Item {

    override fun getType() = String::class.java

    override fun getCategory() = _category
    override fun getName() = _name
    override fun getDescription() = _description

    // TODO
    override fun getValue(): Any = ""

    // TODO
    override fun setValue(value: Any?) {

    }

    override fun getObservableValue(): Optional<ObservableValue<out Any>> =
        Optional.empty()
}


class CSSAlignmentPropertySheetItem (
    _name: String,
    element : Element,
    _category: String,
    _description: String
) : CSSPropertySheetItem(
    _name,
    element,
    "justify-content",
    _category,
    _description
) {


}

class CSSAlignmentPropertyEditor : PropertyEditor<String>{

    val editor = AlignControl()

    override fun getEditor(): Node =
        editor


    override fun getValue(): String =
        editor.getAlignment() ?: ""

    override fun setValue(value: String) {
        editor.setAlignment(value)
    }
}