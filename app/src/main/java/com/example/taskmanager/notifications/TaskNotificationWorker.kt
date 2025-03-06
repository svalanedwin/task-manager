package com.example.taskmanager.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskmanager.R

class TaskNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val taskTitle = inputData.getString("task_title") ?: return Result.failure()
        showNotification(taskTitle)
        return Result.success()
    }

    private fun showNotification(taskTitle: String) {
        val channelId = "task_notifications"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Task Notifications", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null
                )
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Task Reminder")
            .setContentText("Your task \"$taskTitle\" is due!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
