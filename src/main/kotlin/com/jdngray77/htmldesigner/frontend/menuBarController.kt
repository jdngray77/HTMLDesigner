package com.jdngray77.htmldesigner.frontend

import com.jdngray77.SplashX6.audio.Spotify
import com.jdngray77.SplashX6.audio.SpotifyAuthHelper
import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.Editor.Companion.project
import com.jdngray77.htmldesigner.frontend.MainViewController.Companion.clearDock
import com.jdngray77.htmldesigner.frontend.controls.RegistryEditor
import com.jdngray77.htmldesigner.frontend.controls.RunAnything
import com.jdngray77.htmldesigner.utility.CopyToClipboard
import com.jdngray77.htmldesigner.utility.getTheme
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import com.jdngray77.htmldesigner.utility.openURL
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.MenuBar
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import java.io.File
import javax.script.ScriptEngineManager

class menuBarController {

    lateinit var MenuBar: MenuBar

    @FXML
    fun initialize() {
            MenuBar.useSystemMenuBarProperty().set(Config[Configs.USE_MAC_MENU_BOOL] as Boolean)
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                        HTML DESIGNER
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    fun menu_soft_restart() =
        Editor.EDITOR.restart()

    fun menu_project_close() =
        Editor.EDITOR.closeProject()

    fun menu_registry() {
        RegistryEditor(Config).showDialog()
    }

    fun menu_exit() =
        Platform.exit()



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     HTML DESIGNER
    //region                                                        DEBUG
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    fun menu_debug_err() {
        throw Exception("This is a test error, not a real problem.")
    }

    fun menu_debug_dirty() {
        Editor.mvc().currentEditor().documentChanged("Debug dirty.")
    }

    fun menu_debug_showcache() {
        showInformationalAlert(
            "Project files loaded into cache are : "
                    +
                    Editor.mvc().Project.getCache().entries.joinToString {
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
        Editor.mvc().Project.PREFERENCES.reset()


    fun menu_debug_reset_config() =
        Config.reset()

    fun menu_debug_eval() {
        ScriptEngineManager().engineFactories.forEach { println(it.extensions) }
        val input = userInput("Enter Kotlin code to evaluate in the MVC.")

        showInformationalAlert(ScriptEngineManager().getEngineByExtension("kts")!!.eval(input).toString())
    }

    fun menu_debug_run_anything(actionEvent: ActionEvent) {
        RunAnything.showDialog()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     DEBUG
    //region                                                        FILE
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun menu_file_revert() {

        if (!mvc().currentEditor().isDirty) {
            showNotification("Nothing to revert.", "No changes have been made since the last save.")
            return
        }

        if (!userConfirm("This will delete any changes you've made since the last time the file was saved. \n\nAre you sure?"))
            return


        mvc().currentEditor().apply {
            // Close and re-load from disk
            forceClose()
            project().removeFromCache(document)
            mvc().openDocument(file)

            // Move the new editor to the same index
            mvc().currentEditor().tab.let {
                with(mvc().MainView.dockEditors) {
                    tabs.remove(it)
                    tabs.add(tabs.indexOf(tab), it)
                    selectionModel.select(it)
                }
            }
        }
    }

    fun menu_file_save() =
        mvc().currentEditor().save()


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     FILE
    //region                                                        EDIT
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    /**
     * This is liable to causes memory leaks.
     */
    fun menu_window_resetdocks() {
        with(mvc().MainView) {
            clearDock(dockLeftTop)
            clearDock(dockLeftBottom)
            clearDock(dockRight)

            addDocks()
        }
        EventType.IDE_FINISHED_LOADING.notify()
        System.gc();
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     EDIT
    //region                                                        TOOLS
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    fun menu_server_start() {
        WebServer.start()
    }

    fun menu_server_stop() {
        WebServer.stop()
    }


    fun menu_server_tglauto(actionEvent: ActionEvent) {
        WebServer.autoRefresh = (actionEvent.source as CheckMenuItem).isSelected
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     TOOLS
    //region                                                        PROJECT
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    fun menu_project_projprefs(actionEvent: ActionEvent) {
        RegistryEditor(Editor.mvc().Project.PREFERENCES).showDialog()
    }



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     PROJECT
    //region                                                        ABOUT
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


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


    fun menu_help_tbird() {
        openURL("https://github.com/Birdie2016")
    }

    fun menu_help_dbrand(actionEvent: ActionEvent) {
        openURL("https://dylanbrand.uk/")
    }

    fun menu_edit_undo() {
        Editor.mvc().currentEditor().undo()
    }

    fun menu_edit_redo() {
        Editor.mvc().currentEditor().redo()
    }

    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     ABOUT
    //region                                                        WINDOW
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun menu_window_isolation() {
        Editor.mvc().currentEditor().toggleStandaloneEditMode()
    }

    fun menu_window_fs() {
        Editor.EDITOR.stage.isFullScreen = !Editor.EDITOR.stage.isFullScreen
    }

    fun menu_window_closeeditors() {
        Editor.mvc().closeAllEditors()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                     WINDOW
    //region                                                        SPOTIFY
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

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
            showInformationalAlert(
                "Your spotify information has been cleared from the registry." +
                        "\n\nYou can confirm this by searching for 'spotify' in the registry editor." +
                        "\n\nSome information about your library may remain in RAM until the program is restarted."
            )
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
            mvc().MainView.albumTest.image = Image(albumartURL)
        }
    }

    fun menu_debug_js() {
        Editor.EDITOR.stage.scene = Scene(loadFXMLComponent<Pane>("jsdesigner/jsDesigner.fxml").first)
        getTheme().scene = Editor.EDITOR.stage.scene
    }
}