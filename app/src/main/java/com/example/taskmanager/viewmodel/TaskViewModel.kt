package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.model.Task
import com.example.taskmanager.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
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

    fun addTask(task: Task) {
        viewModelScope.launch { repository.addTask(task) }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch { repository.updateTask(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            _tasks.value = _tasks.value.filter { it.id != task.id } // Update the state
            println("Task deleted, updated tasks: ${_tasks.value.size} tasks") // Add logging
        }
    }

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
}

