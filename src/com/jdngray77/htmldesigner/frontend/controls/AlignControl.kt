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

import com.jdngray77.htmldesigner.utility.reverseGet
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.ToggleButton
import org.controlsfx.control.SegmentedButton


class AlignControl : SegmentedButton() {

    init {

        // TODO i can't for the life of me get this to use icons.
//        val fontAwesome = GlyphFontRegistry.font("FontAwesome")

        buttons.addAll(
//            ToggleButton("1", fontAwesome.create(Glyph.ALIGN_LEFT)),
//            ToggleButton("2", fontAwesome.create(Glyph.ALIGN_CENTER)),
//            ToggleButton("3", fontAwesome.create(Glyph.ALIGN_JUSTIFY)),
//            ToggleButton("4", fontAwesome.create(Glyph.ALIGN_RIGHT)),
//            ToggleButton("5", fontAwesome.create(Glyph.GEAR)),
//            ToggleButton("6", fontAwesome.create(Glyph.HEART)),
//            ToggleButton("7", fontAwesome.create(Glyph.ARROW_DOWN)),
//            ToggleButton("8", fontAwesome.create(Glyph.AMBULANCE)),

            // TODO sort this fucking mess out
            // TODO value is not cleared if all buttons are de-selected.

            ToggleButton("Start").apply {
               setOnAction {
                   observableValue.value = text
               }
            },
            ToggleButton("Center").apply {
                setOnAction {
                    observableValue.value = text
                }
            },
            ToggleButton("Justify").apply {
                setOnAction {
                    observableValue.value = text
                }
            },
            ToggleButton("End").apply {
                setOnAction {
                    observableValue.value = text
                }
            }
        )
    }


    //TODO listener on buttons

    val observableValue = SimpleStringProperty()

    fun getAlignment() =
        observableValue.value
//        FlexJustify[buttons.filtered { it.isSelected }.first().text]

    fun setAlignment(x : String) {
        buttons.forEach { it.isSelected = false }

        val y = FlexJustify.reverseGet(x)
        buttons.find { it.text == y }?.isSelected = true

        observableValue.value = x
    }
}

object FlexJustify : HashMap<String, String>() {
    init {
        put("Start", "flex-start")
        put("End", "flex-end")
        put("Center", "center")

        put("Between", "space-between")
        put("Around", "space-around")
        put("Evenly", "space-evenly")
    }
}
