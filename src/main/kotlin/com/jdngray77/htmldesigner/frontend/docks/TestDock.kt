package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.frontend.controls.CSSUnitSlider
import com.jdngray77.htmldesigner.frontend.controls.QuadControl
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock

class TestDock : Dock() {

    init {
        center = QuadControl(
            { CSSUnitSlider() },
            { it.toString() },
            { control, value -> control.setValue(value) }
        )
    }

}