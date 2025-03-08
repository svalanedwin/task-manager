package com.example.taskmanager.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkModeFlow: Flow<Boolean>
    val selectedThemeFlow: Flow<String>
    val isNotificationsEnabledFlow: Flow<Boolean>
    val isCloudSyncEnabledFlow: Flow<Boolean>

    suspend fun saveDarkMode(enabled: Boolean)
    suspend fun saveTheme(theme: String)
    suspend fun saveNotifications(enabled: Boolean)
    suspend fun saveCloudSync(enabled: Boolean)
}