package com.mauro.habittracker

import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.core.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeHabitRepository : HabitRepository {

    var shouldThrowError = false

    private val habits = MutableStateFlow<List<Habit>>(emptyList())

    override fun getHabits(): Flow<List<Habit>> {
        return habits
    }

    override suspend fun insertHabit(habit: Habit) {
        if (shouldThrowError) {
            throw RuntimeException("Fake error")
        }

        habits.value = habits.value + habit
    }

    override suspend fun deleteHabit(habit: Habit) {
        habits.value = habits.value - habit
    }

    override fun getLogsForHabit(habitId: Long): Flow<List<HabitLog>> {
        return flowOf(emptyList())
    }

    override suspend fun insertLog(log: HabitLog) {
        // no-op para tests
    }
}