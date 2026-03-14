package com.mauro.habittracker.core.domain.usecase

import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.repository.HabitRepository

class InsertHabitUseCase(
    private val repository : HabitRepository
){
    suspend operator fun invoke(habit: Habit){
        repository.insertHabit(habit)
    }
}