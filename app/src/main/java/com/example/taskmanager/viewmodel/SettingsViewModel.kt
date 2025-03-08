package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    // Dark Mode State
    val isDarkMode: StateFlow<Boolean> = settingsRepository.isDarkModeFlow
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = false)

    // Theme State
    val selectedTheme: StateFlow<String> = settingsRepository.selectedThemeFlow
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = "Blue")

    // Notifications State
    val isNotificationsEnabled: StateFlow<Boolean> = settingsRepository.isNotificationsEnabledFlow
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = true)

    // Cloud Sync State
    val isCloudSyncEnabled: StateFlow<Boolean> = settingsRepository.isCloudSyncEnabledFlow
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = false)

    // Loading State
    val isLoading: StateFlow<Boolean> = settingsRepository.isLoadingFlow
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = false)

    // Toggle Dark Mode
    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveDarkMode(enabled)
        }
    }

    // Set Theme
    fun setTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.saveTheme(theme)
        }
    }

    // Toggle Notifications
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveNotifications(enabled)
        }
    }

    // Toggle Cloud Sync
    fun toggleCloudSync(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveCloudSync(enabled)
        }
    }
}