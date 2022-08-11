//[htmldesigner](../../index.md)/[com.jdngray77.htmldesigner.backend](index.md)/[showErrorNotification](show-error-notification.md)

# showErrorNotification

[jvm]\
fun [showErrorNotification](show-error-notification.md)(error: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), suppress: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = Config[Configs.SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL] as Boolean)

Shows a floating notification in the lower right, displaying the [error](show-error-notification.md)

Has no effect if [Configs.SUPPRESS_EXCEPTION_NOTIFICATIONS_BOOL](../com.jdngray77.htmldesigner.backend.data.config/-configs/-s-u-p-p-r-e-s-s_-e-x-c-e-p-t-i-o-n_-n-o-t-i-f-i-c-a-t-i-o-n-s_-b-o-o-l/index.md) is true
