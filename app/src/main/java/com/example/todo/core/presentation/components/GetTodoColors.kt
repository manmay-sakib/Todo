package com.example.todo.core.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.todo.feature_todo.domain.model.TodoItem

data class TodoItemColor(
    val backgroundColor: Color,
    val textColor: Color,
    val archiveIconColor: Color,
    val checkColor: Color
)

@Composable
fun getTodoColors(todo: TodoItem): TodoItemColor {
    return if (todo.archived){
        TodoItemColor(
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.6f
            ),
            textColor = MaterialTheme.colorScheme.onSecondary,
            archiveIconColor = MaterialTheme.colorScheme.onSecondary,
            checkColor = if (todo.completed) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.onSecondary
        )
    } else{
        TodoItemColor(
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.6f
            ),
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
            archiveIconColor = MaterialTheme.colorScheme.secondary,
            checkColor = if (todo.completed) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.secondary
        )
    }

}