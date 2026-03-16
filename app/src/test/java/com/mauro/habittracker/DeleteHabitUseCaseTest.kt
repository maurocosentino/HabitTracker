package com.mauro.habittracker

import com.mauro.habittracker.core.domain.model.Frequency
import com.mauro.habittracker.core.domain.model.Habit
import com.mauro.habittracker.core.domain.usecase.DeleteHabitUseCase
import com.mauro.habittracker.core.domain.usecase.GetHabitsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class DeleteHabitUseCaseTest {

    private lateinit var repository: FakeHabitRepository
    private lateinit var deleteHabitUseCase: DeleteHabitUseCase
    private lateinit var getHabitsUseCase: GetHabitsUseCase

    @Before
    fun setup() {
        repository = FakeHabitRepository()
        deleteHabitUseCase = DeleteHabitUseCase(repository)
        getHabitsUseCase = GetHabitsUseCase(repository)
    }

    @Test
    fun deleteHabit_removesHabitFromRepository() = runTest {

        val habit = Habit(
            id = 1,
            name = "Read",
            description = "Read 10 pages",
            frequency = Frequency.DAILY,
            reminderTime = LocalTime.NOON,
            isActive = true,
            createdAt = LocalDate.of(2024, 1, 1)
        )

        repository.insertHabit(habit)

        deleteHabitUseCase(habit)

        val habits = getHabitsUseCase().first()

        assertFalse(habits.contains(habit))
    }
}