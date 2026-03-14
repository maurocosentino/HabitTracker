package com.mauro.habittracker.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val frequency: String,
    val reminderTime: String?,
    val isActive : Boolean,
    val createdAt : Long
)