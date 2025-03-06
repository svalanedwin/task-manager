package com.example.taskmanager.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskmanager.model.Priority
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel,
    onTaskSaved: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis()) }

    // Use the custom rememberDatePickerDialog
    val datePickerDialog = rememberDatePickerDialog { selectedDate ->
        dueDate = selectedDate
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("New Task") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (title.isNotBlank()) {
                    val newTask = Task(
                        title = title,
                        description = description.takeIf { it.isNotBlank() },
                        priority = priority,
                        dueDate = dueDate
                    )
                    viewModel.addTask(newTask)
                    onTaskSaved()
                }
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") }
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") }
            )
            PrioritySelector(priority) { priority = it }
            Button(onClick = { datePickerDialog.show() }) {
                Text("Pick Due Date: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(dueDate))}")
            }
        }
    }
}

@Composable
fun rememberDatePickerDialog(
    onDateSelected: (Long) -> Unit
): DatePickerDialog {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    return remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}

@Composable
fun PrioritySelector(selectedPriority: Priority, onPrioritySelected: (Priority) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Priority.values().forEach { priority ->
            Button(
                onClick = { onPrioritySelected(priority) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (priority == selectedPriority) Color.Blue else Color.Gray
                )
            ) {
                Text(priority.name)
            }
        }
    }
}