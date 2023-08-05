package com.malakiapps.notes.common

import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.use_case.AddNote
import com.malakiapps.notes.feature_note.domain.use_case.DeleteNote
import com.malakiapps.notes.feature_note.domain.use_case.GetInitialRunState
import com.malakiapps.notes.feature_note.domain.use_case.GetNote
import com.malakiapps.notes.feature_note.domain.use_case.GetSortedNotes
import com.malakiapps.notes.feature_note.domain.use_case.NoteUseCases
import com.malakiapps.notes.feature_note.domain.use_case.SetInitialRunState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class NotesPresentationTestsStructure: TestsStructure() {
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var notesUseCases: NoteUseCases
    lateinit var noteRepository: FakeNoteRepository

    abstract fun setUpFakeRepository(vararg initialNotes: Note): FakeNoteRepository

    fun getNoteUseCases(initialState: Boolean = false, fakeRepository: FakeNoteRepository = setUpFakeRepository()): NoteUseCases {
        noteRepository = fakeRepository
        val systemDetailsRepository = FakeSystemDetailsRepositoryImpl(initialState = initialState)
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