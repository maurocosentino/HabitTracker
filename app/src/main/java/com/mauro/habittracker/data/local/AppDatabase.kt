package com.mauro.habittracker.data.local

import androidx.room.RoomDatabase
import androidx.room3.Database
import com.mauro.habittracker.data.local.dao.HabitDao
import com.mauro.habittracker.data.local.dao.HabitLogDao
import com.mauro.habittracker.data.local.entity.HabitEntity
import com.mauro.habittracker.data.local.entity.HabitLogEntity

@Database(entities = [HabitEntity::class, HabitLogEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao

}
