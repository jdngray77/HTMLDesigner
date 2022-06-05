package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.AutoDock
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Inspectable
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Title
import java.awt.Desktop
import kotlin.system.exitProcess

/**
 * An example of an automatically populated utility window.
 */
class ProjectDock() : AutoDock(), Subscriber {

    @Title("Project")
    @Inspectable(0)
    var ProjectName = ""
        set(value) {
            field = value
//            EDITOR.mvc.Project.renameOrMoveProject(ProjectName)
        }

    @Inspectable(10)
    var Author = ""
        set(value) {
            field = value
            mvc().Project.author = field
        }

//    @Title("Meta Data")
//    @Inspectable(20)
//    var CreatedOn: Date


    @Inspectable(20)
    fun ShowInSystem() {
        Desktop.getDesktop().open(mvc().Project.locationOnDisk);
    }

    @Inspectable(10000)
    fun CloseProject() {
        exitProcess(69)
    }

    init {
        create()
        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_EDITED, EventType.EDITOR_LOADED)
    }

    override fun notify(e: EventType) {
        // TODO remove guard once notifier is complete
        if (e != EventType.EDITOR_DOCUMENT_EDITED && e != EventType.EDITOR_LOADED) return

        mvc().Project.apply {
            ProjectName = projectName()
            author?.let { Author = it }
        }
        update()
    }
}