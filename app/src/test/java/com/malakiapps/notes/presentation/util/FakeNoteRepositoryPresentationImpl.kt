package com.malakiapps.notes.presentation.util

import com.malakiapps.notes.common.FakeNoteRepository
import com.malakiapps.notes.feature_note.domain.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest

class FakeNoteRepositoryPresentationImpl : FakeNoteRepository() {
    private val _notesFlow = MutableSharedFlow<List<Note>>()

    override suspend fun addNote(note: Note): Int {
        return if (note.id != null){
            updateNote(note)
            note.id!!
        }else {
            coreAddNote(note){
                emitCurrentNotes()
            }
        }
    }

    override suspend fun deleteNote(note: Note) {
        coreDeleteNote(note)
        emitCurrentNotes()
    }

    override suspend fun updateNote(note: Note) {
        coreUpdateNote(note)
        emitCurrentNotes()
    }

    override fun queryAllNotes(): Flow<List<Note>> {
        return _notesFlow
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun emitCurrentNotes(){
        runTest {
            _notesFlow.emit(getCurrentNotes())
        }
    }
}