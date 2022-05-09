import com.shinkson47.bulletjournal.Data
import com.shinkson47.bulletjournal.Habbit

/**
 * # The front facing API
 */
// TODO this class is a right fucking state.
object API {

    /**
     * # List of the API's endpoints.
     */
    enum class APIEndpoint {
        new,
        close,
        save,
        show,
        habbit,

        no
    }

    fun interpret (input : String) = call (APIEndpoint.valueOf(input))
    fun call (endpoint : APIEndpoint) {
        this::class.java.getMethod(endpoint.toString()).invoke(this)
        clear()
        showFeedback()
    }

    private var _feedback : String? = null
    fun instantFeedback(string: String, successful: Boolean = true) {
        feedback(string, successful)
        showFeedback()
    }

    private fun feedback(string: String, successful: Boolean = true) {
        _feedback = "[${if (successful) "✔︎" else "✘" }] $string"
    }

    private fun showFeedback() {
        println()
        println(if (_feedback == null) "No feedback provided." else _feedback)
        _feedback = null
    }

    fun clear() {
        for (i in 1..100)
            println()
    }

    //===============================================
    //#region         END POINTS
    //===============================================

    fun close() {
        Data.DATA = Data()
    }

    fun new() {
        Data.DATA = Data(getString("Enter a title for this journal."))

        feedback("Created a journal with the name ${Data.DATA.name}.")
    }

    fun save() {
        Data.save()
        feedback("Saved ${Data.DATA.name}.")
    }

    fun show() = feedback( "\nShowing ${Data.DATA.name}. \n${Data.render()}")

    fun habbit() {
        Data.DATA.habbits.values.add(Habbit(getString("Enter a name for this habbit.")))
        show()
        feedback("Created new habbit.")
    }

    fun no() {

        feedback("Fuck you, then.")
    }


    //===============================================
    //#endregion      END POINTS
    //#region         Interaction
    //===============================================

    //TODO abstract and other types.
    fun getString(message : String) : String {
        var value : String? = null

        while (value.isNullOrBlank()) {
            println()
            println("⥥⥥⥥⥥⥥⥥⥥⥥⥥⥥⥥⥥")
            println(message)
            print("> ")
            value = readLine()
        }
        println("⥣⥣⥣⥣⥣⥣⥣⥣⥣⥣⥣⥣")
        return value
    }


    //===============================================
    //#endregion      Interaction
    //===============================================
}
