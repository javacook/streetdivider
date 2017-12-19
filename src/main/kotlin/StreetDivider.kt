package de.kotlincook.textmining.streetdivider

import java.text.ParseException

data class Location(val street: String,
                    val houseNumber: Int?,
                    val houseNoAffix: String?) {

    constructor(street: String, houseNumber: String, houseNoAffix: String?) :
            this(street, houseNumber.toInt(),
            if (houseNoAffix?.isBlank()?: false) null else houseNoAffix)

    constructor(street: String) : this(street, null, null)

//    companion object {
//        fun create(street: String, houseNumber: String?, houseNummerExt: String?): Location {
//            val houseNumberInt = houseNumber?.trim()?.toInt()
//            return Location(street.trim(),
//                            houseNumberInt,
//                            if (houseNummerExt == null || houseNummerExt.isBlank()) null
//                            else houseNummerExt.trim())
//        }
//    }
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

        for (ch in street1.reversed()) {
            if (dictionary.contains(street1.standardizeStreetName())) break
            street1 = street1.removeSuffix(ch.toString())
        }

        if (street1.isNotEmpty()) {
            val houseNoWithAffix = input.substring(street1.length)
            if (!street1.endsWithDigit() || !houseNoWithAffix.startsWithDigit()) {
                try {
                    val (houseNumber, houseNoAffix) = divideHouseNoAndAffix(houseNoWithAffix)
                    return Location(street1.removeTrailingSpecialChars(), houseNumber, houseNoAffix)
                } catch (e: ParseException) {
                    return Location(inputTrimmed)
                }
            }
        }
        val (street2, houseNoWithAffix) = divideIntoStreetAndHouseNoWihAffixDueToNumber(inputTrimmed)
        if (houseNoWithAffix == null) {
            return Location(inputTrimmed)
        }
        if (street2 == "") {
            return Location(inputTrimmed)
        }
        try {
            val (houseNumber, houseNoAffix) = divideHouseNoAndAffix(houseNoWithAffix)
            return Location(street2.removeTrailingSpecialChars(), houseNumber, houseNoAffix)
        } catch (e: ParseException) {
            return Location(inputTrimmed)
        }
    }

    open protected fun divideHouseNoAndAffix(str: String): Pair<String, String?> {
        // Beispiel: "Nr. 25 - 27 b"
        if (str == "") return Pair("", null)
        val regexStrassenNummer = Regex("""(Nr\.)? *(\d+)(.*)$""")
        val matchStrassenNr = regexStrassenNummer.find(str)
        if (matchStrassenNr == null) throw ParseException(str, -1)
        return with(matchStrassenNr) {
            Pair(groupValues[2], groupValues[3].trim())
        }
    }

    /**
     * Searches the first occurance of a number in me (street, house no und houseNoAffix) except
     * the input is starting with diggits and divides <code>input</code> in front of the number
     * if exits
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
    println(streetDivider.parse("M45"))
//    println(streetDivider.parse("X45"))
}

