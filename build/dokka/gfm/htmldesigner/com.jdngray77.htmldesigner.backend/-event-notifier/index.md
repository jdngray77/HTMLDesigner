//[htmldesigner](../../../index.md)/[com.jdngray77.htmldesigner.backend](../index.md)/[EventNotifier](index.md)

# EventNotifier

[jvm]\
object [EventNotifier](index.md) : [Restartable](../../com.jdngray77.htmldesigner.utility/-restartable/index.md)

A simple global notification distributing service

See [the documentation](https://github.com/jdngray77/HTMLDesigner/wiki/Technical-Systems) for a full and simple guide on how to use this system.

## Functions

| Name | Summary |
|---|---|
| [notifyEvent](notify-event.md) | [jvm]<br>fun [notifyEvent](notify-event.md)(event: [EventType](../-event-type/index.md))<br>Notify any listening subscribers that an event has occurred. Returns with no effect if the MVC is not available. |
| [restart](restart.md) | [jvm]<br>open override fun [restart](restart.md)()<br>Removes all subscribed listeners. |
| [subscribe](subscribe.md) | [jvm]<br>fun [subscribe](subscribe.md)(newSubscriber: [Subscriber](../-subscriber/index.md), vararg subscribeTo: [EventType](../-event-type/index.md))<br>fun [subscribe](subscribe.md)(newSubscriber: [Subscriber](../-subscriber/index.md), vararg subscribeTo: [EventType](../-event-type/index.md), map: [SubscriberMap](../index.md#1700976140%2FClasslikes%2F-1216412040) = backgroundSubscribers)<br>Subscribes a listener to notifications raised by the given events. |
| [subscribeInBackground](subscribe-in-background.md) | [jvm]<br>fun [subscribeInBackground](subscribe-in-background.md)(newSubscriber: [Subscriber](../-subscriber/index.md), vararg subscribeTo: [EventType](../-event-type/index.md))<br>Subscribes a listener to notifications raised by the given events. |
| [unsubscribe](unsubscribe.md) | [jvm]<br>fun [unsubscribe](unsubscribe.md)(subscriber: [Subscriber](../-subscriber/index.md), vararg unsubFrom: [EventType](../-event-type/index.md), map: [SubscriberMap](../index.md#1700976140%2FClasslikes%2F-1216412040) = backgroundSubscribers)<br>Removes a subscriber from the given event type. |

## Properties

| Name | Summary |
|---|---|
| [backgroundSubscribers](background-subscribers.md) | [jvm]<br>val [backgroundSubscribers](background-subscribers.md): [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[EventType](../-event-type/index.md), [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[Subscriber](../-subscriber/index.md)&gt;&gt;<br>Subscribers that are executed on background threads. |
| [FXSubscribers](-f-x-subscribers.md) | [jvm]<br>val [FXSubscribers](-f-x-subscribers.md): [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[EventType](../-event-type/index.md), [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[Subscriber](../-subscriber/index.md)&gt;&gt;<br>Subscribers that are executed on the JavaFX thread. |
