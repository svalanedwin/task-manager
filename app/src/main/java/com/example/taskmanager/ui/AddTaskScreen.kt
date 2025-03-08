package com.example.taskmanager.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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

    val datePickerDialog = rememberDatePickerDialog { selectedDate ->
        dueDate = selectedDate
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Task") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
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
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Task Details",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Priority",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    PrioritySelector(priority) { priority = it }

                    Button(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Pick Due Date: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(dueDate))}")
                    }
                }
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
            ElevatedButton(
                onClick = { onPrioritySelected(priority) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (priority == selectedPriority) MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text(
                    text = priority.name,
                    color = if (priority == selectedPriority) Color.White else Color.Black // Highlight selected text
                )
            }
        }
    }
}

