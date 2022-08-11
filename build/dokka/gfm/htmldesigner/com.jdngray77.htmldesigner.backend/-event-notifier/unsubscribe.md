//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[EventNotifier](index.md)/[unsubscribe](unsubscribe.md)

# unsubscribe

[jvm]\
fun [unsubscribe](unsubscribe.md)(subscriber: [Subscriber](../-subscriber/index.md), vararg unsubFrom: [EventType](../-event-type/index.md), map: [SubscriberMap](../index.md#1700976140%2FClasslikes%2F-1216412040) = backgroundSubscribers)

Removes a subscriber from the given event type.

After unsubscribing, the subscriber will no longer receive notifications of the given event type(s).

## Parameters

jvm

| | |
|---|---|
| subscriber | The subscriber that to unsub. |
| unsubFrom | The events that the subscriber will no-longer be notified of. |
| map | Either [backgroundSubscribers](background-subscribers.md) or [FXSubscribers](-f-x-subscribers.md). Determines what thread the notification will occur. |
