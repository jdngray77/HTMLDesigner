
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
import com.jdngray77.htmldesigner.frontend.controls.CSSUnitSlider
import com.jdngray77.htmldesigner.frontend.controls.QuadControl
import com.jdngray77.htmldesigner.frontend.controls.removeCSSUnit
import com.jdngray77.htmldesigner.utility.readPrivateProperty
import com.jdngray77.htmldesigner.utility.toHex
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.control.ColorPicker
import javafx.scene.control.ComboBox
import javafx.scene.control.Slider
import javafx.scene.paint.Color
import javafx.util.Callback
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.AbstractPropertyEditor
import org.controlsfx.property.editor.DefaultPropertyEditorFactory
import org.controlsfx.property.editor.PropertyEditor
import org.jsoup.nodes.Element
import java.util.*
import kotlin.math.floor

/**
 *
 *
 *
 *          This file contains items and editors for the TagProperties dock.
 *
 * The architecture is mildly confusing, but not too bad.
 * Below is an explination of the architecture.
 * Instructions on how to create new items and editors can be found in the
 * wiki.
 *
 * https://github.com/jdngray77/HTMLDesigner/wiki/CSS-Property-Sheet
 *
 *
 * For implementation into the IDE, see [TagPropertiesDock].
 *
 * [Element]'s can hold a single attribute to hold all styles, which is shared by all
 * CSS properties.
 *
 * i.e
 *
 * <tag style="property1 = x; property2 = y;".../>
 *
 * The property sheet does not know how to handle this.
 *
 * That is the purpose of these classes.
 *
 * It creates representations of css properties that may be edited by the user,
 * and provides some editor to the user. The result is captured, formatted into the CSS,
 * and applied to the tag's style attribute.
 *
 * The [Element] stores the entire style as a single string, and it is shared by many properties.
 * When editing it, we must read the current value, interpret it, make the changes, then write it back.
 *
 * The [StyleAttributeSnapshot] does this work for us. It is a wrapper around the style attribute that reads the
 * properties and stores them in a map, and converts them to a string to write back.
 *
 *
 *
 * Many properties can be accessed directly with a [CSSPropertySheetItem], but some properties have
 * special extensions.
 *
 * This PlantUML provides a brief overview of the classes.
 * (Use the PlantUML plugin to render the diagram)
 *
 *
 * @startuml
 *
 * class Element {
 *   + Style Attribute
 * }
 *
 * class CSSPropertySheetItem {
 *  + name // Name displayed to user i.e "Background Color"
 *  + property // Name of the CSS property i.e "background-color"
 *  + element // HTML Element to apply to

 *  + category // Groups items together in the UI. i.e "Colors"
 *  + description // Tooltip shown on hover.
 *
 * }
 *
 * class CSSPropertyEditor {
 *  + item: CSSPropertySheetItem // The item to edit
 *  + node: Node // The type of editor to present to the user
 * }
 *
 * class PropertySheet
 *
 * Element -- CSSPropertySheetItem : Represented By >
 * CSSPropertySheetItem -- CSSPropertyEditor : Edited By >
 * CSSPropertyEditor -- PropertySheet : Presented to user by >
 *
 * @enduml
 *
 * @author Jordan Gray
 */



/**
 * Abstract representation of a css property.
 *
 * This is an item that can be added to a [PropertySheet]
 * to represent an editable CSS property for a given html element.
 *
 * When it receives the input from a [CSSPropertyEditor], it
 * handles updating the value in the [Element]'s style attribute.
 *
 * i.e
 *
 * <tag style="... property: value;".../>
 *
 * Edited with a [CSSPropertyEditor] of some kind.
 *
 * Can be used alone for simple properties that, but can be extended for CSS
 * entries that require custom implementations or requirements.
 */
