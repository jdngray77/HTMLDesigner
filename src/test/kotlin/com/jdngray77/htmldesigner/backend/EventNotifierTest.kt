
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

import com.jdngray77.htmldesigner.RequiresEditorGUI
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RequiresEditorGUI::class)
internal class EventNotifierTest {

    val subscriber = object : Subscriber {
        override fun notify(e: EventType) {
            assert(e == EventType.IDE_FINISHED_LOADING)
        }
    }

    /**
     * Tests that only the event subscribed to ends up notifying
     * the subscriber.
     */
    @Test
    fun testEventType() {
        EventNotifier.onIDEShutdown()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribe(subscriber, EventType.IDE_FINISHED_LOADING)
        assert(EventNotifier.FXSubscribers.size == 1)

        EventNotifier.notifyEvent(EventType.IDE_FINISHED_LOADING)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)


        EventNotifier.onIDEShutdown()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribeInBackground(subscriber, EventType.IDE_FINISHED_LOADING)
        assert(EventNotifier.backgroundSubscribers.size == 1)
    }

    @Test
    fun onIDERestart() {
        EventNotifier.onIDEShutdown()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribe(subscriber, EventType.IDE_FINISHED_LOADING)
        assert(EventNotifier.FXSubscribers.size == 1)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.onIDEShutdown()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)
    }
}