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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onTaskClick: (String) -> Unit,
    onAddTaskClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedSort by remember { mutableStateOf("Priority") }
    var selectedFilter by remember { mutableStateOf("All") }
    var expandedSort by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }

    val sortedTasks by viewModel.getSortedTasks(selectedSort).collectAsState(initial = emptyList())
    val filteredTasks by viewModel.getFilteredTasks(selectedFilter).collectAsState(initial = sortedTasks)

    // Define the snackbarHostState and coroutineScope
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Text("+")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ) {
            // Sort Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSort,
                onExpandedChange = { expandedSort = it }
            ) {
                TextField(
                    value = selectedSort,
                    onValueChange = {},
                    label = { Text("Sort by") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSort) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .clickable { expandedSort = !expandedSort }
                        .padding(8.dp)
                )
                ExposedDropdownMenu(expanded = expandedSort, onDismissRequest = { expandedSort = false }) {
                    listOf("Priority", "Due Date", "Alphabetical").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedSort = option
                                expandedSort = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Filter Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedFilter,
                onExpandedChange = { expandedFilter = it }
            ) {
                TextField(
                    value = selectedFilter,
                    onValueChange = {},
                    label = { Text("Filter by") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilter) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .clickable { expandedFilter = !expandedFilter }
                        .padding(8.dp)
                )
                ExposedDropdownMenu(expanded = expandedFilter, onDismissRequest = { expandedFilter = false }) {
                    listOf("All", "Completed", "Pending").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedFilter = option
                                expandedFilter = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Task List with Swipe to Delete
            if (filteredTasks.isEmpty()) {
                // Empty State UI
                EmptyStateUI()
            } else {
                LazyColumn {
                    items(filteredTasks, key = { it.id }) { task ->
                        SwipeToDeleteTask(
                            task = task,
                            viewModel = viewModel,
                            onDelete = {
                                viewModel.deleteTask(task)
                                coroutineScope.launch {
                                    // Ensure only one Snackbar is shown at a time
                                    if (snackbarHostState.currentSnackbarData == null) {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Task deleted",
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.restoreTask(task) // Undo delete action
                                        }
                                    }
                                }
                            },
                            onTaskClick = { onTaskClick(task.id) },
                            onComplete = { updatedTask ->
                                if (!updatedTask.isCompleted) {
                                    viewModel.updateTask(updatedTask.copy(isCompleted = true)) // Mark task as completed
                                    coroutineScope.launch {
                                        // Ensure only one Snackbar is shown at a time
                                        if (snackbarHostState.currentSnackbarData == null) {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "Task marked as complete",
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Short
                                            )
                                            if (result == SnackbarResult.ActionPerformed) {
                                                viewModel.updateTask(updatedTask.copy(isCompleted = false)) // Undo complete action
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun EmptyStateUI() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Illustration (You can replace this with your own illustration or icon)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.List, // Replace with your custom illustration
            contentDescription = "No Tasks",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Motivational Message
        Text(
            text = "No tasks yet!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start by adding a new task and stay organized!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
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
    var swipeDirection by remember { mutableStateOf("") } // Track swipe direction

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    // Detect swipe direction (left or right)
                    swipeDirection = when {
                        offsetX < -dismissThreshold -> "delete" // Swipe left for delete
                        offsetX > dismissThreshold -> "complete" // Swipe right for complete
                        else -> ""
                    }

                    // Trigger actions based on swipe direction
                    if (swipeDirection == "delete") {
                        onDelete() // Trigger delete action
                    } else if (swipeDirection == "complete") {
                        onComplete(task) // Trigger complete action
                    }
                }
            }
    ) {
        TaskItem(task = task, onClick = onTaskClick)
    }
}





