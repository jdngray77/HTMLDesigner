package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.backend.html.style.StyleSheet
import com.jdngray77.htmldesigner.frontend.MainViewController
import org.jsoup.nodes.Document

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
        MainView.openDocument(Project.loadDocument(Project.documents().first()))
    }

    override fun notify(e: EventType) {
//        when (e) {
//            EventType.EDITOR_OPEN_DOCUMENT_CHANGED ->
//                MainView.updateDisplay()
//        }
    }

    fun document() = MainView.currentDocument()

    fun newStylesheet(name: String) = Project.createStylesheet(name)

}