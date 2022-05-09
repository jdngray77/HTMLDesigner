//===============================
//       Virtual machine
//===============================
// The thing that executes our script.
object VirtualMachine : IVirtualMachine {

    private val stack = ArrayList<Frame>()



}


interface IVirtualMachine {

}



//===============================
//          Memory
//===============================
// Stores programme and variable information.


open class RAM : HashMap<String, Any>()
class  Frame : RAM()
object Heap  : RAM()

class Program : ArrayList<String>()



//===============================
//          Runtime
//===============================
// Something that takes a file and loads it into the virtual machine.

object ScriptRunner {
    @JvmStatic
    fun main(args: Array<String>) {

    }
}