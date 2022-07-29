package com.jdngray77.htmldesigner.frontend.controls

import javafx.scene.control.ComboBox

/**
 * A combo box that displays CSS Units.
 *
 * @author Jordan Gray
 */
class CSSUnitCombo(

    defaultValue: String = CSSUnits[0],

) : ComboBox<String>() {


    init {
        items.addAll(*CSSUnits)

        selectionModel.select(defaultValue)
    }
}




//TODO this should not be here.

// TODO this is incomplete
val CSSUnits = arrayOf("px", "em", "rem", "vh", "vw", "vmin", "vmax", "%")

/**
 * When provided a valid CSS size, removes all the units from a CSS size value.
 *
 * i.e "10px" -> "10"
 *
 * @param value just the numeric value of the size
 */
fun removeCSSUnit(value: String): String {
    var output = value

    CSSUnits.forEach {
        output = output.replace(it, "")
    }

    return output.trim()
}

/**
 * It takes a string and returns the CSS unit if it exists in the string
 *
 * i.e "10px" -> "px", "10em" -> "em"
 *
 * @param value The value of the CSS property.
 * @return The CSS unit of the value.
 */
fun determineCSSUnit(value: String): String {
    CSSUnits.forEach {
        if (value.contains(it)) {
            return it
        }
    }

    return ""
}


