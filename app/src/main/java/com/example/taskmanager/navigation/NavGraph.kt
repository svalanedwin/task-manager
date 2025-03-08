package com.example.taskmanager.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanager.ui.AddTaskScreen
import com.example.taskmanager.ui.SettingsScreen
import com.example.taskmanager.ui.TaskDetailsScreen
import com.example.taskmanager.ui.TaskListScreen
import com.example.taskmanager.viewmodel.SettingsViewModel
import com.example.taskmanager.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

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
            val task by taskViewModel.getTaskByIdLive(taskId!!).collectAsState(initial = null)

            if (task == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                LaunchedEffect(task) {
                    delay(2000)  // Optional: Delay before navigating back
                    if (task == null) {
                        println("Error: Task with ID $taskId not found, navigating back")
                        navController.popBackStack()
                    }
                }
            } else {
                TaskDetailsScreen(
                    task = task!!,
                    viewModel = taskViewModel,
                    onBack = { navController.popBackStack() }
                )
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



