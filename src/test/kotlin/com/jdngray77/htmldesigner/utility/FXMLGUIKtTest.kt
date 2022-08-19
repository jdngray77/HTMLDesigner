package com.jdngray77.htmldesigner.utility

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

internal class FXMLGUIKtTest {

    @Test
    fun loadFXMLScene() {
    }

    @Test
    fun loadFXMLComponent() {
    }

    @Test
    fun toHex() {
        val red = Color.RED.toHex()
        val green = Color.LIME.toHex()
        val blue = Color.BLUE.toHex()

        basicTestHex(red)
        basicTestHex(green)
        basicTestHex(blue)
        assertEquals("#ff0000", red, "RED.toHex did not provide #ff0000")
        assertEquals("#00ff00", green, "GREEN.toHex did not provide #00ff00")
        assertEquals("#0000ff", blue, "BLUE.toHex did not provide #0000ff")

        Color::class.java.declaredFields
            .filter { classEquals(it.type, Color::class.java) }
            .forEach {
                basicTestHex((it.get(null) as Color).toHex())
            }
    }

    private fun basicTestHex(testValue: String) {
        assert(testValue.startsWith("#")) { "Hex does not start with #" }
        assert(testValue.length == 7) {"Hex is not 7 characters total."}
    }

    @Test
    fun growH() {
        val l = Label()
        l.growH()

        assert(HBox.getHgrow(l) == Priority.ALWAYS)
    }

    @Test
    fun growV() {
        val l = Label()
        l.growV()

        assert(VBox.getVgrow(l) == Priority.ALWAYS)
    }

    @Test
    fun grow() {
        val l = Label()
        l.grow()

        assert(HBox.getHgrow(l) == Priority.ALWAYS)
        assert(VBox.getVgrow(l) == Priority.ALWAYS)
    }

    @Test
    fun openURL() {
        assertDoesNotThrow {
            openURL("https://www.google.com")
        }
    }
}