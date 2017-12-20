package de.kotlincook.textmining.streetdivider

import java.text.CharacterIterator

fun String.removeTrailingSpecialChars():String {
    var result = this
    for (ch in this.reversed()) {
        if (!ch.isLetterOrDigit() && ch != '.') {
            result = result.removeSuffix(ch.toString())
        }
    }
    while (result.endsWith("..")) {
        result = result.substring(0, result.lastIndex)
    }
    return result
}


fun String.standardizeLetters1(): String {
    var result = ""
    for (ch in this) {
        if (ch.isLetterOrDigit()) {
            val lowerCh = ch.toLowerCase()
            result += when (lowerCh) {
                'é','è','ê' -> "e"
                'ä' -> "ae"
                'ö' -> "oe"
                'ü' -> "ue"
                'ß' -> "ss"
                else -> lowerCh.toString()
            }
        }
    }
    return result
}

fun String.standardizeLetters(): String {
    var result = ""
    for (pos in 0..this.lastIndex) {
        val lowerCh = this[pos].toLowerCase()
        if (lowerCh.isLetterOrDigit()) {
            result += when (lowerCh) {
                'é','è','ê' -> "e"
                'ä' -> "ae"
                'ö' -> "oe"
                'ü' -> "ue"
                'ß' -> "ss"
                else -> lowerCh.toString()
            }
        }
        else if (this.charAt(pos-1)?.isDigit()?:false && this.charAt(pos+1)?.isDigit()?:false) {
            result += " "
        }
    }
    return result
}

fun String.charAt(pos: Int): Char? {
    return if (pos < 0 || pos > this.lastIndex) null else this[pos]
}


fun String.standardizeStreetSuffix(): String {
    if (this.length < 6) return this
    return when (this.substring(this.length - 6)) {
        "Straße" -> this.substring(0, this.length - 6) + "Str."
        "straße" -> this.substring(0, this.length - 6) + "str."
        else -> this
    }
}

fun String.standardizeStreetName(): String {
    return this.standardizeStreetSuffix().standardizeLetters();
}

fun String.startsWithDigit(): Boolean {
    return this.length > 0 && this.first().isDigit()
}

fun String.endsWithDigit(): Boolean {
    return this.length > 0 && this.last().isDigit()
}

fun String.emptyToNull(): String? {
    return if (this.isEmpty()) null else this
}

fun String.subString(from: Int, to: Int): String {
    return if (this.length <= to) this else this.substring(from, to)
}


fun main(args: Array<String>) {
    println("Kultstraße 3".standardizeStreetSuffix().standardizeLetters())
    println("Kultstraße".standardizeStreetSuffix().standardizeLetters())
    println("Straße 10    12".standardizeStreetSuffix().standardizeLetters())
//    println("Garten12Weg".standardizeLetters())
//    println(StreetDivider().parse("Heideweg2a"))
//    println(StreetDivider().parse("M1, Nr. 3"))
//    println(StreetDivider().parse("1. Wasserstro 3b"))
}