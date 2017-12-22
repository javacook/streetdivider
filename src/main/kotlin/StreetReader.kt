package de.kotlincook.textmining.streetdivider

import java.nio.charset.Charset

/**
 * Reads the special streets from the file <code>specialstreets.txt</code>. It
 * can be accessed via the public property <code>sreeet</code>
 */
object StreetReader {
    val streets: List<String>

    init {
        val resource = this::class.java.classLoader.getResource("specialstreets.txt")
        val readText = resource.readText(Charset.forName("UTF-8"))
        var lines: List<String> = arrayListOf()
        // Try all line separator types...
        for (lineSeparator in arrayOf("""\r\n""", """\n""", """\r""")) {
            lines = readText.split(Regex(lineSeparator)).map({it.trim()})
            if (lines.size > 1) break
        }
        streets = lines
        if (lines.size <= 1) {
            throw Exception("The file 'specialstreet.txt' could not be read correctly.")
        }
    }
}

fun main(args: Array<String>) {
    println(StreetReader.streets)
}