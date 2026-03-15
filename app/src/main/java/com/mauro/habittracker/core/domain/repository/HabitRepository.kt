package com.mauro.habittracker.core.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.model.HabitLog
import kotlinx.coroutines.flow.Flow
@RequiresApi(Build.VERSION_CODES.O)
interface HabitRepository{
    fun getHabits() : Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    fun getLogsForHabit(habitId: Long) : Flow<List<HabitLog>>
    suspend fun insertLog(log: HabitLog)
}