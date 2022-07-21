
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

import com.jdngray77.htmldesigner.utility.changed
import com.jdngray77.htmldesigner.backend.html.StyleAttributeSnapshot
import com.jdngray77.htmldesigner.frontend.controls.AlignControl
import com.jdngray77.htmldesigner.utility.readPrivateProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.AbstractPropertyEditor
import org.controlsfx.property.editor.DefaultPropertyEditorFactory
import org.jsoup.nodes.Element
import java.util.*

/*
 *
 *
 *
 *          This file contains items and editors for the TagProperties dock.
 *
 *
 *
 */

/**
 * The base item for editable CSS properties.
 */
open class CSSPropertySheetItem(

    /**
     * The name of the property displayed to the user.
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

    /**
     * The styles obtained from the [element] at time
     * of creation of this item
     *
     */
    val styles = StyleAttributeSnapshot(element)

    /**
     * A copy of [styles], used to notify the editor of changes.
     */
    private val observableProp = SimpleStringProperty(styles.toString())

    override fun getType() = String::class.java
    override fun getCategory() = _category
    override fun getName() = _name
    override fun getDescription() = _description

    /**
     * Returns the current value of [property] in the [element]'s
     * [styles].
     *
     * May be null if the style [property] is not in [styles].
     */
    override fun getValue(): Any? = styles[property]

    override fun setValue(value: Any?) {
        // re-capture so we don't overwrite any changes made elsewhere.
        styles.capture()

        // Make our changes
        if (value == null || value.toString().isBlank())
            styles.clear()
        else
            styles[property] = value.toString()

        // Commit the changes back to the tag
        styles.commit()

        // Notify the property sheet of the change
        observableProp.value = styles.toString()

        // Notify the IDE of the change
        element.ownerDocument()?.changed()
    }

    override fun getObservableValue(): Optional<ObservableValue<out Any>> =
        Optional.of(observableProp)
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
    : CSSPropertySheetItem (_name, element, "justify-content", _category, _description) {

    // TODO If value is not null, require display flex
    init { styles["display"] = "flex" }

}

/**
 * Editor for the above [CSSAlignmentPropertySheetItem].
 *
 * Uses an [AlignControl].
 */
class CSSAlignmentPropertyEditor(

    property: CSSPropertySheetItem

) : CSSPropertyEditor(property, AlignControl()) {

    init {
        (readPrivateProperty(AbstractPropertyEditor::class, "control") as AlignControl).observableValue.addListener {
            a,b,c ->
            property.value = c
        }
    }


    override fun setValue(value: String?) {
        (editor as AlignControl).setAlignment(value)
    }

    override fun getValue() =
        (editor as AlignControl).getAlignment()?.lowercase() ?: ""


    override fun getObservableValue() =
        property.observableValue.get() as ObservableValue<String>
//

}



// ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                 Alignment Property
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

