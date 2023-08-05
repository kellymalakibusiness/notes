package com.malakiapps.notes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.malakiapps.notes.common.FakeSystemDetailsRepositoryImpl
import com.malakiapps.notes.feature_note.data.data_source.NotesDatabase
import com.malakiapps.notes.feature_note.data.repository.NoteRepositoryImpl
import com.malakiapps.notes.feature_note.domain.repository.NoteRepository
import com.malakiapps.notes.feature_note.domain.use_case.AddNote
import com.malakiapps.notes.feature_note.domain.use_case.DeleteNote
import com.malakiapps.notes.feature_note.domain.use_case.GetInitialRunState
import com.malakiapps.notes.feature_note.domain.use_case.GetNote
import com.malakiapps.notes.feature_note.domain.use_case.GetSortedNotes
import com.malakiapps.notes.feature_note.domain.use_case.NoteUseCases
import com.malakiapps.notes.feature_note.domain.use_case.SetInitialRunState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AndroidTestAppModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): NotesDatabase {
        return Room.inMemoryDatabaseBuilder(
            context, NotesDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(notesDatabase: NotesDatabase): NoteRepository {
        return NoteRepositoryImpl(
            notesDao = notesDatabase.notesDao
        )
    }

    @Provides
    @Singleton
    fun provideSystemDetailsRepository(): FakeSystemDetailsRepositoryImpl{
        return FakeSystemDetailsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideNoteUseCase(noteRepository: NoteRepository, systemDetailsRepository: FakeSystemDetailsRepositoryImpl): NoteUseCases {
        return NoteUseCases(
            getSortedNotes = GetSortedNotes(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            addNote = AddNote(noteRepository),
            getNote = GetNote(noteRepository),
            getInitialRunState = GetInitialRunState(systemDetailsRepository),
            setInitialRunState = SetInitialRunState(systemDetailsRepository)
        )
    }
}