
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

import com.jdngray77.htmldesigner.TestHelper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EventNotifierTest {

    @BeforeEach
    internal fun setUp() =
        TestHelper.setUp()

    @AfterEach
    internal fun tearDown() =
        TestHelper.tearDown()

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
        EventNotifier.restart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribe(subscriber, EventType.EDITOR_LOADED)
        assert(EventNotifier.FXSubscribers.size == 1)

        EventNotifier.notifyEvent(EventType.EDITOR_LOADED)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_SWITCH)
        EventNotifier.notifyEvent(EventType.EDITOR_DOCUMENT_EDITED)


        EventNotifier.restart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribeInBackground(subscriber, EventType.EDITOR_LOADED)
        assert(EventNotifier.backgroundSubscribers.size == 1)
    }

    @Test
    fun onIDERestart() {
        EventNotifier.restart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.subscribe(subscriber, EventType.EDITOR_LOADED)
        assert(EventNotifier.FXSubscribers.size == 1)
        assert(EventNotifier.backgroundSubscribers.size == 0)

        EventNotifier.restart()
        assert(EventNotifier.FXSubscribers.size == 0)
        assert(EventNotifier.backgroundSubscribers.size == 0)
    }
}