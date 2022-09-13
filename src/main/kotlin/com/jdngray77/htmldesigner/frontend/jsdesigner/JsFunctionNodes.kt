package com.jdngray77.htmldesigner.frontend.jsdesigner

import com.jdngray77.htmldesigner.backend.JsFunction
import com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphDataType
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import java.lang.System.gc
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance



//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//
//
//
//
// This file defines a range of functions that can be used by the javascript designer.
//
// It is essentially the js designer's SDK.
//
//
//
//
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░





// TODO STOPSHIP: These functions were generated with copilot. They need to be compiled and tested in a javascript runtime.
// TODO delay function
// TODO logical test functions (Greater than, AND, OR, etc)



/**
 * A factory used to new function objects, and find what functions are available.
 *
 * Also constructs the context menu used by the designer to add new functions.
 */
object JsFunctionFactory {

    /*
     *
     * These lists below are constructed into context menus for the jsdesigner.
     *
     * Order the items as you'd like them to appear in the UI, and use [null] to indicate a menu separator.
     *
     */


    val objectFactories = listOf<KClass<*>?>(
        JsColorFactoryFunction::class
    )

    val stringFunctions = listOf<KClass<*>?>(
        JsJoinFunction::class,
        JsLengthFunction::class,
        JsContainsFunction::class,
        null,
        JsStartsWithFunction::class,
        JsEndsWithFunction::class,
        JsSubstringFunction::class,
    )

    val mathFunctions = listOf<KClass<*>?>(
        JsAddFunction::class,
        JsSubtractFunction::class,
        JsMultiplyFunction::class,
        JsDivideFunction::class,
        null,
        JsAbsFunction::class,
        JsFloorFunction::class,
        JsCeilFunction::class,
        JsSqrtFunction::class,
        JsSquareFunction::class,
    )

    val randomFunctions = listOf<KClass<*>?>(
//        JsRandomFloatFunction::class,
        JsRandomNumberFunction::class,
        null,
        JsRandomBooleanFunction::class,
        null,
        JsRandomColorFunction::class,
        JsRandomColorWithAlphaFunction::class,
    )


    /**
     * A congregated collection of all available functions.
     */
    val allFunctions = (objectFactories + stringFunctions + mathFunctions + randomFunctions).filterNotNull()

    /**
     * Creates a new function instance by name.
     *
     * @throws [NullPointerException] is no function with the given name is found.
     */
    @Deprecated("Just know that this is horrendously inefficient.")
    fun byName(name: String) =
        allFunctions.find { nameOf(it) == name }!!.createInstance() as JsFunction

    /**
     * Creates and returns a contenxt menu with every function
     * available.
     *
     * The menu is only constructed on first call, afterwhich
     * the same menu is cached and returned.
     */
    fun asMenus(onAction : (JsFunction) -> Unit ): List<MenuItem> {
        val menu = mutableListOf<MenuItem>()
        with(menu) {
            add(constructSubMenu(onAction, "Maths", mathFunctions))
            add(constructSubMenu(onAction, "Strings", stringFunctions))
            add(constructSubMenu(onAction, "Random", randomFunctions))
            add(constructSubMenu(onAction, "Objects", objectFactories))
        }

        return menu
    }

    /**
     * Constructs a single menu based on a list of [JsFunction]'s.
     */
    private fun constructSubMenu(onAction : (JsFunction) -> Unit, name: String, functions : List<KClass<*>?>) : Menu {
        val menu = Menu(name)

        functions
            .filterIsInstance<KClass<JsFunction>?>()
            .forEach {
            menu.items.add(
                if (it == null)
                    SeparatorMenuItem()
                 else
                    MenuItem(nameOf(it)).also {
                        menu ->
                        menu.setOnAction {
                            onAction(
                                byName(menu.text)
                            )
                            gc()
                        }
                    }
            )
        }

        return menu
    }

    private fun nameOf(JsFun : KClass<*>) = (JsFun.createInstance() as JsFunction).name

}


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//region                                                     Object factories
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


// For demonstration, the format of definition is as follows :
class JsColorFactoryFunction : JsFunction (
    // Name of the function, that is displayed in the UI.
    "Color Factory",

    // The type of the output :
    JsGraphDataType.Color,

    // The inputs :

    // First input.
    Triple(
        // The name of the input.
        "r",

        // Type of data that can be accepted.
        JsGraphDataType.Number,

        // Default value used if no data is provided.
        0.0f
    ),

    // More inputs.
    Triple("g", JsGraphDataType.Number, 0.0f),
    Triple("b", JsGraphDataType.Number, 0.0f),
    Triple("a", JsGraphDataType.Number, 1.0f),

    // The javascript of the function.
    // Names used within must match the parameters.
    javascript = "new Color(r, g, b, a);"
)

//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                  Object factories
//region                                                     Math functions
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * Adds two numbers.
 */
class JsAddFunction : JsFunction (
    "Add",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    Triple("b", JsGraphDataType.Number, 0.0f),
    javascript = "a + b;"
)

/**
 * Subtracts two numbers.
 */
class JsSubtractFunction : JsFunction (
    "Subtract",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    Triple("b", JsGraphDataType.Number, 0.0f),
    javascript = "a - b;"
)

