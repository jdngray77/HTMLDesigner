package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.utility.LibrariesAndLicenses
import com.jdngray77.htmldesigner.utility.getTheme
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.web.WebView
import javafx.stage.Stage
import org.apache.commons.io.IOUtils
import org.controlsfx.control.SegmentedButton
import java.nio.charset.StandardCharsets

class LicencesWindow : SplitPane(){

    val lv = ListView<String>()

    val wv = WebView()

    private val tblic = ToggleButton("Licenses")
    private val tblib = ToggleButton("Libraries")


    val sb = SegmentedButton(
        FXCollections.observableArrayList(
            tblib,
            tblic
        )
    )

    init {

        wv.engine.locationProperty().addListener {
            _, _, newValue ->
            if (newValue == "about:blank")
                wv.engine.loadContent(LibrariesAndLicenses.page404)
        }
        wv.style = "-fx-background-color: #373737;"

        sb.toggleGroup.selectedToggleProperty().addListener {
            _,_,new ->

            if (new == tblic) {
                lv.items.setAll(
                    *LibrariesAndLicenses.idAndURLs().toTypedArray()
                )
            } else {
                lv.items.setAll(
                    *LibrariesAndLicenses.libraries.keys.toTypedArray()
                )
            }

            lv.selectionModel.select(0)
            lv.focusModel.focus(0)
        }


        lv.selectionModel.selectedItemProperty().addListener {
            _,_,new ->
            if (sb.toggleGroup.selectedToggle == tblib) {
                wv.engine.load(LibrariesAndLicenses.libraries[new])
            } else {
                LibrariesAndLicenses.fetchLicenceHTML(new)?.let {
                    wv.engine.loadContent(it)
                } ?: wv.engine.load(LibrariesAndLicenses.fetchLicenceURL(new))
            }
        }

        tblic.isSelected = true
        sb.toggleGroup.selectToggle(tblic)

        val vb = VBox(
            HBox(sb,
                Button("Library license notice").apply {
                    setOnAction {
                        // TODO
                        lv.selectionModel.select(null)
                        wv.engine.loadContent(IOUtils.toString(LicencesWindow::class.java.getResourceAsStream("licenses.html"), StandardCharsets.UTF_8))
                    }
                    HBox.getHgrow(this)
                    alignment = Pos.CENTER
                })
            , lv)

        setDividerPosition(0, 0.3)
        VBox.setVgrow(lv, Priority.ALWAYS)

        items.addAll(vb,wv)



        Stage().apply {
            scene = Scene(this@LicencesWindow)
            isResizable = true
            initOwner(Editor.EDITOR.stage)
            getTheme().scene = scene
            show()
        }
    }
}