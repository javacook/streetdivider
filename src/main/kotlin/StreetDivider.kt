package de.kotlincook.textmining.streetdivider

data class Location(val street: String,
                    val houseNumber: Int?,
                    val houseNumberAffix: String?) {

    companion object {
        fun create(street: String, houseNumber: String?, houseNummerExt: String?): Location {
            require(street != "")
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
open class StreetDivider(val dictionary: Dictionary) {
    constructor(streets: List<String>) : this(Dictionary(streets.map {
        t -> t.standardizeStreetName()
    }))
    constructor(vararg streets:String) : this(streets.asList())
    constructor() : this(StreetReader.streets)


    open fun parse(str: String): Location? {
        var prefixOfStr = str
        for (ch in str.reversed()) {
            if (dictionary.contains(prefixOfStr.standardizeStreetName())) break
            prefixOfStr = prefixOfStr.removeSuffix(ch.toString())
        }

        if (prefixOfStr.isNotEmpty()) {
            val street = prefixOfStr.removeTrailingSpecialChars()
            val houseNoWithAffix = str.substring(prefixOfStr.length)
            val houseNoAffixPair = parserHouseNoAndAffix(houseNoWithAffix)
            if (houseNoAffixPair != null) {
                return Location.create(street, houseNoAffixPair.houseNumber, houseNoAffixPair.affix)
            }
        }
        val (street, houseNoWithAffix) = str.divideIntoStreetAndHouseNoWihAffixDueToNumber()
        if (houseNoWithAffix == null) {
            return Location.create(street, null, null)
        }
        if (street == "") {
            return Location(str.trim(), null, null)
        }
        val temp = parserHouseNoAndAffix(houseNoWithAffix)
        if (temp != null) {
            return Location.create(street, temp.houseNumber, temp.affix)
        }

        return Location(str.trim(), null, null)
    }

    data class HouseNumberAffixPair(val houseNumber: String, var affix: String) {
        constructor() : this("", "")
    }

    fun parserHouseNoAndAffix(str: String): HouseNumberAffixPair? {
        // Beispiel: "Nr. 25 - 27 b"
        val regexStrassenNummer = Regex("""(Nr\.)? *(\d+)(.*)$""")
        val matchStrassenNr = regexStrassenNummer.find(str)
        if (matchStrassenNr != null) {
            return HouseNumberAffixPair(matchStrassenNr.groupValues[2], matchStrassenNr.groupValues[3].trim())
        }
        return null
    }


    /**
     * Searches the first occurance of a number in me (street, house no und affix) and
     * divides
     */
    fun String.divideIntoStreetAndHouseNoWihAffixDueToNumber(): Pair<String, String?> {
        for (i in 0 until this.length) {
            if (this[i].isDigit()) {
                return Pair(this.substring(0, i).trim(), this.substring(i).trim())
            }
        }
        return Pair(this.trim(), null)
    }
}

