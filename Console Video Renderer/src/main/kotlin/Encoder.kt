
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.imageio.ImageIO
import kotlin.concurrent.thread


object Encoder {

    lateinit var threadPool: ExecutorService

    val isWorking : Boolean = false

    lateinit var data: Array<Array<Array<Int>>?>


    fun Encode(directory: File) : Array<Array<Array<Int>>?> {
        threadPool = Executors.newFixedThreadPool(10)

        System.gc()
        var i = 0
        directory.listFiles().apply {
            sort();
            data = arrayOfNulls(size)

            forEach {
            threadPool.submit(worker(it, i))
            i++
        }}

        threadPool.shutdown()
        while (!threadPool.isTerminated){}
        return data
    }

    private class worker(val f: File, val frameID: Int) : Runnable {
        override fun run() {
            println(threadPool)

            val bi = ImageIO.read(f)

            data[frameID] = Array(bi.width) { row ->
                Array(bi.height) { col ->
                    from(bi.getRGB(row, col))
                }
            }

            bi.flush()
        }

        fun from(color: Int): Int {
            val blue: Int =     color and 0xff
            val green: Int =    color and 0xff00 shr 8
            val red: Int =      color and 0xff0000 shr 16
            return ((red+blue+green)/3)
        }
    }
}