open class CSSPropertySheetItem(
//TODO default value param
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

    /**
     * Used by the [PropertySheet] to group items together in the GUI.
     */
    val _category: String,

    /**
     * Used by the [PropertySheet] to display a tooltip when the user hovers over the item.
     */
    val _description: String,

    /**
     * Determines what type of editor to use for this property.
     */
    val _type: Class<*> = String::class.java,

    /**
     * A function to cast the string found on the tag to the data type required by the GUI editor.
     */
    val caster: (String) -> Any = { it }

    ) : PropertySheet.Item {

    /**
     * The styles obtained from the [element] at time
     * of creation of this item
     *
     */
    val styles = StyleAttributeSnapshot(element)

    /**
     * A copy of [styles] wrapped in an observable property, used to notify the [PropertySheet] / [CSSPropertyEditor] of changes made to the item.
     *
     * The editor and [PropertySheet] attach change listeners to the observable value.
     * This is set to the output of [styles] when this item is updated.
     */
    @Deprecated("This is only for notifying the editor of changes. It is not used to store or access data.")
    private val observableProp = SimpleStringProperty(styles.toString())

    /**
     * Provides a read-only listenable property for this item.
     *
     * Can be used to attach change listeners.
     */
    final override fun getObservableValue(): Optional<ObservableValue<out Any>> =
        Optional.of(observableProp)

    /**
     * Tells the [PropertySheet] the data type of the item.
     *
     * Always a string, because that's how the data is stored in the element.
     */
    final override fun getType() = _type



    override fun getCategory() = _category
    override fun getName() = _name
    override fun getDescription() = _description

    /**
     * Returns the current value of [property] in the [element]'s
     * [styles].
     *
     * May be null if the style [property] is not in [styles].
     */
    override fun getValue(): Any? = styles.capture()[property]?.let { caster(it) }// The capture here is to ensure the current value is returned, not the value of the last capture.

    override fun setValue(value: Any?) {
        // re-capture to get the current style attribute state, so we don't overwrite any changes made elsewhere.
        styles.capture()

        // Make our changes
        if (value == null || value.toString().isBlank()) {
            if (styles.contains(property)) styles.remove(property)
        } else
            styles[property] = value.toString()

        // Invoke any extra behaviour of extensions.
        onChange(value)

        // Commit the changes back to the tag
        styles.commit()

        // Notify the property sheet of the change
        observableProp.value = styles.toString()

        // Notify the IDE of the change
        element.ownerDocument()?.changed()
    }

    /**
     * An overridable function that is called when the item is updated.
     *
     * Called after the [styles] have been updated, but not committed back to the [Element]
     */
    protected open fun onChange(value: Any?) {}


    companion object {

        val colorCaster: (String) -> Color = { Color.web(it) }
        val doubleCaster: (String) -> Double = { removeCSSUnit(it).toDouble() }


    }
}





/**
 * The base editor for editing the above [CSSPropertySheetItem].
 *
 * This is an editor that can be added to a [PropertySheet].
 *
 * Since HTML [Element]'s have to be edited in special ways to apply the
 * CSS, default property cannot be directly used.
 *
 * Default editors, like a [TextPropertyEditor], could still be used to accept an
 * input from the user, but it must be wrapped with [CSSPropertyEditor] to apply the
 * user's changes to the style attribute in the [Element].
 *
 * This editor Listens to the given editor, and when updated by the user the [_item] is automatically updated.
 *
 * If using the single parameter constructor, the editor to be used will be automatically inferred by type
 * using the [DefaultPropertyEditorFactory].
 *
 * Alternately, an [editor] may be provided.
 *
 * @param T the type of data provided by and to the editor. The result will always be stored as a string.
 */
