package com.example.taskmanager.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// Create a DataStore instance using preferencesDataStore delegate
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(context: Context) {

    private val dataStore = context.dataStore

    // Loading state
    private val _isLoadingFlow = MutableStateFlow(false)
    val isLoadingFlow: Flow<Boolean> = _isLoadingFlow

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val THEME_KEY = stringPreferencesKey("theme")
        val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
        val CLOUD_SYNC_KEY = booleanPreferencesKey("cloud_sync")
    }

    // Save dark mode preference
    suspend fun saveDarkMode(enabled: Boolean) {
        _isLoadingFlow.value = true
        try {
            dataStore.edit { preferences ->
                preferences[DARK_MODE_KEY] = enabled
            }
        } finally {
            _isLoadingFlow.value = false
        }
    }

    // Get dark mode preference as Flow
    val isDarkModeFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    // Save theme preference
    suspend fun saveTheme(theme: String) {
        _isLoadingFlow.value = true
        try {
            dataStore.edit { preferences ->
                preferences[THEME_KEY] = theme
            }
        } finally {
            _isLoadingFlow.value = false
        }
    }

    // Get theme preference as Flow
    val selectedThemeFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: "Blue"
        }

    // Save notifications preference
    suspend fun saveNotifications(enabled: Boolean) {
        _isLoadingFlow.value = true
        try {
            dataStore.edit { preferences ->
                preferences[NOTIFICATIONS_KEY] = enabled
            }
        } finally {
            _isLoadingFlow.value = false
        }
    }

    // Get notifications preference as Flow
    val isNotificationsEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[NOTIFICATIONS_KEY] ?: true
        }

    // Save cloud sync preference
    suspend fun saveCloudSync(enabled: Boolean) {
        _isLoadingFlow.value = true
        try {
            dataStore.edit { preferences ->
                preferences[CLOUD_SYNC_KEY] = enabled
            }
        } finally {
            _isLoadingFlow.value = false
        }
    }

    // Get cloud sync preference as Flow
    val isCloudSyncEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[CLOUD_SYNC_KEY] ?: false
        }
}