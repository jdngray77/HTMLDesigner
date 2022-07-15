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

package com.jdngray77.htmldesigner.frontend.controls

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.data.config.Config
import com.jdngray77.htmldesigner.backend.data.config.Configs
import com.jdngray77.htmldesigner.backend.data.config.Registry
import com.jdngray77.htmldesigner.backend.showInformationalAlert
import com.jdngray77.htmldesigner.backend.userInput
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.utility.delete
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

class Task(val name: String, val script: () -> Unit) : Runnable {
    final override fun toString() = name

    override fun run() =
        script()
}

object RunAnything : SearchableList<Task>(
    arrayListOf(
        *RegistryTaskFactory("Project").createTasks(Config),
        *RegistryTaskFactory("IDE").createTasks(Config),
        Task("IDE Registry > Toggle Dark Mode (requires restart)") {
            Config[Configs.DARK_MODE_BOOL] = !(Config[Configs.DARK_MODE_BOOL] as Boolean)
            Editor.EDITOR.restart()
        },



        Task("Restart") { Editor.EDITOR.restart() },
        Task("Quit") { Editor.EDITOR.stop() },


        Task("Project > Validate Cache") { mvc().Project.validateCache() },
        Task("Project > Close") { Editor.EDITOR.closeProject() },

        Task("Notify Event") {
            EventNotifier.notifyEvent(
                EventType.valueOf(userInput("Enter an Event to notify of."))
            )
        },

        Task("Current Document > Delete") { mvc().currentDocument().delete() },
        Task("Current Document > Validate") {
            showInformationalAlert(
                if (Jsoup.isValid(mvc().currentDocument().toString(), Safelist.relaxed()))
                    "Document contains tags which Jsoup considers un-safe in regards to XSS attacks."
                else
                    "Jsoup believes the document to be safe, containing only it considers safe."
            )
        },

        Task("Current Editor > Close") { mvc().currentEditor().requestClose() },
        Task("Current Editor > Save") { mvc().currentEditor().save() },
        Task("Current editor > Mark Dirty") { mvc().currentEditor().documentChanged() },
        Task("Current editor > Close without saving") { mvc().currentEditor().forceClose() },


        )
) {

    override fun onAction(item: Task) {
        //BackgroundTask.submit(item)
        item.run()
        dialog.hide()
    }


    val dialog = Dialog<ButtonType>().apply {

        title = "Run a Task"

        dialogPane.content = this@RunAnything
        dialogPane.setPrefSize(800.0, 500.0)
        isResizable = true

        dialogPane.buttonTypes.addAll(ButtonType.CLOSE)

        JMetro(Style.LIGHT).scene = dialogPane.scene

        setOnShown {
            clearSearch()
            searchBox.requestFocus()
        }
    }


    fun showDialog() =
        dialog.show()

}


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                        Task factories
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
// The helper classes below here help produce tasks to place in the window above.

private interface TaskFactory<T> {
    fun createTasks(item: T): Array<Task>
}

private class RegistryTaskFactory(val regName: String) : TaskFactory<Registry<*>> {
    override fun createTasks(item: Registry<*>) =
        arrayOf(
            Task("$regName Registry > Edit") { RegistryEditor(item).showDialog() },
            Task("$regName Registry > Force save") { item.forceFlush() },
            Task("$regName Registry > Save") { item.flush() },
            Task("$regName Registry > Restore defaults") { item.reset() },
            Task("$regName Registry > Validate") { Config.validate() },
        )
}

