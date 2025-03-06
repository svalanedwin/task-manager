package com.example.taskmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskmanager.model.Priority
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddTaskScreen(viewModel: TaskViewModel, navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var expanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Task") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize() // Ensure focus is handled properly
                .padding(padding)
                .padding(16.dp)
        ) {
            // Title input
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description input
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Priority dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Priority: ${priority.name}")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Priority.values().forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                priority = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
