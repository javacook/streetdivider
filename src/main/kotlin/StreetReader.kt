package de.kotlincook.textmining.streetdivider

import java.nio.charset.Charset

object StreetReader {
    val streets: List<String>

    init {
        val resource = this::class.java.classLoader.getResource("specialstreets.txt")
        val readText = resource.readText(Charset.forName("UTF-8"))
        val lines = readText.split(Regex("""\r\n""")).map({it.trim()})
        if (lines.size > 1) {
            streets = lines
        }
        else {
            val lines = readText.split(Regex("""\n""")).map({it.trim()})
            if (lines.size > 1) {
                streets = lines
            }
            else {
                val lines = readText.split(Regex("""\r""")).map({it.trim()})
                if (lines.size > 1) {
                    streets = lines
                }
                throw Exception("The file 'specialstreet.txt' could not be read correctly.")
            }
        }
    }
}

fun main(args: Array<String>) {
    println(StreetReader.streets)
}