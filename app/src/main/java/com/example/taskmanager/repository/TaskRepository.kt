package com.example.taskmanager.repository

import com.example.taskmanager.data.TaskDao
import com.example.taskmanager.model.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    private val db = Firebase.firestore
    private val taskCollection = db.collection("tasks")
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun getTasksByStatus(completed: Boolean): Flow<List<Task>> {
        return taskDao.getTasksByStatus(completed)
    }

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }
    fun getAllTasksSortedByPriority(): Flow<List<Task>> {
        return taskDao.getAllTasksSortedByPriority()
    }
    fun getAllTasksSortedByDueDate(): Flow<List<Task>> {
        return taskDao.getAllTasksSortedByDueDate()
    }


    fun saveTaskToCloud(task: Task) {
        taskCollection.document(task.id.toString()).set(task)
    }

    fun deleteTaskFromCloud(taskId: Int) {
        taskCollection.document(taskId.toString()).delete()
    }
}
