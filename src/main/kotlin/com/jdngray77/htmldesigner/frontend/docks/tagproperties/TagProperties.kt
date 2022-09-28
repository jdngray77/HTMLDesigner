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
import com.jdngray77.htmldesigner.backend.showErrorNotification
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.frontend.controls.PlaceholderPropertySheetItem
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.frontend.docks.tagproperties.CSSPropertySheetItem.Companion.colorCaster
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeDocumentEditor
import com.jdngray77.htmldesigner.frontend.editors.EditorManager.activeEditorIsDocument
import com.jdngray77.htmldesigner.utility.ReflectivePropertySheetItem
import com.jdngray77.htmldesigner.utility.readPrivateProperty
import impl.org.controlsfx.skin.PropertySheetSkin
import javafx.scene.control.Accordion
import javafx.scene.control.ScrollPane
import javafx.scene.paint.Color
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

    private var scrollPane: ScrollPane? = null


    init {
        center = sheet
        EventNotifier.subscribe(
            this,
            EventType.EDITOR_SELECTED_TAG_CHANGED,
            EventType.EDITOR_DOCUMENT_SWITCH,
            EventType.EDITOR_DOCUMENT_CLOSED
        )
    }

    override fun notify(e: EventType) {
        if (scrollPane == null)
            scrollPane = sheet.skin.readPrivateProperty<ScrollPane>(PropertySheetSkin::class, "scroller")

        sheet.items.clear()

        if (e == EventType.EDITOR_DOCUMENT_CLOSED && !activeEditorIsDocument())
            return

        activeDocumentEditor()?.let { editor ->

            editor.selectedTag?.apply {
                sheet.propertyEditorFactory = CSSPropertyEditorFactory

                var lastCategorySelected: String? = null
                with(scrollPane!!.content) {
                    if (this is Accordion)
                        lastCategorySelected = expandedPane?.text
                }



                try {
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

                        CSSPropertySheetItem(
                            "Background color",
                            this,
                            "background-color",
                            "Color",
                            "Changes color displayed behind the content of this tag.",
                            Color::class.java,
                            colorCaster
                        ),

                        CSSPropertySheetItem(
                            "Foreground color",
                            this,
                            "color",
                            "Color",
                            "Changes color of the content of this tag, such as text.",
                            Color::class.java,
                            colorCaster
                        ),

                        CSSDropdownItem(
                            "Border style",
                            this,
                            "border-style",
                            "Border",
                            "Required to add a border. Determines the style line drawn around this element.",
                            "none", "solid", "dashed", "dotted", "double", "groove", "ridge", "inset", "outset"
                        ),

                        CSSPropertySheetItem(
                            "Border color",
                            this,
                            "border-color",
                            "Border",
                            "Changes color of the border, if there is one.",
                            Color::class.java,
                            colorCaster
                        ),

                        CSSRangeItem(
                            "Border thickness",
                            this,
                            "border-width",
                            "Border",
                            "Determines the width of the border, if there is a border.",
                            0.0, 100.0
                        ),

                        CSSRangeItem(
                            "Border radius",
                            this,
                            "border-radius",
                            "Border",
                            "Determines the radius of the border, if there is a border.",
                            0.0, 90.0, false
                        ),

                        CSSRangeItem(
                            "Border spacing",
                            this,
                            "padding",
                            "Border",
                            "Creates a gap between the border and the content in this element.",
                            0.0, 100.0
                        ),

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
                } catch (e: Exception) {
                    showErrorNotification(e, false)
                    e.printStackTrace()
                }

                if (lastCategorySelected != null)
                    (scrollPane!!.content as Accordion).apply {
                        expandedPane = panes.find { it.text == lastCategorySelected }
                    }

            }
            gc()
        }
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