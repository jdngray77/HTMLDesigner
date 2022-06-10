package com.jdngray77.htmldesigner.frontend.docks

import com.jdngray77.htmldesigner.backend.EventNotifier
import com.jdngray77.htmldesigner.backend.EventType
import com.jdngray77.htmldesigner.backend.Subscriber
import com.jdngray77.htmldesigner.frontend.docks.dockutils.Dock
import com.jdngray77.htmldesigner.frontend.docks.dockutils.ReflectivePropertySheetItem
import org.controlsfx.control.PropertySheet
import org.jsoup.nodes.Element

/**
 * A dock which displays a [PropertySheet] for
 * the currently selected [Element]
 */
class TagProperties : Dock(), Subscriber {

    private val sheet = PropertySheet()

    init {
        center = sheet
        EventNotifier.subscribe(this, EventType.EDITOR_SELECTED_TAG_CHANGED)
    }

    override fun notify(e: EventType) {
        // TODO Remove
//        if (e != EventType.EDITOR_SELECTED_TAG_CHANGED)
//            return

        Element("h1").apply {
            sheet.items.add(
                    ElementProperty<String>(
                        this,
                        "id",
                        "A unique reference to this tag.",
                        "Scripting",
                        true
                    )
                )
        }
    }

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
            setter.invoke(obj, value)
        }
    }

}