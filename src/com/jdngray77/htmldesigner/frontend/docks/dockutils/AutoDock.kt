
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

package com.jdngray77.htmldesigner.frontend.docks.dockutils


import com.jdngray77.htmldesigner.backend.logWarning
import com.jdngray77.htmldesigner.frontend.controls.AlignControl
import com.jdngray77.htmldesigner.utility.camelToSentence
import com.jdngray77.htmldesigner.utility.changeProperty
import javafx.scene.Parent
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.web.WebView
import org.jsoup.nodes.Document
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                                                     Annotations
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * Marks a field or a variable to be shown in the editor,
 * so the user can modify it directly.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class Inspectable(val position: Int)

/**
 * Marks a field intended to store the GUI Control created
 * to modify an [Inspectable]
 */
@Target(AnnotationTarget.PROPERTY)
annotation class GUI


/**
 * When added to an [Inspectable], adds a horizontal
 * [Separator] and a title to the GUI just above the
 * property it's attached to.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class Title(val title: String)


/**
 * When added to a [Number], the control used is changed from a
 * [Spinner] to a [Slider]
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Slider(val min: Int, val max: Int)

/**
 * When added to a [String], the control used is changed from a
 * [TextArea] to a [TextField]
 */
@Target(AnnotationTarget.PROPERTY)
annotation class LargeText()

/**
 * When added to a [Boolean], the control used is changed from a
 * [CheckBox] to a [ToggleButton]
 */
@Target(AnnotationTarget.PROPERTY)
annotation class ToggleButton()


/**
 * When added to an [Enum], the control used is changed from a
 * [ComboBox] to a [RadioButton]
 */
@Target(AnnotationTarget.PROPERTY)
annotation class RadioButton()


/**
 * To be determined exactly *how* this will be used.
 *
 * Will be used to add multiple pages to
 * a window using a [Pagination].
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Page(val index: Int)

/**
 * When added to a property, the provided [string] is
 * displayed when the user mouses over the control.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Tooltip(val string: String)


/**
 * Exception thrown when properties marked [Inspectable] are not fit to be so.
 *
 * i.e a val cannot be edited, so it cannot be presented to be edited.
 */
class InspectableException(message: String? = null) : Exception(message)


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                     Annotations
//region                                                         AutoDock
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░







/**
 * == ***YOU MUST CALL [create] AFTER VARIABLES ARE POPULATED TO POPULATE THE DOCK!*** ==
 *
 * A dockable window that automatically populates itself with controls that modify
 * variables.
 *
 * Similar to a [PropertySheet], but significantly easier to implement.
 *
 * Use a property sheet :
 *
 *      - When different objects may need to be edited.
 *      - When custom editors are required
 *      - When categorisation and searching for fields is desired.
 *
 * Use AutoDock :
 *
 *      - When youre not editing multiple objects
 *      - When you require only simple field editors.
 *      - When you want to create a new dock rather quickly and simply without the stress.
 *
 * Simply mark variables or functions with the [Inspectable] annotation to have them added to the window
 * automatically.
 *
 * The [Inspectable] annotation recieves a number, this is used to sort the order of controls in the window.
 *
 * This you to order variables and functions in your class as you wish, but order them differently in the UI.
 * Read below for more information on ordering.
 *
 * # Functions
 * Functions with no parameters are added as buttons for the user to click.
 *
 * TODO support functions with parameters by exposing the parameters with controls?
 *
 * # Variables
 * Supported variable types can be manipulated through a control. Simply annotate the variable with [Inspectable],
 * and they'll be exposed to the user.
 *
 * See [createGUIForProperty] for supported types.
 *
 * i.e :
 * ```
 *  @Inspectable(0) var number = 1
 * ```
 *
 * # Order
 * Reflection is used to get a list of all properties, but unfortunately the order is random.
 *
 * For us to know what order to add the elements in, you must provide an order in the [Inspectable] annotation.
 *
 * Note that this order number :
 * - Is simply used to sort the list into the desired order
 * - It's not the final index. The numbers are just relative to each other. Larger numbers appear after lower numbers.
 * - Having two variables at the same position will not overwrite one of them.
 *
 * I recommend incrementing in 10's, so you have gaps to insert
 * new controls into a pane in the future without needing to change all the numbers.
 *
 *
 * # References to the GUI
 * References to the GUI controls created for each item can be stored in a variable, if desired :
 *
 *  - Create a second variable with the same name as the [Inspectable] variable, value or function.
 *  - Give it the type of the control. See [createGUIForProperty].
 *  - Append "_GUI" to the end of the name.
 *  - Annotate it with [GUI]
 *
 * i.e :
 * ``` Kotlin
 *     @Inspectable(0) var number = 420
 *     @GUI lateinit var number_GUI: Spinner<Int>
 * ```
 *
 *
 * # Seperation
 * Attaching a @[Title] to an [Inspectable] adds a seperator and a title
 * above that item.
 */
