package com.example.taskmanager.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val selectedTheme by viewModel.selectedTheme.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()
    val isCloudSyncEnabled by viewModel.isCloudSyncEnabled.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val backgroundColor = if (isDarkMode) Color.DarkGray else Color.White
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showCloudSyncConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dark Mode Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.toggleDarkMode(!isDarkMode)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Dark mode ${if (isDarkMode) "disabled" else "enabled"}")
                            }
                        },
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
                            OutlinedButton(
                                onClick = { viewModel.setTheme(theme) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (theme == selectedTheme) Color.Blue else Color.Transparent,
                                    contentColor = if (theme == selectedTheme) Color.White else Color.Black
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
                        .clickable { showCloudSyncConfirmation = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Cloud Sync", modifier = Modifier.weight(1f))
                    Switch(checked = isCloudSyncEnabled, onCheckedChange = { showCloudSyncConfirmation = true })
                }

                Spacer(modifier = Modifier.height(20.dp))

                // About Section
                Text("About", fontWeight = FontWeight.Bold)
                Text("Task Manager App v1.0", color = if (isDarkMode) Color.White else Color.Gray)
                Text("Developed by Valan Edwin", color = if (isDarkMode) Color.White else Color.Gray)
            }
        }
    }

    if (showCloudSyncConfirmation) {
        AlertDialog(
            onDismissRequest = { showCloudSyncConfirmation = false },
            title = { Text("Confirm Cloud Sync") },
            text = { Text("Are you sure you want to ${if (isCloudSyncEnabled) "disable" else "enable"} cloud sync?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.toggleCloudSync(!isCloudSyncEnabled)
                        showCloudSyncConfirmation = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCloudSyncConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}