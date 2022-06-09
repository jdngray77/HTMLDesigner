package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.frontend.MainViewController
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File


/**
 * Model View Controller.
 *
 * Editor requests change > MVC > Change the data
 * Data is updated or loaded > MVC > Editor is updated to display it.
 *
 * Communication between the backend and from end must
 * pass through here.
 */
class MVC (

    var Project : Project,

    var MainView : MainViewController

) : Subscriber {

    init {
        EventNotifier.subscribe(this, EventType.EDITOR_LOADED)
    }

    override fun notify(e: EventType) {
        if (e == EventType.EDITOR_LOADED)
            MainView.openDocument(Project.loadDocument(Project.documents().first()))


//        when (e) {
//            EventType.EDITOR_OPEN_DOCUMENT_CHANGED ->
//                MainView.updateDisplay()
//        }
    }

    fun document() = MainView.currentDocument()

    fun newStylesheet(name: String) = Project.createStylesheet(name)

    fun openDocument(document: File) {
        // TODO this loads the file from disk each time. Can we check to see if it's already loaded?
        MainView.switchToDocument(Project.loadDocument(document))
    }

    /**
     * Deletes [tag] after confirming with the user.
     */
    fun deleteTag(tag: Element) {
        if (userConfirm("Delete " + tag.tagName() + " ?"))
                tag.remove()

    }


}