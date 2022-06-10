package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvcIsAvail

/**
 * Some examples of types of events.
 */
enum class EventType {
    USER_SAVE,
    USER_EXIT,

    AUTO_SAVE,

    PROJECT_HTML_CHANGED,
    PROJECT_PREFERENCES_CHANGED,
    PROJECT_SAVED,
    PROJECT_EXPORTED,
    PROJECT_BACKEDUP,
    PROJECT_NEW_DOCUMENT_CREATED,

    EDITOR_DOCUMENT_SWITCH, // The editor has switched to show a different document.
    EDITOR_DOCUMENT_EDITED  // The editor has made a change to the open document.
    ,
    PROJECT_PAGE_DELETED,
    PROJECT_PAGE_CREATED,
    EDITOR_LOADED


}

/**
 * An interface for objects that wish to be subscribed to use.
 */
interface Subscriber {
    fun notify(e: EventType)
}

/**
 * Distributes notifications of events
 * throughout the system.
 *
 * [subscribe] a [subscriber], and it will be invoked when
 * the requested events occour.
 */
object EventNotifier {

    // FIXME this is just so i can test things which need the events to work.
    //       This is not the final implementation. Replace it.
    val tempList = arrayListOf<Subscriber>()

    fun subscribe(s: Subscriber, vararg NotifyOnEvents: EventType) {
        tempList.add(s)
    }

    /**
     * Notify the rest of the system that an event has occoured.
     */
    fun notifyEvent(e: EventType) {
        if (!mvcIsAvail()) return

        // TODO remove this once threading is in place. Just don't start the threading until well after the
        //      editor has initialised.
        try {
            Editor.EDITOR.mvc
        } catch (e : UninitializedPropertyAccessException) {
            return
        }

        // TODO detect circular notifications. Maybe check what thread is calling [subscribe] whilst inside of [notify]?
        //      Also not sure that this is a good idea. Maybe just warn.

        tempList.map {
            it.notify(e)
        }

        log("Notified $e to ${tempList.size} Subscribers ($tempList)")
    }

}