package com.mauro.habittracker.data.local.typeconverter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.mauro.habittracker.core.domain.model.Frequency
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class DateConverters {

    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun fromFrequency(value: Frequency?): String? {
        return value?.name
    }

    @TypeConverter
    fun toFrequency(value: String?): Frequency? {
        return value?.let { Frequency.valueOf(it) }
    }


}
