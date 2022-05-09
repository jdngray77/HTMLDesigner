import java.lang.Thread.sleep
import kotlin.collections.ArrayList

/**
 * <h1></h1>
 * <br>
 * <p>
 *
 * </p>
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 05/03/2022</a>
 * @since v1
 * @version 1
 */
object Renderer {
    fun render(data: Array<Array<Array<Int>>?>) {
        val timePerFrame = 60L / 60L

        data.forEach {
            it?.let { renderFrame(it) }
            sleep(timePerFrame)
        }
    }

    private fun renderFrame(frame: Array<Array<Int>>) {
        println()

        for (x in 0 until frame[0].size) {
            for (y in 0 until frame.size)
                print(chars[(frame[y][x] / chars.size).coerceAtMost(chars.size-1)])
            println()
        }
    }

    private val chars: Array<String> = arrayOf(//".",":","-","=","+","*","#","%","@","䷀"
        "䷀",
        "䷈",
        "䷅",
        "䷃",
        "䷓",
        "䷖",
        "䷁",
        "𝌆",
        "𝌇",
        "𝌌",
        "𝌕",
        "𝌗",
        "𝌝",
        "𝌠",
        "𝌻",
        "𝌺",
        "𝍖",
        "☰",
        "☲",
        "☵",
        "☶",
        "☷",
        "⚌",
        "⚍",
        "⚏",
        "⚊",
        "⚋"
    )
}