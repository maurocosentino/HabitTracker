    package com.mauro.habittracker.feature.habits

    import android.os.Build
    import androidx.annotation.RequiresApi
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.mauro.habittracker.core.domain.model.Habit
    import com.mauro.habittracker.core.domain.model.HabitLog
    import com.mauro.habittracker.core.domain.usecase.DeleteHabitUseCase
    import com.mauro.habittracker.core.domain.usecase.GetHabitsUseCase
    import com.mauro.habittracker.core.domain.usecase.InsertHabitUseCase
    import com.mauro.habittracker.core.domain.usecase.InsertLogUseCase
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.channels.Channel
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.catch
    import kotlinx.coroutines.flow.distinctUntilChanged
    import kotlinx.coroutines.flow.launchIn
    import kotlinx.coroutines.flow.onEach
    import kotlinx.coroutines.flow.receiveAsFlow
    import kotlinx.coroutines.flow.update
    import kotlinx.coroutines.launch
    import java.time.LocalDate
    import javax.inject.Inject

    @RequiresApi(Build.VERSION_CODES.O)
    @HiltViewModel
    class HabitsViewModel @Inject constructor(
        private val getHabitsUseCase: GetHabitsUseCase,
        private val insertHabitUseCase: InsertHabitUseCase,
        private val deleteHabitUseCase: DeleteHabitUseCase,
        private val insertLogUseCase: InsertLogUseCase
    ) : ViewModel() {
        private val _state = MutableStateFlow(HabitState())
        val state: StateFlow<HabitState> = _state.asStateFlow()
        private val _effect = Channel<HabitEffect>()
        val effect = _effect.receiveAsFlow()

        init {
            observeHabits()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun observeHabits() {

            reduce { it.copy(isLoading = true) }

            getHabitsUseCase()
                .distinctUntilChanged()
                .onEach { habits ->
                    reduce {
                        it.copy(
                            habits = habits,
                            isLoading = false
                        )
                    }
                }
                .catch { e ->
                    reduce { it.copy(isLoading = false) }

                    _effect.send(
                        HabitEffect.ShowError(e.message ?: "Unknown error")
                    )
                }
                .launchIn(viewModelScope)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun onIntent(intent: HabitIntent) {
            when (intent) {
                is HabitIntent.AddHabit -> addHabit(intent.habit)
                is HabitIntent.DeleteHabit -> deleteHabit(intent.habit)
                is HabitIntent.CompleteHabit -> completeHabit(intent.habitId)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun addHabit(habit: Habit) {
            viewModelScope.launch {
                try {
                    insertHabitUseCase(habit)
                } catch (e: Exception) {
                    _effect.send(HabitEffect.ShowError("Error inserting habit"))
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun deleteHabit(habit: Habit) {
            viewModelScope.launch {
                try {

                    deleteHabitUseCase(habit)

                } catch (e: Exception) {

                    _effect.send(
                        HabitEffect.ShowError("Could not delete habit")
                    )
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun completeHabit(habitId: Long) {
            viewModelScope.launch {
                try {

                    val log = HabitLog(
                        habitId = habitId,
                        completedDate = LocalDate.now()
                    )

                    insertLogUseCase(log)

                } catch (e: Exception) {

                    _effect.send(
                        HabitEffect.ShowError("Could not complete habit")
                    )
                }
            }
        }
        private fun reduce(transform: (HabitState) -> HabitState) {
            _state.update(transform)
        }

    }

