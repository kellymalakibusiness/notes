package com.malakiapps.notes.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.malakiapps.notes.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Upsert
    suspend fun upsertNote(note: Note): Long

    @Query("SELECT * FROM note")
    fun queryAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun fetchNoteById(id: Int): Note?

    @Delete
    suspend fun deleteNote(note: Note)
}