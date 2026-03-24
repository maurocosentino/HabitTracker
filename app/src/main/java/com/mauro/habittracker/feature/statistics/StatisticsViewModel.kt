package com.mauro.habittracker.feature.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mauro.habittracker.core.domain.usecase.GetHabitLogsUseCase
import com.mauro.habittracker.core.domain.usecase.GetHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
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
        loadStats()
    }

    fun onIntent(intent: StatisticsIntent) {
        when (intent) {
            is StatisticsIntent.LoadStats -> loadStats()
        }
    }

    private fun loadStats() {
        _state.update { it.copy(isLoading = true) }

        getHabitsUseCase()
            .catch { e ->
                _effect.trySend(StatisticsEffect.ShowError(e.message ?: "Error loading stats"))
                _state.update { it.copy(isLoading = false) }
            }
            .onEach { habits ->
                if (habits.isEmpty()) {
                    _state.update {
                        it.copy(
                            habitStats = emptyList(),
                            totalHabits = 0,
                            totalCompletions = 0,
                            isLoading = false
                        )
                    }
                    return@onEach
                }

                val logFlows = habits.map { habit ->
                    getHabitLogsUseCase(habit.id)
                }

                combine(logFlows) { logArrays ->
                    var totalCompletions = 0
                    val habitStats = habits.mapIndexed { index, habit ->
                        val count = logArrays[index].size
                        totalCompletions += count
                        HabitStats(habit = habit, completionCount = count)
                    }
                    Pair(habitStats, totalCompletions)
                }
                    .catch { e ->
                        _effect.trySend(StatisticsEffect.ShowError(e.message ?: "Error loading logs"))
                        _state.update { it.copy(isLoading = false) }
                    }
                    .onEach { (habitStats, totalCompletions) ->
                        _state.update {
                            it.copy(
                                habitStats = habitStats,
                                totalHabits = habits.size,
                                totalCompletions = totalCompletions,
                                isLoading = false
                            )
                        }
                    }
                    .launchIn(viewModelScope)
            }
            .launchIn(viewModelScope)
    }
}
