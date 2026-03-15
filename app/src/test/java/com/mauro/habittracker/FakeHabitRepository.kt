package com.mauro.habittracker

import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.core.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeHabitRepository : HabitRepository {

    private val habits = mutableListOf<Habit>()

    private val flow = MutableStateFlow<List<Habit>>(emptyList())

    override fun getHabits(): Flow<List<Habit>> {
        return flow
    }

    override suspend fun insertHabit(habit: Habit) {
        habits.add(habit)
        flow.value = habits.toList()
    }

    override suspend fun deleteHabit(habit: Habit) {
        habits.remove(habit)
        flow.value = habits.toList()
    }

    override fun getLogsForHabit(habitId: Long): Flow<List<HabitLog>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLog(log: HabitLog) {
        TODO("Not yet implemented")
    }
}