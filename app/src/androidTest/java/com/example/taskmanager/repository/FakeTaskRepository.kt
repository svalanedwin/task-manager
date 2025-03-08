package com.example.taskmanager.repository

import com.example.taskmanager.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeTaskRepository : TaskRepository {

    private val tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun getAllTasks(): Flow<List<Task>> = tasks
    override fun getPendingTasks(): Flow<List<Task>> = tasks.map { list ->
        list.filter { !it.isCompleted }
    }
    override fun getCompletedTasks(): Flow<List<Task>> = tasks.map { list ->
        list.filter { it.isCompleted }
    }
    override fun getTaskById(taskId: String): Flow<Task?> = tasks.map { list ->
        list.find { it.id == taskId }
    }

    override suspend fun addTask(task: Task) {
        tasks.update { it + task }
    }

    override suspend fun updateTask(task: Task) {
        tasks.update { list ->
            list.map { if (it.id == task.id) task else it }
        }
    }

    override suspend fun deleteTask(task: Task) {
        tasks.update { list ->
            list.filter { it.id != task.id }
        }
    }

    override suspend fun deleteAllCompletedTasks() {
        tasks.update { list ->
            list.filter { !it.isCompleted }
        }
    }
}