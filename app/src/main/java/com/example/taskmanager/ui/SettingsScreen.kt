package com.example.taskmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: TaskViewModel) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Appearance", style = MaterialTheme.typography.titleMedium)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { viewModel.toggleTheme() }
            )
        }
    }
}
