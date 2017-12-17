import org.junit.Test
import kotlin.test.assertEquals


val SPECIAL_STREETS = Dictionary("bundesstraße1", "bundesstraße2", "straße6", "straße73" ,"1maja",
        "a1", "a2", "a3", "a5",
        "b1", "b2", "b4", "b5", "b6", "b7",
        "c1", "c2", "c3", "c4", "c7", "c8",
        "d1", "d2", "d3", "d4", "d5", "d6", "d7",
        "e1", "e3", "e4",
        "f1", "f2", "f3", "f4", "f5", "f6", "f7",
        "g1", "g2", "g3", "g4", "g6", "g7",
        "h1", "h2", "h5", "h7",
        "i1", "i3", "i6",
        "j7",
        "k1", "k2", "k3", "k4", "k5",
        "l1", "l2", "l4", "l5", "l6", "l7", "l8", "l9", "l10", "l11", "l12", "l13", "l14", "l15",
        "m1", "m2", "m3", "m4", "m5", "m6", "m7",
        "n1", "n2", "n3", "n4", "n5", "n6", "n7",
        "o1", "o3", "o4", "o5", "o6", "o7",
        "p1", "p2", "p3", "p4", "p5", "p6", "p7",
        "q1", "q2", "q3", "q4", "q5", "q7",
        "r1", "r3", "r4", "r5", "r6", "r7",
        "s1", "s4", "s5", "s6",
        "t1", "t2", "t3", "t6",
        "t1", "t2", "t4", "t5", "t6",
        "u1", "u2", "u4", "u5", "u6",
        "straßedes17juni", "straßedes18oktober")


class StreetDividerTest {
    @Test
    fun testNummernstrasseMitBruchzahl() {
        val actual = StreetDivider().parse("Bundesstraße 1 25 1/3")
        val expected = Location("Bundesstraße 1", 25, "1/3")
        assertEquals(actual, expected)
    }

    @Test
    fun testNummernstrasseNrOhneZusatz() {
        val actual = StreetDivider().parse("Bundesstraße 2 Nr. 25")
        val expected = Location("Bundesstraße 2", 25, null)
        assertEquals(actual, expected)
    }

    @Test
    fun testNummernstrasseNrMitEinfachzusatz() {
        val actual = StreetDivider().parse("Bundesstraße 2 Nr. 25a")
        val expected = Location("Bundesstraße 2", 25, "a")
        assertEquals(actual, expected)
    }

    @Test
    fun testNummernstrasseMitEinfachzusatz() {
        val actual = StreetDivider().parse("Straße 73 5a")
        val expected = Location("Straße 73", 5, "a")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitHausnummerOhneZusatz() {
        val actual = StreetDivider().parse("Gartenstr. 25")
        val expected = Location("Gartenstr.", 25, null)
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitHausnummerUndEinfachzusatz() {
        val actual = StreetDivider().parse("Allertshäuser Straße 25a")
        val expected = Location("Allertshäuser Straße", 25, "a")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitEinfachusatz() {
        val actual = StreetDivider().parse("Gartenstr. a")
        val expected = Location("Gartenstr. a", null, null)
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz1a() {
        val actual = StreetDivider().parse("Heideweg 3-5")
        val expected = Location("Heideweg", 3, "-5")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz1b() {
        val actual = StreetDivider().parse("Heideweg 3 -52")
        val expected = Location("Heideweg", 3, "-52")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz1c() {
        val actual = StreetDivider().parse("Heideweg 32- 5")
        val expected = Location("Heideweg", 32, "- 5")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz1d() {
        val actual = StreetDivider().parse("Heideweg 32 - 5")
        val expected = Location("Heideweg", 32, "- 5")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz2a() {
        val actual = StreetDivider().parse("Heideweg 32/5")
        val expected = Location("Heideweg", 32, "/5")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz2b() {
        val actual = StreetDivider().parse("Heideweg 32 /5")
        val expected = Location("Heideweg", 32, "/5")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz2c() {
        val actual = StreetDivider().parse("Heideweg 32/ 5")
        val expected = Location("Heideweg", 32, "/ 5")
        assertEquals(actual, expected)
    }

    @Test
    fun testStandardStrasseMitZusatz2d() {
        val actual = StreetDivider().parse("Heideweg 32 / 5")
        val expected = Location("Heideweg", 32, "/ 5")
        assertEquals(actual, expected)
    }

    @Test
    fun testZahlenstrasseMitZusatz1() {
        val actual = StreetDivider().parse("1 Maja 3A")
        val expected = Location("1 Maja", 3, "A")
        assertEquals(actual, expected)
    }

    @Test
    fun testZahlenstrasseMitZusatz2() {
        val actual = StreetDivider().parse("1 Maja 3a-d")
        val expected = Location("1 Maja", 3, "a-d")
        assertEquals(actual, expected)
    }

    @Test
    fun testZahlenstrasseMitZusatz3() {
        val actual = StreetDivider().parse("1 Maja 34 a-d")
        val expected = Location("1 Maja", 34, "a-d")
        assertEquals(actual, expected)
    }

    @Test
    fun testStdstrasseMitZusat4() {
        val actual = StreetDivider().parse("Hellersbergstr. 34 am 3. Schafott")
        val expected = Location("Hellersbergstr.", 34, "am 3. Schafott")
        assertEquals(actual, expected)
    }

    @Test
    fun testSonderfall1() {
        val actual = StreetDivider().parse("Kerberweg 25-1/3")
        val expected = Location("Kerberweg", 25, "-1/3")
        assertEquals(actual, expected)
    }

    @Test
    fun testUnbekannt1() {
        val actual = StreetDivider().parse("1. Unbekannt 5")
        val expected = Location("1. Unbekannt 5", null, null)
        assertEquals(actual, expected)
    }

    @Test
    fun testUnbekannt2() {
        val actual = StreetDivider().parse("Unbekannt 1 Unbekannt 5")
        val expected = Location("Unbekannt", 1, "Unbekannt 5")
        assertEquals(actual, expected)
    }

    @Test
    fun testD4() {
        val actual = StreetDivider().parse("D 4, 3")
        val expected = Location("D 4", 3, null)
        assertEquals(actual, expected)
    }

    @Test
    fun testB41010a() {
        val actual = StreetDivider().parse("B 4 10–10a")
        val expected = Location("B 4", 10, "–10a")
        assertEquals(actual, expected)
    }

}