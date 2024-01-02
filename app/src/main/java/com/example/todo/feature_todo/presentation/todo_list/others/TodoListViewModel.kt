package com.example.todo.feature_todo.presentation.todo_list.others

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.core.util.TodoListString
import com.example.todo.feature_todo.data.di.IoDispatcher
import com.example.todo.feature_todo.domain.model.TodoItem
import com.example.todo.feature_todo.domain.use_case.TodoUseCases
import com.example.todo.feature_todo.presentation.todo_list.others.TodoListEvent
import com.example.todo.feature_todo.presentation.todo_list.others.TodoListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
   @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _state = mutableStateOf(TodoListState())
    val state: State<TodoListState> = _state

    private var undoTodoItem: TodoItem? = null
    private var getTodoItemsJob: Job? = null

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    fun onEvent(event: TodoListEvent){
        when(event){
            is TodoListEvent.Delete -> {

                // Delete the todo
                viewModelScope.launch(dispatcher + errorHandler) {
                    todoUseCases.deleteTodoItem(event.todo)

                    // update the list
                    getTodoItems()

                    undoTodoItem = event.todo
                }

                // update the list
            }
            is TodoListEvent.Sort -> {

                val stateOrderAlreadyMatchesEventOrder =
                        _state.value.todoItemOrder::class == event.todoItemOrder::class &&
                        _state.value.todoItemOrder.showArchived == event.todoItemOrder.showArchived &&
                        _state.value.todoItemOrder.sortingDirection == event.todoItemOrder.sortingDirection

                if (stateOrderAlreadyMatchesEventOrder){
                    return
                }

              _state.value = _state.value.copy(
                  todoItemOrder = event.todoItemOrder
              )
            }
            is TodoListEvent.ToggleArchived -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    todoUseCases.toggleArchiveTodoItem(todo = event.todo)
                    getTodoItems()
                }
            }
            is TodoListEvent.ToggleCompleted -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    todoUseCases.toggleCompletedTodoItem(todo = event.todo)
                    getTodoItems()
                }
            }
            TodoListEvent.UndoDelete -> {
               viewModelScope.launch(dispatcher + errorHandler) {
                   todoUseCases.addTodoItem(undoTodoItem?: return@launch)
                   undoTodoItem = null
                   getTodoItems()
               }
            }
        }

    }

    fun getTodoItems(){

        // for avoid memory leak
        getTodoItemsJob?.cancel()

        getTodoItemsJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = todoUseCases.getTodoItems(
                todoItemOrder = _state.value.todoItemOrder
            )
            when(result){
                is TodoUseCases.TodoUseCaseResult.Success ->{
                   _state.value = _state.value.copy(
                       todoItems = result.todoItems,
                       todoItemOrder = _state.value.todoItemOrder,
                       isLoading = false
                   )


                }
                is TodoUseCases.TodoUseCaseResult.Error -> {
                    _state.value = _state.value.copy(
                        error = TodoListString.CANT_GET_TODOS,
                        isLoading = false
                    )

                }
            }
        }

    }
}