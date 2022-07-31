package com.jdngray77.htmldesigner.backend

import com.vladsch.javafx.webview.debugger.DevToolsDebuggerJsBridge
import javafx.scene.web.WebView
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DebugServerTest {

    @Test
    internal fun lib() {
        try {
            DevToolsDebuggerJsBridge(WebView(),WebView().engine,0,null)
        } catch (e: NoClassDefFoundError) {
            fail("vladsch/javafx/webview/debugger/DevToolsDebuggerJsBridge was missing from the classpath.")
        } catch (e: Exception) {}
    }
}