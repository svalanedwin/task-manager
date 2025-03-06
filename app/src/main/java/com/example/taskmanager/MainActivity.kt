package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.navigation.TaskNavGraph
import com.example.taskmanager.ui.theme.TaskManagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                val navController = rememberNavController()
                val viewModel: TaskViewModel = viewModel()
                TaskNavGraph(navController, viewModel)
            }
        }
    }
}
