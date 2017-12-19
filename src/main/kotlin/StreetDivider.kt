package de.kotlincook.textmining.streetdivider

import java.text.ParseException

data class Location(val street: String,
                    val houseNumber: Int?,
                    val houseNoAffix: String?) {

    companion object {
        fun create(street: String, houseNumber: String?, houseNummerExt: String?): Location {
            val houseNumberInt = houseNumber?.trim()?.toInt()
            require(houseNumberInt == null || houseNumberInt > 0)
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

    open fun parse(str: String): Location {
        var prefixOfStr = str
        for (ch in str.reversed()) {
            if (dictionary.contains(prefixOfStr.standardizeStreetName())) break
            prefixOfStr = prefixOfStr.removeSuffix(ch.toString())
        }

        if (prefixOfStr.isNotEmpty()) {
            val street = prefixOfStr.removeTrailingSpecialChars()
            val houseNoWithAffix = str.substring(prefixOfStr.length)
            try {
                with(parseHouseNoAndAffix(houseNoWithAffix)) {
                    return Location.create(street, houseNumber, houseNoAffix)
                }
            } catch (e: ParseException) {
                return Location(str.trim(), null, null)
            }
        }
        val (street, houseNoWithAffix) = divideIntoStreetAndHouseNoWihAffixDueToNumber(str)
        if (houseNoWithAffix == null) {
            return Location.create(street, null, null)
        }
        if (street == "") {
            return Location(str.trim(), null, null)
        }
        try {
            with(parseHouseNoAndAffix(houseNoWithAffix)) {
                return Location.create(street, houseNumber, houseNoAffix)
            }
        } catch (e: ParseException) {
            return Location(str.trim(), null, null)
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
     * Searches the first occurance of a number in me (street, house no und houseNoAffix) and
     * divides
     */
    open protected fun divideIntoStreetAndHouseNoWihAffixDueToNumber(str: String): Pair<String, String?> {
        for (i in 0 until str.length) {
            if (str[i].isDigit()) {
                return Pair(str.substring(0, i).trim(), str.substring(i).trim())
            }
        }
        return Pair(str.trim(), null)
    }
}

fun main(args: Array<String>) {
    val streetDivider = StreetDivider()
    println(streetDivider.parse("M4N"))
}

