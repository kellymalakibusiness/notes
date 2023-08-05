package com.malakiapps.notes.common

import com.malakiapps.notes.feature_note.domain.repository.SystemDetailsRepository

class FakeSystemDetailsRepositoryImpl: SystemDetailsRepository {
    var initialState = false
    override suspend fun getInitialRunState(): Boolean {
        return initialState
    }

    override suspend fun updateInitialRunState(value: Boolean) {
        initialState = value
    }
}