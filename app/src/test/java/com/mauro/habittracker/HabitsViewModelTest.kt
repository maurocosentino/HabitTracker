package com.mauro.habittracker

import app.cash.turbine.test
import com.mauro.habittracker.core.domain.model.Frequency
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.usecase.DeleteHabitUseCase
import com.mauro.habittracker.core.domain.usecase.GetHabitLogsUseCase
import com.mauro.habittracker.core.domain.usecase.GetHabitsUseCase
import com.mauro.habittracker.core.domain.usecase.InsertHabitUseCase
import com.mauro.habittracker.core.domain.usecase.InsertLogUseCase
import com.mauro.habittracker.feature.habits.HabitEffect
import com.mauro.habittracker.feature.habits.HabitIntent
import com.mauro.habittracker.feature.habits.HabitsViewModel
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HabitsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var repository: FakeHabitRepository

    private lateinit var getHabitsUseCase: GetHabitsUseCase
    private lateinit var getHabitLogsUseCase: GetHabitLogsUseCase
    private lateinit var insertHabitUseCase: InsertHabitUseCase
    private lateinit var deleteHabitUseCase: DeleteHabitUseCase
    private lateinit var insertLogUseCase: InsertLogUseCase

    private lateinit var viewModel: HabitsViewModel

    @Before
    fun setup() {
        repository = FakeHabitRepository()

        getHabitsUseCase = GetHabitsUseCase(repository)
        getHabitLogsUseCase = GetHabitLogsUseCase(repository)
        insertHabitUseCase = InsertHabitUseCase(repository)
        deleteHabitUseCase = DeleteHabitUseCase(repository)
        insertLogUseCase = InsertLogUseCase(repository)

        viewModel = HabitsViewModel(
            getHabitsUseCase,
            getHabitLogsUseCase,
            insertHabitUseCase,
            deleteHabitUseCase,
            insertLogUseCase
        )
    }

    @Test
    fun initialState_isEmpty() = runTest {

        viewModel.state.test {

            val state = awaitItem()

            assertTrue(state.habits.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun addHabit_updatesStateWithNewHabit() = runTest {

        // GIVEN
        val habit = Habit(
            id = 1L,
            name = "Leer",
            createdAt = LocalDate.of(2024, 2, 1),
            isActive = true,
            reminderTime = null,
            frequency = Frequency.DAILY,
            description = "Leer documentacion"
        )

        viewModel.onIntent(HabitIntent.AddHabit(habit))

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state.habits.contains(habit))
    }

    @Test
    fun deleteHabit_removesHabitFromState() = runTest {

        // GIVEN
        val habit = Habit(
            id = 1L,
            name = "Leer",
            createdAt = LocalDate.of(2024, 2, 1),
            isActive = true,
            reminderTime = null,
            frequency = Frequency.DAILY,
            description = "Leer documentacion"
        )

        viewModel.onIntent(HabitIntent.AddHabit(habit))
        advanceUntilIdle()

        assertTrue(viewModel.state.value.habits.contains(habit))

        // WHEN
        viewModel.onIntent(HabitIntent.DeleteHabit(habit))
        advanceUntilIdle()

        // THEN
        val state = viewModel.state.value
        assertFalse(state.habits.contains(habit))
    }

    @Test
    fun addHabit_error_emitsShowErrorEffect() = runTest {

        repository.shouldThrowError = true

        val habit = Habit(
            id = 1L,
            name = "Leer",
            createdAt = LocalDate.of(2024,2,1),
            isActive = true,
            reminderTime = null,
            frequency = Frequency.DAILY,
            description = "Leer docs"
        )

        viewModel.effect.test {

            viewModel.onIntent(HabitIntent.AddHabit(habit))

            advanceUntilIdle()

            val effect = awaitItem()

            assertTrue(effect is HabitEffect.ShowError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun repositoryUpdate_updatesViewModelState() = runTest {

        val habit = Habit(
            id = 1L,
            name = "Leer",
            createdAt = LocalDate.of(2024,2,1),
            isActive = true,
            reminderTime = null,
            frequency = Frequency.DAILY,
            description = "Leer docs"
        )

        viewModel.state.test {

            val initial = awaitItem()
            assertTrue(initial.habits.isEmpty())

            repository.insertHabit(habit)

            val updated = awaitItem()

            assertTrue(updated.habits.contains(habit))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun multipleAddHabit_intents_addAllHabits() = runTest {

        val habit1 = Habit(
            id = 1L,
            name = "Leer",
            createdAt = LocalDate.of(2024,2,1),
            isActive = true,
            reminderTime = null,
            frequency = Frequency.DAILY,
            description = "Leer docs"
        )

        val habit2 = Habit(
            id = 2L,
            name = "Ejercicio",
            createdAt = LocalDate.of(2024,2,1),
            isActive = true,
            reminderTime = null,
            frequency = Frequency.DAILY,
            description = "Entrenar"
        )

        viewModel.onIntent(HabitIntent.AddHabit(habit1))
        viewModel.onIntent(HabitIntent.AddHabit(habit2))

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state.habits.contains(habit1))
        assertTrue(state.habits.contains(habit2))
    }
}