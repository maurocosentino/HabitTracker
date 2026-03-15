package com.mauro.habittracker.core.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val repository : HabitRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(): Flow<List<Habit>> {
        return repository.getHabits()
    }
}