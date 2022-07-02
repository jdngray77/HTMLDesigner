
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

package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.frontend.docks.dockutils.ReflectivePropertySheetItem
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.DefaultPropertyEditorFactory
import org.controlsfx.property.editor.Editors
import org.jsoup.nodes.Element

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

            sheet.items.addAll(
                    ElementProperty<String>(
                        this,
                        "id",
                        "A unique reference to this tag.",
                        "Scripting",
                        true
                    ),

//                    ElementProperty<>(
//                        this,
//                        "",
//                        "A unique reference to this tag.",
//                        "Layout",
//                        true
//                    ),
                )
            }
        }
    }

    /**
     * A wrapper for [ReflectivePropertySheetItem],
     * which can invoke getters and setters inside an
     * html [Element] with the given data type.
     */
    inner class ElementProperty<T>(
        val element : Element,
        fieldName: String,
        _description : String,
        _category : String,
        _isEditable: Boolean = true
    ) : ReflectivePropertySheetItem<T>(
        fieldName,
        _description,
        _category,
        element,
        _isEditable
    ) {

        val getter = obj::class.java.getDeclaredMethod(fieldName)

        val setter = obj::class.java.getDeclaredMethod(fieldName, type)

        override fun getType(): Class<*> = getter.returnType

        override fun getValue() = getter.invoke(obj) as T

        override fun setValue(value: Any?) {
            val before = getValue()
            setter.invoke(obj, value)
            println("Changed $fieldName from $before to ${getValue()}")

        }
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