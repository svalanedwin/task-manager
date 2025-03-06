package com.example.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String? = null,
    val priority: Priority,
    val dueDate: Long,
    val isPinned: Boolean = false,
    val isCompleted: Boolean = false
)

enum class Priority {
    LOW, MEDIUM, HIGH
}
