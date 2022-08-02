package com.jdngray77.htmldesigner.frontend

import com.jdngray77.htmldesigner.utility.loadFXMLComponent
import javafx.animation.*
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration

class SplashScreen : Application() {

    override fun start(primaryStage: Stage) {
        val splash = loadFXMLComponent<AnchorPane>("SplashScreen.fxml")

        val splashController = splash.second as SplashScreenController

        val splashWindow = Stage()
        splashWindow.scene = Scene(splash.first)

        splashWindow.initStyle(StageStyle.TRANSPARENT)

        // Pulse the text
//        val shadow = (splashController.txtTitle.effect as DropShadow)
//        val timeline = Timeline()
//        timeline.keyFrames.setAll(
//            KeyFrame(
//                Duration.millis(100.0),
//                KeyValue(shadow.radiusProperty(), .1),
//            ),
//
//            KeyFrame(
//                Duration.millis(100.0),
//                KeyValue(shadow.radiusProperty(), 1),
//            )
//        )
//
//        timeline.cycleCount = 3
//        timeline.isAutoReverse = true
//        timeline.play()

        //Load splash screen with fade in effect
        val fadeIn = FadeTransition(Duration.seconds(0.8), splash.first)
        fadeIn.fromValue = 0.0
        fadeIn.toValue = 1.0
        fadeIn.cycleCount = 1

        //After fade in, start fade out
        fadeIn.setOnFinished { e ->
            Editor().start(primaryStage)
            primaryStage.show()
        }

        splashWindow.show()
        fadeIn.play()
    }
}

class SplashScreenController {
    lateinit var txtTitle: Label
}