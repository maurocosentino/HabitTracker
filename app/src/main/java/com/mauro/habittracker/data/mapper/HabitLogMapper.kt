package com.mauro.habittracker.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.data.local.entity.HabitLogEntity
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun HabitLogEntity.toDomain(): HabitLog {
    return HabitLog(
        id = this.id,
        habitId = this.habitId,
        completedDate = LocalDate.ofEpochDay(this.completedDate)
    )
}
@RequiresApi(Build.VERSION_CODES.O)
fun HabitLog.toEntity(): HabitLogEntity {
    return HabitLogEntity(
        id = this.id,
        habitId = this.habitId,
        completedDate = this.completedDate.toEpochDay()
    )
}