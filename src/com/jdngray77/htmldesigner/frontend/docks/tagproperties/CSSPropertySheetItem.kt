package com.jdngray77.htmldesigner.frontend.docks.tagproperties

import javafx.beans.value.ObservableValue
import org.controlsfx.control.PropertySheet
import org.jsoup.nodes.Element
import org.w3c.dom.css.CSSStyleSheet
import java.util.*


class CSSPropertySheetItem(
    val element : Element,
    val property : String,
    val sheet: CSSStyleSheet,
    val _category: String,
    val _description: String
) : PropertySheet.Item {

    override fun getType() = String::class.java

    override fun getCategory() = _category
    override fun getName() = property
    override fun getDescription() = _description

    override fun getValue(): Any {
        TODO("Not yet implemented")
    }

    override fun setValue(value: Any?) {
        TODO("Not yet implemented")
    }

    override fun getObservableValue(): Optional<ObservableValue<out Any>> {
        TODO("Not yet implemented")
    }

}