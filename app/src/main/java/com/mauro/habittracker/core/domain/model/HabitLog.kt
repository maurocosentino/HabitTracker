package com.mauro.habittracker.core.domain.model

import java.time.LocalDate

data class HabitLog(
    val id: Long = 0L,
    val habitId: Long,
    val completedDate: LocalDate
)