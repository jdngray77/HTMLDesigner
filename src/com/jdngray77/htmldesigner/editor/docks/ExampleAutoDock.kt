package com.jdngray77.htmldesigner.editor.docks

import com.jdngray77.htmldesigner.UserWarning
import java.time.Instant
import java.util.*

/**
 * An example of an automatically populated utility window.
 */
class ExampleAutoDock() : AutoDock() {

    @Title("Project")
    @Inspectable(0) var ProjectName = "Yeet"
    @Inspectable(10) var Author = "Big Dick McGee"

    @Title("Meta Data")
    @Inspectable(20) var CreatedOn = Date.from(Instant.now())
    @Inspectable(30) var BacksUpEveryXMinutes = 10

    @Inspectable(10000)
    fun CloseProject() {
        System.exit(69)
    }

    @Inspectable(20)
    fun HelloWorld() = UserWarning("Fuck you.")

    init { create() }
}