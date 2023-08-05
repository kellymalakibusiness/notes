package com.malakiapps.notes.feature_note.domain.repository

import com.malakiapps.notes.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: Note): Int

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)

    fun queryAllNotes(): Flow<List<Note>>

    suspend fun fetchNote(id: Int): Note?
}