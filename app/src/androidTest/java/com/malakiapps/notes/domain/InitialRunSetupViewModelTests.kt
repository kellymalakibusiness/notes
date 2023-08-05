package com.malakiapps.notes.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.malakiapps.notes.common.FakeSystemDetailsRepositoryImpl
import com.malakiapps.notes.di.AppModule
import com.malakiapps.notes.feature_note.domain.repository.NoteRepository
import com.malakiapps.notes.feature_note.domain.use_case.AddNote
import com.malakiapps.notes.feature_note.domain.use_case.DeleteNote
import com.malakiapps.notes.feature_note.domain.use_case.GetInitialRunState
import com.malakiapps.notes.feature_note.domain.use_case.GetNote
import com.malakiapps.notes.feature_note.domain.use_case.GetSortedNotes
import com.malakiapps.notes.feature_note.domain.use_case.NoteUseCases
import com.malakiapps.notes.feature_note.domain.use_case.SetInitialRunState
import com.malakiapps.notes.feature_note.presentation.notes_home.NotesHomeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class InitialRunSetupViewModelTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var noteRepository: NoteRepository

    @Inject
    lateinit var systemDetailsRepository: FakeSystemDetailsRepositoryImpl

    private lateinit var noteUseCases: NoteUseCases
    private lateinit var viewModel: NotesHomeViewModel

    @Before
    fun setup(){
        hiltRule.inject()
        noteUseCases = NoteUseCases(
            getSortedNotes = GetSortedNotes(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            addNote = AddNote(noteRepository),
            getNote = GetNote(noteRepository),
            getInitialRunState = GetInitialRunState(systemDetailsRepository),
            setInitialRunState = SetInitialRunState(systemDetailsRepository)
        )
    }

    @Test
    fun whenInitialStateOfSystemDetailsRepositoryIsTrue_ItShouldCreateTheInitialTwoNotesThenUpdateItToFalse() = runTest {
        //Arrange
        //Let's set the initialState value to true first
        systemDetailsRepository.initialState = true
        val initialSystemState = systemDetailsRepository.getInitialRunState()
        val initialNotesSize = noteRepository.queryAllNotes().first().size
        noteUseCases = NoteUseCases(
            getSortedNotes = GetSortedNotes(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            addNote = AddNote(noteRepository),
            getNote = GetNote(noteRepository),
            getInitialRunState = GetInitialRunState(systemDetailsRepository),
            setInitialRunState = SetInitialRunState(systemDetailsRepository)
        )

        //Act
        viewModel = NotesHomeViewModel(
            noteUseCases = noteUseCases
        )

        //Check if the viewModel will update the repository value
        val updatedNotesSize = noteRepository.queryAllNotes().first {
            it.size == 2
        }.size

        val updatedSystemState = systemDetailsRepository.getInitialRunState()

        //Assert
        assertThat(initialSystemState).isTrue()
        assertThat(initialNotesSize).isEqualTo(0)
        assertThat(updatedNotesSize).isEqualTo(2)
        assertThat(updatedSystemState).isFalse()
    }

}