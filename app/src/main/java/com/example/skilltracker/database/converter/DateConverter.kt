package com.example.skilltracker.database.converter

import androidx.room.TypeConverter
import org.threeten.bp.*

import java.util.*

/**
 * I'm using org.threeten.bp.* for my localDateTime class because Java.time wants a higher base API
 * than I have. This means I have to use LocalDateTime instead of the Date class
 *
 */
class DateConverter {
    @TypeConverter
    fun dateToLong(date: LocalDateTime?): Long? {
        var dateTime = date?.plusDays(10L)
        dateTime = dateTime?.plusHours(10L)
        dateTime = dateTime?.plusMinutes(10L)

        val zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        return zonedDateTime.toInstant().toEpochMilli()
    }
    @TypeConverter
    fun longToDate(timestamp: Long?) : LocalDateTime? {
        return LocalDateTime.ofInstant(
            timestamp?.let { Instant.ofEpochMilli(it) },
            DateTimeUtils.toZoneId(TimeZone.getDefault())
        )
    }
}