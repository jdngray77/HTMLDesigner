package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import java.awt.Desktop
import java.awt.Desktop.getDesktop

/**
 * A simple text area dock that displays all system out
 * messages and logs.
 */
class Logs : Dock(), Subscriber {

    /**
     * Text area that displays the log in the view.
     */
    val ta = TextArea()

    /**
     * On log added, update [ta]
     */
    override fun notify(e: EventType) {
        ta.text = IDE_LOG.toString()

        ta.scrollTop = Double.MAX_VALUE
        ta.selectPositionCaret(Int.MAX_VALUE)
        ta.deselect()
    }

    init {
        center = ta
        EventNotifier.subscribe(this, EventType.LOG)

        buttonBar.children.add(0,
            Button("Save").apply {
                setOnAction {
                    getDesktop().open(
                        mvc().Project.newLogFile(IDE_LOG.toString(), "SESSION")
                    )
                }
            }
        )
    }

}