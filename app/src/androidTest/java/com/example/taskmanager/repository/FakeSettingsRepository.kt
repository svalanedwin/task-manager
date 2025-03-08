package com.example.taskmanager.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsRepository : SettingsRepository {

    private val isDarkMode = MutableStateFlow(false)
    private val selectedTheme = MutableStateFlow("Blue")
    private val isNotificationsEnabled = MutableStateFlow(true)
    private val isCloudSyncEnabled = MutableStateFlow(false)

    override val isDarkModeFlow: Flow<Boolean> = isDarkMode
    override val selectedThemeFlow: Flow<String> = selectedTheme
    override val isNotificationsEnabledFlow: Flow<Boolean> = isNotificationsEnabled
    override val isCloudSyncEnabledFlow: Flow<Boolean> = isCloudSyncEnabled

    override suspend fun saveDarkMode(enabled: Boolean) {
        isDarkMode.value = enabled
    }

    override suspend fun saveTheme(theme: String) {
        selectedTheme.value = theme
    }

    override suspend fun saveNotifications(enabled: Boolean) {
        isNotificationsEnabled.value = enabled
    }

    override suspend fun saveCloudSync(enabled: Boolean) {
        isCloudSyncEnabled.value = enabled
    }
}