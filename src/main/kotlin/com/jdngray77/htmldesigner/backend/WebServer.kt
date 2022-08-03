package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.utility.Restartable
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.awt.Desktop
import java.net.InetSocketAddress
import java.net.URI

/**
 * A (Live-ish) web server for development and debugging.
 *
 * Hosts the current document, so it can be viewed in a real-world
 * web browser.
 *
 * Generally the only user-feedback of this system is some
 * notifications show at [start] and [stop].
 * @author Jordan Gray
 */
object WebServer : Subscriber, Restartable {

    /**
     * The port that the server will/is being hosted on.
     *
     * Loaded from the [Config] at time of server [start]
     */
    private var PORT = Config[Configs.WEB_SERVER_PORT_INT] as Int

    /**
     * The underlying [HttpServer] that is hosting our files.
     */
    private var server: HttpServer? = null


    /**
     * The document that is being served, if any.
     */
    var DOCUMENT_SERVING : Document? = null
        private set

    /**
     * A meta-tag injected into pages served, if [autoRefresh] is enabled.
     *
     * Causes the browser to update frequently.
     *
     * Refresh delay is read from [Config] when the page is served.
     * (When this val is read.)
     */
    private val REFRESH = Element("meta")
        .attr("http-equiv", "refresh")
        .id("HTMLDESIGNER-META-REFRESH")
        get() {
            field.attr("content", (Config[Configs.WEB_SERVER_REFRESH_DELAY_INT] as Int).toString())
            return field
        }

    /**
     * Determines whether the browser should automatically refresh the page.
     *
     * When true, [REFRESH] is injected into the document when it's served.
     *
     * > N.B There's a [CheckMenuItem] in the menu bar whose [isSelected] will fall out of sync with [autoRefresh] if this is used.
     * >     This isn't crutial, but the check mark shown to the user will be made unreliable.
     */
    @Deprecated("Causes a checked menu item in the GUI to not accurately reflect the state of auto-refresh.")
    var autoRefresh : Boolean = true
        set(value) {
            field = value
            serve(DOCUMENT_SERVING)

            if (value)
                showNotification("Web Server", "Refresh your browser manually in order to re-start auto-refreshing.")
        }

    /**
     * Toggles [autoRefresh]
     *
     * > N.B There's a [CheckMenuItem] in the menu bar whose [isSelected] will fall out of sync with [autoRefresh] if this is used.
     * >     This isn't crutial, but the check mark shown to the user will be made unreliable.
     */
    @Deprecated("Causes a checked menu item in the GUI to not accurately reflect the state of auto-refresh.")
    fun toggleAutoRefresh() {
        autoRefresh = !autoRefresh
    }

    /**
     * A document served for a few seconds as the server shuts down.
     *
     * Pages that are auto-refreshing frequently may be able to make a request as the
     * server is shutting down, and the this page will be served to the user - saying that
     * the server was closed.
     */
    private var DOCUMENT_DISCONNECTED : Document = Document("").also {
            it.body()
            .insertChildren(
                0,
                    Element("h1")
                            .text("HTML Designer's Web Server was shut down.")
                            .attr("style", "text-align: center;"),

                    Element("p")
                            .text("You can restart it in the menu (menu > Tools > Live Server > Start).")
                            .attr("style", "text-align: center;")
            )
    }

    /**
     * Boots the server.
     */
    fun start() {
        if (!mvc().editorAvail()) {
            showNotification("Web Server", "The server was not started.\nThere is no document open to host.")
            return
        }

        if (server != null) {
            showNotification("Web Server", "The server was not started.\nThe server is already running.")
            return
        }



        try {
            // Create Server
            server = HttpServer.create(InetSocketAddress(PORT), 0)

            // Tell the server what to do when the file is requested.
            server!!.createContext("/") {
                    httpExchange: HttpExchange ->

                if (autoRefresh && DOCUMENT_SERVING != DOCUMENT_DISCONNECTED)
                    DOCUMENT_SERVING!!.head().appendChild(REFRESH)

                // Inject a refresh meta tag to force a refresh of the page.
                val response = DOCUMENT_SERVING.toString().toByteArray(charset("UTF-8"))

                // Remove the tag, so the document doesn't stay modified with it.
                // It's only added whilst adding the document to the server.
                DOCUMENT_SERVING!!.head().getElementById("HTMLDESIGNER-META-REFRESH")?.remove()
                assert(!DOCUMENT_SERVING!!.head().toString().contains(REFRESH.toString()))


                httpExchange.responseHeaders.add("Content-Type", "text/HTML; charset=UTF-8")
                httpExchange.sendResponseHeaders(200, response.size.toLong())
                val out = httpExchange.responseBody
                out.write(response)
                out.close()
            }

            // Start the server
            server!!.start()
        } catch (tr: Throwable) {
            tr.printStackTrace()
        }

        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_EDITED, EventType.EDITOR_DOCUMENT_SWITCH)

        serve(mvc().currentDocument())
        Desktop.getDesktop().browse(URI.create("http://localhost:$PORT"))

        showNotification("Web Server", "The server was started.")
        mvc().MainView.setStatus("Web Server Started")
    }

    /**
     * Stops the server.
     */
    fun stop() {
        serve(DOCUMENT_DISCONNECTED)
        server?.stop(2)
        server = null
        DOCUMENT_SERVING = null

        EventNotifier.unsubscribe(this, EventType.EDITOR_DOCUMENT_EDITED, EventType.EDITOR_DOCUMENT_SWITCH)

        showNotification("Web Server", "The server was stopped.")
        mvc().MainView.setStatus("Web Server Stopped")
    }

    /**
     * Shuts down the server when the IDE is restarted.
     */
    override fun restart() {
        stop()
    }

    /**
     * Changes the document that is being served.
     */
    fun serve(e: Document?) {
        if (
            server == null
         || e == null
         || e == DOCUMENT_SERVING
        ) return

        DOCUMENT_SERVING = e
        mvc().MainView.setStatus("Web Server's document updated.")
    }

    /**
     * Updates the page being served.
     */
    override fun notify(e: EventType) {
        if (server != null && DOCUMENT_SERVING != null)
            if (e == EventType.EDITOR_DOCUMENT_SWITCH)
                serve(mvc().currentDocument())
            else
                serve(DOCUMENT_SERVING!!)
    }
}