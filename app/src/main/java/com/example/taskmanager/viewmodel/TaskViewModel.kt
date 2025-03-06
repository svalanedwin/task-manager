package com.example.taskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.model.Task
import com.example.taskmanager.notifications.TaskNotificationWorker
import com.example.taskmanager.repository.TaskRepository
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val allTasks: LiveData<List<Task>>

    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme


    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks.asLiveData()
    }

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }
    fun toggleTheme() {
        _isDarkTheme.value = _isDarkTheme.value?.not()
    }
    fun scheduleTaskNotification(task: Task) {
        val workRequest = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
            .setInitialDelay(task.dueDate - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("task_title" to task.title))
            .build()

        WorkManager.getInstance(getApplication()).enqueue(workRequest)
    }
    fun scheduleRecurringTask(task: Task, repeatInterval: Long) {
        val workRequest = PeriodicWorkRequestBuilder<TaskNotificationWorker>(repeatInterval, TimeUnit.DAYS)
            .setInputData(workDataOf("task_title" to task.title))
            .build()

        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
            task.id.toString(),
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
