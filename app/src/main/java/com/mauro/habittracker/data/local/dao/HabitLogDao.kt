package com.mauro.habittracker.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.mauro.habittracker.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitLogDao {

    @Insert
    suspend fun insertLog(log: HabitLogEntity)

    @Query("SELECT * FROM habitLog WHERE habitId = :habitId")
    fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>>


}