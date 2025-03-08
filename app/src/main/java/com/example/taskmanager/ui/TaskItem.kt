package com.example.taskmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp), // Corrected usage of elevation
        shape = MaterialTheme.shapes.medium // Rounded corners for the card
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp) // Added spacing below title
            )

            // Conditional description rendering
            if (!task.description.isNullOrBlank()) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp) // Added spacing below description
                )
            }

            // Due date display
            Text(
                text = "Due: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(task.dueDate))}",
                style = MaterialTheme.typography.bodySmall,
                color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp) // Added top padding for due date
            )

            // Strikethrough for completed task
            if (task.isCompleted) {
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth() // Makes the text take full width for better alignment
                        .clickable { onClick() }
                )
            }
        }
    }
}