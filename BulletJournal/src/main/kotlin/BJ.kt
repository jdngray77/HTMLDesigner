
import API.clear
import java.lang.IllegalArgumentException

/**
 * # TODO
 * @author [Jordan T. Gray](https://www.shinkson47.in) on 07/09/2021
 * @since v1
 * @version 1
 */
object BJ {

    @JvmStatic
    fun main(args: Array<String>) {
        clear()

        banner()

        loop()
    }


    private fun banner() {
        println()
        println("Hello")
        println()
    }

    private fun loop() {
        while (true) {
            // TODO dupe.
            with (API) {
                try {
                    interpret(getString("Enter a command."))
                } catch (e : IllegalArgumentException ) {
                    instantFeedback("Try again.", false)
                }
            }
        }
    }
}

