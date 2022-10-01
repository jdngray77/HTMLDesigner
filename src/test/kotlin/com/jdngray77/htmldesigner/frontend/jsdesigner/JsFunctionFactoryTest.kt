package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.frontend.editors.jsdesigner.JsFunctionFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.reflect.full.createInstance

internal class JsFunctionFactoryTest {

    /**
     * If able to instantiate, then the parameters are configured correctly.
     */
    @Test
    fun allFunctionsInstantiable() {
        JsFunctionFactory.allFunctions.forEach {
            assertDoesNotThrow { it.createInstance() }
        }
    }
}