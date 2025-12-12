package com.liftfiercely.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    
    companion object {
        private val BODY_WEIGHT_KEY = doublePreferencesKey("body_weight")
        private val DEFAULT_TIMER_DURATION_KEY = intPreferencesKey("default_timer_duration")
        private val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")
    }
    
    // Body Weight
    val bodyWeight: Flow<Double> = context.dataStore.data.map { preferences ->
        preferences[BODY_WEIGHT_KEY] ?: 0.0
    }
    
    suspend fun setBodyWeight(weight: Double) {
        context.dataStore.edit { preferences ->
            preferences[BODY_WEIGHT_KEY] = weight
        }
    }
    
    suspend fun getBodyWeightOnce(): Double {
        return context.dataStore.data.first()[BODY_WEIGHT_KEY] ?: 0.0
    }
    
    // Default Timer Duration (in seconds)
    val defaultTimerDuration: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[DEFAULT_TIMER_DURATION_KEY] ?: 90
    }
    
    suspend fun setDefaultTimerDuration(duration: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_TIMER_DURATION_KEY] = duration
        }
    }
    
    suspend fun getDefaultTimerDurationOnce(): Int {
        return context.dataStore.data.first()[DEFAULT_TIMER_DURATION_KEY] ?: 90
    }
    
    // Dark Theme
    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME_KEY] ?: false
    }
    
    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME_KEY] = isDark
        }
    }
    
    suspend fun getIsDarkThemeOnce(): Boolean {
        return context.dataStore.data.first()[IS_DARK_THEME_KEY] ?: false
    }
}

