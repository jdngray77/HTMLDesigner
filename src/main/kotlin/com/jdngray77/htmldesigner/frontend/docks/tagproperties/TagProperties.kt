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
import com.jdngray77.htmldesigner.frontend.controls.PlaceholderPropertySheetItem
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.utility.ReflectivePropertySheetItem
import com.jdngray77.htmldesigner.utility.readPrivateProperty
import impl.org.controlsfx.skin.PropertySheetSkin
import javafx.scene.control.Accordion
import javafx.scene.control.ScrollPane
import org.controlsfx.control.PropertySheet
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

    private var scrollPane : ScrollPane? = null


    init {
        center = sheet
        EventNotifier.subscribe(this, EventType.EDITOR_SELECTED_TAG_CHANGED)
    }

    override fun notify(e: EventType) {
        if (scrollPane == null)
            scrollPane = sheet.skin.readPrivateProperty<ScrollPane>(PropertySheetSkin::class, "scroller")



        mvc().currentEditor().let { editor ->


            editor.selectedTag?.apply {
                sheet.propertyEditorFactory = TagPropertyEditorFactory

                var lastCategorySelected : String? = null
                with (scrollPane!!.content) {
                    if (this is Accordion)
                        lastCategorySelected = expandedPane?.text
                }


                sheet.items.clear()

                sheet.items.addAll(
                    ReflectivePropertySheetItem<String>(
                        "id",
                        "Uniquely identify this element for scripting.",
                        "Advanced",
                        this,
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


                    // TODO align text
                    // TODO manual editor

                    CSSAlignmentPropertySheetItem(
                        "Align children",
                        this,
                        "Alignment",
                        "Moves content within the selected tag to one side, the center, or the other side.\nSee 'Align Direction' to change direction.",
                    ),

                    PlaceholderPropertySheetItem(
                        "Align direction",
                        "Appearance"
                    ),
                )

                if (lastCategorySelected != null)
                    (scrollPane!!.content as Accordion).apply {
                        expandedPane = panes.find { it.text == lastCategorySelected }
                    }
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