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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(task: Task, viewModel: TaskViewModel, navController: NavController) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description ?: "") }
    var priority by remember { mutableStateOf(task.priority) }
    val repeatOptions = listOf("None", "Daily", "Weekly")
    var selectedOption by remember { mutableStateOf("None") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Task") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            Spacer(modifier = Modifier.height(8.dp))

            Text("Priority:")
            Row {
                Priority.values().forEach {
                    Button(
                        onClick = { priority = it },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (priority == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(it.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Reminder Frequency:")

            //  Fixed Dropdown Menu
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                TextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()  //  Required for correct dropdown alignment
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    repeatOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val interval = when (selectedOption) {
                    "Daily" -> 1L
                    "Weekly" -> 7L
                    else -> return@Button
                }
                viewModel.scheduleRecurringTask(task, interval)
            }) {
                Text("Save Reminder")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        viewModel.update(task.copy(title = title, description = description, priority = priority))
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save Task")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.delete(task)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete Task")
                }
            }
        }
    }
}

