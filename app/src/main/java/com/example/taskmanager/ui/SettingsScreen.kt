package com.example.taskmanager.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val selectedTheme by viewModel.selectedTheme.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()
    val isCloudSyncEnabled by viewModel.isCloudSyncEnabled.collectAsState()

    // Define the background color based on the dark mode state
    val backgroundColor = if (isDarkMode) Color.DarkGray else Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Apply background color to the Column modifier
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor) // Apply the background color here
                .padding(padding) // Apply scaffold padding
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dark Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleDarkMode(!isDarkMode) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Mode", modifier = Modifier.weight(1f))
                Switch(checked = isDarkMode, onCheckedChange = { viewModel.toggleDarkMode(it) })
            }

            // Theme Selection
            Column {
                Text("Choose Theme")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Blue", "Red", "Green", "Purple").forEach { theme ->
                        Button(
                            onClick = { viewModel.setTheme(theme) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (theme == selectedTheme) Color.Blue else Color.Gray
                            )
                        ) {
                            Text(theme)
                        }
                    }
                }
            }

            // Notifications Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleNotifications(!isNotificationsEnabled) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Task Notifications", modifier = Modifier.weight(1f))
                Switch(checked = isNotificationsEnabled, onCheckedChange = { viewModel.toggleNotifications(it) })
            }

            // Cloud Sync Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleCloudSync(!isCloudSyncEnabled) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Cloud Sync", modifier = Modifier.weight(1f))
                Switch(checked = isCloudSyncEnabled, onCheckedChange = { viewModel.toggleCloudSync(it) })
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About Section
            Text("About", fontWeight = FontWeight.Bold)
            Text("Task Manager App v1.0", color = if (isDarkMode) Color.White else Color.Gray)
            Text("Developed by YourName", color = if (isDarkMode) Color.White else Color.Gray)
        }
    }
}