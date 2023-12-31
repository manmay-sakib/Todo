package com.example.todo.feature_todo.data.repo

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.todo.feature_todo.data.di.IoDispatcher
import com.example.todo.feature_todo.data.local.dao.TodoDao
import com.example.todo.feature_todo.data.mapper.toLocalTodoItem
import com.example.todo.feature_todo.data.mapper.toLocalTodoItemListFromRemote
import com.example.todo.feature_todo.data.mapper.toRemoteTodoItem
import com.example.todo.feature_todo.data.mapper.toTodoItem
import com.example.todo.feature_todo.data.mapper.toTodoItemListFromLocal
import com.example.todo.feature_todo.data.remote.api.TodoApi
import com.example.todo.feature_todo.domain.model.TodoItem
import com.example.todo.feature_todo.domain.repo.TodoListRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.UnknownHostException

class TodoListRepoImpl(
    private val dao: TodoDao,
    private val api: TodoApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): TodoListRepo {
    override suspend fun getAllTodos(): List<TodoItem> {
       getAllTodosFromRemote()
        return dao.getAllTodoItems().toTodoItemListFromLocal()
    }

    override suspend fun getAllTodosFromLocalCache(): List<TodoItem> {
        return dao.getAllTodoItems().toTodoItemListFromLocal()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getAllTodosFromRemote() {

        return withContext(dispatcher){
            try {
                refreshRoomCache()

            }catch (e: Exception){
                when(e){
                    is UnknownHostException,
                        is ConnectException,
                        is HttpException ->{
                        Log.e("Http", "Error: No data from Remote")
                        if (isCacheEmpty()){
                            Log.e("Cache", "Error: No data from local Room cache")
                            throw Exception("Error: Device offline and\nno data from local Room cache")

                        }

                    } else -> throw e
                }
            }
        }
    }

    private suspend fun refreshRoomCache(){
        val remoteBooks = api.getAllTodos().filterNotNull()
        dao.addAllTodoItems(remoteBooks.toLocalTodoItemListFromRemote())
    }




    // fun for Empty cache
    private fun isCacheEmpty(): Boolean{
        var empty = true
        if(dao.getAllTodoItems().isNotEmpty()) empty = false
        return empty
    }



    override suspend fun getSingleTodoItemById(id: Int): TodoItem? {
       return dao.getSingleTodoItemById(id)?.toTodoItem()
    }

    override suspend fun addTodoItem(todo: TodoItem) {
        val newId = dao.addTodoItem(todo.toLocalTodoItem())
        val id = newId.toInt()
        val url = "todo/$id.json"
        api.addTodo(url, todo.toRemoteTodoItem().copy(id=id))
    }

    override suspend fun updateTodoItem(todo: TodoItem) {
        dao.addTodoItem(todo.toLocalTodoItem())
        api.updateTodoItem(todo.id, todo.toRemoteTodoItem())
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun deleteTodoItem(todo: TodoItem) {
        try {
            val response = api.deleteTodo(todo.id)
            if(response.isSuccessful){
                Log.i("API_DELETE", "Response Successful")
            } else{
                Log.i("API_DELETE", "Response Unsuccessful")
                Log.i("API_DELETE", response.message())

            }

        }catch (e: Exception){
            when(e){
                is UnknownHostException,
                    is ConnectException,
                    is HttpException ->{
                        Log.e("HTTP", "Error: Could not delete")

                    } else -> throw e
            }

        }
    }
}