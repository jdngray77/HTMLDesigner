//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[EventNotifier](index.md)/[subscribe](subscribe.md)

# subscribe

[jvm]\
fun [subscribe](subscribe.md)(newSubscriber: [Subscriber](../-subscriber/index.md), vararg subscribeTo: [EventType](../-event-type/index.md))

Subscribes a listener to notifications raised by the given events.

This method explicitly subscribes events to the JavaFX thread.

## Parameters

jvm

| | |
|---|---|
| newSubscriber | The subscriber that will receive notifications. |
| subscribeTo | The events that the subscriber will be notified of. |
| map | Either [backgroundSubscribers](background-subscribers.md) or [FXSubscribers](-f-x-subscribers.md). Determines what thread the notification will occur. |

[jvm]\
fun [subscribe](subscribe.md)(newSubscriber: [Subscriber](../-subscriber/index.md), vararg subscribeTo: [EventType](../-event-type/index.md), map: [SubscriberMap](../index.md#1700976140%2FClasslikes%2F-1216412040) = backgroundSubscribers)

Subscribes a listener to notifications raised by the given events.

## Parameters

jvm

| | |
|---|---|
| newSubscriber | The subscriber that will receive notifications. |
| subscribeTo | The events that the subscriber will be notified of. |
| map | Either [backgroundSubscribers](background-subscribers.md) or [FXSubscribers](-f-x-subscribers.md). Determines what thread the notification will occur. |
