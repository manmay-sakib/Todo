package com.example.todo.feature_todo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todo.feature_todo.data.local.dto.LocalTodoItem


@Dao
interface TodoDao {

    // getting all of the todoItems from the database
    @Query("SELECT * FROM todo")
    fun getAllTodoItems(): List<LocalTodoItem>


    // for getting specific item in database
    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getSingleTodoItemById(id: Int): LocalTodoItem?


    // for local caching
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllTodoItems(todos: List<LocalTodoItem>)


    // adding for single todo item
    // the function will return a long
    // this Long is id that room assign
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todo: LocalTodoItem): Long

    @Delete
    suspend fun deleteTodoItem(todo: LocalTodoItem)
}