abstract class CSSPropertyEditor<T>(

    /**
     * The item to edit.
     */
    val _item: CSSPropertySheetItem,

    /**
     * The actual GUI editor to provide to the user.
     */
    val _editor: Node

) : AbstractPropertyEditor<T, Node>(_item, _editor) {

    /**
     * Alternate constructor which infers what GUI editor to use by data type by
     * using the [DefaultPropertyEditorFactory].
     */
    constructor(property: CSSPropertySheetItem) : this(property, DefaultPropertyEditorFactory().call(property).editor)

    init {
        _editor.setOnMouseClicked {
            if (it.clickCount == 2) {
                value = null
            }
        }
    }

    /**
     * Gets the GUI editor used to edit the item.
     *
     * TODO check if this is called multiple times
     */
    override fun getEditor(): Node =
        _editor


    /**
     * A Change listener that should be attached to the GUI editor,
     * which updates the [_item] when the user changes the value in the editor.
     */
    protected fun <T> onUserEditedEditor() = ChangeListener<T> { _, _, c ->
        property.value = getCSSValue()
    }

    /**
     * Returns the value provided by the editor in a form to
     * inject into a CSS value.
     *
     * i.e a Color could be returned as hex.
     *
     * example pseudo :
     *
     * `css-property: getCSSValue();`
     *
     * If not provided, will return the `toString` of `getValue`
     *
     */
    protected open fun getCSSValue(): String = value.toString()

    /**
     * Gets the value provided by the user, found in the GUI editor.
     */
//    override fun getValue(): String

    /**
     * Sets the value as shown in the GUI editor
     */
//    override fun setValue(value: String?)

    /**
     * Provides a read-only listenable property for the editor.
     *
     * Used to notify when the user changes the GUI editor.
     */
//    override fun getObservableValue(): ObservableValue<String>

    /**
     * A work-around for early access to the GUI editor.
     *
     * When the [AbstractPropertyEditor] is created, it accesses the [getObservableValue]. However, this function is called in
     * the super, when this object has not been created yet, and [_editor] does not yet exist.
     *
     * This means, that the class extending this attempts to access this class, but it has not been created yet.
     *
     * This function may be used to obtain the editor early by reading it from the super using a sneaky trick.
     */
    protected fun getEditorEarly() =
        if (_editor == null) // Do not simplify this, as intellij suggests
            readPrivateProperty(AbstractPropertyEditor::class,"control") as Node
        else
            _editor
}





/**
 * Used by the [PropertySheet] to create the correct editor for each item
 */
object CSSPropertyEditorFactory : Callback<PropertySheet.Item, PropertyEditor<*>> {
    override fun call(item: PropertySheet.Item): PropertyEditor<*> {
        return when (item.type) {
            // First map the type of the item.
            // This allows the CSSProptySheetItem to be used alone.
                Color::class.java -> CSSColorPropertyEditor(item as CSSPropertySheetItem)


            // Then map item extensions.
            else -> when (item) {
                // justify-content
                is CSSAlignmentPropertySheetItem -> CSSAlignmentPropertyEditor(item)
                is CSSDropdownItem -> CSSDropdownEditor(item)
                is CSSRangeItem -> CSSRangeEditor(item)
                is CSSQuadRangeItem -> CSSQuadRangeEditor(item)


                // Default to the default editor, which is a based on
                else -> DefaultPropertyEditorFactory().call(item)
            }
        }
    }
}





// ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                                 Alignment Property (justify-content)
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * A [CSSPropertySheetItem] for the [justify-content] property.
 */
class CSSAlignmentPropertySheetItem (_name: String, element : Element, _category: String, _description: String)
    : CSSPropertySheetItem (_name, element, "justify-content", _category, _description) {


    /**
     * If there's an alignment of any kind, the element must be flex for it to take effect.
     *
     * Otherwise, it's not required.
     *
     * TODO this won't care if other items require flex. It will remove it, even if it's required somewhere else.
     */
    override fun onChange(value: Any?) {
        if (value != null)
            styles["display"] = "flex"
         else
            styles.remove("display")
    }
}

/**
 * Editor for the above [CSSAlignmentPropertySheetItem].
 *
 * Uses an [AlignControl].
 */
class CSSAlignmentPropertyEditor(

    property: CSSPropertySheetItem

) : CSSPropertyEditor<String>(property, AlignControl()) {

    init {
//        (getEditorEarly() as AlignControl).observableValue.addListener(onUserEditedEditor())
    }

    override fun setValue(value: String?) {
        (editor as AlignControl).setAlignment(value)
    }

    override fun getValue() =
        (editor as AlignControl).getAlignment()?.lowercase() ?: ""


    // TODO !! IMPORTANT !! Check this still works
    override fun getObservableValue() =
        (getEditorEarly() as AlignControl).observableValue
}



// ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                 Alignment Property
//region                                      Color Property
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * Editor for any color based property.
 *
 * i.e. color, background-color, border-color, etc.
 *
 * Uses a [ColorPicker].
 */
class CSSColorPropertyEditor(

    property: CSSPropertySheetItem

) : CSSPropertyEditor<Color>(property, ColorPicker()) {

    init {
        (editor as ColorPicker).valueProperty().addListener(onUserEditedEditor())
        // TODO read or default color on init
    }

    override fun setValue(value: Color?) {
        (editor as ColorPicker).value = value
    }

    override fun getValue() =
        (editor as ColorPicker).value

    override fun getCSSValue(): String =
        (getEditorEarly() as ColorPicker).value.toHex()

    override fun getObservableValue() =
        (getEditorEarly() as ColorPicker).valueProperty()
}



// ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                 Color Property
//region                                  Dropdown Property
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




/**
 * A [CSSPropertySheetItem] with a list of possible values to select from.
 *
 * Edited with a [CSSDropdownEditor]
 */
class CSSDropdownItem(_name: String, element : Element, property: String, _category: String, _description: String, vararg _possibleValues: String)
    : CSSPropertySheetItem (_name, element, property, _category, _description) {
    val possibleValues = _possibleValues.toList()
}


class CSSDropdownEditor(

    property: CSSDropdownItem

) : CSSPropertyEditor<String>(property, ComboBox<String>()) {

    init {
        (editor as ComboBox<String>).items = FXCollections.observableList(property.possibleValues)
        (editor as ComboBox<String>).valueProperty().addListener(onUserEditedEditor())
    }

    override fun setValue(value: String?) {
        (editor as ComboBox<String>).selectionModel.select(value)
    }

    override fun getValue() =
        (editor as ComboBox<String>).selectionModel.selectedItem

    override fun getCSSValue(): String = value

    override fun getObservableValue() =
        (getEditorEarly() as ComboBox<String>).valueProperty()
}



// ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                 Dropdown Property
//region                                      Range Property
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

class CSSRangeItem(_name: String, element : Element, property: String, _category: String, _description: String, val min: Double, val max: Double, val enableUnits: Boolean = true)
    : CSSPropertySheetItem (_name, element, property, _category, _description)

class CSSRangeEditor(

    property: CSSRangeItem

) : CSSPropertyEditor<String>(property, CSSUnitSlider(property.min, property.max, enableUnits = property.enableUnits)) {

    init {
        (editor as CSSUnitSlider).observableValue.addListener(onUserEditedEditor())
    }

    override fun setValue(value: String?) {
        (editor as CSSUnitSlider).setValue(value)
    }

    override fun getValue() =
        (editor as CSSUnitSlider).getValue()

    override fun getCSSValue(): String =
        value

    override fun getObservableValue() =
        (getEditorEarly() as CSSUnitSlider).observableValue
}


class CSSQuadRangeItem(_name: String, element : Element, property: String, _category: String, _description: String, val min: Double, val max: Double, val enableUnits: Boolean = true)
    : CSSPropertySheetItem (_name, element, property, _category, _description)

class CSSQuadRangeEditor(

    property: CSSQuadRangeItem

) : CSSPropertyEditor<String>(property,
        QuadControl(
            { CSSUnitSlider(property.min, property.max, enableUnits = property.enableUnits) },
            { it.getValue() },
            { it, value -> it.setValue(value) }
        )
    )
{

    init {
//        (editor as QuadControl<String, CSSUnitSlider>).observableValue.addListener(onUserEditedEditor())
    }

    override fun setValue(value: String?) {
        (editor as QuadControl<String, CSSUnitSlider>).setValue(value)
    }

    override fun getValue() =
        (editor as QuadControl<String, CSSUnitSlider>).getValue()

    override fun getCSSValue(): String =
        value

    override fun getObservableValue() =//TODO this is empty.
        SimpleStringProperty("")
}