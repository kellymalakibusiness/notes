package com.malakiapps.notes.presentation.note_instance

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.malakiapps.notes.common.FakeNoteRepository
import com.malakiapps.notes.common.FakeNoteRepositoryDomainImpl
import com.malakiapps.notes.common.FakeSystemDetailsRepositoryImpl
import com.malakiapps.notes.common.MainDispatcherRule
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.repository.SystemDetailsRepository
import com.malakiapps.notes.feature_note.domain.use_case.AddNote
import com.malakiapps.notes.feature_note.domain.use_case.DeleteNote
import com.malakiapps.notes.feature_note.domain.use_case.GetInitialRunState
import com.malakiapps.notes.feature_note.domain.use_case.GetNote
import com.malakiapps.notes.feature_note.domain.use_case.GetSortedNotes
import com.malakiapps.notes.feature_note.domain.use_case.NoteUseCases
import com.malakiapps.notes.feature_note.domain.use_case.SetInitialRunState
import com.malakiapps.notes.feature_note.presentation.note_instance.NoteInstanceEvent
import com.malakiapps.notes.feature_note.presentation.note_instance.NoteInstanceResources
import com.malakiapps.notes.feature_note.presentation.note_instance.NoteInstanceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteInstanceViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NoteInstanceViewModel

    private lateinit var notesUseCases: NoteUseCases
    private lateinit var noteRepository: FakeNoteRepository
    private lateinit var systemDetailsRepository: SystemDetailsRepository


    @Before
    fun setup(){
        noteRepository = FakeNoteRepositoryDomainImpl()
        systemDetailsRepository = FakeSystemDetailsRepositoryImpl(initialState = false)
        notesUseCases = NoteUseCases(
            getSortedNotes = GetSortedNotes(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            addNote = AddNote(noteRepository),
            getNote = GetNote(noteRepository),
            getInitialRunState = GetInitialRunState(systemDetailsRepository),
            setInitialRunState = SetInitialRunState(systemDetailsRepository)
        )
        viewModel = NoteInstanceViewModel(
            noteUseCases = notesUseCases,
            savedStateHandle = SavedStateHandle()
            )
    }

    @Test
    fun `note instance with no parameters should be empty`(){
        assertThat(viewModel.titleState.value.text).isEmpty()
        assertThat(viewModel.bodyState.value.text).isEmpty()
    }

    @Test
    fun `it should display the hint when no text is inserted on the title and remove it when it is`(){
        //Arrange
        val initialTitleState = viewModel.titleState.value
        val titleValue = "title-value"

        //Act
        viewModel.onEvent(NoteInstanceEvent.InsertedTitle(titleValue))

        val afterAddingTextState = viewModel.titleState.value

        viewModel.onEvent(NoteInstanceEvent.InsertedTitle(""))

        val afterDeletingText = viewModel.titleState.value

        //Assert
        assertThat(initialTitleState.text).isEqualTo("")
        assertThat(initialTitleState.isHintVisible).isTrue()
        assertThat(afterAddingTextState.text).isEqualTo(titleValue)
        assertThat(afterAddingTextState.isHintVisible).isFalse()
        assertThat(afterDeletingText.text).isEqualTo("")
        assertThat(afterDeletingText.isHintVisible).isTrue()
    }

    @Test
    fun `it should display the hint when no text is inserted on the body and remove it when it is`(){
        //Arrange
        val initialBodyState = viewModel.bodyState.value
        val bodyValue = "title-value"

        //Act
        viewModel.onEvent(NoteInstanceEvent.InsertedBody(bodyValue))

        val afterAddingTextState = viewModel.bodyState.value

        viewModel.onEvent(NoteInstanceEvent.InsertedBody(""))

        val afterDeletingText = viewModel.bodyState.value

        //Assert
        assertThat(initialBodyState.text).isEqualTo("")
        assertThat(initialBodyState.isHintVisible).isTrue()
        assertThat(afterAddingTextState.text).isEqualTo(bodyValue)
        assertThat(afterAddingTextState.isHintVisible).isFalse()
        assertThat(afterDeletingText.text).isEqualTo("")
        assertThat(afterDeletingText.isHintVisible).isTrue()
    }

    @Test
    fun `it should open a note when an id is passed on SavedStateHandle`() = runTest {
        //Arrange
        //First lets create the note and fetch the id
        val createdNote = Note(title = "note title", body = "note body", date = 1)
        val noteId = notesUseCases.addNote(createdNote)

        //Act
        viewModel = NoteInstanceViewModel(
            notesUseCases,
            SavedStateHandle(
                mapOf(NoteInstanceResources.noteId to noteId)
            )
            )

        //Assert
        assertThat(viewModel.titleState.value.text).isEqualTo(createdNote.title)
        assertThat(viewModel.bodyState.value.text).isEqualTo(createdNote.body)
    }
}