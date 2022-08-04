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

package com.jdngray77.htmldesigner

import com.jdngray77.htmldesigner.RequiresEditorGUI.Companion.editorHasBeenLoaded
import com.jdngray77.htmldesigner.frontend.Editor
import javafx.application.Platform
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import testMain

/**
 * For tests that require the GUI, or other editor systems, to be loaded.
 *
 * When the first test class that requires it is ran,
 * this boots the editor and using the [testMain] entry point, and waits for it to
 * load.
 *
 * It then returns to the test class to run the tests.
 *
 * The [editorHasBeenLoaded] flag prevents the editor from being loaded multiple times.
 * Only the first test class that requires it will load it, and it will just stay open untill
 * all tests have completed.
 *
 * Once all tests are completed, it uses the shutdown routine to close the ide.
 */
class RequiresEditorGUI : BeforeAllCallback, CloseableResource {

    companion object {
        private var editorHasBeenLoaded = false
    }

    override fun beforeAll(context: ExtensionContext) {
        if (!editorHasBeenLoaded) {
            editorHasBeenLoaded = true
            // The following line registers a callback hook when the root test context is shut down
            context.root.getStore(ExtensionContext.Namespace.GLOBAL).put("RequiresEditorGUI", this)

            // Your "before all tests" startup logic goes here

            Thread { testMain() }.apply {
                isDaemon = true
                start()
            }

            print("Awaiting editor load.")
            while (!Editor.mvcIsAvail()) {
                Thread.sleep(100)
            }
        }
    }

    override fun close() {
        // Your "after all tests" logic goes here
        Editor.EDITOR.exit()
    }


}