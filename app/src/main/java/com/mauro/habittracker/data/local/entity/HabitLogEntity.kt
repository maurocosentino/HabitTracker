package com.mauro.habittracker.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.ForeignKey.Companion.CASCADE
import androidx.room3.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = CASCADE
        )
    ]
)
data class HabitLogEntity(
    @PrimaryKey val id: Long = 0,
    val habitId: Long,
    val completedDate : Long
)