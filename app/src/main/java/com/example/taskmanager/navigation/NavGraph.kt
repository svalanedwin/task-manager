package com.example.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanager.ui.AddTaskScreen
import com.example.taskmanager.ui.SettingsScreen
import com.example.taskmanager.ui.TaskDetailsScreen
import com.example.taskmanager.ui.TaskListScreen
import com.example.taskmanager.viewmodel.SettingsViewModel
import com.example.taskmanager.viewmodel.TaskViewModel
@Composable
fun TaskNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    settingsViewModel: SettingsViewModel
) {
    NavHost(navController, startDestination = "task_list") {
        composable("task_list") {
            TaskListScreen(
                viewModel = taskViewModel,
                onTaskClick = { taskId -> navController.navigate("taskDetails/$taskId") },
                onAddTaskClick = { navController.navigate("addTask") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }

        composable("taskDetails/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            if (taskId != null) {
                val task = taskViewModel.getTaskById(taskId) // Fetch the task by ID
                if (task != null) {
                    TaskDetailsScreen(
                        task = task,
                        viewModel = taskViewModel,
                        onBack = { navController.popBackStack() }
                    )
                } else {
                    // Handle the case where the task is not found
                    // You can navigate back or show an error message
                    // For now, let's navigate back
                    navController.popBackStack()
                }
            } else {
                // Handle the case where taskId is null
                navController.popBackStack() // Navigate back if taskId is null
            }
        }

        composable("addTask") {
            AddTaskScreen(
                viewModel = taskViewModel,
                onTaskSaved = {
                    navController.popBackStack() // Navigate back after saving the task
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}



