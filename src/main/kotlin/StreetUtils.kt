package de.kotlincook.textmining.streetdivider

fun String.removeTrailingSpecialChars():String {
    var result = this
    for (ch in this.reversed()) {
        if (!ch.isLetterOrDigit() && ch != '.') {
            result = result.removeSuffix(ch.toString())
        }
    }
    return result
}


fun String.standardizeLetters(): String {
    var result = ""
    for (ch in this) {
        if (ch.isLetterOrDigit()) {
            val lowerCh = ch.toLowerCase()
            result += when (lowerCh) {
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


fun main(args: Array<String>) {
    println("Kultstraße 3".standardizeStreetSuffix().standardizeLetters())
    println("Kultstraße".standardizeStreetSuffix().standardizeLetters())
    println("straße 73".standardizeStreetSuffix().standardizeLetters())
//    println("Garten12Weg".standardizeLetters())
//    println(StreetDivider().parse("Heideweg2a"))
//    println(StreetDivider().parse("M1, Nr. 3"))
//    println(StreetDivider().parse("1. Wasserstro 3b"))
}