package com.example.taskmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onTaskClick: (String) -> Unit,
    onAddTaskClick: () -> Unit,
    onSettingsClick: () -> Unit // ✅ Add settings navigation
) {
    var selectedSort by remember { mutableStateOf("Priority") }
    var expanded by remember { mutableStateOf(false) }
    val sortedTasks by viewModel.getSortedTasks(selectedSort).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                actions = {
                    IconButton(onClick = onSettingsClick) { // ✅ Navigate to Settings
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ) {
            // Sort Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                TextField(
                    value = selectedSort,
                    onValueChange = {},
                    label = { Text("Sort by") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                        .padding(8.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Priority", "Due Date").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedSort = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Task List
            if (sortedTasks.isEmpty()) {
                Text("No tasks available", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(sortedTasks, key = { it.id }) { task ->
                        SwipeToDeleteTask(
                            task = task,
                            viewModel = viewModel,
                            onDelete = { viewModel.deleteTask(task) },
                            onTaskClick = { onTaskClick(task.id) },
                            onComplete = { updatedTask -> viewModel.updateTask(updatedTask) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SwipeToDeleteTask(
    task: Task,
    viewModel: TaskViewModel,
    onDelete: () -> Unit,
    onTaskClick: () -> Unit,
    onComplete: (Task) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val dismissThreshold = 300f // Adjust swipe distance threshold

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    if (offsetX < -dismissThreshold) {
                        onDelete()
                    }
                }
            }
    ) {
        TaskItem(task = task, onClick = onTaskClick)
    }
}

