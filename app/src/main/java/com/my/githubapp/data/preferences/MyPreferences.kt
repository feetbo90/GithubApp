package com.my.githubapp.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyPreferences @Inject constructor(private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>){
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(darkModeState: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = darkModeState
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("setting")
    }
}