package com.mauro.habittracker.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.mauro.habittracker.core.domain.model.Frequency
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.data.local.entity.HabitEntity
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = this.id,
        name = this.name,
        description = this.description,
        frequency = Frequency.valueOf(this.frequency),
        isActive = this.isActive,
        createdAt = LocalDate.ofEpochDay(this.createdAt),
        reminderTime = this.reminderTime?.let { LocalTime.parse(it) }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        frequency = this.frequency.name,
        isActive = this.isActive,
        createdAt = this.createdAt.toEpochDay(),
        reminderTime = this.reminderTime?.toString()
    )
}