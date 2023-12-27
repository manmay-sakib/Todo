package com.example.todo.feature_todo.data.di

import android.content.Context
import androidx.room.Room
import com.example.todo.feature_todo.data.local.TodoDatabase
import com.example.todo.feature_todo.data.local.dao.TodoDao
import com.example.todo.feature_todo.data.remote.api.TodoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): TodoApi{
        return retrofit.create(TodoApi::class.java)
    }



    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("https://todo-425b7-default-rtdb.firebaseio.com/")
            .build()
    }

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