package com.malakiapps.notes.feature_note.domain.repository

interface SystemDetailsRepository {
    suspend fun getInitialRunState(): Boolean
    suspend fun updateInitialRunState(value: Boolean)
}