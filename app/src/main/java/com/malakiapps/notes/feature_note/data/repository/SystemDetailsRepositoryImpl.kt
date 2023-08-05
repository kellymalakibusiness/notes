package com.malakiapps.notes.feature_note.data.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.malakiapps.notes.feature_note.domain.repository.SystemDetailsRepository
import kotlinx.coroutines.flow.first

class SystemDetailsRepositoryImpl(
    private val application: Application
): SystemDetailsRepository {
    private val Context.dataStore by preferencesDataStore(
        name = NotesPrefs.SystemSharedPrefName
    )
    //DataStore<Preferences> by preferenceData//= application.applicationContext.create()//getSharedPreferences(SharedPrefs.SystemSharedPrefName, Context.MODE_PRIVATE)

    override suspend fun getInitialRunState(): Boolean {
        val appInitialRunStateKey = booleanPreferencesKey(NotesPrefs.appInitialRunState)

        val preferences =  application.dataStore.data.first()
        return preferences[appInitialRunStateKey] ?: true
    }
    override suspend fun updateInitialRunState(value: Boolean){
        val appInitialRunStateKey = booleanPreferencesKey(NotesPrefs.appInitialRunState)
        application.dataStore.edit { mutablePreferences ->
            mutablePreferences[appInitialRunStateKey] = value
        }
    }
}