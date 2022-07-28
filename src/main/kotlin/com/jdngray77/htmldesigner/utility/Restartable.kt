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

/**
 * When the IDE restarts, static systems are not unloaded, as the JVM is still running.
 *
 * Only instances of objects created within the [Editor] are unloaded and re-created.
 *
 * This is for the cases where static systems or objects that need to clean up when the IDE:
 *  - unloads a project,
 *  - shuts down
 *  - or restarts.
 *
 * When implemented, [restart] will automatically be invoked when the IDE's [Editor.stop] is invoked.
 *
 * Instances are located and invoked using reflection, so there no need to invoke or subscribe.
 *
 * @author Jordan T. Gray
 */
interface Restartable {
    fun restart()
}