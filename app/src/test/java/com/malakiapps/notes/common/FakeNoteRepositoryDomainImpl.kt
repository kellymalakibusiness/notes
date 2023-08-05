package com.malakiapps.notes.common

import com.malakiapps.notes.common.FakeNoteRepository
import com.malakiapps.notes.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeNoteRepositoryDomainImpl(vararg initialNotes: Note) : FakeNoteRepository(*initialNotes) {

    override suspend fun addNote(note: Note): Int {
        return if(note.id != null){
            updateNote(note)
            note.id!!
        }else{
            coreAddNote(note)
        }
    }

    override suspend fun deleteNote(note: Note) {
        coreDeleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        coreUpdateNote(note)
    }

    override fun queryAllNotes(): Flow<List<Note>> {
        return flowOf(getCurrentNotes())
    }
}