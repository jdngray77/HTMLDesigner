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

package com.jdngray77.htmldesigner.frontend

import com.jdngray77.SplashX6.audio.Spotify
import com.jdngray77.SplashX6.audio.SpotifyAuthHelper
import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.Editor.Companion.project
import com.jdngray77.htmldesigner.frontend.controls.RegistryEditor
import com.jdngray77.htmldesigner.frontend.controls.RunAnything
import com.jdngray77.htmldesigner.frontend.docks.*
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.frontend.docks.tagproperties.TagProperties
import com.jdngray77.htmldesigner.frontend.docks.toolbox.ToolboxDock
import com.jdngray77.htmldesigner.utility.CopyToClipboard
import com.jdngray77.htmldesigner.utility.camelToSentence
import com.jdngray77.htmldesigner.utility.openURL
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import org.eclipse.fx.ui.controls.tabpane.DndTabPane
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory.FeedbackType
import org.eclipse.fx.ui.controls.tabpane.skin.DnDTabPaneSkin
import org.jsoup.nodes.Document
import java.io.File
import java.lang.System.gc
import javax.script.ScriptEngineManager


/**
 * Front-End controller for the Main View.
 *
 * Manages the UI of the main window only.
 *
 * Must
 *
 */
class MainViewController {

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                   UI References.
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    lateinit var anchorDockLeftBottom: VBox
    lateinit var anchorDockLeftTop: VBox
    lateinit var anchorDockRight: VBox

    @FXML
    lateinit var dockEditors: TabPane

    @FXML
    lateinit var dockLeftTop: DndTabPane
    @FXML
    lateinit var dockLeftBottom: DndTabPane

    @FXML
    lateinit var dockRight: DndTabPane
    @FXML
    lateinit var dockBottom: TabPane

    @FXML
    lateinit var htmlEditor: HTMLEditor

    @FXML
    lateinit var lblLeftStatus: Label
    @FXML
    lateinit var lblRightStatus: Label

    @FXML
    lateinit var documentation: WebView


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                UI References
    //region                                                   Setup
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * Sets up the window when it's created.
     */
    @FXML
    fun initialize() {

        // We need to create the skin manually, could also be your custom skin.

        val skin = DnDTabPaneSkin(dockLeftTop)
        val skin1 = DnDTabPaneSkin(dockLeftBottom)
        val skin2 = DnDTabPaneSkin(dockRight)

        // Setup the dragging.
        //DndTabPaneFactory.setup(FeedbackType.MARKER, containerPane, skin2)
        DndTabPaneFactory.setup(FeedbackType.MARKER, anchorDockLeftTop, skin)
        DndTabPaneFactory.setup(FeedbackType.MARKER, anchorDockLeftBottom, skin1)
        DndTabPaneFactory.setup(FeedbackType.MARKER, anchorDockRight, skin2)

        dockLeftTop.skin = skin
        dockLeftBottom.skin = skin1
        dockRight.skin = skin2
        anchorDockRight.children.add(albumTest)



        htmlEditor.setOnContextMenuRequested {
            textEditor_Open(mvc().currentDocument().html())
        }

        htmlEditor.setOnKeyReleased {
//            EDITOR.mvc
//            renderer_Open(htmlEditor.htmlText)
            EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)
        }

        // Trigger switch event when user switches tabs.
        dockEditors.selectionModel.selectedItemProperty().addListener { _, _, _ ->
            EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
        }

        dockEditors.background = Background(
            BackgroundImage(
                Image("/com/jdngray77/htmldesigner/frontend/template.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
            )
        )

        addDocks()
        documentation.engine.load("https://www.github.com/Jdngray77/HTMLDesigner/wiki")
    }

    /**
     * Adds dock windows to the dock tabs.
     */
    private fun addDocks() {

        // TOP LEFT
        implAddDock(dockLeftTop, ToolboxDock(), Prefabs())

        // BOTTOM LEFT
        implAddDock(dockLeftBottom, Pages(), TagHierarchy(), ProjectDock())

        // RIGHT
        implAddDock(dockRight, TagProperties())
        implAddDock(dockRight, TestDock())
        implAddDock(dockRight, HistoryDock())
    }

