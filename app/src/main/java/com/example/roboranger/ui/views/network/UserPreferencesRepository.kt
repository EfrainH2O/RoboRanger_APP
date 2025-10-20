package com.example.roboranger.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * A data class to hold the credentials for the last connected network.
 */
data class LastConnectedNetwork(val ssid: String, val password: String)

/**
 * Manages user preferences, such as the last connected network credentials,
 * using Jetpack DataStore for persistence.
 */
@Singleton
class UserPreferencesRepository @Inject constructor(@param:ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val LAST_CONNECTED_SSID = stringPreferencesKey("last_connected_ssid")
        val LAST_CONNECTED_PASSWORD = stringPreferencesKey("last_connected_password")
    }

    /**
     * A flow that emits the credentials of the last successfully connected network.
     */
    val lastConnectedNetwork = context.dataStore.data.map { preferences ->
        val ssid = preferences[PreferencesKeys.LAST_CONNECTED_SSID] ?: ""
        val password = preferences[PreferencesKeys.LAST_CONNECTED_PASSWORD] ?: ""
        LastConnectedNetwork(ssid, password)
    }

    /**
     * Saves the credentials of the last successfully connected network.
     */
    suspend fun saveLastConnectedNetwork(ssid: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_CONNECTED_SSID] = ssid
            preferences[PreferencesKeys.LAST_CONNECTED_PASSWORD] = password
        }
    }
}

