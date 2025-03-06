package com.example.taskmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit // Callback for task click
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            if (!task.description.isNullOrBlank()) {
                Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "Due: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(task.dueDate))}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
