package com.malakiapps.notes.feature_note.domain.use_case

import com.malakiapps.notes.feature_note.domain.repository.SystemDetailsRepository

class GetInitialRunState(
    private val systemDetailsRepository: SystemDetailsRepository
) {
    suspend operator fun invoke(): Boolean{
        return systemDetailsRepository.getInitialRunState()
    }
}