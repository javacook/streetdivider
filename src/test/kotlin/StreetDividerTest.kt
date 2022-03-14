package de.kotlincook.textmining.streetdivider

import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

class StreetDividerTest {

    @Test
    fun checkRandomlyCreatedStreetnamesForExceptions() {
        val time = measureTimeMillis {
            val streetDivider = StreetDivider()
            for (cnt in 0..100) {
                for (len in 0..50) {
                    var input = ""
                    for (i in 0 until len) {
                        val zahl: Int = (Math.random() * 256).toInt()
                        input += (zahl.absoluteValue % 256).toChar()
                    }
                    streetDivider.parse(input)
                }
            }
        }
        println("Execution time $time ms")
    }

    data class StreetTestCase(val input: String,
                              val street: String,
                              val houseNo: Int?,
                              val affix: String?)

    @Test
    fun checkSelectedStreets() {
        val streetDivider = StreetDivider(
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
            "Düne 5"
        )

        listOf(
            StreetTestCase("A"                        , "A"                    , null    , null),
            StreetTestCase("5"                        , "5"                    , null    , null),
            StreetTestCase("143"                      , "143"                  , null    , null),
            StreetTestCase("5001 44"                  , "5001"                 , 44      , null),
            StreetTestCase("374, 4"                   , "374"                  , 4       , null),
            StreetTestCase("B 4 10–10a"               , "B 4"                  , 10      , "–10a"),
            StreetTestCase("B45"                      , "B"                    , 45      , null),
            StreetTestCase("B4 5"                     , "B4"                   , 5       , null),
            StreetTestCase("D 4"                      , "D 4"                  , null    , null),
            StreetTestCase("D 4 3"                    , "D 4"                  , 3       , null),
            StreetTestCase("D 4 3 8"                  , "D 4"                  , 3       , "8"),
            StreetTestCase("D 4 3b"                   , "D 4"                  , 3       , "b"),
            StreetTestCase("D 4 3 8b"                 , "D 4"                  , 3       , "8b"),
            StreetTestCase("D 4, 3"                   , "D 4"                  , 3       , null),
            StreetTestCase("D4"                       , "D4"                   , null    , null),
            StreetTestCase("D4 31"                    , "D4"                   , 31      , null),
            StreetTestCase("D43 1"                    , "D"                    , 43      , "1"),
            StreetTestCase("D431"                     , "D"                    , 431     , null),
            StreetTestCase("D,431"                    , "D"                    , 431     , null),
            StreetTestCase("D+431"                    , "D"                    , 431     , null),
            StreetTestCase("D-431"                    , "D"                    , 431     , null),
            StreetTestCase("D.431"                    , "D."                   , 431     , null),
            StreetTestCase("D4, 3"                    , "D4"                   , 3       , null),
            StreetTestCase("D4, Nr. 3"                , "D4"                   , 3       , null),
            StreetTestCase("D4, Nr.3"                 , "D4"                   , 3       , null),
            StreetTestCase("D4, Nr 3"                 , "D4"                   , 3       , null),
            StreetTestCase("D4, Nr3"                  , "D4"                   , 3       , null),
            StreetTestCase("Bundesstraße 1 25 1/3"    , "Bundesstraße 1"       , 25      , "1/3"),
            StreetTestCase("Bundesstraße 2 Nr. 25"    , "Bundesstraße 2"       , 25      , null),
            StreetTestCase("Bundesstraße 2 Nr. 25a"   , "Bundesstraße 2"       , 25      , "a"),
            StreetTestCase("Bundesstr 2 Nr. 25a"      , "Bundesstr 2"          , 25      , "a"),
            StreetTestCase("Bundesstr2 Nr.25a"        , "Bundesstr2"           , 25      , "a"),
            StreetTestCase("Bundesstr.2 Nr."          , "Bundesstr.2 Nr."      , null    , null),
            StreetTestCase("Bundesstr. 2 Nr.0"        , "Bundesstr. 2"         , 0       , null),
            StreetTestCase("Bundesstr. 2 Nr.O"        , "Bundesstr. 2 Nr.O"    , null    , null),
            StreetTestCase("Straße 73 5a"             , "Straße 73"            , 5       , "a"),
            StreetTestCase("Straße 73"                , "Straße 73"            , null    , null),
            StreetTestCase("Stra ße 73 5a"            , "Stra ße"              , 73      , "5a"),
            StreetTestCase("Str. 73 5a"               , "Str. 73"              , 5       , "a"),
            StreetTestCase("Strasse73 5a"             , "Strasse"              , 73      , "5a"),
            StreetTestCase("Gartenstr. 25a"           , "Gartenstr."           , 25      , "a"),
            StreetTestCase("Allertshäuser Straße 25a" , "Allertshäuser Straße" , 25      , "a"),
            StreetTestCase("Gartenstr. a"             , "Gartenstr. a"         , null    , null),
            StreetTestCase("Heideweg 3-5"             , "Heideweg"             , 3       , "-5"),
            StreetTestCase("Heideweg 3 -52"           , "Heideweg"             , 3       , "-52"),
            StreetTestCase("Heideweg 32- 5"           , "Heideweg"             , 32      , "- 5"),
            StreetTestCase("Heideweg 32 - 5"          , "Heideweg"             , 32      , "- 5"),
            StreetTestCase("Heideweg 32/5"            , "Heideweg"             , 32      , "/5"),
            StreetTestCase("Heideweg 32 /5"           , "Heideweg"             , 32      , "/5"),
            StreetTestCase("Heideweg 32 / 5"          , "Heideweg"             , 32      , "/ 5"),
            StreetTestCase("1 Maja 3A"                , "1 Maja"               , 3       , "A"),
            StreetTestCase("1. Maja 3a-d"             , "1. Maja"              , 3       , "a-d"),
            StreetTestCase("1Maja 34 a-d"             , "1Maja"                , 34      , "a-d"),
            StreetTestCase("1-Maja 34a-d"             , "1-Maja"               , 34      , "a-d"),
            StreetTestCase("2 Willi 3A"               , "2 Willi"              , 3       , "A"),
            StreetTestCase("Badstr. 34 am 3. Schafott", "Badstr."              , 34      , "am 3. Schafott"),
            StreetTestCase("Badstr.34 am 3. Schafott" , "Badstr."              , 34      , "am 3. Schafott"),
            StreetTestCase("Badstr34 am 3. Schafott"  , "Badstr"               , 34      , "am 3. Schafott"),
            StreetTestCase("Kerberweg 25-1/3"         , "Kerberweg"            , 25      , "-1/3"),
            StreetTestCase("1. Unbekannte Str. 5"     , "1. Unbekannte Str."   , 5       , null),
            StreetTestCase("Unbekannt 1 Zusatz 5"     , "Unbekannt"            , 1       , "Zusatz 5"),
            StreetTestCase("Straße Nr. 12"            , "Straße Nr. 12"        , null    , null),
            StreetTestCase("Straße Nr.12"             , "Straße Nr.12"         , null    , null),
            StreetTestCase("Straße Nr12"              , "Straße Nr12"          , null    , null),
            StreetTestCase("Straße Nr. 123"           , "Straße Nr."           , 123     , null),
            StreetTestCase("Straße Nr. 12 3"          , "Straße Nr. 12"        , 3       , null),
            StreetTestCase("Straße Nr12 3"            , "Straße Nr12"          , 3       , null),
            StreetTestCase("Straße Nr. 13"            , "Straße Nr."           , 13      , null),
            StreetTestCase("Straße Nr. 1 3"           , "Straße Nr. 1"         , 3       , null),
            StreetTestCase("Straße Nr.12b"            , "Straße Nr.12b"        , null    , null),
            StreetTestCase("Straße Nr. 12b4"          , "Straße Nr. 12b"       , 4       , null),
            StreetTestCase("Straße Nr.12b4c"          , "Straße Nr.12b"        , 4       , "c"),
            StreetTestCase("Straße 101 2"             , "Straße 101"           , 2       , null),
            StreetTestCase("Straße 10 12"             , "Straße 10"            , 12      , null),
            StreetTestCase("Str. 10 12"               , "Str. 10"              , 12      , null),
            StreetTestCase("Straßenbahnweg 12"        , "Straßenbahnweg"       , 12      , null),
            StreetTestCase("P111 bei 2"               , "P111 bei 2"           , null    , null),
            StreetTestCase("P111 2"                   , "P111"                 , 2       , null),
            StreetTestCase("P 1112"                   , "P"                    , 1112    , null),
            StreetTestCase("P1112c"                   , "P"                    , 1112    , "c"),
            StreetTestCase("P1112 4"                  , "P"                    , 1112    , "4"),
            StreetTestCase("Düne"                     , "Düne"                 , null    , null),
            StreetTestCase("Dünê5"                    , "Dünê5"                , null    , null),
            StreetTestCase("Dünè 5"                   , "Dünè 5"               , null    , null),
            StreetTestCase("Duené5"                   , "Duené5"               , null    , null),
            StreetTestCase("Dunê5"                    , "Dunê"                 , 5       , null),
            StreetTestCase("Åbjerg 17423 ZZ"          , "Åbjerg"               , 17423   , "ZZ")
        ).forEach {
            assertEquals(Location(it.street, it.houseNo, it.affix), streetDivider.parse(it.input))
            assertEquals(Location(it.street, it.houseNo, it.affix), streetDivider.parse(" ${it.input} "))
        }
    }
}