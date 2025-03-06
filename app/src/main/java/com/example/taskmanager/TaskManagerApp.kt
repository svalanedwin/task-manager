package com.example.taskmanager

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.AddTaskScreen
import com.example.taskmanager.ui.SettingsScreen
import com.example.taskmanager.ui.TaskDetailsScreen
import com.example.taskmanager.ui.TaskListScreen
import com.example.taskmanager.viewmodel.SettingsViewModel
import com.example.taskmanager.viewmodel.TaskViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TaskManagerApp(taskViewModel: TaskViewModel, settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "taskList") {
        // ✅ Task List Screen
        composable("taskList") {
            TaskListScreen(
                viewModel = taskViewModel,
                onTaskClick = { taskId ->
                    navController.navigate("taskDetails/$taskId")
                },
                onAddTaskClick = {
                    navController.navigate("addTask")
                },
                onSettingsClick = { // ✅ Navigate to Settings
                    navController.navigate("settings")
                }
            )
        }

        // ✅ Add Task Screen
        composable("addTask") {
            AddTaskScreen(
                viewModel = taskViewModel,
                onTaskSaved = {
                    navController.popBackStack() // Navigate back after saving
                }
            )
        }

        // ✅ Task Details Screen
        composable("taskDetails/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") // Use String for taskId
            val task = taskViewModel.tasks.value?.find { it.id == taskId } // Find task by String ID

            if (task != null) {
                TaskDetailsScreen(
                    task = task,
                    viewModel = taskViewModel,
                    onBack = {
                        navController.popBackStack() // Navigate back after saving or deleting
                    }
                )
            } else {
                navController.popBackStack() // Handle missing task case
            }
        }

        // ✅ Settings Screen (NEW)
        composable("settings") {
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() } // ✅ Back navigation
            )
        }
    }
}
