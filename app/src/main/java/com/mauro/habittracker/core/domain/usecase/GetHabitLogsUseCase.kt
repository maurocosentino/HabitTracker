package com.mauro.habittracker.core.domain.usecase

import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.core.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow

class GetHabitLogsUseCase(
    private val repository: HabitRepository
){
    operator fun invoke(habitId: Long) : Flow<List<HabitLog>> {
        return repository.getLogsForHabit(habitId)
    }
}