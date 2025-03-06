package com.example.taskmanager.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance using preferencesDataStore delegate
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val THEME_KEY = stringPreferencesKey("theme")
        val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
        val CLOUD_SYNC_KEY = booleanPreferencesKey("cloud_sync")
    }

    // Save dark mode preference
    suspend fun saveDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    // Get dark mode preference as Flow
    val isDarkModeFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false // Default to false if not set
        }

    // Save theme preference
    suspend fun saveTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    // Get theme preference as Flow
    val selectedThemeFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: "Blue" // Default to "Blue" if not set
        }

    // Save notifications preference
    suspend fun saveNotifications(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = enabled
        }
    }

    // Get notifications preference as Flow
    val isNotificationsEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[NOTIFICATIONS_KEY] ?: true // Default to true if not set
        }

    // Save cloud sync preference
    suspend fun saveCloudSync(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[CLOUD_SYNC_KEY] = enabled
        }
    }

    // Get cloud sync preference as Flow
    val isCloudSyncEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[CLOUD_SYNC_KEY] ?: false // Default to false if not set
        }
}