package com.jdngray77.htmldesigner.editor.docks

import com.jdngray77.htmldesigner.CamelToSentence
import javafx.scene.text.Text


import javafx.scene.control.Spinner
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import com.jdngray77.htmldesigner.DeveloperWarning
import com.jdngray77.htmldesigner.html.dom.Document
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.DatePicker
import javafx.scene.control.Separator
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.web.WebView
import java.time.ZoneId
import java.util.*
import kotlin.reflect.*

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class Inspectable(val position: Int)

@Target(AnnotationTarget.PROPERTY)
annotation class GUI



@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class Title(val title: String)


@Target(AnnotationTarget.PROPERTY)
annotation class Slider(val min: Int, val max: Int)

@Target(AnnotationTarget.PROPERTY)
annotation class LargeText()

@Target(AnnotationTarget.PROPERTY)
annotation class ToggleButton()

@Target(AnnotationTarget.PROPERTY)
annotation class RadioButton()

@Target(AnnotationTarget.PROPERTY)
annotation class Page(val index: Int)

@Target(AnnotationTarget.PROPERTY)
annotation class Tooltip(val string: String)





class InspectableException(message: String? = null) : Exception(message)

/**
 * == ***YOU MUST CALL [create] TO POPULATE THE DOCK!*** ==
 *
 * A dockable window that automatically populates itself with controls that modify
 * variables.
 *
 * Mark variables or functions with [Inspectable] to have them auto-add to the dock.
 *
 * # Functions
 * Functions with no parameters are added as buttons for the user to click.
 *
 * # Variables
 * Supported variable types can be manipulated through a control. Simply annotate the variable with [Inspectable].
 *
 * See [createGUIForProperty] for supported types.
 *
 * i.e :
 * ```
 *  @Inspectable(0) var number = 1
 * ```
 *
 * # Order
 * Reflection is used to get a list of all properties, but the order is random.
 *
 * So for us to know what order to add the elements in, you must provide an order in the [Inspectable] annotation.
 *
 * Note that this order number :
 * - Is not the final index, i.e having two variables at positions '2' will not overwrite one of them.
 * - Is simply used to sort the list into the desired order
 *
 *
 * # References to the GUI
 * The GUI controls created for each item can be stored in a variable, if
 * desired :
 *
 *  - Create a second variable with the same name as the thing annotated with [Inspectable].
 *  - Give it the type of the control. See [createGUIForProperty].
 *  - Append "_GUI" to the end of the name.
 *  - Annotate it with [GUI]
 *
 * i.e :
 * ```
 *     @GUI lateinit var number_GUI: Spinner<Int>
 * ```
 *
 *
 * # Seperation
 *
 */
// TODO Modify Variables
// TODO Titles won't work on functions.
// TODO Refresh
open class AutoDock : GridPane() {

    /**
     * For creation, keeps track of the row
     * we're on.
     */
    var index = 0

    init {
        vgap = 20.0
        hgap = 5.0
    }

    /**
     * Creates the GUI, and populates [GUI] variables.
     */
    fun create() {
        (this::class.functions + this::class.memberProperties)
            .filter { it.findAnnotation<Inspectable>() != null }
            .sortedWith(compareBy { it.findAnnotation<Inspectable>()!!.position }).forEach {
                if (it is KProperty1<*,*>) addProperty(it) else addFunction(it as KFunction<*>)
        }
    }


    /**
     * Adds a variable to the GUI.
     *
     * @param prop The variable to add a control for.
     */
    private fun <T> addProperty(prop : KProperty<T>){
        val guiname = "${prop.name}_GUI"

        // Create the gui
        val gui = createGUIForProperty(prop, (prop as KProperty1<Any, Any>).get(this))

        prop.findAnnotation<Title>()?.apply { seperate(title) }

        // Add it to the dock
        this.children.let{
            add(Text(prop.name.CamelToSentence()), 0, index)
            add(gui, 1, index)
        }

        index++

        // Locate the variable to store the gui in, if there is one.
        val guivar = this::class.memberProperties.find { it.name == guiname  } as KProperty1<Any, Any>?

        guivar?.apply {
            if (findAnnotation<GUI>() == null) throw InspectableException("$guiname is not annotated with @GUI, so I'm not allowed to place the GUI into it.")
            if (isConst || this !is KMutableProperty<*>) throw InspectableException("$guiname is not variable, so I cannot place the GUI into it.")


            try {
                (this as KMutableProperty1<Any, Any>).set(this@AutoDock, gui)

            } catch (e : Exception) {
                throw InspectableException("${classname()} $guiname has the wrong type ($returnType). It needs to be ${gui::class.qualifiedName} for the ${prop.returnType} stored in ${prop.name}")
            }


        } ?: run {
            DeveloperWarning("${prop.name} does not have a matching $guiname variable for it's UI control.")
        }
    }

    /**
     * Adds a button for a function
     */
    private fun addFunction(function : KFunction<*>) {
        add(Button(function.name.CamelToSentence()).also { it.setOnMouseClicked { function.call(this) } }, 1, index)
        index++
    }

    /**
     * Adds a hsep with a title into the grid.
     */
    private fun seperate(string: String) {
        add(Text(string), 0, index)
        add(Separator(), 1, index)
        index++
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun <T> createGUIForProperty(prop: KProperty<*>, value: T): Parent =
        when (prop.returnType.javaType.toString().split(".").last().lowercase()) {
            String::class.simpleName!!.lowercase()  -> { TextField(value as String) }
            Boolean::class.simpleName!!.lowercase() -> { CheckBox().also { it.isSelected = value as Boolean } }
            Color::class.simpleName!!.lowercase()   -> { ColorPicker().also { it.value = value as Color} }
            Date::class.simpleName!!.lowercase()    -> { DatePicker().also { it.value = (value as Date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() } }
            Int::class.simpleName!!.lowercase()     -> { Spinner<Int>(Int.MIN_VALUE, Int.MAX_VALUE, value as Int) }
            Float::class.simpleName!!.lowercase()   -> { Spinner<Float>(Int.MIN_VALUE, Int.MAX_VALUE, value as Int) }
            Long::class.simpleName!!.lowercase()    -> { Spinner<Long>(Int.MIN_VALUE, Int.MAX_VALUE, value as Int) }
            Short::class.simpleName!!.lowercase()   -> { Spinner<Short>(Int.MIN_VALUE, Int.MAX_VALUE, value as Int) }
            Document::class.simpleName!!.lowercase()-> { WebView().also { it.engine.loadContent( (value as Document).serialize()) } }
//            Enum::class -> {  }
            else -> throw InspectableException("I cannot create an editor for variable of type ${prop.returnType}")

        }

    fun classname() = this::class.simpleName + ":"

}


///**
// * A test of a dock automatically
// * being produced from the variables.
// */
//class TestDock : AutoDock() {
//

//
//    @Inspectable var thisIsSomeText = "Hello"
////    @Inspectable var thisIsADOM = testDOM
//
//    @Inspectable var thisIsABoolean = true
//    @Inspectable var thisIsADate = Date.from(Instant.now())
//    @Inspectable var thisIsAColor = Color.RED
//
//    init {
//        create()
//    }
//}



