package de.kotlincook.textmining.streetdivider

import org.junit.Test
import org.junit.Assert.assertEquals


class StreetDividerTest {
    
    val streetDivider = StreetDivider(
            "Bundesstraße1", "Bundesstr. 2", "Straße73" ,"1 Maja",
            "b4", "d4", "Straße des 18. Oktober", "Straße 10", "Str 101")

    @Test
    fun testNummernstrasseMitBruchzahl() {
        val actual = streetDivider.parse("Bundesstraße 1 25 1/3")
        val expected = Location("Bundesstraße 1", 25, "1/3")
        assertEquals(expected, actual)
    }

    @Test
    fun testNummernstrasseNrOhneZusatz() {
        val actual = streetDivider.parse("Bundesstraße 2 Nr. 25")
        val expected = Location("Bundesstraße 2", 25)
        assertEquals(expected, actual)
    }

    @Test
    fun testNummernstrasseNrMitEinfachzusatz() {
        val actual = streetDivider.parse("Bundesstraße 2 Nr. 25a")
        val expected = Location("Bundesstraße 2", 25, "a")
        assertEquals(expected, actual)
    }

    @Test
    fun testNummernstrasseMitEinfachzusatz() {
        val actual = streetDivider.parse("Straße 73 5a")
        val expected = Location("Straße 73", 5, "a")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitHausnummerOhneZusatz() {
        val actual = streetDivider.parse("Gartenstr. 25")
        val expected = Location("Gartenstr.", 25, null)
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitHausnummerUndEinfachzusatz() {
        val actual = streetDivider.parse("Allertshäuser Straße 25a")
        val expected = Location("Allertshäuser Straße", 25, "a")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitEinfachusatz() {
        val actual = streetDivider.parse("Gartenstr. a")
        val expected = Location("Gartenstr. a")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz1a() {
        val actual = streetDivider.parse("Heideweg 3-5")
        val expected = Location("Heideweg", 3, "-5")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz1b() {
        val actual = streetDivider.parse("Heideweg 3 -52")
        val expected = Location("Heideweg", 3, "-52")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz1c() {
        val actual = streetDivider.parse("Heideweg 32- 5")
        val expected = Location("Heideweg", 32, "- 5")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz1d() {
        val actual = streetDivider.parse("Heideweg 32 - 5")
        val expected = Location("Heideweg", 32, "- 5")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz2a() {
        val actual = streetDivider.parse("Heideweg 32/5")
        val expected = Location("Heideweg", 32, "/5")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz2b() {
        val actual = streetDivider.parse("Heideweg 32 /5")
        val expected = Location("Heideweg", 32, "/5")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz2c() {
        val actual = streetDivider.parse("Heideweg 32/ 5")
        val expected = Location("Heideweg", 32, "/ 5")
        assertEquals(expected, actual)
    }

    @Test
    fun testStandardStrasseMitZusatz2d() {
        val actual = streetDivider.parse("Heideweg 32 / 5")
        val expected = Location("Heideweg", 32, "/ 5")
        assertEquals(expected, actual)
    }

    @Test
    fun testZahlenstrasseMitZusatz1a() {
        val actual = streetDivider.parse("1 Maja 3A")
        val expected = Location("1 Maja", 3, "A")
        assertEquals(expected, actual)
    }

    @Test
    fun testZahlenstrasseMitZusatz2() {
        val actual = streetDivider.parse("1. Maja 3a-d")
        val expected = Location("1. Maja", 3, "a-d")
        assertEquals(expected, actual)
    }

    @Test
    fun testZahlenstrasseMitZusatz3() {
        val actual = streetDivider.parse("1Maja 34 a-d")
        val expected = Location("1Maja", 34, "a-d")
        assertEquals(expected, actual)
    }

    @Test
    fun testZahlenstrasseMitZusatz4() {
        val actual = streetDivider.parse("1-Maja 34a-d")
        val expected = Location("1-Maja", 34, "a-d")
        assertEquals(expected, actual)
    }

    @Test
    fun testStdstrasseMitZusat4() {
        val actual = streetDivider.parse("Hellersbergstr. 34 am 3. Schafott")
        val expected = Location("Hellersbergstr.", 34, "am 3. Schafott")
        assertEquals(expected, actual)
    }

    @Test
    fun testSonderfall1() {
        val actual = streetDivider.parse("Kerberweg 25-1/3")
        val expected = Location("Kerberweg", 25, "-1/3")
        assertEquals(expected, actual)
    }

    @Test
    fun testUnbekannt1() {
        val actual = streetDivider.parse("1. Unbekannte Str. 5")
        val expected = Location("1. Unbekannte Str.", 5)
        assertEquals(expected, actual)
    }

    @Test
    fun testUnbekannt2() {
        val actual = streetDivider.parse("Unbekannt 1 Zusatz 5")
        val expected = Location("Unbekannt", 1, "Zusatz 5")
        assertEquals(expected, actual)
    }

    @Test
    fun testD_4() {
        val actual = streetDivider.parse("D 4, 3")
        val expected = Location("D 4", 3)
        assertEquals(expected, actual)
    }

    @Test
    fun testD43() {
        val actual = streetDivider.parse("D4, 3")
        val expected = Location("D4", 3)
        assertEquals(expected, actual)
    }

    @Test
    fun testD4NrDot_3() {
        val actual = streetDivider.parse("D4, Nr. 3")
        val expected = Location("D4", 3)
        assertEquals(expected, actual)
    }

    @Test
    fun testD4NrDot3() {
        val actual = streetDivider.parse("D4, Nr.3")
        val expected = Location("D4", 3)
        assertEquals(expected, actual)
    }

    @Test
    fun testD4Nr_3() {
        val actual = streetDivider.parse("D4, Nr 3")
        val expected = Location("D4", 3)
        assertEquals(expected, actual)
    }

    @Test
    fun testD4Nr3() {
        val actual = streetDivider.parse("D4, Nr3")
        val expected = Location("D4", 3)
        assertEquals(expected, actual)
    }

    @Test
    fun testB41010a() {
        val actual = streetDivider.parse("B 4 10–10a")
        val expected = Location("B 4", 10, "–10a")
        assertEquals(expected, actual)
    }

    @Test
    fun testB45() {
        val actual = streetDivider.parse("B45")
        val expected = Location("B", 45)
        assertEquals(expected, actual)
    }

    @Test
    fun testB4_5() {
        val actual = streetDivider.parse("B4 5")
        val expected = Location("B4", 5)
        assertEquals(expected, actual)
    }

    @Test
    fun testNummerstrMitKomma() {
        val actual = streetDivider.parse("374, 4")
        val expected = Location("374", 4)
        assertEquals(expected, actual)
    }

    @Test
    fun testNummerStrasseSpezialfall101_2() {
        val actual = streetDivider.parse("Straße 101 2")
        val expected = Location("Straße 101", 2)
        assertEquals(expected, actual)
    }

    @Test
    fun testNummerStrasseSpezialfall10_12() {
        val actual = streetDivider.parse("Straße 10 12")
        val expected = Location("Straße 10", 12)
        assertEquals(expected, actual)
    }

    @Test
    fun testStraßennormierung() {
        val actual = streetDivider.parse("Str. 10 12")
        val expected = Location("Str. 10", 12)
        assertEquals(expected, actual)
    }

    @Test
    fun testStraßennormierungStraßenbahn() {
        val actual = streetDivider.parse("Straßenbahnweg 12")
        val expected = Location("Straßenbahnweg", 12)
        assertEquals(expected, actual)
    }

}