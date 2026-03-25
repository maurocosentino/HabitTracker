package com.mauro.habittracker.core.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Habit(
    val id: Long = 0L,
    val name: String,
    val description: String,
    val frequency: Frequency,
    val reminderTime: LocalTime?,
    val isActive: Boolean,
    val createdAt: LocalDate
)
