package com.example.taskmanager.repository

import com.example.taskmanager.data.TaskDao
import com.example.taskmanager.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getPendingTasks(): Flow<List<Task>> = taskDao.getPendingTasks()
    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()

    suspend fun addTask(task: Task) = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    suspend fun deleteAllCompletedTasks() = taskDao.deleteAllCompletedTasks()
}
