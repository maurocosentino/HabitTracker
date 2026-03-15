package com.mauro.habittracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mauro.habittracker.data.local.dao.HabitDao
import com.mauro.habittracker.data.local.dao.HabitLogDao
import com.mauro.habittracker.data.local.entity.HabitEntity
import com.mauro.habittracker.data.local.entity.HabitLogEntity
import com.mauro.habittracker.data.local.typeconverter.DateConverters


@Database(entities = [HabitEntity::class, HabitLogEntity::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao

}
