package com.mauro.habittracker.feature.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauro.habittracker.core.domain.usecase.GetHabitLogsUseCase
import com.mauro.habittracker.core.domain.usecase.GetHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class StatisticsViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val getHabitLogsUseCase: GetHabitLogsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    private val _effect = Channel<StatisticsEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        observeStats()
    }

    fun onIntent(intent: StatisticsIntent) {
        when (intent) {
            is StatisticsIntent.LoadStats -> observeStats()
        }
    }

    private fun observeStats() {
        getHabitsUseCase()
            .distinctUntilChanged()
            .flatMapLatest { habits ->
                if (habits.isEmpty()) {
                    return@flatMapLatest flowOf(StatisticsState(isLoading = false))
                }

                val logFlows = habits.map { habit -> getHabitLogsUseCase(habit.id) }

                combine(logFlows) { logArrays ->
                    var totalCompletions = 0
                    val habitStats = habits.mapIndexed { index, habit ->
                        val count = logArrays[index].size
                        totalCompletions += count
                        HabitStats(habit = habit, completionCount = count)
                    }
                    StatisticsState(
                        habitStats = habitStats,
                        totalHabits = habits.size,
                        totalCompletions = totalCompletions,
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                _effect.trySend(StatisticsEffect.ShowError(e.message ?: "Error loading stats"))
                _state.update { it.copy(isLoading = false) }
            }
            .onEach { newState ->
                _state.value = newState
            }
            .launchIn(viewModelScope)
    }
}