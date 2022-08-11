
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

package com.jdngray77.htmldesigner.backend

import com.jdngray77.htmldesigner.backend.data.Project
import com.jdngray77.htmldesigner.frontend.DocumentEditor
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvcIsAvail
import com.jdngray77.htmldesigner.frontend.MainViewController
import com.jdngray77.htmldesigner.utility.addIfAbsent
import com.jdngray77.htmldesigner.utility.createPrefab
import javafx.application.Platform
import org.jsoup.nodes.Element


/**
 * Some examples of types of events.
 */
enum class EventType {
    /**
     * Raised by a [DocumentEditor] when it saves a document.
     */
    USER_SAVE,

    /**
     * Unused.
     */
    AUTO_SAVE,

    /**
     * Unused.
     */
    PROJECT_HTML_CHANGED,

    /**
     * Unused.
     */
    PROJECT_PREFERENCES_CHANGED,

    /**
     * Unused.
     */
    PROJECT_SAVED,

    /**
     * Unused.
     */
    PROJECT_EXPORTED,

    /**
     * Unused.
     */
    PROJECT_BACKEDUP,

    /**
     * Raised by the [Project] when it creates a new document.
     */
    PROJECT_CREATED,

    /**
     * Raised by the [Project] when a page is deleted
     */
    PROJECT_PAGE_DELETED,

    /**
     * Raised by the [MainViewController] when the user or the IDE changes between document tabs.
     */
    EDITOR_DOCUMENT_SWITCH,

    /**
     * Raised by the [DocumentEditor] when it is notified of the document being changed.
     */
    EDITOR_DOCUMENT_EDITED,

    /**
     * Raised by the [DocumentEditor] when it's containing tab is closed.
     */
    EDITOR_DOCUMENT_CLOSED,

    /**
     * Raised by the [DocumentEditor] when it's selected tag property is changed.
     */
    EDITOR_SELECTED_TAG_CHANGED,

    /**
     * Raised by [Element.createPrefab] when creating a prefab from an existing element.
     */
    PROJECT_PREFAB_CREATED,

    IDE_SHUTDOWN,

    IDE_FINISHED_LOADING
}

fun EventType.notify() {
    EventNotifier.notifyEvent(this)
}

/**
 * An interface for objects that wish to be subscribed to use.
 */
interface Subscriber {
    fun notify(e: EventType)
}

typealias SubscriberMap = HashMap<EventType, ArrayList<Subscriber>>

/**
 * A simple global notification distributing service
 *
 * See [the documentation](https://github.com/jdngray77/HTMLDesigner/wiki/Technical-Systems)
 * for a full and simple guide on how to use this system.
 */
object EventNotifier {

    /**
     * Subscribers that are executed on background threads.
     *
     * @see BackgroundTask
     */
    val backgroundSubscribers = SubscriberMap()

    /**
     * Subscribers that are executed on the JavaFX thread.
     */
    val FXSubscribers = SubscriberMap()

    /**
     * Subscribes a listener to notifications raised by the given events.
     *
     * This method explicitly subscribes events to the JavaFX thread.
     * @param newSubscriber The subscriber that will receive notifications.
     * @param subscribeTo The events that the subscriber will be notified of.
     * @param map Either [backgroundSubscribers] or [FXSubscribers]. Determines what thread the notification will occur.
     */
    fun subscribe(newSubscriber: Subscriber, vararg subscribeTo: EventType) =
        subscribe(newSubscriber, *subscribeTo, map = FXSubscribers)

    /**
     * Subscribes a listener to notifications raised by the given events.
     *
     * This method subscribes explicitly to [backgroundSubscribers]
     * @param newSubscriber The subscriber that will receive notifications.
     * @param subscribeTo The events that the subscriber will be notified of.
     */
    fun subscribeInBackground(newSubscriber: Subscriber, vararg subscribeTo: EventType) =
        subscribe(newSubscriber, *subscribeTo, map = backgroundSubscribers)

    /**
     * Subscribes a listener to notifications raised by the given events.
     *
     * @param newSubscriber The subscriber that will receive notifications.
     * @param subscribeTo The events that the subscriber will be notified of.
     * @param map Either [backgroundSubscribers] or [FXSubscribers]. Determines what thread the notification will occur.
     */
    fun subscribe(newSubscriber: Subscriber, vararg subscribeTo: EventType, map: SubscriberMap = backgroundSubscribers) {
        subscribeTo.forEach {
            // Create an entry for the event type, if it's not already there,
            map.putIfAbsent(it, ArrayList())

            map[it]!!.addIfAbsent(newSubscriber)
        }
    }

    /**
     * Notify any listening subscribers that an event has occurred.
     * Returns with no effect if the MVC is not available.
     */
    fun notifyEvent(event: EventType) {
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

        implNotify(backgroundSubscribers, event) {
            BackgroundTask.submit {
                it.notify(event)
            }
        }

        implNotify(FXSubscribers, event) {
            Platform.runLater {
                it.notify(event)
            }
        }

        logStatus("Notified $event to ${backgroundSubscribers.size + FXSubscribers.size} Subscribers")
    }

    /**
     * Notification performing sub routine.
     */
    private fun implNotify(map: SubscriberMap, event: EventType, submitter: (Subscriber) -> Unit) {
        map[event]?.map {
            submitter.invoke(it)
        }
    }

    /**
     * Removes all subscribed listeners.
     */
    fun onIDEShutdown() {
        backgroundSubscribers.clear()
        FXSubscribers.clear()
    }

    /**
     * Removes a subscriber from the given event type.
     *
     * After unsubscribing, the subscriber will no longer receive notifications of the given event type(s).
     * @param subscriber The subscriber that to unsub.
     * @param unsubFrom The events that the subscriber will no-longer be notified of.
     * @param map Either [backgroundSubscribers] or [FXSubscribers]. Determines what thread the notification will occur.
     */
    fun unsubscribe(subscriber: Subscriber, vararg unsubFrom: EventType, map: SubscriberMap = backgroundSubscribers) {
        unsubFrom.forEach {
            map[it]?.remove(subscriber)
        }
    }
}