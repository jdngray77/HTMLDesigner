package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor.Companion.EDITOR

/**
 * An example of an automatically populated utility window.
 */
class ProjectDock() : AutoDock(), Subscriber {

    @Title("Project")
    @Inspectable(0)
    var ProjectName = ""
        set(value) {
            field = value
            EDITOR.mvc.Project.renameProject(ProjectName)
        }

    @Inspectable(10)
    var Author = ""
        set(value) {
            field = value
            EDITOR.mvc.Project.author = field
        }

//    @Title("Meta Data")
//    @Inspectable(20)
//    var CreatedOn: Date

    @Inspectable(10000)
    fun CloseProject() {
        System.exit(69)
    }

    init {
        create()
        EventNotifier.subscribe(this, EventType.EDITOR_DOCUMENT_EDITED)
    }

    override fun notify(e: EventType) {
        if (e != EventType.EDITOR_DOCUMENT_EDITED) return
        EDITOR.mvc.Project.apply {
            ProjectName = projectName()
            author?.let { Author = it }
        }
        update()
    }
}