    private fun implAddDock(to: TabPane, vararg it: Dock) {
        it.forEach {
            to.tabs.add(
                Tab(
                    it::class.simpleName!!.camelToSentence(),
                    it
                )
            )
        }
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Setup
    //region                                                  MCV API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    /**
     * Updates the GUI to represent
     * the [openDocument].
     *
     * Invoked after changing the document to
     * display the changes.
     */
    fun updateDisplay(document: Document) {
        textEditor_Open(document)
    }


    /**
     * It returns the text from the htmlEditor.
     */
    fun textEditor_Read() = htmlEditor.htmlText

    /**
     * `textEditor_Open` opens a document in the text editor
     *
     * @param doc The document to open.
     */
    fun textEditor_Open(doc: Document) = textEditor_Open(doc.toString())

    /**
     * A function that takes a string as an argument and sets the htmlText property
     * of the htmlEditor object to the value of the string
     *
     * @param rawHTML The raw HTML to be loaded into the editor.
     */
    fun textEditor_Open(rawHTML: String) {
        htmlEditor.htmlText = rawHTML
    }

    // TODO move these to logging
    fun setStatus(string: String) {
        lblLeftStatus.text = string
    }

    // TODO move these to logging
    fun setAction(string: String) {
        lblRightStatus.text = string
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                               MCV API
    //region                                                    Menu
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun menu_debug_err() {
        throw Exception("This is a test error, not a real problem.")
    }

    fun menu_debug_dirty() {
        mvc().currentEditor().documentChanged("Debug dirty.")
    }

    fun menu_debug_showcache() {
        showInformationalAlert(
            "Project files loaded into cache are : "
                    +
                    mvc().Project.getCache().entries.joinToString {
                        it.key + if (File(it.key).exists()) "" else "(Missing)" + "\n"
                    }
        )
    }

    fun menu_debug_showserial() {
        val serial = Project::class.hashCode()
        CopyToClipboard(serial.toString())
        showInformationalAlert("The current project serial hash version is : \n $serial")
    }

    fun menu_debug_reset_projprefs() =
        mvc().Project.PREFERENCES.reset()


    fun menu_debug_reset_config() =
        Config.reset()

    fun menu_soft_restart() =
        Editor.EDITOR.restart()

    fun menu_project_close() =
        Editor.EDITOR.closeProject()

    fun menu_registry() {
        RegistryEditor(Config).showDialog()
    }

    fun menu_debug_eval() {
        ScriptEngineManager().engineFactories.forEach { println(it.extensions) }
        val input = userInput("Enter Kotlin code to evaluate in the MVC.")

        showInformationalAlert(ScriptEngineManager().getEngineByExtension("kts")!!.eval(input).toString())
    }

    fun menu_debug_run_anything(actionEvent: ActionEvent) {
        RunAnything.showDialog()
    }

    fun menu_project_projprefs(actionEvent: ActionEvent) {
        RegistryEditor(mvc().Project.PREFERENCES).showDialog()
    }

    fun menu_file_revert() {

        if (!mvc().currentEditor().isDirty) {
            showNotification("Nothing to revert.", "No changes have been made since the last save.")
            return
        }

        if (!userConfirm("This will delete any changes you've made since the last time the file was saved. \n\nAre you sure?"))
            return


        mvc().currentEditor().apply {


            // Store the current tab position
            val index = dockEditors.tabs.indexOf(tab)

            // Close and re-load from disk
            forceClose()
            project().removeFromCache(document)
            mvc().openDocument(file)

            // Move the new editor to the same index
            mvc().currentEditor().tab.let {
                with(dockEditors) {
                    tabs.remove(it)
                    tabs.add(index, it)
                    selectionModel.select(it)
                }
            }
        }
    }

    fun menu_file_save() =
        mvc().currentEditor().save()

    fun menu_exit() =
        Platform.exit()

    fun menu_server_start() {
        WebServer.start()
    }

    fun menu_server_stop() {
        WebServer.stop()
    }

    fun menu_server_tglauto(actionEvent: ActionEvent) {
        WebServer.autoRefresh = (actionEvent.source as CheckMenuItem).isSelected
    }

    fun menu_help_about() {
        AboutWindow()
    }

    fun menu_help_licences() {
        LicencesWindow()
    }

    fun menu_help_wiki() {
        openURL("https://github.com/jdngray77/HTMLDesigner/wiki/A-Users-Guide-To-Getting-Started")
    }

    fun menu_help_repo() {
        openURL("https://github.com/jdngray77/HTMLDesigner/")
    }

    fun menu_help_issues() {
        openURL("https://github.com/jdngray77/HTMLDesigner/issues/new/choose")
    }

    fun menu_help_jdngray() {
        openURL("https://www.jordantgray.uk/")
    }

    fun menu_help_dbrand(actionEvent: ActionEvent) {
        openURL("https://dylanbrand.uk/")
    }

    fun menu_edit_undo() {
        mvc().currentEditor().undo()
    }

    fun menu_edit_redo() {
        mvc().currentEditor().redo()
    }

    /**
     * This is liable to causes memory leaks.
     */
    fun menu_window_resetdocks() {
        clearDock(dockLeftTop)
        clearDock(dockLeftBottom)
        clearDock(dockRight)

        addDocks()
        EventType.IDE_FINISHED_LOADING.notify()
        gc();
    }

    private fun clearDock(dock: TabPane) {
        dock.tabs.apply {
            map {
                if (it is Subscriber)
                    EventNotifier.unsubscribeFromAll(it)
            }
            clear()
        }
    }

    fun menu_window_isolation() {
        mvc().currentEditor().toggleStandaloneEditMode()
    }

    fun menu_window_fs() {
        Editor.EDITOR.stage.isFullScreen = !Editor.EDITOR.stage.isFullScreen
    }

    fun menu_window_closeeditors() {
        mvc().closeAllEditors()
    }

    fun menu_spotify_next() {
        Spotify.next()
        menu_spotify_nowplaying()
    }

    fun menu_spotify_previous() {
        Spotify.previous()
        menu_spotify_nowplaying()
    }

    fun menu_spotify_pause() {
        Spotify.pause()
    }

    fun menu_spotify_play() {
        Spotify.play()
        menu_spotify_nowplaying()
    }

    fun menu_spotify_connect() {
        SpotifyAuthHelper.create()
    }

    fun menu_spotify_help() {
        openURL("https://www.github.com/Jdngray77/HTMLDesigner/wiki/Spotify")
    }

    fun menu_spotify_deletedata() {
        if (userConfirm("Spotify will no longer work after deleting all info. Are you sure you want to delete your data?")) {
            SpotifyAuthHelper.clearConfigData()
            showInformationalAlert("Your spotify information has been cleared from the registry." +
                    "\n\nYou can confirm this by searching for 'spotify' in the registry editor." +
                    "\n\nSome information about your library may remain in RAM until the program is restarted.")
        }
    }

    /**
     * Shows a notification with some basic now playing information.
     */
    fun menu_spotify_nowplaying() {

        // Sanity check connection. Not necessary, but nice for the user.
        if (!Spotify.testConnection())
            showWarningNotification("Spotify is not connected.", "Click 'Connect', and refer to the wiki.")


        // Gets infomation about the current state of the player.
        // i.e isPlaying, shuffle, volume, player, seek position, repeat, etc.
        val clientInfo = Spotify.info()


        // Gets information about the song that's currently playing, if any.
        val songInfo = Spotify.nowPlaying()


        // No spotify session.
        if (clientInfo == null) {
            showNotification("Spotify", "Unable to find an active session." +
                    "\n\nOpen spotify and play something!")
            return
        }

        if (clientInfo.device.is_private_session) {
            showWarningNotification("Spotify", "Spotify is in a private session.\n\nWe cannot retrieve information about private playback.")
            return
        }


        // Session connected, but is not playing.
        if (clientInfo.is_playing != true) {
            showNotification("Spotify", "No song is currently playing.")
            return
        }


        // Unable to retrieve song info.
        // This sometime occours for no reason, and maybe also when user is using spotify's private session.
        if (songInfo == null) {
            showNotification("Spotify", "Unable to retrieve the song that's playing." +
                    "\n\nOpen spotify and play something!" +
                    "\n\nBtw, if you're using a private session, we can't see what you're jamming to!")
            return
        }

        else {
            showNotification(
                "Spotify - Now Playing",
                "${songInfo.item.name} - ${Spotify.aboutTrack(songInfo.item.uri)!!.artists.first().name}"
            )

            val albumartURL = let {
                // Fetch info about what the user is listening to.
                val nowPlaying = Spotify.nowPlaying()!!.item.uri

                // Fetch more detailed information about the song itself.
                val trackInfo = Spotify.aboutTrack(nowPlaying)

                // Get the url to the song's album art.
                trackInfo!!.album.images.first().url
            }

            // Display the album art.
            albumTest.image = Image(albumartURL)
        }
    }

    /**
     * Stupid image view in the GUI to view the album art.
     *
     * Added to [dockRight] in [initialize]
     */
    private val albumTest = ImageView()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                 Menu
    //region                                          Private Utility Methods
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


}


