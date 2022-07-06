

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

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.DefaultPropertyEditorFactory
import org.controlsfx.property.editor.Editors
import org.jsoup.nodes.Element
import java.lang.System.gc

/**
 * A dock which displays a [PropertySheet] for
 * the currently selected [Element]
 */
class TagProperties : Dock(), Subscriber {

    /**
     * A controlsfx property sheet used as the front end for editing things.
     */
    private val sheet = PropertySheet().also {
        // Dirty fix for the default CSS conflicting with the dark mode CSS,
        // Making the property sheet unreadable.
        //
        // Removing the CSS class removes the conflicting styles,
        // BUT it makes it difficult to target the property sheet
        // in CSS, if we ever should need to.
        it.styleClass.clear()

        it.mode = PropertySheet.Mode.CATEGORY
    }

    init {
        center = sheet
        EventNotifier.subscribe(this, EventType.EDITOR_SELECTED_TAG_CHANGED)
    }

    override fun notify(e: EventType) {
        // TODO Remove
        if (e != EventType.EDITOR_SELECTED_TAG_CHANGED)
            return

        // TODO offload to worker threads. This is not efficient.
        mvc().currentEditor().let { editor ->



            editor.selectedTag?.apply {
            sheet.setPropertyEditorFactory {
                when (it.type) {
                    // TODO this is useless, just here as an example.
                    //      We can add custom editors here when they're needed.
                    String::class.java -> Editors.createTextEditor(it)

                    else -> DefaultPropertyEditorFactory().call(it)
                }
            }

            val currentEditor = mvc().currentEditor()
            sheet.items.clear()

            sheet.items.addAll(
                ReflectivePropertySheetItem<String>(
                    "id",
                    "Uniquely identify this element for scripting.",
                    "Advanced",
                    this,
                    currentEditor,
                    true
                ),

                PlaceholderPropertySheetItem(
                    "Custom CSS",
                    "Advanced"
                ),

                PlaceholderPropertySheetItem(
                    "Attributes",
                    "Advanced"
                ),

                // TODO list
                // Breadcrumb

                PlaceholderPropertySheetItem(
                    "Width",
                "Size & Position"
                ),
                PlaceholderPropertySheetItem(
                    "Height",
                    "Size & Position"
                ),

                PlaceholderPropertySheetItem(
                    "Padding",
                    "Size & Position"
                ),

                PlaceholderPropertySheetItem(
                    "Margin",
                    "Size & Position"
                ),




                PlaceholderPropertySheetItem(
                    "Border",
                    "Appearance"
                ),

                PlaceholderPropertySheetItem(
                    "Background",
                    "Appearance"
                ),

                PlaceholderPropertySheetItem(
                    "Filters",
                    "Appearance"
                ),







                PlaceholderPropertySheetItem(
                    "Display",
                    "Alignment"
                ),

                PlaceholderPropertySheetItem(
                    "Flexbox",
                    "Alignment"
                ),
                )
            }
        }
        gc()
    }


    inner class CSSProperty(
        val element: Element,
        val propertyName: String

    ) {

    }



//    inner class CSSProperty (
//        val element : Element,
//        fieldName: String,
//        _description : String,
//        _category : String,
//        _isEditable: Boolean = true
//        ) : ReflectivePropertySheetItem<T>(
//        fieldName,
//        _description,
//        _category,
//        element,
//        _isEditable
//    ) {
//
//    }
}