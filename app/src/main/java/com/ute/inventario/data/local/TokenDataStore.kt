package com.ute.inventario.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "inventario_session")

@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private val KEY_ACCESS = stringPreferencesKey("access_token")
        private val KEY_REFRESH = stringPreferencesKey("refresh_token")
        private val KEY_IS_STAFF = booleanPreferencesKey("is_staff")
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { it[KEY_ACCESS] }
    val refreshToken: Flow<String?> = context.dataStore.data.map { it[KEY_REFRESH] }
    val isStaff: Flow<Boolean> = context.dataStore.data.map { it[KEY_IS_STAFF] ?: false }

    suspend fun getAccessToken(): String? = accessToken.first()
    suspend fun getRefreshToken(): String? = refreshToken.first()

    suspend fun saveTokens(access: String, refresh: String?, staff: Boolean? = null) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS] = access
            if (refresh != null) {
                prefs[KEY_REFRESH] = refresh
            }
            if (staff != null) {
                prefs[KEY_IS_STAFF] = staff
            }
        }
    }

    suspend fun saveStaffStatus(staff: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_STAFF] = staff
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
