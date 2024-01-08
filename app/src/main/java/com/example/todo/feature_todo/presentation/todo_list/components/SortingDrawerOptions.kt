package com.example.todo.feature_todo.presentation.todo_list.components

import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import com.example.todo.core.util.TodoListString
import com.example.todo.feature_todo.domain.util.SortingDirection
import com.example.todo.feature_todo.domain.util.TodoItemOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingDrawerOptions(
    todoItemOrder: TodoItemOrder,
    onOrderChange: (TodoItemOrder) -> Unit,
) {
    val titleSelected = todoItemOrder::class == TodoItemOrder.Title::class
    NavigationDrawerItem(
        label = {
                IconRow(
                    text = TodoListString.TITLE,
                    isChecked = titleSelected
                )
        },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Title(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    val timeSelected = todoItemOrder::class == TodoItemOrder.Time::class
    NavigationDrawerItem(
        label = {
            IconRow(
                text = TodoListString.TIME,
                isChecked = timeSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Time(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    val completedSelected = todoItemOrder::class == TodoItemOrder.Completed::class
    NavigationDrawerItem(
        label = {
            IconRow(
                text = TodoListString.COMPLETED,
                isChecked = completedSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Completed(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    Divider()

    val sortDownSelected = todoItemOrder.sortingDirection == SortingDirection.Down
    NavigationDrawerItem(
        label = {
            IconRow(
                text = TodoListString.SORT_DOWN,
                isChecked = sortDownSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(SortingDirection.Down, todoItemOrder.showArchived))
        }
    )

    val sortUpSelected = todoItemOrder.sortingDirection == SortingDirection.Up
    NavigationDrawerItem(
        label = {
            IconRow(
                text = TodoListString.SORT_UP,
                isChecked = sortUpSelected
            )
        },
        selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(SortingDirection.Up, todoItemOrder.showArchived))
        }
    )
}