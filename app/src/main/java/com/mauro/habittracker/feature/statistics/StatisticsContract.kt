package com.mauro.habittracker.feature.statistics

import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.model.HabitLog

data class HabitStats(
    val habit: Habit,
    val completionCount: Int
)

data class StatisticsState(
    val habitStats: List<HabitStats> = emptyList(),
    val totalHabits: Int = 0,
    val totalCompletions: Int = 0,
    val isLoading: Boolean = false
)

sealed class StatisticsIntent {
    data object LoadStats : StatisticsIntent()
}

sealed class StatisticsEffect {
    data class ShowError(val message: String) : StatisticsEffect()
}
