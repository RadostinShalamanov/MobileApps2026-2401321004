package bg.uni_plovdiv.fmi.plantcare.domain

import java.util.TimeZone


object WateringCalculator {
    private const val MILLIS_PER_DAY = 86_400_000L

    fun todayEpochDay(
        nowMillis: Long = System.currentTimeMillis(),
        timeZone: TimeZone = TimeZone.getDefault()
    ): Long = Math.floorDiv(nowMillis + timeZone.getOffset(nowMillis), MILLIS_PER_DAY)

    fun nextWateringDay(lastWateredDay: Long, intervalDays: Int): Long =
        lastWateredDay + intervalDays

    fun daysUntilWatering(lastWateredDay: Long, intervalDays: Int, today: Long): Long =
        nextWateringDay(lastWateredDay, intervalDays) - today

    fun needsWater(lastWateredDay: Long, intervalDays: Int, today: Long): Boolean =
        daysUntilWatering(lastWateredDay, intervalDays, today) <= 0
}