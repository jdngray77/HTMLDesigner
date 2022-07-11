
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

import com.jdngray77.htmldesigner.backend.extensions.asStyleSheet
import com.jdngray77.htmldesigner.backend.extensions.changed
import com.jdngray77.htmldesigner.backend.html.StyleAttribute
import com.jdngray77.htmldesigner.frontend.controls.AlignControl
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.AbstractPropertyEditor
import org.controlsfx.property.editor.DefaultPropertyEditorFactory
import org.jsoup.nodes.Element
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * The base for editable CSS properties.
 */
open class CSSPropertySheetItem(

    /**
     * The name of the property
     * displayed to the user.
     *
     * This is not used to identify the name
     * of the css property
     *
     * @see property
     */
    val _name: String,

    /**
     * The element that is being edited
     */
    val element : Element,

    /**
     * The name of the CSS property
     * that is being edited on the [element]
     */
    val property : String,


    val _category: String,
    val _description: String

    ) : PropertySheet.Item {

    val styles = StyleAttribute()

    override fun getType() = String::class.java

    override fun getCategory() = _category
    override fun getName() = _name
    override fun getDescription() = _description

    override fun getValue(): Any = styles[property].toString()

    // TODO If value is not null, require display flex
    init { styles["display"] = "flex" }
    override fun setValue(value: Any?) {
        if (value == null || value.toString().isBlank())
            styles.clear()
        else
            styles[property] = value.toString()

        element.attr("style", styles.toString())
        element.ownerDocument()?.changed()
    }

    override fun getObservableValue(): Optional<ObservableValue<out Any>> =
        Optional.empty()
}




/**
 * The base editor for editing the above
 * [CSSPropertySheetItem].
 *
 * Listens to the field in the editor,
 * and when it's changed, the [_item] is
 * automatically updated.
 *
 * If using the single parameter constructor, the
 * editor to be used will be automatically inferred by type
 * using the [DefaultPropertyEditorFactory].
 *
 * Alternately, an [editor] may be provided.
 */
abstract class CSSPropertyEditor(

    /**
     * The item to edit.
     */
    val _item: CSSPropertySheetItem,

    /**
     * The editor to edit the [_item]
     */
    val _editor: Node

) : AbstractPropertyEditor<String, Node>(_item, _editor) {

    /**
     * Alternate constructor which infers by type by
     * using the [DefaultPropertyEditorFactory].
     */
    constructor(property: CSSPropertySheetItem) : this(property, DefaultPropertyEditorFactory().call(property).editor)

    /*
     * TODO check if this is called multiple times
     */
    override fun getEditor(): Node =
        _editor
}







// ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                                 Alignment Property
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * Property editor for `justify-content`
 */
class CSSAlignmentPropertySheetItem (_name: String, element : Element, _category: String, _description: String)
    : CSSPropertySheetItem (_name, element, "justify-content", _category, _description)

/**
 * Editor for the above [CSSAlignmentPropertySheetItem].
 *
 * Uses an [AlignControl].
 */
class CSSAlignmentPropertyEditor(

    property: CSSPropertySheetItem

) : CSSPropertyEditor(property, AlignControl()) {

    override fun setValue(value: String) {
        (editor as AlignControl).setAlignment(value)
    }

    override fun getValue() =
        (editor as AlignControl).getAlignment() ?: ""


    override fun getObservableValue() =

        // TODO document and turn this into a function
        (AbstractPropertyEditor::class.memberProperties.find { it.name == "control" }!!.let {
            it.isAccessible = true
            it.get(this)
        } as AlignControl)
            .observableValue
}



// ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                 Alignment Property
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

