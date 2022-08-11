
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
            assert(e == EventType.EDITOR_LOADED)
        }
    }

    /**
     * Tests that only the event subscribed to ends up notifying
     * the subscriber.
     */
    @Test
    fun testEventType() {
        EventNotifier.onIDERestart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribe(subscriber, EventType.EDITOR_LOADED)
        assert(EventNotifier.FXSubscribers.size == 1)

        EventNotifier.notifyEvent(EventType.EDITOR_LOADED)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)


        EventNotifier.onIDERestart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribeInBackground(subscriber, EventType.EDITOR_LOADED)
        assert(EventNotifier.backgroundSubscribers.size == 1)
    }

    @Test
    fun onIDERestart() {
        EventNotifier.onIDERestart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribe(subscriber, EventType.EDITOR_LOADED)
        assert(EventNotifier.FXSubscribers.size == 1)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.onIDERestart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)
    }
}