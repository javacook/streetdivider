import de.kotlincook.textmining.streetdivider.Location
import de.kotlincook.textmining.streetdivider.StreetDivider
import spock.lang.Specification

class StreetDividerTest extends Specification  {

    def streetDivider = new StreetDivider(
            "Bundesstraße1", "Bundesstr. 2", "Straße73" ,"1 Maja", "2. Willi",
            "b4", "D4", "Straße des 18. Oktober", "Straße 10", "Str 101", "143",
            "P 111")

    def "division of compound street names into their parts works correct"() {
        expect:
        streetDivider.parse(input) == new Location(street, houseNo, affix)

        where:
        input                      | street                 | houseNo | affix
        "Bundesstraße 1 25 1/3"    | "Bundesstraße 1"       | 25      | "1/3"
        "Bundesstraße 2 Nr. 25"    | "Bundesstraße 2"       | 25      | null
        "Bundesstraße 2 Nr. 25a"   | "Bundesstraße 2"       | 25      | "a"
        "Straße 73 5a"             | "Straße 73"            | 5       | "a"
        "Gartenstr. 25a"           | "Gartenstr."           | 25      | "a"
        "Allertshäuser Straße 25a" | "Allertshäuser Straße" | 25      | "a"
        "Gartenstr. a"             | "Gartenstr. a"         | null    | null
        "Heideweg 3-5"             | "Heideweg"             | 3       | "-5"
        "Heideweg 3 -52"           | "Heideweg"             | 3       | "-52"
        "Heideweg 32- 5"           | "Heideweg"             | 32      | "- 5"
        "Heideweg 32 - 5"          | "Heideweg"             | 32      | "- 5"
        "Heideweg 32/5"            | "Heideweg"             | 32      | "/5"
        "Heideweg 32 /5"           | "Heideweg"             | 32      | "/5"
    }

}
