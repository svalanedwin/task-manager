package com.example.taskmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel, onTaskClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClicked() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title Text
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Adding truncation if text overflows
            )
            // Description Text
            Text(
                text = task.description ?: "No description available",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,  // Ensures that the description doesn't overflow too much
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            IconButton(onClick = {  viewModel.togglePin(task) }) {
                Icon(imageVector = if (task.isPinned) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Pin Task")
            }
        }
    }
}
