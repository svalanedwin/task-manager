package com.example.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val priority: Priority,
    val dueDate: Long,
    val isCompleted: Boolean = false
)

enum class Priority {
    LOW, MEDIUM, HIGH
}
