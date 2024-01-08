package com.example.todo.feature_todo.presentation.todo_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.todo.R
import com.example.todo.core.util.TodoListStrings
import com.example.todo.feature_todo.presentation.todo_list.others.TodoListEvent
import com.example.todo.feature_todo.presentation.todo_list.others.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember{SnackbarHostState()}

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    val backgroundImage = if (isPortrait){
        R.drawable.background_portrait
    } else{
        R.drawable.background_landscape
    }

    LaunchedEffect(key1 = true){
        viewModel.getTodoItems()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = TodoListStrings.SORT_BY,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 34.sp,
                    lineHeight = 38.sp
                )
                Divider()
                SortingDrawerOptions(
                    todoItemOrder = state.todoItemOrder,
                    onOrderChange = {order ->
                        viewModel.onEvent(TodoListEvent.Sort(order))
                    }
                )
            }
        }
    ) {
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(onClick = {

                }) {

                }
            }
        ){

        }

    }





}