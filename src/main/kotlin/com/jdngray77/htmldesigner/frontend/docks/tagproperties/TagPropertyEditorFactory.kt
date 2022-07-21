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

package com.jdngray77.htmldesigner.frontend.docks.tagproperties

import javafx.util.Callback
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.DefaultPropertyEditorFactory
import org.controlsfx.property.editor.PropertyEditor

/**
 * Creates instances of custom CSS property editors for the
 * [TagProperties] dock.
 */
object TagPropertyEditorFactory : Callback<PropertySheet.Item, PropertyEditor<*>> {
    override fun call(item: PropertySheet.Item): PropertyEditor<*> {
        return when (item) {


            // justify-content
            is CSSAlignmentPropertySheetItem -> CSSAlignmentPropertyEditor(item)



            else -> DefaultPropertyEditorFactory().call(item)
        }
    }
}