package com.example.taskmanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.repository.SettingsRepository
import com.example.taskmanager.repository.TaskRepository

class TaskViewModelFactory(
    private val taskRepository: TaskRepository,
    private val settingsRepository: SettingsRepository, // Additional dependency
    private val context: Context // Additional dependency
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(taskRepository, settingsRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}