// TODO Refresh
// TODO Warn on @GUI tags that aren't used.
open class AutoDock : Dock() {

    /**
     * For creation, keeps track of the row
     * we're on.
     */
    var index = 0

    val grid = GridPane()

    init {
        grid.vgap = 20.0
        grid.hgap = 5.0
        center = grid
    }

    /**
     * Creates the GUI, and populates [GUI] variables.
     */
    fun create() {
        (this::class.functions + this::class.memberProperties)
            .filter { it.findAnnotation<Inspectable>() != null }
            .sortedWith(compareBy { it.findAnnotation<Inspectable>()!!.position }).forEach {
                if (it is KProperty1<*, *>) addProperty(it) else addFunction(it as KFunction<*>)
            }
    }


    // TODO this is shit.
    fun update() {
        grid.children.clear()
        index = 0
        create()
    }



    /**
     * Adds an appropriate UI control to this pane for the given
     * variable.
     *
     * @param prop The variable to add a control for.
     * @throws InspectableException if variables are not named or annotated correctly, or if the variables types don't match.
     */
    private fun <T> addProperty(prop: KProperty<T>) {
        val guiname = "${prop.name}_GUI"

        // Constants are displayed in fields that are disabled, so they cannot be edited.
        //if (prop.isConst) throw InspectableException("${loggableClassName()} ${prop.name} is not variable, so it cannot be added to the editor for the user to edit.")

        // Create the gui, but don't add it yet. It's done early to check it's valid.
        val gui = createGUIForProperty(prop, (prop as KProperty1<Any, Any>).get(this))

        // If this property has a Title annotation, add a title first.
        checkTitle(prop)

        // Now we can add the GUI.
        this.children.let {
            grid.add(Text(prop.name.camelToSentence()), 0, index)
            grid.add(gui, 1, index)
        }

        index++

        // Locate the variable to store the gui in, if there is one.
        val guivar = this::class.memberProperties.find { it.name == guiname } as KProperty1<Any, Any>?

        guivar?.apply {
            if (findAnnotation<GUI>() == null) throw InspectableException("${loggableClassName()} $guiname is not annotated with @GUI, so I'm not allowed to place the GUI into it.")
            if (isConst || this !is KMutableProperty<*>) throw InspectableException("${loggableClassName()} $guiname is not variable, so I cannot place the GUI into it.")

            try {
                (this as KMutableProperty1<Any, Any>).set(this@AutoDock, gui)
            } catch (e: Exception) {
                throw InspectableException("${loggableClassName()} $guiname has the wrong type ($returnType). It needs to be ${gui::class.qualifiedName} for the ${prop.returnType} stored in ${prop.name}")
            }
        } ?: run {
            logWarning("${loggableClassName()} ${prop.name} does not have a matching $guiname variable for it's UI control.")
        }
    }

    /**
     * If the given [prop]erty is annotated with [Title],
     * then a title and horizontal line is appended to the grid.
     */
    private fun checkTitle(prop: KProperty<*>) {
        prop.findAnnotation<Title>()?.apply {
            grid.add(Text(title), 0, index)
            grid.add(Separator(), 1, index)
            index++
        }
    }


    /**
     * Adds a button for a function
     */
    private fun addFunction(function: KFunction<*>) {
        if (function.parameters.size != 1) throw InspectableException("${loggableClassName()} ${function.name} has parameters, so it can't be @Inspectable because I wouldn't know what to put in the parameters!")
        grid.add(Button(function.name.camelToSentence()).also { it.setOnMouseClicked { function.call(this) } }, 1, index)
        index++
    }


