package com.example.taskmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
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
                    items(tasks) { task ->
                        TaskItem(task = task, onComplete = { updatedTask ->
                            viewModel.update(updatedTask)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onComplete: (Task) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onComplete(task.copy(isCompleted = !task.isCompleted)) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onComplete(task.copy(isCompleted = it)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Priority: ${task.priority}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
