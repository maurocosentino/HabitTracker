package com.mauro.habittracker.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.core.domain.repository.HabitRepository
import com.mauro.habittracker.data.local.dao.HabitDao
import com.mauro.habittracker.data.local.dao.HabitLogDao
import com.mauro.habittracker.data.mapper.toDomain
import com.mauro.habittracker.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitLogDao: HabitLogDao
) : HabitRepository {

    override fun getHabits(): Flow<List<Habit>>
    {
        return habitDao.getAllHabits()
            .map { habitEntities -> habitEntities.map {
                entity -> entity.toDomain()
            }}
    }

    override suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
    }

    override fun getLogsForHabit(habitId: Long): Flow<List<HabitLog>> {
        return habitLogDao.getLogsForHabit(habitId)
            .map {listLogs -> listLogs.map {it.toDomain()}}
    }

    override suspend fun insertLog(log: HabitLog) {
        habitLogDao.insertLog(log.toEntity())
    }
}