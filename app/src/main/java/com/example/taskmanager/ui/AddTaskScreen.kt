package com.example.taskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskmanager.model.Priority
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: TaskViewModel, navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Task") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text("Priority: ")
                DropdownMenu(expanded = true, onDismissRequest = {}) {
                    Priority.values().forEach {
                        DropdownMenuItem(text = { Text(it.name) }, onClick = { priority = it })
                    }
                }
            }

            Button(
                onClick = {
                    val task = Task(
                        title = title,
                        description = description,
                        priority = priority,
                        dueDate = Calendar.getInstance().timeInMillis
                    )
                    viewModel.insert(task)
                    navController.popBackStack()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Save")
            }
        }
    }
}
