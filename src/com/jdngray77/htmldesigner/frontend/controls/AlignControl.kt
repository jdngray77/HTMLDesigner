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

import com.jdngray77.htmldesigner.backend.utility.reverseGet
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

            ToggleButton("Start"),
            ToggleButton("Center"),
            ToggleButton("Justify"),
            ToggleButton("End")
        )
    }

    fun getAlignment() =
        FlexJustify[buttons.filtered { it.isSelected }.first().text]

    fun setAlignment(x : String) {
        buttons.forEach { it.isSelected = false }

        val y = FlexJustify.reverseGet(x)
        buttons.find { it.text == y }?.isSelected = true
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
