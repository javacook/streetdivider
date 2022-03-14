package de.kotlincook.textmining.streetdivider

import de.kotlincook.textmining.streetdivider.Status.*

/**
 * Removes special characters at the end except the dot.
 * This routine leaves one point at the end,
 * example: "street&.../$" --> "street&."
 */
fun String.removeTrailingSpecialChars(): String {
    var result = this
    for (ch in this.reversed()) {
        if (!ch.isLetterOrDigit() && ch != '.') {
            result = result.removeSuffix(ch.toString())
        }
    }
    while (result.endsWith("..")) {
        result = result.dropLast(1)
    }
    return result
}


/**
 * Exotic letters are converted to the ASCII character set.
 */
fun String.standardizeLetters(): String {
    var result = ""
    for (pos in 0..this.lastIndex) {
        val lowerCh = this[pos].lowercaseChar()
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
                'þ' -> "p"
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


/**
 * Stores the parsing status inside encodeSpecialChars
 * @see encodeSpecialChars
 */
private enum class Status {
    /** doku */
    DEFAULT,

    /** doku */
    DIGIT,

    /** doku */
    DIGIT_SPECIAL
}

/**
 * Removes all special characters for normalization,
 * except special characters between two digits:
 * "Gartenstr. 25-27" -> "Gartenstr25_27"
 */
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


/**
 * Replaces "Straße" with "Str." in all combinations (lower/upper case)
 */
fun String.standardizeStreetInfix(): String {
    return this.replace(Regex("""(straße)([^a-zA-Z])"""), "str.$2")
        .replace(Regex("""straße$"""), "str.")
        .replace(Regex("""(Straße)([^a-zA-Z])"""), "Str.$2")
        .replace(Regex("""Straße$"""), "Str.")
}

/**
 * Concatenates three standardization functions
 */
fun String.standardizeStreetName(): String {
    return this.standardizeStreetInfix() // it is important to do this first
        .standardizeLetters()        // because here are ß's replaced with ss
        .encodeSpecialChars()
}

/**
 * Checks whether the string starts with a digit
 * @return true if it starts with a digit
 */
fun String.startsWithDigit(): Boolean {
    return this.isNotEmpty() && this.first().isDigit()
}

/**
 * Checks whether the string ends with a digit
 * @return true if it ends with a digit
 */
fun String.endsWithDigit(): Boolean {
    return this.isNotEmpty() && this.last().isDigit()
}

/**
 * Returns the string unchanged exception the empty string
 * @return null if the string is empty
 */
fun String.emptyToNull(): String? {
    return this.ifEmpty { null }
}

/**
 * An index-safe modification of String.substring
 */
fun String.subString(from: Int, to: Int): String {
    return if (this.length <= to) this else this.substring(from, to)
}


//fun main() {
//    println("Gartenstr. 25-27".encodeSpecialChars())
//    println("Kultstraße-3".standardizeLetters())
//    println("_Ku.lt-45,.._str23 34aße".standardizeStreetName())
//    println("Gartenstraße23".standardizeStreetInfix())
//    println("Gartenstraße".standardizeStreetInfix())
//    println("Straßenbahnstr. 23".standardizeStreetInfix())
//    println("Straße12".standardizeStreetInfix())
//    println("Straße 12".standardizeStreetInfix())
//    println("Kultstraße".standardizeStreetInfix().standardizeLetters())
//    println("Straße 10    12".standardizeStreetInfix().standardizeLetters())
//    println(" Garten12Weg ".standardizeLetters())
//    println(StreetDivider().parse(" Heideweg2a "))
//    println(StreetDivider().parse(" M1, Nr.3"))
//    println(StreetDivider().parse("1.Wasserstro 3 b "))
//}