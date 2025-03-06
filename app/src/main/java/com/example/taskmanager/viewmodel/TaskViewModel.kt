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
        viewModelScope.launch { repository.deleteTask(task) }
    }

    fun getSortedTasks(sortBy: String): Flow<List<Task>> {
        return when (sortBy) {
            "Priority" -> _tasks.map { it.sortedBy { task -> task.priority } }
            "Due Date" -> _tasks.map { it.sortedBy { task -> task.dueDate } }
            else -> _tasks
        }
    }
}

