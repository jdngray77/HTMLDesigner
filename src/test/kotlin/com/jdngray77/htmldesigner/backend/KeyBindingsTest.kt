package com.jdngray77.htmldesigner.backend

import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class KeyBindingsTest {

    @Test
    fun assignKeys() {}

    @Test
    fun getKeyArray() {
        val keys = KeyBindings.array

        assertNotNull(keys)
        assert(keys.size >= 0)
    }

    @Test
    fun checkColumns() {

    }

    @Test
    fun checkRows() {

    }

    @After
    fun tearDown() {
        TODO("Not yet implemented")
    }
}