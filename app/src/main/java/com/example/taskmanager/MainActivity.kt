package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.navigation.TaskNavGraph
import com.example.taskmanager.repository.SettingsRepository
import com.example.taskmanager.repository.TaskRepository
import com.example.taskmanager.ui.theme.TaskManagerTheme
import com.example.taskmanager.viewmodel.SettingsViewModel
import com.example.taskmanager.viewmodel.SettingsViewModelFactory
import com.example.taskmanager.viewmodel.TaskViewModel
import com.example.taskmanager.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Get database instance (Fixed: Use TaskDatabase)
        val database = TaskDatabase.getInstance(applicationContext)
        val taskDao = database.taskDao()

        // ✅ Create repositories with required dependencies
        val settingsRepository = SettingsRepository(applicationContext)
        val taskRepository = TaskRepository(taskDao) // ✅ Pass taskDao

        // ✅ Create ViewModels
        val settingsViewModel = ViewModelProvider(this, SettingsViewModelFactory(settingsRepository))
            .get(SettingsViewModel::class.java)

        val taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository,settingsRepository,applicationContext))
            .get(TaskViewModel::class.java)

        setContent {
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val selectedTheme by settingsViewModel.selectedTheme.collectAsState()

            TaskManagerTheme(
                darkTheme = isDarkMode,
                selectedTheme = selectedTheme
            ) {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskNavGraph(
                        navController = navController,
                        taskViewModel = taskViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}
