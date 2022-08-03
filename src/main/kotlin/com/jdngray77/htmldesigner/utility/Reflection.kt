

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

package com.jdngray77.htmldesigner.utility

import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * Uses Kotlin reflection to mutate a variable in an object.
 *
 * @param [prop] the property to mutate. KProperties can be obtained from the list of an object's properties - `X::class.memberProperties`
 */
fun <R, T> changeProperty(prop : KProperty<*>, instance: R, newValue: T) =
    (prop as KMutableProperty1<R, T>).set(instance, newValue)


/**
 * Returns the value of a property in this object's supers,
 * that you wouldn't otherwise be allowed to access.
 *
 * Sometimes there's a perfectly good reason to read a value from a class
 * you're extending, but you can't because it's private. That's
 * what this is for.
 *
 * @param superClass The class of the super you want to retrieve the value from.
 *                   Must be the KClass of a class in your extension hierarchy.
 *                   This may just be the class you're extending, or it may be higher,
 *                   like the super's super.
 */
fun <T> Any.readPrivateProperty(superClass: KClass<*>, propertyName: String) : T =
    (superClass as KClass<Any>).memberProperties.find { it.name == propertyName }?.let {
        it.isAccessible = true
        it.get(this)
    } as T

val reflections = Reflections("com.jdngray77.htmldesigner")

/**
 * Returns a list of all instances of the [clazz]
 */
fun everyInstanceOf(clazz: KClass<*>) =
    reflections.getSubTypesOf(clazz.java).mapNotNull { it.kotlin.objectInstance }