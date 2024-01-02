package com.example.todo.feature_todo.presentation.todo_list.others

import com.example.todo.feature_todo.domain.model.TodoItem
import com.example.todo.feature_todo.domain.util.SortingDirection
import com.example.todo.feature_todo.domain.util.TodoItemOrder

data class TodoListState(
    val todoItems: List<TodoItem> = emptyList(),
    val todoItemOrder: TodoItemOrder = TodoItemOrder.Time(SortingDirection.Down, true),
    val isLoading: Boolean = true,
    val error: String? = null
)
