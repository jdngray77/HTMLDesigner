
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

package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.frontend.docks.dockutils.*
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Slider
import javafx.embed.swing.JFXPanel
import javafx.scene.control.*
import javafx.scene.paint.Color
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

import java.time.Instant
import java.util.*

internal class AutoDockTest {


    @BeforeEach
    internal fun setUp() {
            JFXPanel() // This is needed to initalise the JavaFX stuff under the hood.
    }


    @Test
    fun TestAllDataTypeCreation () {
        val dock = object : AutoDock() {
            @Inspectable(0) var string: String = "Hello"
            @GUI
            lateinit var string_GUI: TextField

//            @LargeText
//            @Inspectable(1) var largestring: String = "Hello"
//            @GUI lateinit var largestring_GUI: TextField

            @Inspectable(2) var boolean = true
            @GUI
            lateinit var boolean_GUI: CheckBox

//            @Inspectable(0) var toggleboolean = true
//            @GUI lateinit var toggleboolean_GUI: ToggleButton

            @Inspectable(3) var color =  Color.RED
            @GUI
            lateinit var color_GUI: ColorPicker

            @Inspectable(4) var date = Date.from(Instant.now())
            @GUI
            lateinit var data_GUI: TextArea

            @Inspectable(5) var int = 1
            @GUI
            lateinit var int_GUI: Spinner<Int>

            @Inspectable(6) var float = 1f
            @GUI
            lateinit var float_GUI: Spinner<Float>

            @Inspectable(7) var double = 1.0
            @GUI
            lateinit var double_GUI: Spinner<Double>

            @Inspectable(8) var short: Short = 1
            @GUI
            lateinit var short_GUI: Spinner<Short>

            @Slider(0, 100)
            @Inspectable(9) var sliderint = 1
            @GUI
            lateinit var sliderint_GUI: javafx.scene.control.Slider

            @Slider(0, 100)
            @Inspectable(10) var sliderfloat = 1f
            @GUI
            lateinit var sliderfloat_GUI: javafx.scene.control.Slider

            @Slider(0, 100)
            @Inspectable(11) var sliderdouble = 1.0
            @GUI
            lateinit var sliderdouble_GUI: javafx.scene.control.Slider

            @Slider(0, 100)
            @Inspectable(12) var slidershort: Short = 1
            @GUI
            lateinit var slidershort_GUI: javafx.scene.control.Slider

//            @Inspectable(13) var enum: PREFERENCE = PREFERENCE.EDITOR_UNDOHISTORY_LENGTH_INT
//            @GUI lateinit var enum_GUI: ComboBox<PREFERENCE>

//            @RadioButton
//            @Inspectable(14) var radioenum: PREFERENCE = PREFERENCE.EDITOR_UNDOHISTORY_LENGTH_INT
//            @GUI lateinit var raduoenum_GUI: RadioGroup
        }

        try {
            dock.create()
        } catch (e: Exception) {
            e.printStackTrace()
            fail<Unit>(e)
        }


    }

    @Test
    fun TestWrongType () {
        val dock = object : AutoDock() {
            @Inspectable(0) var string: String = "Hello"
            @GUI
            lateinit var string_GUI: TextArea
        }


        try {
            dock.create()
        } catch (e: InspectableException) {
            assertTrue(e.message?.contains("string_GUI has the wrong type") == true)
            return
        }

        fail<Unit>("Invalid field type was not detected")
    }

    @Test
    fun TestVal () {
        val dock = object : AutoDock() {
            @Inspectable(0) val string: String = "Hello"
            @GUI
            lateinit var string_GUI: TextField
        }

        dock.create()
        assertFalse(dock.string_GUI.disableProperty().value)
    }

}