package com.example.taskmanager


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.AddTaskScreen
import com.example.taskmanager.ui.TaskDetailsScreen
import com.example.taskmanager.ui.TaskListScreen
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun TaskManagerApp(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "taskList") {
        composable("taskList") {
            TaskListScreen(viewModel, navController)
        }
        composable("addTask") {
            AddTaskScreen(viewModel, navController)
        }
        composable("taskDetails/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            val task = viewModel.allTasks.value?.find { it.id == taskId }
            if (task != null) {
                TaskDetailsScreen(task, viewModel, navController)
            }
        }
    }
}