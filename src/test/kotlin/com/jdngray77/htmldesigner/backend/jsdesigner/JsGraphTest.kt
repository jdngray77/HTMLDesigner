package com.jdngray77.htmldesigner.backend.jsdesigner

import com.jdngray77.htmldesigner.frontend.jsdesigner.*
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


internal class JsGraphTest {

    lateinit var data: JsGraph

    /**
     * The number of nodes in the graph at creation.
     */
    var dataNodeCount : Int = 0

    /**
     * The number of groups in the graph at creation.
     */
    var dataGroupCount : Int = 0

    /**
     * Creates a graph with nodes a links, based on
     * a random test graph i make in the designer.
     *
     * It's arbitrary and messy, but it has a bit of complexity
     * to test.
     */
    @BeforeEach
    internal fun setUp() {

        // New graph
        data = JsGraph("TEST")

        // Add some web page elements
        val link = data.addElement(Element("a").id("Link"))
        val header = data.addElement(Element("h1").id("Header"))
        val body = data.addElement(Element("body").id("Body"))


        // Create a color factory and feed it random RGBA values
        // We do have a JsRandomColorFunction, but this adds complexity for
        // the sake of testing.
        val colorFactory = data.addFunction(JsColorFactoryFunction())

        val randR = data.addFunction(JsRandomNumberFunction())
        val randG = data.addFunction(JsRandomNumberFunction())
        val randB = data.addFunction(JsRandomNumberFunction())
        val randA = data.addFunction(JsRandomNumberFunction())

        randR.output().emit(colorFactory.receiver("r"))
        randG.output().emit(colorFactory.receiver("g"))
        randB.output().emit(colorFactory.receiver("b"))
        randA.output().emit(colorFactory.receiver("a"))

        // Group that color stuff together
        data.addGroup(
            JsGraphNodeGroup(
                "Random Color Generator",
                colorFactory, randR, randG, randB, randA
            )
        )

        // Feed the random alpha generator a random min and max (for fun)
        val randAMin = data.addFunction(JsRandomNumberFunction())
        val randAMax = data.addFunction(JsRandomNumberFunction())

        randAMin.output()!!.emit(randA.receiver("min")!!)
        randAMax.output()!!.emit(randA.receiver("max")!!)

        data.addGroup(
            JsGraphNodeGroup(
                "Random Alpha",
                randAMin, randAMax
            )
        )

        // Take the output of this shite, and use it on for
        // something on the web page.
        data.addGroup(
            JsGraphNodeGroup(
                "Outputs",
                header, body
            )
        )

        // Clicking the link triggers the random color, which is used on the header & body.
        link.emitter("Click").emit(colorFactory.trigger())
        colorFactory.output().emit(header.receiver("Back Color"))
        colorFactory.output().emit(body.receiver("Back Color"))

        // For fun, when mousing over the link, randomly make the body visible or not.
        val randBool = data.addFunction(JsRandomBooleanFunction())

        randBool.output().emit(body.receiver("Visible"))

        link.emitter("Hover").emit(randBool.trigger())


        dataNodeCount = data.getNodes().size
        dataGroupCount = data.getGroups().size
    }

    /**
     * Checks that the graph has the correct number of nodes,
     * especially when adding and removing.
     */
    @Test
    fun nodeAndGroupCount() {
        // Data should match at creation
        assertEquals(dataNodeCount, data.getNodes().size)
        assertEquals(dataGroupCount, data.getGroups().size)

        // Adding a node should increase node count by one.
        val h2 = data.addElement(Element("h2").id("h2"))
        assertEquals(dataNodeCount+1, data.getNodes().size)

        // Adding a group should increase group count by one.
        val testGroup = data.addGroup(JsGraphNodeGroup("Test", h2))
        assertEquals(dataGroupCount+1, data.getGroups().size)

        // Removing a node should decrease it by one.
        data.removeNode("h2")
        assertEquals(dataNodeCount, data.getNodes().size)

        // Removing the node does not automatically delete the empty group.
        assertEquals(dataGroupCount+1, data.getGroups().size)


        // Removing all nodes should leave us with an empty graph.
        data.deleteEverything()

        assertEquals(0, data.getNodes().size)
        assertEquals(0, data.getGroups().size)
    }

    @Test
    fun deleteEverything() {
        data.deleteEverything()
        assertEquals(0, data.getNodes().size)
        assertEquals(0, data.getGroups().size)
    }

    @Test
    fun getElementNode() {
        assertNotNull(data.getElement("Link"))

        data.removeNode("Link")

        assertNull(data.getElement("Link"))
    }

    @Test
    fun assertElementExists() {
        assertDoesNotThrow { data.assertElementExists("Link") }
        data.removeNode("Link")
        assertThrows(IllegalArgumentException::class.java) { data.assertElementExists("Link") }
    }