/**
 * Multiplies two numbers.
 */
class JsMultiplyFunction : JsFunction (
    "Multiply",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    Triple("b", JsGraphDataType.Number, 0.0f),
    javascript = "a * b;"
)

/**
 * Divides two numbers.
 */
class JsDivideFunction : JsFunction (
    "Divide",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    Triple("b", JsGraphDataType.Number, 0.0f),
    javascript = "a / b;"
)

// TODO mod, abs, floor, ceil, round, etc

/**
 * Returns the absolute value of a number.
 */
class JsAbsFunction : JsFunction (
    "Abs",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    javascript = "Math.abs(a);"
)

/**
 * Rounds a natural number down.
 */
class JsFloorFunction : JsFunction (
    "Round down",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    javascript = "Math.floor(a);"
)

/**
 * Rounds a natural number up
 */
class JsCeilFunction : JsFunction (
    "Round up",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    javascript = "Math.ceil(a);"
)

/**
 * Finds a square root of a number.
 */
class JsSqrtFunction : JsFunction (
    "Square root",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    javascript = "Math.sqrt(a);"
)

/**
 * Squares a number.
 */
class JsSquareFunction : JsFunction (
    "Square",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.Number, 0.0f),
    javascript = "a * a;"
)


//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                  Math functions
//region                                                     String functions
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

/**
 * Joins two strings.
 */
class JsJoinFunction : JsFunction (
    "Join text",
    JsGraphDataType.String,
    Triple("a", JsGraphDataType.String, ""),
    Triple("b", JsGraphDataType.String, ""),
    javascript = "a + b;"
)

/**
 * Returns the length of a string.
 */
class JsLengthFunction : JsFunction (
    "Text length",
    JsGraphDataType.Number,
    Triple("a", JsGraphDataType.String, ""),
    javascript = "a.length;"
)

/**
 * Returns true if a string contains another string.
 */
class JsContainsFunction : JsFunction (
    "Text contains...?",
    JsGraphDataType.Boolean,
    Triple("a", JsGraphDataType.String, ""),
    Triple("b", JsGraphDataType.String, ""),
    javascript = "a.includes(b);"
)

/**
 * Returns true if a string starts with another string.
 */
class JsStartsWithFunction : JsFunction (
    "Text starts with...?",
    JsGraphDataType.Boolean,
    Triple("a", JsGraphDataType.String, ""),
    Triple("b", JsGraphDataType.String, ""),
    javascript = "a.startsWith(b);"
)

/**
 * Returns true if a string ends with another string.
 */
class JsEndsWithFunction : JsFunction (
    "Text ends with...?",
    JsGraphDataType.Boolean,
    Triple("a", JsGraphDataType.String, ""),
    Triple("b", JsGraphDataType.String, ""),
    javascript = "a.endsWith(b);"
)

/**
 * Returns a substring of a string.
 */
class JsSubstringFunction : JsFunction (
    "Sub text",
    JsGraphDataType.String,
    Triple("a", JsGraphDataType.String, ""),
    Triple("b", JsGraphDataType.Number, 0.0f),
    Triple("c", JsGraphDataType.Number, 0.0f),
    javascript = "a.substring(b, c);"
)

//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                  String functions
//region                                                     Random functions
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


//
///**
// * Generates a random float between 0 and 1.
// */
//class JsRandomFloatFunction : JsFunction (
//        "Random Float",
//        _root_ide_package_.com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphDataType.Number,
//        Triple("min", _root_ide_package_.com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphDataType.Number, 0.0f),
//        Triple("max", _root_ide_package_.com.jdngray77.htmldesigner.backend.jsdesigner.JsGraphDataType.Number, 1.0f),
//        javascript = " Math.floor(Math.random() * max) + min"
//)

/**
 * Generates a random integer between 0 and 1.
 */
class JsRandomNumberFunction : JsFunction (
        "Random Number",
        JsGraphDataType.Number,
        Triple("min", JsGraphDataType.Number, Int.MIN_VALUE),
        Triple("max", JsGraphDataType.Number,  Int.MAX_VALUE),
        javascript = " Math.floor(Math.random() * max) + min"
)

/**
 * Generates a random boolean.
 */
class JsRandomBooleanFunction : JsFunction (
        "Random Boolean",
        JsGraphDataType.Boolean,
        javascript = " Math.floor(Math.random() * 2) == 1"
)

/**
 * Generates a random color.
 */
class JsRandomColorFunction : JsFunction (
        "Random Color",
        JsGraphDataType.Color,
        javascript = "Color.rgb(Math.floor(Math.random() * 255), Math.floor(Math.random() * 255), Math.floor(Math.random() * 255))"
)

/**
 * Generates a random color with transparency.
 */
class JsRandomColorWithAlphaFunction : JsFunction (
        "Random Color With Alpha",
        JsGraphDataType.Color,
        javascript = "Color.rgb(Math.floor(Math.random() * 255), Math.floor(Math.random() * 255), Math.floor(Math.random() * 255), Math.floor(Math.random() * 255))"
)

//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//endregion                                                  Random functions
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░