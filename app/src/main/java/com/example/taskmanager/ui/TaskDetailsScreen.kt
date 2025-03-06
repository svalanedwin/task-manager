package com.example.taskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    task: Task,
    viewModel: TaskViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description ?: "") }
    var isCompleted by remember { mutableStateOf(task.isCompleted) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Task Details") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Checkbox(checked = isCompleted, onCheckedChange = { isCompleted = it })
                Text(text = "Mark as Completed")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    val updatedTask = task.copy(
                        title = title,
                        description = description.takeIf { it.isNotBlank() },
                        isCompleted = isCompleted
                    )
                    viewModel.updateTask(updatedTask)
                    onBack()
                }) {
                    Text("Save")
                }
                Button(
                    onClick = {
                        viewModel.deleteTask(task)
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }
            }
        }
    }
}


