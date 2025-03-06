package com.example.taskmanager.utils

import android.content.Context
import android.content.Intent
import com.example.taskmanager.model.Task

object ShareUtils {
    fun shareTask(context: Context, task: Task) {
        val shareText = "Task: ${task.title}\nPriority: ${task.priority}\nDue: ${task.dueDate}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share Task via"))
    }
}
