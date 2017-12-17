import java.nio.charset.Charset

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


fun String.normStreetSuffix(): String {
    val thisEnlarged = this + " "
    val thisEnlargedToLower = thisEnlarged.toLowerCase()
    return when {
        thisEnlargedToLower.contains("straße ") ->
            thisEnlarged.replace("straße ", "str. ").replace("Straße ", "Str. ").trim()
        thisEnlargedToLower.contains("strasse ") ->
            thisEnlarged.replace("strasse ", "str. ").replace("Strasse ", "Str. ").trim()
        else -> this
    }
}

fun String.normStreetName(): String {
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

fun Char.isValidStreetSpecialChar(): Boolean {
    return ".- ".contains(this);
}

fun String.removeFromBeginning(normedPrefix: String): Pair<String, String> {
    var i = 0
    for (ch in normedPrefix) {
        if (this[i].toLowerCase() == ch) i++
        while (this[i].isValidStreetSpecialChar()) i++
    }
    return Pair(this.substring(0, i).trim(), this.substring(i).trim())
}

fun String.divideAtFirstNumber(): Pair<String, String?> {
    for (i in 0 until this.length) {
        if (this[i].isDigit()) return Pair(this.substring(0, i).trim(), this.substring(i).trim())
    }
    return Pair(this.trim(), null)
}

// https://www.strassenkatalog.de/str/
//http://www.strassen-in-deutschland.de/



open class StreetDivider(val dictionary: Dictionary) {
    constructor() : this(loadSpecialStreets())

    open fun parse(str: String): Location? {
        val streetOfDictionary = dictionary.prefixOf(str.normStreetName())
        if (streetOfDictionary != null) {
            val (street, houseNoWithAffix) = str.removeFromBeginning(streetOfDictionary)
            val houseNoAndAffix = parserHouseNoAndAffix2(houseNoWithAffix)
            if (houseNoAndAffix != null) {
                return Location.create(street, houseNoAndAffix.houseNumber, houseNoAndAffix.affix)
            }
        }
        val (street, houseNoWithAffix) = str.divideAtFirstNumber()
        if (houseNoWithAffix == null) {
            return Location.create(street, null, null)
        }
        if (street == "") {
            return Location(str.trim(), null, null)
        }
        val temp = parserHouseNoAndAffix2(houseNoWithAffix)
        if (temp != null) {
            return Location.create(street, temp.houseNumber, temp.affix)
        }

        return Location(str.trim(), null, null)
    }

    data class HouseNumberAndAffix(val houseNumber: String, var affix: String) {
        constructor() : this("", "")
    }

    fun parserHouseNoAndAffix2(str: String): HouseNumberAndAffix? {
        // Beispiel: "Nr. 25 - 27 b"
        val regexStrassenNummer = Regex("""^,? ?(Nr\.)? *(\d+)(.*)$""")
        val matchStrassenNr = regexStrassenNummer.find(str)
        if (matchStrassenNr != null) {
            println("***************" + matchStrassenNr.groupValues)
            return HouseNumberAndAffix(matchStrassenNr.groupValues[2], matchStrassenNr.groupValues[3].trim())
        }

        return null
    }
}

fun loadSpecialStreets(): Dictionary {
    return Dictionary(StreetReader.streets.map {
        t -> t.normStreetSuffix().normStreetName()
    })
}

fun main(args: Array<String>) {
//    println("Kultstraße 3".normStreetSuffix())
//    println("Garten12Weg".normStreetName())
//    println(StreetDivider().parse("Heideweg2a"))
    println(StreetDivider().parse("M1, Nr. 3"))
}