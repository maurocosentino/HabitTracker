package com.mauro.habittracker

import com.mauro.habittracker.core.domain.model.Frequency
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.usecase.InsertHabitUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertTrue
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class InsertHabitUseCaseTest {

    private lateinit var repository: FakeHabitRepository
    private lateinit var insertHabitUseCase: InsertHabitUseCase

    @Before
    fun setup() {
        repository = FakeHabitRepository()
        insertHabitUseCase = InsertHabitUseCase(repository)
    }

    @Test
    fun `insert habit adds habit to repository`() = runTest {

        val habit = Habit(
            id = 1,
            name = "Read",
            description = "Read 20 minutes",
            createdAt =  LocalDate.of(2024, 1, 1),
            frequency = Frequency.DAILY,
            isActive = true,
            reminderTime = null
        )

        insertHabitUseCase(habit)

        val habits = repository.getHabits().first()

        assertTrue(habits.contains(habit))
    }
}