package com.mauro.habittracker.feature.habits

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.model.HabitLog
import com.mauro.habittracker.core.domain.usecase.DeleteHabitUseCase
import com.mauro.habittracker.core.domain.usecase.DeleteLogUseCase
import com.mauro.habittracker.core.domain.usecase.GetHabitLogsUseCase
import com.mauro.habittracker.core.domain.usecase.GetHabitsUseCase
import com.mauro.habittracker.core.domain.usecase.InsertHabitUseCase
import com.mauro.habittracker.core.domain.usecase.InsertLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class HabitsViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val getHabitLogsUseCase: GetHabitLogsUseCase,
    private val insertHabitUseCase: InsertHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val insertLogUseCase: InsertLogUseCase,
    private val deleteLogUseCase: DeleteLogUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HabitState())
    val state: StateFlow<HabitState> = _state.asStateFlow()

    private val _effect = Channel<HabitEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        observeHabitsWithLogs()
    }

    fun onIntent(intent: HabitIntent) {
        when (intent) {
            is HabitIntent.AddHabit -> addHabit(intent.habit)
            is HabitIntent.DeleteHabit -> deleteHabit(intent.habit)
            is HabitIntent.ToggleComplete -> toggleComplete(intent.habitId, intent.isCompleted)
        }
    }

    private fun observeHabitsWithLogs() {
        getHabitsUseCase()
            .distinctUntilChanged()
            .flatMapLatest { habits ->
                if (habits.isEmpty()) {
                    return@flatMapLatest flowOf(HabitState(isLoading = false))
                }

                val today = LocalDate.now().toEpochDay()
                val logFlows = habits.map { habit ->
                    getHabitLogsUseCase(habit.id)
                }

                combine(logFlows) { logArrays ->
                    val completedToday = mutableSetOf<Long>()
                    logArrays.forEachIndexed { index, logs ->
                        val habit = habits[index]
                        if (logs.any { it.completedDate.toEpochDay() == today }) {
                            completedToday.add(habit.id)
                        }
                    }
                    HabitState(
                        habits = habits,
                        completedTodayIds = completedToday,
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                _effect.trySend(HabitEffect.ShowError(e.message ?: "Error loading habits"))
            }
            .onEach { newState ->
                _state.value = newState
            }
            .launchIn(viewModelScope)
    }

    private fun addHabit(habit: Habit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                insertHabitUseCase(habit)
            } catch (e: Exception) {
                _effect.send(HabitEffect.ShowError(e.message ?: "Error adding habit"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                deleteHabitUseCase(habit)
            } catch (e: Exception) {
                _effect.send(HabitEffect.ShowError(e.message ?: "Error deleting habit"))
            }
        }
    }

    private fun toggleComplete(habitId: Long, shouldBeCompleted: Boolean) {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                if (shouldBeCompleted) {
                    insertLogUseCase(HabitLog(habitId = habitId, completedDate = today))
                    _effect.send(HabitEffect.ShowSuccess("Habit completed!"))
                } else {
                    deleteLogUseCase(habitId, today)
                    _effect.send(HabitEffect.ShowSuccess("Habit uncompleted"))
                }
            } catch (e: Exception) {
                _effect.send(HabitEffect.ShowError(e.message ?: "Error updating habit"))
            }
        }
    }
}
