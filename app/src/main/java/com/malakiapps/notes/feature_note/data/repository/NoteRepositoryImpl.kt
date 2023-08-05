package com.malakiapps.notes.feature_note.data.repository

import com.malakiapps.notes.feature_note.data.data_source.NotesDao
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val notesDao: NotesDao
): NoteRepository {
    override suspend fun addNote(note: Note): Int {
        return notesDao.upsertNote(note).toInt()
    }

    override suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        notesDao.upsertNote(note)
    }

    override fun queryAllNotes(): Flow<List<Note>> {
        return notesDao.queryAllNotes()
    }

    override suspend fun fetchNote(id: Int): Note? {
        return notesDao.fetchNoteById(id)
    }
}