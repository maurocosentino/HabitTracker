package com.mauro.habittracker.di

import android.content.Context
import androidx.room.Room
import com.mauro.habittracker.core.domain.repository.HabitRepository
import com.mauro.habittracker.data.local.AppDatabase
import com.mauro.habittracker.data.local.dao.HabitDao
import com.mauro.habittracker.data.local.dao.HabitLogDao
import com.mauro.habittracker.data.repository.HabitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideHabitDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "habit_db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideHabitDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    fun provideHabitLogDao(database: AppDatabase): HabitLogDao {
        return database.habitLogDao()
    }

}
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        habitRepositoryImpl: HabitRepositoryImpl
    ): HabitRepository
}
