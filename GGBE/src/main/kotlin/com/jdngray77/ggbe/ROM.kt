package com.jdngray77.ggbe

import java.io.File

class ROM(val data : ROMRAW) {
    constructor(data : ByteArray) : this(ROMRAW(data))
    constructor(file: File) : this(file.readBytes())
    constructor(path: String) : this(File(path))

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val rom = ROM("/Users/gordie/Downloads/Pokemon Red.gb")
            println()
            println()
            println("Loaded ROM")
            println("[ ${rom.data.readString(0x0134..0x143)} ]")
        }
    }
}

typealias HEXAddress = Byte
typealias HEXAddressRange = IntProgression

fun b(i : Int) = i.toByte()

class ROMRAW(val data: ByteArray) {

    fun readBytes(at: HEXAddressRange): ByteArray
        = data.copyOfRange(at.first, at.last)

    /**
     * Interprets the entire address range as a string,
     * even empty bytes.
     */
    fun readFullString(at: HEXAddressRange)
        = readBytes(at).decodeToString()

    /**
     * Unlike [readFullString], Reads only populated bytes as characters.
     *
     * 0x00 bytes will be removed.
     */
    fun readString(at: HEXAddressRange)
        = readBytes(at).filter { it != b(0x00) }.toByteArray().decodeToString()

}