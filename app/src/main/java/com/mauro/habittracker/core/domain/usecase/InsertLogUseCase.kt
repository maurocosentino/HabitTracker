package com.mauro.habittracker.core.domain.usecase

import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.core.domain.repository.HabitRepository

class InsertLogUseCase(
    private val repository: HabitRepository)
{
    suspend operator fun invoke(log : HabitLog){
        repository.insertLog(log)
    }
}