package com.example.todo.feature_todo.data.di

import android.content.Context
import androidx.room.Room
import com.example.todo.feature_todo.data.local.TodoDatabase
import com.example.todo.feature_todo.data.local.dao.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    // by this function we are telling dagger hilt to
    // provide dao instances
    @Provides
    fun providesRoomDao(database: TodoDatabase): TodoDao{
        return database.dao
    }

    // by this function we are telling dagger hilt to
    // provide dao instances
    @Singleton
    @Provides
    fun providesRoomDb(
        @ApplicationContext appContext: Context
    ): TodoDatabase{
        return Room.databaseBuilder(
            appContext.applicationContext,
            TodoDatabase::class.java,
            name = "todo_database"
        ).fallbackToDestructiveMigration().build()

    }
}