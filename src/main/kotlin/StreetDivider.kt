package de.kotlincook.textmining.streetdivider

import java.text.ParseException

data class Location(val street: String,
                    val houseNumber: Int?,
                    val houseNoAffix: String?) {

    companion object {
        fun create(street: String, houseNumber: String?, houseNummerExt: String?): Location {
            val houseNumberInt = houseNumber?.trim()?.toInt()
            return Location(street.trim(),
                            houseNumberInt,
                            if (houseNummerExt == null || houseNummerExt.isBlank()) null
                            else houseNummerExt.trim())
        }
    }
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
        var streetCandidate = input.trim()
        for (ch in streetCandidate.reversed()) {
            if (dictionary.contains(streetCandidate.standardizeStreetName())) break
            streetCandidate = streetCandidate.removeSuffix(ch.toString())
        }

        if (streetCandidate.isNotEmpty()) {
            val houseNoWithAffix = input.substring(streetCandidate.length)
            if (!streetCandidate.endsWithDigit() || !houseNoWithAffix.startsWithDigit()) {
                try {
                    with(parseHouseNoAndAffix(houseNoWithAffix)) {
                        return Location.create(streetCandidate.removeTrailingSpecialChars(), houseNumber, houseNoAffix)
                    }
                } catch (e: ParseException) {
                    return Location(input, null, null)
                }
            }
        }
        val (street, houseNoWithAffix) = divideIntoStreetAndHouseNoWihAffixDueToNumber(input)
        if (houseNoWithAffix == null) {
            return Location.create(street, null, null)
        }
        if (street == "") {
            return Location(input.trim(), null, null)
        }
        try {
            with(parseHouseNoAndAffix(houseNoWithAffix)) {
                return Location.create(street, houseNumber, houseNoAffix)
            }
        } catch (e: ParseException) {
            return Location(input.trim(), null, null)
        }
    }

    data class HouseNumberAffixPair(val houseNumber: String?, var houseNoAffix: String?) {
        constructor() : this(null, null)
    }

    open protected fun parseHouseNoAndAffix(str: String): HouseNumberAffixPair {
        // Beispiel: "Nr. 25 - 27 b"
        if (str == "") return HouseNumberAffixPair()
        val regexStrassenNummer = Regex("""(Nr\.)? *(\d+)(.*)$""")
        val matchStrassenNr = regexStrassenNummer.find(str)
        if (matchStrassenNr == null) throw ParseException(str, -1)
        return with(matchStrassenNr) {
            HouseNumberAffixPair(groupValues[2], groupValues[3].trim())
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

