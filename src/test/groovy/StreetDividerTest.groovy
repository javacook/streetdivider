import de.kotlincook.textmining.streetdivider.Location
import de.kotlincook.textmining.streetdivider.StreetDivider
import spock.lang.Specification

class StreetDividerTest extends Specification  {

    def streetDivider = new StreetDivider(
            "Bundesstraße1", "Bundesstr. 2",
            "Straße73",
            "1 Maja", "2. Willi",
            "b4", "D 4",
            "Straße des 18. Oktober",
            "Straße 10",
            "Str 101",
            "143",
            "P 111",
            "Straße Nr. 1", "Straße Nr. 12", "Straße Nr. 12b",
            "Düne 5")

    def "division of compound street names into their parts works correct"() {
        expect:
        streetDivider.parse(input) == new Location(street, houseNo, affix)
        streetDivider.parse(" " + input + " ") == new Location(street, houseNo, affix)

        where:
        input                      | street                 | houseNo | affix
        ""                         | ""                     | null    | null
        " "                        | ""                     | null    | null
        "A"                        | "A"                    | null    | null
        "5"                        | "5"                    | null    | null
        "143"                      | "143"                  | null    | null
        "5001 44"                  | "5001"                 | 44      | null
        "374, 4"                   | "374"                  | 4       | null
        "B 4 10–10a"               | "B 4"                  | 10      | "–10a"
        "B45"                      | "B"                    | 45      | null
        "B4 5"                     | "B4"                   | 5       | null
        "D 4"                      | "D 4"                  | null    | null
        "D 4 3"                    | "D 4"                  | 3       | null
        "D 4 3 8"                  | "D 4"                  | 3       | "8"
        "D 4 3b"                   | "D 4"                  | 3       | "b"
        "D 4 3 8b"                 | "D 4"                  | 3       | "8b"
        "D 4, 3"                   | "D 4"                  | 3       | null
        "D4"                       | "D4"                   | null    | null
        "D4 31"                    | "D4"                   | 31      | null
        "D43 1"                    | "D"                    | 43      | "1"
        "D431"                     | "D"                    | 431     | null
        "D,431"                    | "D"                    | 431     | null
        "D+431"                    | "D"                    | 431     | null
        "D-431"                    | "D"                    | 431     | null
        "D.431"                    | "D."                   | 431     | null
        "D4, 3"                    | "D4"                   | 3       | null
        "D4, Nr. 3"                | "D4"                   | 3       | null
        "D4, Nr.3"                 | "D4"                   | 3       | null
        "D4, Nr 3"                 | "D4"                   | 3       | null
        "D4, Nr3"                  | "D4"                   | 3       | null
        "Bundesstraße 1 25 1/3"    | "Bundesstraße 1"       | 25      | "1/3"
        "Bundesstraße 2 Nr. 25"    | "Bundesstraße 2"       | 25      | null
        "Bundesstraße 2 Nr. 25a"   | "Bundesstraße 2"       | 25      | "a"
        "Bundesstr 2 Nr. 25a"      | "Bundesstr 2"          | 25      | "a"
        "Bundesstr2 Nr.25a"        | "Bundesstr2"           | 25      | "a"
        "Bundesstr.2 Nr."          | "Bundesstr.2 Nr."      | null    | null
        "Bundesstr. 2 Nr.0"        | "Bundesstr. 2"         | 0       | null
        "Bundesstr. 2 Nr.O"        | "Bundesstr. 2 Nr.O"    | null    | null
        "Straße 73 5a"             | "Straße 73"            | 5       | "a"
        "Straße 73"                | "Straße 73"            | null    | null
        "Stra ße 73 5a"            | "Stra ße"              | 73      | "5a"
        "Str. 73 5a"               | "Str. 73"              | 5       | "a"
        "Strasse73 5a"             | "Strasse"              | 73      | "5a"
        "Gartenstr. 25a"           | "Gartenstr."           | 25      | "a"
        "Allertshäuser Straße 25a" | "Allertshäuser Straße" | 25      | "a"
        "Gartenstr. a"             | "Gartenstr. a"         | null    | null
        "Heideweg 3-5"             | "Heideweg"             | 3       | "-5"
        "Heideweg 3 -52"           | "Heideweg"             | 3       | "-52"
        "Heideweg 32- 5"           | "Heideweg"             | 32      | "- 5"
        "Heideweg 32 - 5"          | "Heideweg"             | 32      | "- 5"
        "Heideweg 32/5"            | "Heideweg"             | 32      | "/5"
        "Heideweg 32 /5"           | "Heideweg"             | 32      | "/5"
        "Heideweg 32 / 5"          | "Heideweg"             | 32      | "/ 5"
        "1 Maja 3A"                | "1 Maja"               | 3       | "A"
        "1. Maja 3a-d"             | "1. Maja"              | 3       | "a-d"
        "1Maja 34 a-d"             | "1Maja"                | 34      | "a-d"
        "1-Maja 34a-d"             | "1-Maja"               | 34      | "a-d"
        "2 Willi 3A"               | "2 Willi"              | 3       | "A"
        "Badstr. 34 am 3. Schafott"| "Badstr."              | 34      | "am 3. Schafott"
        "Badstr.34 am 3. Schafott" | "Badstr."              | 34      | "am 3. Schafott"
        "Badstr34 am 3. Schafott"  | "Badstr"               | 34      | "am 3. Schafott"
        "Kerberweg 25-1/3"         | "Kerberweg"            | 25      | "-1/3"
        "1. Unbekannte Str. 5"     | "1. Unbekannte Str."   | 5       | null
        "Unbekannt 1 Zusatz 5"     | "Unbekannt"            | 1       | "Zusatz 5"
        "Straße Nr. 12"            | "Straße Nr. 12"        | null    | null
        "Straße Nr.12"             | "Straße Nr.12"         | null    | null
        "Straße Nr12"              | "Straße Nr12"          | null    | null
        "Straße Nr. 123"           | "Straße Nr."           | 123     | null
        "Straße Nr. 12 3"          | "Straße Nr. 12"        | 3       | null
        "Straße Nr12 3"            | "Straße Nr12"          | 3       | null
        "Straße Nr. 13"            | "Straße Nr."           | 13      | null
        "Straße Nr. 1 3"           | "Straße Nr. 1"         | 3       | null
        "Straße Nr.12b"            | "Straße Nr.12b"        | null    | null
        "Straße Nr. 12b4"          | "Straße Nr. 12b"       | 4       | null
        "Straße Nr.12b4c"          | "Straße Nr.12b"        | 4       | "c"
        "Straße 101 2"             | "Straße 101"           | 2       | null
        "Straße 10 12"             | "Straße 10"            | 12      | null
        "Str. 10 12"               | "Str. 10"              | 12      | null
        "Straßenbahnweg 12"        | "Straßenbahnweg"       | 12      | null
        "P111 bei 2"               | "P111 bei 2"           | null    | null
        "P111 2"                   | "P111"                 | 2       | null
        "P 1112"                   | "P"                    | 1112    | null
        "P1112c"                   | "P"                    | 1112    | "c"
        "P1112 4"                  | "P"                    | 1112    | "4"
        "Düne"                     | "Düne"                 | null    | null
        "Dünê5"                    | "Dünê5"                | null    | null
        "Dünè 5"                   | "Dünè 5"               | null    | null
        "Duené5"                   | "Duené5"               | null    | null
        "Dunê5"                    | "Dunê"                 | 5       | null
        "Åbjerg 17423 ZZ"          | "Åbjerg"               | 17423   | "ZZ"
    }

}
