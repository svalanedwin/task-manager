package com.example.taskmanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.taskmanager.model.Task
import com.example.taskmanager.notifications.TaskNotificationWorker
import com.example.taskmanager.repository.SettingsRepository
import com.example.taskmanager.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TaskViewModel(
    private val repository: TaskRepository,
    private val settingsRepository: SettingsRepository,
    private val context: Context // Pass the context for WorkManager
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    // Function to get a task by its ID
    fun getTaskById(taskId: String): Task? {
        return _tasks.value.find { it.id == taskId }
    }

    // Add a new task and schedule a notification if enabled
    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
            if (settingsRepository.isNotificationsEnabledFlow.first()) {
                scheduleNotification(task)
            }
        }
    }

    // Update an existing task and reschedule a notification if enabled
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            if (settingsRepository.isNotificationsEnabledFlow.first()) {
                scheduleNotification(task)
            }
        }
    }

    // Delete a task
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            _tasks.value = _tasks.value.filter { it.id != task.id } // Update the state
            println("Task deleted, updated tasks: ${_tasks.value.size} tasks") // Add logging
        }
    }

    // Restore a deleted task
    fun restoreTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    // Move a task in the list
    fun moveTask(fromIndex: Int, toIndex: Int) {
        val updatedList = _tasks.value.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }
        _tasks.value = updatedList
    }

    // Get sorted tasks based on the selected sorting option
    fun getSortedTasks(sortBy: String): Flow<List<Task>> {
        return _tasks.map { tasks ->
            when (sortBy) {
                "Priority" -> tasks.sortedByDescending { it.priority } // Sort by priority (HIGH first)
                "Due Date" -> tasks.sortedBy { it.dueDate } // Sort by due date (earliest first)
                "Alphabetical" -> tasks.sortedBy { it.title } // Sort alphabetically
                else -> tasks // Default sorting
            }
        }
    }
    fun getTaskByIdLive(taskId: String): Flow<Task?> {
        return repository.getTaskById(taskId)
    }
    fun getFilteredTasks(filterBy: String): Flow<List<Task>> {
        return _tasks.map { tasks ->
            when (filterBy) {
                "Completed" -> tasks.filter { it.isCompleted }
                "Pending" -> tasks.filter { !it.isCompleted }
                else -> tasks // Default filter for "All"
            }
        }
    }

    // Schedule a notification for a task if the due date is in the future
    private fun scheduleNotification(task: Task) {
        if (task.dueDate > System.currentTimeMillis()) {
            val delay = task.dueDate - System.currentTimeMillis()
            val workRequest = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
                .setInputData(workDataOf("task_title" to task.title))
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}