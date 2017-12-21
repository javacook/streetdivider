package de.kotlincook.textmining.streetdivider

import de.kotlincook.textmining.streetdivider.Status.*

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



fun String.standardizeLetters(): String {
    var result = ""
    for (pos in 0..this.lastIndex) {
        val lowerCh = this[pos].toLowerCase()
        if (lowerCh.isLetterOrDigit()) {
            result += when (lowerCh) {
                'à', 'á', 'â', 'ã', 'å' -> "a"
                'æ', 'ä' -> "ae"
                'ç' -> "c"
                'ë', 'ê', 'é', 'è' -> "e"
                'Ì', 'Í', 'Î', 'Ï' -> "i"
                'ñ' -> "n"
                'ò', 'ó', 'ô', 'õ', 'ø' -> "o"
                'ö' -> "oe"
                'ù', 'ú', 'û' -> "u"
                'ü' -> "ue"
                'þ'-> "p"
                'ý' -> "y"
                'ß', 'β' -> "ss"
                else -> lowerCh.toString()
            }
        } else {
            result += this[pos]
        }
    }
    return result
}


enum class Status {
    DEFAULT, DIGIT, DIGIT_SPECIAL
}

fun String.encodeSpecialChars(): String {
    val CODE_FOR_SPECIAL = "_"
    var result = ""
    var status = DEFAULT
    for (ch in this) {
        val pair =
        when (status) {
            DEFAULT -> {
                when {
                    ch.isDigit() -> Pair(DIGIT, ch.toString())
                    ch.isLetter() -> Pair(DEFAULT, ch.toString())
                    else -> Pair(DEFAULT, "")
                }
            }
            DIGIT -> {
                when {
                    ch.isDigit() -> Pair(DIGIT, ch.toString())
                    ch.isLetter() -> Pair(DEFAULT, ch.toString())
                    else -> Pair(DIGIT_SPECIAL, "")
                }
            }
            DIGIT_SPECIAL -> {
                when {
                    ch.isDigit() -> Pair(DIGIT, CODE_FOR_SPECIAL + ch)
                    ch.isLetter() -> Pair(DEFAULT, ch.toString())
                    else -> Pair(DIGIT_SPECIAL, "")
                }
            }
        }
        status = pair.first
        result += pair.second
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
    return this.standardizeStreetSuffix().standardizeLetters().encodeSpecialChars();
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
    println("Kultstraße-3".standardizeLetters())
    println("_Ku.lt-45,.._str23 34aße".standardizeStreetName())

//    println("Kultstraße".standardizeStreetSuffix().standardizeLetters())
//    println("Straße 10    12".standardizeStreetSuffix().standardizeLetters())
//println("println("Garten12Weg".standardizeLetters())
//println("println(StreetDivider().parse("Heideweg2a"))
//println("println(StreetDivider().parse("M1, Nr. 3"))
//println("println(StreetDivider().parse("1. Wasserstro 3b"))
}