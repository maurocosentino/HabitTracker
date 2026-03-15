package com.mauro.habittracker.feature.habits

import com.mauro.habittracker.core.domain.model.Habit

data class HabitState(
    val habits: List<Habit> = emptyList(),
    val isLoading: Boolean = false
)

sealed class HabitIntent {

    object LoadHabits : HabitIntent()

    data class AddHabit(val habit: Habit) : HabitIntent()

    data class DeleteHabit(val habit: Habit) : HabitIntent()

    data class CompleteHabit(val habitId: Long) : HabitIntent()
}

sealed class HabitEffect {

    data class ShowError(val message: String) : HabitEffect()

    object NavigateToCreateHabit : HabitEffect()
}