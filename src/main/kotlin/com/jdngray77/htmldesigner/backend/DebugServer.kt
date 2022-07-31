package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.frontend.DocumentEditor
import com.vladsch.javafx.webview.debugger.DevToolsDebuggerJsBridge
import javafx.concurrent.Worker
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

object DebugServer {

    /**
     * The server connection.
     *
     * Extended with some custom funcitonality.
     */
    private class implBridge(
        val editor: DocumentEditor
    ) : DevToolsDebuggerJsBridge(editor.contentRenderer, editor.contentRenderer.engine, 0, null) {

        init {
            // TODO Use this to notify close.
            editor.debugger = EditorDebugger

            // Attach a listener to the editors webview.
            // When it loads a page, connect the
            editor.contentRenderer.engine.loadWorker.stateProperty().addListener {
                stateProperty, oldState, newState ->
                if (newState == Worker.State.SUCCEEDED) {
                    documentChanged()
                    bridge!!.connectJsBridge()
                }
            }
        }

    }

    /**
     * Interface methods given to the editor currently being hosted.
     *
     * This lets the editor know that it's being debugged, and provides it
     * a way to call back - such as when it refreshes.
     */
    object EditorDebugger {

        fun documentChanged() = DebugServer.documentChanged()

    }

    /**
     * The active connection to chrome.
     */
    private var bridge: implBridge? = null

    /**
     * TODO This is very useful, but i don't want it here.
     *
     * Move to document editor use to select tags.
     */
    var clickListener =
        EventListener { evt: Event ->
            val element = evt.target as Node
            val id = element.attributes.getNamedItem("id")
            val idSelector = if (id != null) "#" + id.nodeValue else ""
            //addMessage("onClick: clicked on " + element.nodeName + idSelector)
        }.also {
            //var document: Document = myWebView.getEngine().getDocument()
            //(document as org.w3c.dom.events.EventTarget?).addEventListener("click", clickListener, false)
        }



    fun host(editor: DocumentEditor) {
        bridge?.let {
            // Request debug server to stop
            it.stopDebugServer() {
                // Once stopped
                bridge!!.editor.debugger = null
                bridge = null
            }
        }

        // Start a new server
        bridge = implBridge(editor)

        // TODO these are configurable
        bridge!!.startDebugServer(255655, null, null)
    }



    fun documentChanged() =
        bridge!!.reloadPage(false, false)

}