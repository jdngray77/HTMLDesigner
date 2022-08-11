//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[EventNotifier](index.md)/[subscribeInBackground](subscribe-in-background.md)

# subscribeInBackground

[jvm]\
fun [subscribeInBackground](subscribe-in-background.md)(newSubscriber: [Subscriber](../-subscriber/index.md), vararg subscribeTo: [EventType](../-event-type/index.md))

Subscribes a listener to notifications raised by the given events.

This method subscribes explicitly to [backgroundSubscribers](background-subscribers.md)

## Parameters

jvm

| | |
|---|---|
| newSubscriber | The subscriber that will receive notifications. |
| subscribeTo | The events that the subscriber will be notified of. |
