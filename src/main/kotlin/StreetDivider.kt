package de.kotlincook.textmining.streetdivider

import java.text.ParseException
import kotlin.math.absoluteValue

data class Location(val street: String,
                    val houseNumber: Int? = null,
                    val houseNoAffix: String? = null) {

    constructor(street: String, houseNumberStr: String?, houseNoAffix: String?) :
            this(street, houseNumberStr?.subString(0,6)?.toInt(), houseNoAffix)
}


/**
 * https://www.strassenkatalog.de/str/
 * http://www.strassen-in-deutschland.de/
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
            if (!street1.endsWithDigit() || !houseNoWithAffix.startsWithDigit()) {
                try {
                    val (houseNumber, houseNoAffix) = divideIntoHouseNoAndAffix(houseNoWithAffix)
                    return Location(
                            street1.removeTrailingSpecialChars(),
                            houseNumber, houseNoAffix?.emptyToNull())
                } catch (e: ParseException) {
                    // happens in the case M4a where M4 is a special street:
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

    open fun divideIntoHouseNoAndAffix(str: String): Pair<String?, String?> {
        // Example: "Nr. 25 - 27 b"
        if (str == "") return Pair(null, null)
        val regexStrassenNummer = Regex("""(Nr\.)? *(\d+)(.*)$""")
        val matchStrassenNr = regexStrassenNummer.find(str)
        if (matchStrassenNr == null) throw ParseException(str, -1)
        return with(matchStrassenNr) {
            Pair(groupValues[2], groupValues[3].trim())
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

}

fun main(args: Array<String>) {
    val streetDivider = StreetDivider()
//    streetDivider.divideIntoHouseNoAndAffix("Hallo")


    for (cnt in 0..10000){
        for (len in 0..100) {
            var input = ""
            for (i in 0 until len) {
                val zahl: Int = (Math.random() * 65536).toInt()
                input += (zahl.absoluteValue % 65536).toChar();
            }
            // println(streetDivider.parse(input))
        }
    }


//    println(streetDivider.parse("M45"))
//    println(streetDivider.parse("X45"))
}

