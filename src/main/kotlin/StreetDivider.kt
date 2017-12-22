package de.kotlincook.textmining.streetdivider

import java.text.ParseException
import kotlin.math.absoluteValue

// https://www.strassenkatalog.de/str/
// http://www.strassen-in-deutschland.de/

/**
 * The result of the "street division"
 */
data class Location(val street: String,
                    val houseNumber: Int? = null,
                    val houseNoAffix: String? = null) {

    constructor(street: String, houseNumberStr: String?, houseNoAffix: String?) :
            this(street, houseNumberStr?.subString(0,6)?.toInt(), houseNoAffix)
}


/**
 * The main class with the central function <code>parse</code>
 * A list of special streets can be given as argument. Some streets contain
 * a number as suffix or infix so that it would be impossible to decide whether
 * it is a house number or a part of the street name itselfs. Example "Straße 101".
 * To make this decision unambiguous the street "Straße 101" can added to the list
 * <code>streets</code>
 */
open class StreetDivider(private val dictionary: Dictionary) {
    constructor(streets: List<String>) : this(Dictionary(streets.map {
        t -> t.standardizeStreetName()
    }))
    constructor(vararg streets:String) : this(streets.asList())
    constructor() : this(StreetReader.streets)

    open fun parse(input: String): Location {
        val inputTrimmed = input.trim()
        var street1 = inputTrimmed

        for (ch in inputTrimmed.reversed()) {
            if (dictionary.contains(street1.standardizeStreetName())) break
            street1 = street1.removeSuffix(ch.toString())
        }

        if (street1.isNotEmpty()) {
            val houseNoWithAffix = input.substring(street1.length)
            // The following "if" avoids that B54 is devided into B5 and house no 4
            // if B5 is a special street:
            if (!street1.endsWithDigit() || !houseNoWithAffix.startsWithDigit()) {
                try {
                    val (houseNumber, houseNoAffix) = divideIntoHouseNoAndAffix(houseNoWithAffix)
                    return Location(
                            street1.removeTrailingSpecialChars(),
                            houseNumber, houseNoAffix?.emptyToNull())
                } catch (e: ParseException) {
                    // happens in the case M4a where M4 is a special street
                    // => Fallback that takes the complete input as street
                    return Location(inputTrimmed)
                }
            }
        }
        val (street2, houseNoWithAffix) = divideIntoStreetAndHouseNoWihAffixDueToNumber(inputTrimmed)
        if (street2 == "" || houseNoWithAffix == null) {
            return Location(inputTrimmed.removeTrailingSpecialChars())
        }
        try {
            val (houseNumber, houseNoAffix) = divideIntoHouseNoAndAffix(houseNoWithAffix)
            return Location(street2.removeTrailingSpecialChars(), houseNumber, houseNoAffix?.emptyToNull())
        } catch (e: ParseException) {
            // to be safety - this case can actually not happen as long as
            // divideIntoStreetAndHouseNoWihAffixDueToNumber works correct
            return Location(inputTrimmed)
        }
    }

    /**
     * Searches for the first occurrence of a number in me (street, house no und houseNoAffix),
     * except the input is starting with digits and divides <code>input</code> in front of the number
     */
    open protected fun divideIntoStreetAndHouseNoWihAffixDueToNumber(input: String): Pair<String, String?> {
        // Skipping over a possibly existing number prefix:
        var posAfterNumberPrefix = 0
        while (posAfterNumberPrefix < input.length) {
            if (!input[posAfterNumberPrefix++].isDigit()) break
        }

        for (i in posAfterNumberPrefix until input.length) {
            if (input[i].isDigit()) {
                return Pair(input.substring(0, i).trim(), input.substring(i).trim())
            }
        }
        return Pair(input, null)
    }

    /**
     * This method expects that <code>str</code> starts with "Nr." or a number
     * and splits <code>str</code> into the number part in the front and and the rest
     * @param str house no and affix to be divided
     */
    @Throws(ParseException::class)
    open protected fun divideIntoHouseNoAndAffix(str: String): Pair<String?, String?> {
        // Example: "Nr. 25 - 27 b"
        if (str == "") return Pair(null, null)
        val regexStrassenNummer = Regex("""^(Nr\.?)? *(\d+)(.*)$""")
        val matchStrassenNr = regexStrassenNummer.find(str)
        if (matchStrassenNr == null) throw ParseException(str, -1)
        return with(matchStrassenNr) {
            Pair(groupValues[2], groupValues[3].trim())
        }
    }

}


fun main(args: Array<String>) {
    val streetDivider = StreetDivider()
    println(streetDivider.parse("P111 2"))
}

