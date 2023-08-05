package com.malakiapps.notes.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.malakiapps.notes.feature_note.domain.model.Note
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import com.google.common.truth.Truth.assertThat
import com.malakiapps.notes.di.AppModule
import com.malakiapps.notes.feature_note.data.data_source.NotesDao
import com.malakiapps.notes.feature_note.data.data_source.NotesDatabase
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first

@SmallTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class NotesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: NotesDatabase
    private lateinit var dao: NotesDao

    @Before
    fun databaseSetup(){
        hiltRule.inject()
        dao = database.notesDao
    }

    @After
    fun closeDatabase(){
        database.close()
    }

    @Test
    fun itShouldCreateANote() = runTest {
        //Arrange
        val note = getNote()
        val noteId = dao.upsertNote(note).toInt()

        //Assertion
        assertThat(noteId).isNotEqualTo(-1)
    }

    @Test
    fun itShouldReadACreatedNote() = runTest {
        //Arrange
        val note = getNote()
        val noteId = dao.upsertNote(note).toInt()
        val createdNote = note.copy(id = noteId)

        //Act
        val fetchedNote = dao.fetchNoteById(noteId)

        //Assertion
        assertThat(fetchedNote).isEqualTo(createdNote)
    }

    @Test
    fun itShouldDeleteACreatedNote() = runTest {
        //Arrange
        val note = getNote()
        val noteId = dao.upsertNote(note).toInt()
        val createdNote = note.copy(id = noteId)

        //Act
        dao.deleteNote(createdNote)

        //Assertion
        val fetchedNote = dao.fetchNoteById(noteId)
        assertThat(fetchedNote).isNull()
    }

    @Test
    fun itShouldUpdateACreatedNote() = runTest {
        //Arrange
        val initialNote = getNote()
        val noteId = dao.upsertNote(initialNote).toInt()

        //Act
        val updatedNote = Note(
            id = noteId,
            title = "Updated Title",
            body = "Updated Body",
            date = 2
        )
        dao.upsertNote(updatedNote)

        //Assertion
        val fetchAfterUpdate = dao.fetchNoteById(id = noteId)
        assertThat(fetchAfterUpdate).isEqualTo(updatedNote)
    }

    @Test
    fun itShouldQueryAllCreatedNotes() = runTest {
        //Arrange
        val createdNotes = buildList {
            (1..5).forEach { index ->
                val note = getNote(
                    title = "note $index"
                )
                val noteId = dao.upsertNote(note).toInt()
                add(note.copy(id = noteId))
            }
        }

        //Act
        val fetchedNotes = dao.queryAllNotes().first()

        //Assertion
        createdNotes.forEach {eachNote ->
            assertThat(fetchedNotes).contains(eachNote)
        }
    }

    //Handles creating a dummy note to keep the code clean
    private fun getNote(title: String = "Title", body: String = "Body", date: Long = 0): Note{
        return Note(
            title = title,
            body = body,
            date = date
        )
    }
}
