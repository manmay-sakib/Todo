package com.example.todo.feature_todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.feature_todo.data.local.dao.TodoDao
import com.example.todo.feature_todo.data.local.dto.LocalTodoItem

@Database(
   entities = [LocalTodoItem::class],
   version = 1,
    exportSchema = false
)
abstract class TodoDatabase: RoomDatabase() {

    // reference to dao
    abstract val dao: TodoDao

    companion object{
        const val DATABASE_NAME = "todo_db"
    }
}