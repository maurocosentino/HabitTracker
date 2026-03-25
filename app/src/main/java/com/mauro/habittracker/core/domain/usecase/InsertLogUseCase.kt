package com.mauro.habittracker.core.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.core.domain.repository.HabitRepository
import javax.inject.Inject

class InsertLogUseCase @Inject constructor(
    private val repository: HabitRepository)
{
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(log : HabitLog){
        repository.insertLog(log)
    }
}