    /**
     * Creates A UI control appropriate for the given [prop]erty.
     *
     * Below is the mapping of types to controls :
     *  - String > [TextArea]
     *  - Boolean > [CheckBox]
     *  - Color > [ColorPicker]
     *  - Date > [DatePicker]
     *  - Enum > [ComboBox]
     *  - Int > [Spinner]<Int>
     *  - Float > [Spinner]<Float>
     *  - Short > [Spinner]<Short>
     *  - Double > [Spinner]<Double>
     *
     * Note that some annotations may be used to get a different control.
     * TODO this is not yet implemented.
     *
     * These alternate options are :
     *  - @LargeText String > [TextField]
     *  - @ToggleButton Boolean > [ToggleButton]
     *  - @RadioButton Enum > [ComboBox]
     *  - @Slider Number > [Slider]<E : Number>
     *
     */
    @OptIn(ExperimentalStdlibApi::class)
    // TODO abstract this shite...
    // TODO don't add listener if disabled.
    private fun <T> createGUIForProperty(prop: KProperty<*>, value: T): Parent =
        when (prop.returnType.javaType.toString().split(".").last().lowercase()) {
            String::class.simpleName!!.lowercase() -> {
                TextField(value as String).also {

                    it.disableProperty().set(prop.isConst)

                    it.textProperty().addListener { p0, p1, p2 ->
                            changeProperty(prop, this, it.text)
                    }

                }
            }

            // TODO fill each case below here.

            Boolean::class.simpleName!!.lowercase() -> {
                CheckBox().also {
                    it.disableProperty().set(prop.isConst)
                    it.isSelected = value as Boolean
                    it.selectedProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, it.isSelected)
                        }
                    }
                }
            }

            Color::class.simpleName!!.lowercase() -> {
                ColorPicker().also {
                    it.value = value as Color
                    it.disableProperty().set(prop.isConst)
                    it.valueProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, it.valueProperty().value)
                        }
                    }
                }
            }

            Date::class.simpleName!!.lowercase() -> {
                DatePicker().also {
                    it.value = (value as Date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    it.disableProperty().set(prop.isConst)
                    it.valueProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, Date(it.value.toEpochDay()))
                        }
                    }
                }
            }

            Int::class.simpleName!!.lowercase() -> {
                Spinner<Int>(Int.MIN_VALUE, Int.MAX_VALUE, value as Int).also {
                    it.disableProperty().set(prop.isConst)
                    it.valueProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, it.value)
                        }
                    }
                }
            }

            Float::class.simpleName!!.lowercase() -> {
                Spinner<Float>(Int.MIN_VALUE, Int.MAX_VALUE, (value as Float).toInt()).also {
                    it.disableProperty().set(prop.isConst)
                    it.valueProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, it.value)
                        }
                    }
                }
            }

            Double::class.simpleName!!.lowercase() -> {
                Spinner<Double>(Int.MIN_VALUE, Int.MAX_VALUE, (value as Double).toInt()).also {
                    it.disableProperty().set(prop.isConst)
                    it.valueProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, it.value)
                        }
                    }
                }
            }

            Long::class.simpleName!!.lowercase() -> {
                Spinner<Long>(Int.MIN_VALUE, Int.MAX_VALUE, (value as Long).toInt()).also {
                    it.disableProperty().set(prop.isConst)
                    it.valueProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, it.value)
                        }
                    }
                }
            }

            Short::class.simpleName!!.lowercase() -> {
                Spinner<Short>(Int.MIN_VALUE, Int.MAX_VALUE, (value as Short).toInt()).also {
                    it.disableProperty().set(prop.isConst)
                    it.valueProperty().addListener { p0, p1, p2 ->
                        run {
                            changeProperty(prop, this, it.value)
                        }
                    }
                }
            }

            Document::class.simpleName!!.lowercase() -> {
                WebView().also {
                    it.engine.loadContent((value as Document).toString())
                }
            }

            //            Enum::class -> {  }

            else -> throw InspectableException("I cannot create an editor for variable of type ${prop.returnType}")

        }

    private fun loggableClassName() = this::class.simpleName + ":"
}





/**
 * A test of a dock automatically
 * being produced from the variables.
 */
class TestDock : AutoDock() {

    @Inspectable(0) var thisIsSomeText = "Hello"
//    @Inspectable var thisIsADOM = testDOM

    @Inspectable(10) var thisIsABoolean = true
    @Inspectable(20) var thisIsADate = Date.from(Instant.now())
    @Inspectable(30) var thisIsAColor = Color.RED

    init {
        create()
    }
}


/**
 * An example of an automatically populated utility window.
 */
class ExampleAutoDock() : AutoDock() {

    // Create a heading. Will appear above the next variable, where ever that is.
    @Title("Project")
    @Inspectable(0) var ProjectName = "Yeet"
        set(value) {
            field = value
            println(value)
        }

    // The position number determines where in the list the item will be.
    // I recommend incrementing in 10's, so you have space to insert
    // new things without having to change all of the numbers.
    // i.e : Want something between 10 and 20? put it at 15!
    //       Want something directly after 10? insert it at 11!
    @Inspectable(10) var Author = "Big Dick McGee"

    @Title("Meta Data")
    @Inspectable(20) var CreatedOn = Date.from(Instant.now())
    @Inspectable(30) var BacksUpEveryXMinutes = 10

    // A function with no params is added as a button.
    // Stupid high number so it's always at the bottom, even if i add more things and forget about it.
    @Inspectable(10000)
    fun CloseProject() {
        System.exit(69)
    }

    // Note the position number here. Even though it's at the bottom of the file, it gets placed into the first group of controls.
    @Inspectable(20)
    fun HelloWorld() = logWarning("Fuck you.")


    // Remember to call create when ready to populate the GUI.
    // Variables must contain the value you want to show before creating,
    // otherwise the value won't be shown until the next time
    // the window is updated.
    init { create() }




    init {

        top = AlignControl()

    }
}