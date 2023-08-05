package com.malakiapps.notes.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.malakiapps.notes.common.FakeNoteRepositoryDomainImpl
import com.malakiapps.notes.common.FakeNoteRepository
import com.malakiapps.notes.common.FakeSystemDetailsRepositoryImpl
import com.malakiapps.notes.common.TestsStructure
import com.malakiapps.notes.feature_note.domain.repository.SystemDetailsRepository
import com.malakiapps.notes.feature_note.domain.use_case.AddNote
import com.malakiapps.notes.feature_note.domain.use_case.DeleteNote
import com.malakiapps.notes.feature_note.domain.use_case.GetInitialRunState
import com.malakiapps.notes.feature_note.domain.use_case.GetNote
import com.malakiapps.notes.feature_note.domain.use_case.GetSortedNotes
import com.malakiapps.notes.feature_note.domain.use_case.NoteUseCases
import com.malakiapps.notes.feature_note.domain.use_case.SetInitialRunState
import com.malakiapps.notes.feature_note.domain.util.OrderMode
import com.malakiapps.notes.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NoteUseCasesTests: TestsStructure() {
    private lateinit var noteUseCases: NoteUseCases
    private lateinit var noteRepository: FakeNoteRepository
    private lateinit var systemDetailsRepository: SystemDetailsRepository
    private val preAddedNote1 = generateANote(
    id = 0,
    title = "a",
    body = "z",
    date = 2
    )
    private val preAddedNote2 = generateANote(
    id = 1,
    title = "b",
    body = "y",
    date = 3
    )
    private val preAddedNote3 = generateANote(
    id = 2,
    title = "c",
    body = "x",
    date = 1
    )

    override fun testSetup() {
        noteRepository = FakeNoteRepositoryDomainImpl(preAddedNote1, preAddedNote2, preAddedNote3)
        systemDetailsRepository = FakeSystemDetailsRepositoryImpl(false)
        noteUseCases = NoteUseCases(
            getSortedNotes = GetSortedNotes(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            addNote = AddNote(noteRepository),
            getNote = GetNote(noteRepository),
            getInitialRunState = GetInitialRunState(systemDetailsRepository),
            setInitialRunState = SetInitialRunState(systemDetailsRepository)
        )
    }


    //GET_SORTED_NOTES
    @Test
    fun `it should sort the notes by date in ascending order`() = runBlocking {
        //Arrange
        val expectedOutput = listOf(
            preAddedNote3, preAddedNote1, preAddedNote2
        )

        //Act
        val foundOutput = noteUseCases.getSortedNotes(OrderType.Date(OrderMode.Ascending)).first()

        //Assert
        assertThat(foundOutput).isEqualTo(expectedOutput)
    }

    @Test
    fun `it should sort the notes by date in descending order`() = runBlocking {
        //Arrange
        val expectedOutput = listOf(
            preAddedNote2, preAddedNote1, preAddedNote3
        )

        //Act
        val foundOutput = noteUseCases.getSortedNotes(OrderType.Date(OrderMode.Descending)).first()

        //Assert
        assertThat(foundOutput).isEqualTo(expectedOutput)
    }

    @Test
    fun `it should sort the notes by title in ascending order`() = runBlocking {
        //Arrange
        val expectedOutput = listOf(
            preAddedNote1, preAddedNote2, preAddedNote3
        )

        //Act
        val foundOutput = noteUseCases.getSortedNotes(OrderType.Title(OrderMode.Ascending)).first()

        //Assert
        assertThat(foundOutput).isEqualTo(expectedOutput)
    }

    @Test
    fun `it should sort the notes by title in descending order`() = runBlocking {
        //Arrange
        val expectedOutput = listOf(
            preAddedNote3, preAddedNote2, preAddedNote1
        )

        //Act
        val foundOutput = noteUseCases.getSortedNotes(OrderType.Title(OrderMode.Descending)).first()

        //Assert
        assertThat(foundOutput).isEqualTo(expectedOutput)
    }

    @Test
    fun `it should sort the notes by body in ascending order`() = runBlocking {
        //Arrange
        val expectedOutput = listOf(
            preAddedNote3, preAddedNote2, preAddedNote1
        )

        //Act
        val foundOutput = noteUseCases.getSortedNotes(OrderType.Body(OrderMode.Ascending)).first()

        //Assert
        assertThat(foundOutput).isEqualTo(expectedOutput)
    }

    @Test
    fun `it should sort the notes by body in descending order`() = runBlocking {
        //Arrange
        val expectedOutput = listOf(
            preAddedNote1, preAddedNote2, preAddedNote3
        )

        //Act
        val foundOutput =noteUseCases.getSortedNotes(OrderType.Title(OrderMode.Ascending)).first()

        //Assert
        assertThat(foundOutput).isEqualTo(expectedOutput)
    }


    //DELETE_NOTE
    @Test
    fun `it should delete a note that exists in the repository`() = runBlocking {
        //Act
        noteUseCases.deleteNote(preAddedNote1)
        val afterDeleteNotesState = noteRepository.fetchNote(preAddedNote1.id!!)

        //Assert
        assertThat(afterDeleteNotesState).isNull()
    }


    //ADD_NOTE
    @Test
    fun `it should add a valid note`() = runBlocking {
        //Arrange
        val note = generateANote()

        //Act
        var foundException = false
        try {
            noteUseCases.addNote(note)
        } catch (e: Exception){
            logError("An error occurred while adding note ${e.message}")
            foundException = true
        }

        //Assert
        assertThat(foundException).isFalse()
    }

    @Test
    fun `it should not add an invalid note`() = runBlocking {
        //Arrange
        val invalidNotes = listOf(
            generateANote(title = "", body = ""),
            generateANote(title = "   ", body = ""),
            generateANote(title = "       ", body = ""),
            generateANote(title = "     ", body = "       "),
        )

        //Act
        var aNoteWasCreated = false
        invalidNotes.forEach { eachNote ->
            try {
                noteUseCases.addNote(eachNote)
                aNoteWasCreated = true
            } catch (e: Exception){
                logError("Error while generating $eachNote. \nErrorMessage: ${e.message}\n")
            }
        }

        //Assert
        assertThat(aNoteWasCreated).isFalse()
    }


    //GET_NOTE
    @Test
    fun `it should fetch a note that exists in the repository`() = runBlocking {
        //Act
        val fetchedNote = noteUseCases.getNote(preAddedNote1.id!!)
        if (fetchedNote == null){
            logError("The pre-added note is missing from the repository. The test needs it to progress correctly")
        }

        //Assert
        assertThat(fetchedNote).isEqualTo(preAddedNote1)
    }


    //GET_INITIAL_RUN_STATE
    @Test
    fun `it should fetch the run state of the device`() = runBlocking {
        //Act
        val runState = noteUseCases.getInitialRunState()

        //Assert
        assertThat(runState).isNotNull()
    }


    //SET_INITIAL_RUN_STATE
    @Test
    fun `it should update the initial run state value`() = runBlocking {
        //Arrange
        val initialRunState = noteUseCases.getInitialRunState()

        //Act
        noteUseCases.setInitialRunState(!initialRunState)
        val update1 = noteUseCases.getInitialRunState()

        noteUseCases.setInitialRunState(!update1)
        val update2 = noteUseCases.getInitialRunState()

        //Assert
        assertThat(initialRunState).isEqualTo(!update1)
        assertThat(update1).isEqualTo(!update2)
    }
}