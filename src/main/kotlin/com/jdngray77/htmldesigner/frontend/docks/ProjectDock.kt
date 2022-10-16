
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

package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.IDE.Companion.EDITOR
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.AutoDock
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Inspectable
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Title
import java.awt.Desktop

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
            mvc().Project.apply {
                meta.author = field
                saveDesignerFile()
            }
        }

//    @Title("Meta Data")
//    @Inspectable(20)
//    var CreatedOn: Date


    @Inspectable(20)
    fun ShowInSystem() {
        mvc().Project.showInExplorer()
    }

    @Inspectable(10000)
    fun CloseProject() {
        EDITOR.closeProject()
    }

    init {
        create()
        EventNotifier.subscribe(this, EventType.IDE_FINISHED_LOADING)
    }

    override fun notify(e: EventType) {
        mvc().Project.apply {
            ProjectName = meta.name
            meta.author?.let { Author = it }
        }
        update()
    }
}