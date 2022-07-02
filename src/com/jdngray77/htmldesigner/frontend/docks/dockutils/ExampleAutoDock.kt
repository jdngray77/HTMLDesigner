
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

package com.jdngray77.htmldesigner.frontend.docks.dockutils

import com.jdngray77.htmldesigner.backend.logWarning
import java.time.Instant
import java.util.*

/**
 * An example of an automatically populated utility window.
 */
class ExampleAutoDock() : AutoDock() {

    // Create a heading. Will appear above the next variable, where ever that is.
    @Title("Project")
    @Inspectable(0) var ProjectName = "Yeet"
        set(value) {
            field = value
            println(value)
        }

    // The position number determines where in the list the item will be.
    // I recommend incrementing in 10's, so you have space to insert
    // new things without having to change all of the numbers.
    // i.e : Want something between 10 and 20? put it at 15!
    //       Want something directly after 10? insert it at 11!
    @Inspectable(10) var Author = "Big Dick McGee"

    @Title("Meta Data")
    @Inspectable(20) var CreatedOn = Date.from(Instant.now())
    @Inspectable(30) var BacksUpEveryXMinutes = 10

    // A function with no params is added as a button.
    // Stupid high number so it's always at the bottom, even if i add more things and forget about it.
    @Inspectable(10000)
    fun CloseProject() {
        System.exit(69)
    }

    // Note the position number here. Even though it's at the bottom of the file, it gets placed into the first group of controls.
    @Inspectable(20)
    fun HelloWorld() = logWarning("Fuck you.")


    // Remember to call create when ready to populate the GUI.
    // Variables must contain the value you want to show before creating,
    // otherwise the value won't be shown until the next time
    // the window is updated.
    init { create() }
}