package com.shinkson47.bulletjournal
import aat.AsciiArtTable
import kotlinx.datetime.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.ArrayList

@Serializable data class Habbit(val name: String, val completions : ArrayList<LocalDate> = ArrayList())
@Serializable data class Habbits(val values : ArrayList<Habbit> = ArrayList())

@Serializable data class Data(
    val name : String       = "untitled",
    @Contextual val habbits : Habbits   = Habbits()
) {
    companion object {
        lateinit var DATA : Data
        val FILE = File("./journal.bj")

        fun load() {
            DATA = Json.decodeFromString(FILE.readText())
        }

        fun save() {
            FILE.writeText(Json.encodeToString(DATA))
        }

        @JvmStatic
        fun main(args: Array<String>) {
            if (!FILE.exists()) FILE.createNewFile()

            DATA = Data("hello")

            val x = Habbit("Clean")
            x.completions.add(LocalDate(2021, 2, 1))

            DATA.habbits.values.add(x)

            save()
        }

        fun render() : String {
            with (AsciiArtTable()) {

                // Column of headers.
                addHeaderCols("Habbits")

                // get current day
                val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)

                // go back to previous monday
                // itterate over seven days, rendering each habbit.




                for (value in DATA.habbits.values) {
                    addHeaderCols(value.name)
                }


                add("bello", "pussy", "hans")
                return toString()
            }
        }
    }
}