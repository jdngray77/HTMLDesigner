
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

package com.jdngray77.htmldesigner.project

import com.jdngray77.htmldesigner.backend.data.PREFERENCE
import com.jdngray77.htmldesigner.backend.data.Preferences
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test


class PreferencesTest {

    val preference: Preferences = Preferences()

    @Test
    @Order(1)
    fun InitialisationAndRead() {
        preference.apply {
            PREFERENCE.values().forEach { // For every PREFERENCE key,
                try {
                    assertTrue(get(it)!!::class.simpleName!! == typeOf(it)) // Try to read it, and check that the type recieved matches the type in the key.
                    println("Read $it")
                } catch (e: NullPointerException) {
                    fail("Preference key was not initialised (i'm pretty sure) : $it")
                } catch (e: IllegalStateException) {
                    fail(e.message) // Data does not match name.
                }
            }
        }
    }


    @Test
    fun flush() {
        TODO()
    }

    @Test
    fun reloadFromProject() {
        TODO()
    }
}