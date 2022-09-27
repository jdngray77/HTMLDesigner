package com.jdngray77.htmldesigner.frontend.splash

import com.jdngray77.htmldesigner.frontend.IDE
import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.animation.*
import javafx.application.Application
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import org.apache.commons.lang3.RandomUtils.nextInt
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * A proxy application to launch the main application, preposed with a splash screen.
 *
 * This proxy application is started first, creating the stage and using it to
 * display a splash screen.
 *
 * After starting, a fade in animation plays. Once done, the main application is
 * created. The use of this animation also offloads the work of creating the
 * main app until later, allowing the [start] to return and the splash screen to be displayed
 * first, before the application hangs for loading.
 *
 * As normal, the [start] method of the new application sets the scene in the stage.
 * Since it will use the same stage as the splash screen, the splash screen will be
 * replaced with the main application.
 *
 * @param clazz the class of the real application to launch
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
        val splash = loadFXMLComponent<AnchorPane>("SplashScreen.fxml", this::class.java)

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
            try {
                IDE().start(primaryStage)
            } catch (e: Exception) {
                Error("The editor failed to open.", e).printStackTrace()
                IDE.EDITOR.exit()
            }
            splashWindow.close()
        }

        // Display the splash.
        splashWindow.show()
        fadeIn.play()
    }
}

/**
 * Controller for the splash screen window.
 */
class SplashScreenController {

    /**
     * Invoked by the [main] when the IDE is provided arguments
     * that indicate that tests are being ran.
     *
     * Invoking this provides obvious visual feedback at boot
     * that the application is running in test mode.
     */
    internal fun testMode() {
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
        lblVersion.text = IDE.getVersionString()

        img.image = Image(javaClass.getResourceAsStream("Splash${nextInt(0, SPLASH_IMG_COUNT)}.jpeg"))

        if (IDE.TESTING)
            testMode()
    }

    companion object {

        /**
         * The number of images that are available for use.
         */
        const val SPLASH_IMG_COUNT = 10

    }
}