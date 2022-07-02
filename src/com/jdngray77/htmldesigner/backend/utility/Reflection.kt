
/*░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
 ░ Jordan T. Gray's                                                                               ░
 ░                                                                                                ░
 ░ HTML Designer                                                                                  ░
 ░                                                                                                ░
 ░ FOSS 2022.                                                                                     ░
 ░ License decision pending.                                                                      ░
 ░                                                                                                ░
 ░ https://www.github.com/jdngray77/HTMLDesigner/                                                 ░
 ░ https://www.jordantgray.uk                                                                     ░
 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

package com.jdngray77.htmldesigner.backend.utility

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty

/**
 * Uses Kotlin reflection to mutate a variable in an object.
 *
 * @param [prop] the property to mutate. KProperties can be obtained from the list of an object's properties - `X::class.memberProperties`
 */
fun <R, T> changeProperty(prop : KProperty<*>, instance: R, newValue: T) =
    (prop as KMutableProperty1<R, T>).set(instance, newValue)