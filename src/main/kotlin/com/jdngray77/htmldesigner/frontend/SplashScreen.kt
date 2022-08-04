package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.animation.*
import javafx.application.Application
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration

/**
 * A proxy application to launch the main application, preposed with a splash screen.
 *
 * @author Jordan Gray
 * @see SplashScreen.fxml for visuals
 */
class SplashScreen() : Application() {

    /**
     * Entry Point.
     *
     * Displays a splash screen, and then launches the main application.
     * The splash screen is then disposed once after the appliction has launched.
     */
    override fun start(primaryStage: Stage) {
        // Load the visuals.
        val splash = loadFXMLComponent<AnchorPane>("SplashScreen.fxml")

        // Create a new window. We won't interfere with the main stage; that's for the main application to use.
        val splashWindow = Stage()
        splashWindow.scene = Scene(splash.first)
        splashWindow.initStyle(StageStyle.TRANSPARENT)

        //Load splash screen with fade in effect
        val fadeIn = FadeTransition(Duration.seconds(0.8), splash.first)
        fadeIn.fromValue = 0.0
        fadeIn.toValue = 1.0
        fadeIn.cycleCount = 1

        //After fade in, load the app.
        fadeIn.setOnFinished { e ->
            Editor().start(primaryStage)
            splashWindow.close()
        }

        // Display the splash.
        splashWindow.show()
        fadeIn.play()
    }
}

/**
 * Reference to the GUI of the splash.
 */
class SplashScreenController {

    fun testMode() {
        img.isVisible = false
        img.isVisible = false
        hbProduction.isVisible = false
        txtName.isVisible = false
        txtLoading.isVisible = false
        hbIcons.isVisible = false
        lblVersion.isVisible = false

        txtTitle.text = "TESTING IDE..."
        txtTitle.effect = null
        txtTitle.textFill = Color.BLACK
    }

    lateinit var img: ImageView
    lateinit var hbProduction: VBox
    lateinit var txtName: Label
    lateinit var txtLoading: Label
    lateinit var hbIcons: HBox
    lateinit var lblVersion: Label
    lateinit var txtTitle: Label

    @FXML
    fun initialize() {
        lblVersion.text = Editor.getVersionString()

        if (Editor.TESTING)
            testMode()
    }
}