package com.malakiapps.notes.presentation.notes_home

import com.google.common.truth.Truth.assertThat
import com.malakiapps.notes.common.FakeNoteRepository
import com.malakiapps.notes.common.NotesPresentationTestsStructure
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.util.OrderMode
import com.malakiapps.notes.feature_note.domain.util.OrderType
import com.malakiapps.notes.feature_note.presentation.notes_home.NotesHomeEvent
import com.malakiapps.notes.feature_note.presentation.notes_home.NotesHomeViewModel
import com.malakiapps.notes.presentation.util.FakeNoteRepositoryPresentationImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesHomeViewModelPresentationTests: NotesPresentationTestsStructure() {
    private lateinit var viewModel: NotesHomeViewModel
    override fun testSetup() {
        notesUseCases = getNoteUseCases()
        viewModel = NotesHomeViewModel(
            noteUseCases = notesUseCases
        )
    }

    override fun setUpFakeRepository(vararg initialNotes: Note): FakeNoteRepository {
        return FakeNoteRepositoryPresentationImpl()
    }

    @Test
    fun `it should update homeState value when the state of the repository is updated`() = runTest {
        //Arrange
        val noteId = 0
        val initialNote = generateANote(id = 0)
        val updatedNote = generateANote(id = noteId)

        //Act
        //Initial
        val initialState: List<Note> = emptyList()
        val viewModelInitialState = viewModel.homeState.value.notes

        //Adding
        noteRepository.addNote(initialNote)
        val afterAddingNoteState = listOf(initialNote.copy(id = noteId))
        val viewModelAfterAddingNoteState = viewModel.homeState.value.notes

        //Updating
        noteRepository.updateNote(updatedNote)
        val afterUpdatingNoteState = listOf(updatedNote)
        val viewModelAfterUpdatingNoteState = viewModel.homeState.value.notes

        //Deleting
        noteRepository.deleteNote(updatedNote)
        val afterDeletingNoteState: List<Note>  = emptyList()
        val viewModelAfterDeletingNoteState = viewModel.homeState.value.notes

        //Assert
        assertThat(initialState).isEqualTo(viewModelInitialState)
        assertThat(afterAddingNoteState).isEqualTo(viewModelAfterAddingNoteState)
        assertThat(afterUpdatingNoteState).isEqualTo(viewModelAfterUpdatingNoteState)
        assertThat(afterDeletingNoteState).isEqualTo(viewModelAfterDeletingNoteState)
    }

    @Test
    fun `it should toggle order selector from onEvent`(){
        //Arrange
        val oldValue = viewModel.homeState.value.isOrderSelectorVisible

        //Act
        viewModel.onEvent(NotesHomeEvent.ToggleOrderSelector)
        val newValue = viewModel.homeState.value.isOrderSelectorVisible

        //Assert
        assertThat(oldValue).isFalse()
        assertThat(newValue).isTrue()
    }

    @Test
    fun `it should update the order from onEvent`() = runTest {
        //Arrange
        val note1 = generateANote(id = 5, date = 1)
        val note2 = generateANote(id = 6, date = 99)
        //We also listen for changes
        noteRepository.addNote(note1)
        noteRepository.addNote(note2)

        //Act
        viewModel.onEvent(NotesHomeEvent.Order(OrderType.Date(OrderMode.Descending)))
        val noteToDelete = generateANote(id = 10)
        noteRepository.addNote(noteToDelete)
        noteRepository.deleteNote(noteToDelete)
        val expectedDescendingResults = listOf(note2, note1)
        val descendingResults = viewModel.homeState.value.notes

        viewModel.onEvent(NotesHomeEvent.Order(OrderType.Date(OrderMode.Ascending)))
        noteRepository.addNote(noteToDelete)
        noteRepository.deleteNote(noteToDelete)
        val expectedAscendingResults = listOf(note1, note2)
        val ascendingResults = viewModel.homeState.value.notes

        //Assert
        assertThat(descendingResults).isEqualTo(expectedDescendingResults)
        assertThat(ascendingResults).isEqualTo(expectedAscendingResults)
    }

    @Test
    fun `it should delete a note from onEvent`() = runTest {
        //Arrange
        val note = generateANote(id = 1)
        noteRepository.addNote(note)

        //Act
        viewModel.onEvent(NotesHomeEvent.DeleteNote(note))
        val afterDeleting = viewModel.homeState.value

        //Assert
        assertThat(afterDeleting.notes.size).isEqualTo(0)
    }

    @Test
    fun `it should restore a deleted note from onEvent`() = runTest {
        //Arrange
        val note = generateANote(id = 1)
        noteRepository.addNote(note)

        //Act
        viewModel.onEvent(NotesHomeEvent.DeleteNote(note))
        viewModel.onEvent(NotesHomeEvent.RestoreDeletedNote)
        val afterRestoring = viewModel.homeState.value.notes

        //Assert
        assertThat(afterRestoring).isEqualTo(listOf(note))
    }

    @Test
    fun `it should fetch and delete a note from onEvent`() = runTest {
        //Arrange
        val note = generateANote(id = 1)
        noteRepository.addNote(note)

        //Act
        viewModel.onEvent(NotesHomeEvent.GetThenDeleteNote(note.id!!))
        val afterGetThenDelete = viewModel.homeState.value.notes.size

        //Assert
        assertThat(afterGetThenDelete).isEqualTo(0)
    }
}