    @Test
    fun assertElementDoesNotExist() {
        assertThrows(IllegalArgumentException::class.java) { data.assertElementDoesNotExist("Link") }
        data.removeNode("Link")
        assertDoesNotThrow { data.assertElementDoesNotExist("Link") }
    }

    @Test
    fun addElement() {
        assertThrows(IllegalArgumentException::class.java) { data.addElement(Element("Link").id("Link")) }
        data.addElement(Element("h2").id("test"))
        assertNotNull(data.getElement("test"))
    }

    @Test
    fun removeNode() {
        assertThrows(IllegalArgumentException::class.java) { data.removeNode("test") }
        data.addElement(Element("h2").id("test"))
        assertNotNull(data.getElement("test"))
    }

    @Test
    fun testRemoveNode() {
        val first = data.getNodes().first()
        assertDoesNotThrow { data.removeNode(first) }
        assertFalse(data.getNodes().contains(first))
    }

    //region compilation

    /**
     * Checks that the compiler warns and excludes a function is being triggered, but the output is not being used.
     */
    @Test
    fun triggerUnused() {
        data.deleteEverything()
        println("deleted everything")

        val header = data.addElement(Element("a").id("Header"))
        val bool = data.addFunction(JsRandomBooleanFunction())
        println("Added a header and a random boolean function")

        header.emitter("Click").emit(bool.trigger())

        val compileResult = JsGraphCompiler.compileGraph(data)
        println(compileResult)

        assertEquals(JsGraphCompiler.JsGraphCompilationResult.ResultType.ERROR, compileResult.type, "The compilation should be marked a failed, since there is no output.")

        assertFalse(compileResult.messages.isEmpty(), "There should be warnings.")
        assertTrue(compileResult.builder.jsFunctions.isEmpty(), "The function should have been excluded, since it's trigger was useless.")
        assertTrue(compileResult.javascript.isBlank(), "There should be no output.")



        assertNotNull(compileResult.messages.find { it.type == JsGraphCompiler.MessageType.WARNING_TRIGGERED_FUNCTION_UNUSED }, "There should be a warning about the unused trigger.")
        assertNotNull(compileResult.messages.find { it.type == JsGraphCompiler.MessageType.WARNING_NO_JAVASCRIPT_GENERATED }, "There should be a warning about the graph not producing any output.")
    }

    @Test
    fun missingRequiredParameter() {
        // The add function requires inputs.
        // Not providing them will throw an error.
        val add = data.addFunction(JsAbsFunction())

        // Trigger it
        data.getNode("Link")!!.emitter("Click").emit(add.trigger())

        // abs will be ignored if the output is not used, so we need to connect it to something.
        data.getFunction("Abs")!!.output().emit(data.getFunction("Color Factory")!!.receiver("a"))

        val compileResult = JsGraphCompiler.compileGraph(data)
        println(compileResult)

        assertEquals(JsGraphCompiler.JsGraphCompilationResult.ResultType.ERROR, compileResult.type, "The compilation should have failed, and be marked as error.")

        assertNotNull(
            compileResult.messages.find {
                it.level == JsGraphCompiler.MessageLevel.ERROR
            },
            "There should be an error."
        )

        assertTrue(
            compileResult.messages.first().level
             == JsGraphCompiler.MessageLevel.ERROR,
            "The error should be first."
        )
    }

    @Test
    fun blankProducesNoOutput() {
        data.deleteEverything()

        val compileResult = JsGraphCompiler.compileGraph(data)
        println(compileResult)

        assertEquals(JsGraphCompiler.JsGraphCompilationResult.ResultType.ERROR, compileResult.type, "The compilation should be empty, and be marked as error.")

        assertNotNull(compileResult.messages.find { it.type == JsGraphCompiler.MessageType.WARNING_NO_JAVASCRIPT_GENERATED}, "There should be a warning about the graph not producing any output.")
        assertTrue(compileResult.messages.first().level == JsGraphCompiler.MessageLevel.ERROR, "The error should be first.")
    }

    @Test
    fun blankParameters() {
        val nodesWithNoAdmissions = data.getNodes().filter {
            node ->
            node.receivers.all {
                (it.isTrigger() && node.receivers.size > 1)  || !it.hasAdmission()
            }
        }

        println(nodesWithNoAdmissions)

        assertTrue(nodesWithNoAdmissions.isNotEmpty(), "Cannot complete test with no nodes with no admissions.")

        val compilationResult = JsGraphCompiler.compileGraph(data)
        println(compilationResult)

        val noAdmissionWarnings = compilationResult.messages.filter {
            it.type == JsGraphCompiler.MessageType.WARNING_FUNCTION_HAS_NO_INPUT
        }

        assertEquals(nodesWithNoAdmissions.size, noAdmissionWarnings.size)
    }

    // TODO incorrect function trigger
    fun incorrectFunctionTrigger() {
        data.getNodes()
    }

    @Test
    fun invalidConnections() {
        // TODO test attempting to make connections between incompatible types.
    }

    //endregion


}