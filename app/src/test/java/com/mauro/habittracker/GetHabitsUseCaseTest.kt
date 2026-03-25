package com.mauro.habittracker

import app.cash.turbine.test
import com.mauro.habittracker.core.domain.model.Frequency
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.usecase.GetHabitsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class GetHabitsUseCaseTest {

    private lateinit var repository: FakeHabitRepository
    private lateinit var getHabitsUseCase: GetHabitsUseCase

    @Before
    fun setup() {
        repository = FakeHabitRepository()
        getHabitsUseCase = GetHabitsUseCase(repository)
    }

    @Test
    fun getHabits_emitsInsertedHabits() = runTest {

        val habit1 = Habit(
            id = 1,
            name = "Read",
            description = "Read 10 pages",
            frequency = Frequency.DAILY,
            reminderTime = LocalTime.NOON,
            isActive = true,
            createdAt = LocalDate.of(2024, 1, 1)
        )

        val habit2 = Habit(
            id = 2,
            name = "Exercise",
            description = "30 minutes workout",
            frequency = Frequency.DAILY,
            reminderTime = null,
            isActive = true,
            createdAt = LocalDate.of(2024, 2, 1)
        )

        repository.insertHabit(habit1)
        repository.insertHabit(habit2)

        getHabitsUseCase().test {

            val habits = awaitItem()

            assertEquals(2, habits.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}