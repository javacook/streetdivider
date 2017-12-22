package de.kotlincook.textmining.streetdivider

import org.junit.Test
import kotlin.math.absoluteValue


class StreetDividerTest {
    
    @Test
    fun checkRandomlyCreatedStreetnamesForExceptions() {
        val startTime = System.currentTimeMillis()
        val streetDivider = StreetDivider()
        for (cnt in 0..100){
            for (len in 0..50) {
                var input = ""
                for (i in 0 until len) {
                    val zahl: Int = (Math.random() * 256).toInt()
                    input += (zahl.absoluteValue % 256).toChar();
                }
                streetDivider.parse(input)
            }
        }
        val endTime = System.currentTimeMillis()
        println("Execution time ${endTime - startTime} ms")
    }

}