package com.malakiapps.notes.feature_note.domain.use_case

import com.malakiapps.notes.feature_note.domain.repository.SystemDetailsRepository

class SetInitialRunState(
    private val systemDetailsRepository: SystemDetailsRepository
) {
    suspend operator fun invoke(value: Boolean){
        systemDetailsRepository.updateInitialRunState(value)
    }
}