package com.mauro.habittracker.core.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.mauro.habittracker.core.domain.repository.HabitRepository
import java.time.LocalDate
import javax.inject.Inject

class DeleteLogUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(habitId: Long, date: LocalDate) {
        repository.deleteLog(habitId, date)
    }
}
