package com.example.taskmanager.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanager.ui.AddTaskScreen
import com.example.taskmanager.ui.TaskDetailsScreen
import com.example.taskmanager.ui.TaskListScreen
import com.example.taskmanager.viewmodel.TaskViewModel

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
    object TaskDetails : Screen("task_details/{taskId}") {
        fun createRoute(taskId: Int) = "task_details/$taskId"
    }
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
        composable(Screen.TaskDetails.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            val task = viewModel.allTasks.value?.find { it.id == taskId }
            if (task != null) {
                TaskDetailsScreen(task, viewModel, navController)
            }
        }
    }
}
