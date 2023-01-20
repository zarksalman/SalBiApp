package com.techventure.androidext.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DatastoreUtils(val context: Context) {

    private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        name = ExtConstant.APP_NAME
    )

    suspend fun setString(key: String, value: String) {
        val mKey = stringPreferencesKey(key)
        context.preferencesDataStore.edit {
            it[mKey] = value
        }
    }

    fun getString(key: String, defaultValue: String) = context.preferencesDataStore.data.map {
        val mKey = stringPreferencesKey(key)
        it[mKey] ?: defaultValue
    }
}