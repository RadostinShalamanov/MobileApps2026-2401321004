package bg.uni_plovdiv.fmi.plantcare

import bg.uni_plovdiv.fmi.plantcare.domain.WateringCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.TimeZone

class WateringCalculatorTest {

    @Test
    fun nextWateringDay_addsInterval() {
        assertEquals(107L, WateringCalculator.nextWateringDay(100L, 7))
    }

    @Test
    fun daysUntilWatering_positiveBeforeDue() {
        assertEquals(3L, WateringCalculator.daysUntilWatering(100L, 7, today = 104L))
    }

    @Test
    fun daysUntilWatering_negativeWhenOverdue() {
        assertEquals(-2L, WateringCalculator.daysUntilWatering(100L, 7, today = 109L))
    }

    @Test
    fun needsWater_trueOnDueDayAndAfter() {
        assertTrue(WateringCalculator.needsWater(100L, 7, today = 107L))
        assertTrue(WateringCalculator.needsWater(100L, 7, today = 120L))
    }

    @Test
    fun needsWater_falseBeforeDue() {
        assertFalse(WateringCalculator.needsWater(100L, 7, today = 106L))
    }

    @Test
    fun todayEpochDay_epochStartIsDayZero() {
        assertEquals(0L, WateringCalculator.todayEpochDay(0L, TimeZone.getTimeZone("UTC")))
    }

    @Test
    fun todayEpochDay_respectsTimeZoneOffset() {
        // 23:00 UTC на 1.1.1970 е вече 2.1.1970 в UTC+2.
        val millis = 23L * 3_600_000L
        assertEquals(0L, WateringCalculator.todayEpochDay(millis, TimeZone.getTimeZone("UTC")))
        assertEquals(1L, WateringCalculator.todayEpochDay(millis, TimeZone.getTimeZone("GMT+2")))
    }
}