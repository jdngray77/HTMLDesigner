import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.reflect.MalformedParametersException


fun main(args: Array<String>) {
    progargs.init(args)

    // Check if a file was parsed
    progargs.mustHave("dir")

    // Check if file parsed exists
    with (File(progargs["dir"])) {
        if (!exists())
            throw FileNotFoundException("Passed file does not exist.")
        if (!isDirectory)
            throw IOException("Path was a file. Expecting a folder of images.")

        progData.file = this
        println("Operating on ${progData.file}.")
    }


    Renderer.render(
        Encoder.Encode(progData.file)
    )
}

object progData {
    lateinit var file: File
}


object progargs : HashMap<String, String>() {

    fun init(_args: Array<String>) {
        _args.forEach {
            it.split(':').apply {
                if (size != 2)
                    throw MalformedParametersException("Param is not in format key:value : $this")

                var x = get(1)
                if (x.endsWith("\""))
                    x.substring(0,x.length)

                if (x.startsWith("\""))
                    x.substring(1,x.length+1)


                this@progargs[get(0)] = get(1)
            }
        }
    }

    fun mustHave(vararg keys: String){
        keys.forEach {
            if (get(it) == null) {
                throw MalformedParametersException("Missing '$it' parameter, which is required.")
            }
        }
    }
}



/**

 */
