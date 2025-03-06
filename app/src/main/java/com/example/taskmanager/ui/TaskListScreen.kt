package com.example.taskmanager.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskmanager.model.Task
import com.example.taskmanager.navigation.Screen
import com.example.taskmanager.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel, navController: NavController) {
    val tasks by viewModel.allTasks.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Manager") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddTask.route) }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (tasks.isEmpty()) {
                Text("No tasks available", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(tasks, key = { it.id }) { task ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            SwipeToDeleteTask(
                                task = task,
                                onDelete = { viewModel.delete(task) },
                                onTaskClick = { navController.navigate(Screen.TaskDetails.createRoute(task.id)) },
                                onComplete = { updatedTask -> viewModel.update(updatedTask) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeToDeleteTask(
    task: Task,
    onDelete: () -> Unit,
    onTaskClick: () -> Unit,
    onComplete: (Task) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val dismissThreshold = 300f  // Adjust swipe distance threshold

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
        TaskItem(task = task, onTaskClicked = onTaskClick)
    }
}
