package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.frontend.MainViewController
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
        MainView.setAction("Opened ${document.name}")
    }

    /**
     * Deletes [tag] after confirming with the user.
     *
     * If a [tag] does not belong to a document
     * (i.e it's already been deleted) it will be ignored.
     */
    fun deleteTag(vararg tag: Element) {

        if (!
            if (tag.size > 1)
                userConfirm("Delete multiple tags? \n\n ${tag.joinToString { "\n" + it.tagName() }}")
            else
                userConfirm("Delete ${tag[0].tagName()} ?")
        ) return

        DocumentModificationTransaction().apply {
            tag.forEach {
                val doc = it.ownerDocument() ?: return
                it.remove()
                modified(doc)
            }

            finishedModifying()
        }
        MainView.setAction("Deleted tag(s)")
    }

    private fun documentModified(document: Document) {
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)
        MainView.findEditorFor(document)?.apply {
            dirty()
            reRender()
        }
    }

    /**
     * When making many changes at once, this can be used to delay the
     * change notifications to after completing all changes.
     *
     * Make an instance then use `add` or `modified` to queue documents that have been changed
     * (even if they're the same / duplicates).
     *
     * Once all changes are made, call `finishedModifying`
     *
     */
    inner class DocumentModificationTransaction : ArrayList<Document>() {

        private var done = false

        fun modified(document: Document) =
            add(document)

        fun finishedModifying() {
            done = true
            distinct().forEach {
                documentModified(it)
            }
        }

        protected fun finalize() {
            if (done) return
            finishedModifying()
            IllegalStateException("A Transaction was disposed without being completed!").printStackTrace()
        }

    }
}