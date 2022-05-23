package com.jdngray77.htmldesigner.backend

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

    EDITOR_DOCUMENT_SWITCH, // The editor has switched to show a new document.
    EDITOR_OPEN_DOCUMENT_CHANGED // The editor has made a change to the open document.
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

    fun subscribe(s: Subscriber, vararg NotifyOnEvents: EventType) {
        TODO()
    }

    /**
     * Notify the rest of the system that an event has occoured.
     */
    fun notifyEvent(e: EventType) {

    }

}