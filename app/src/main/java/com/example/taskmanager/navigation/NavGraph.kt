package com.example.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanager.ui.AddTaskScreen
import com.example.taskmanager.ui.TaskListScreen
import com.example.taskmanager.viewmodel.TaskViewModel

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
}

@Composable
fun TaskNavGraph(navController: NavHostController, viewModel: TaskViewModel) {
    NavHost(navController = navController, startDestination = Screen.TaskList.route) {
        composable(Screen.TaskList.route) {
            TaskListScreen(viewModel, navController)
        }
        composable(Screen.AddTask.route) {
            AddTaskScreen(viewModel, navController)
        }
    }
}
