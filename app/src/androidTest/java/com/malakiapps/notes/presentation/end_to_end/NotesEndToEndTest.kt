package com.malakiapps.notes.presentation.end_to_end

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.LargeTest
import com.malakiapps.notes.R
import com.malakiapps.notes.di.AppModule
import com.malakiapps.notes.feature_note.presentation.MainActivity
import com.malakiapps.notes.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

//@LargeTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun itShouldCreateAndSaveANote_ItShouldReflectOnHomePage() {
        //Arrange
        val addNoteButtonMatcher =
            hasContentDescription(composeRule.activity.getString(R.string.msg_add_note)) and hasClickAction()

        val noteInstanceBackButtonMatcher = hasContentDescription(composeRule.activity.getString(R.string.msg_go_back))
        val note1 = NoteCreator(
            title = "Note Title",
            body = "Note body"
        )

        //Act
        //First make sure the screen has no notes
            assertNoteExistence(noteAvailable = false, note1)
            composeRule.onNode(addNoteButtonMatcher).performClick()
            insertNoteDetails(note1)
            composeRule.onNode(noteInstanceBackButtonMatcher).performClick()

        //Assert
        assertNoteExistence(noteAvailable = true, note1)
    }

    @Test
    fun backButtonFromNoteInstance_ShouldSaveANote(){
        //Arrange
        val addNoteButtonMatcher =
            hasContentDescription(composeRule.activity.getString(R.string.msg_add_note)) and hasClickAction()

        val note1 = NoteCreator(
            title = "Note Title2",
            body = "Note body2"
        )

        //Act
        //First make sure the screen has no notes
        assertNoteExistence(noteAvailable = false, note1)
        composeRule.onNode(addNoteButtonMatcher).performClick()
        insertNoteDetails(note1)
        composeRule.activity.onBackPressedDispatcher.onBackPressed()

        //Assert
        assertNoteExistence(noteAvailable = true, note1)
    }

    @Test
    fun itShouldDeleteCreatedNoteFromTheDashboard(){
        //Arrange
        val addNoteButtonMatcher =
            hasContentDescription(composeRule.activity.getString(R.string.msg_add_note)) and hasClickAction()
        val emptyDashboardTextMatcher = hasText(composeRule.activity.getString(R.string.msg_no_notes_found))
        val note1 = NoteCreator(
            title = "Note Title3",
            body = "Note body3"
        )

        //Act
        assertNoteExistence(noteAvailable = false, note1)
        composeRule.onNode(addNoteButtonMatcher).performClick()
        insertNoteDetails(note1)
        composeRule.activity.onBackPressedDispatcher.onBackPressed()

        assertNoteExistence(noteAvailable = true, note1)

        //Now we delete the note
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.msg_delete_note)).performClick()
        composeRule.onNode(emptyDashboardTextMatcher).assertExists()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.msg_delete_note, note1.title)).assertExists()
    }

    @Test
    fun itShouldDeleteAValidNoteFromNoteInstanceScreen(){
        //Arrange
        val addNoteButtonMatcher =
            hasContentDescription(composeRule.activity.getString(R.string.msg_add_note)) and hasClickAction()
        val note1 = NoteCreator(
            title = "Note Title4",
            body = "Note body4"
        )

        //Act
        assertNoteExistence(noteAvailable = false, note1)
        composeRule.onNode(addNoteButtonMatcher).performClick()
        insertNoteDetails(note1)

        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.delete_note)).performClick()

        //Assert
        assertNoteExistence(noteAvailable = false, note1)
    }

    @Test
    @Ignore("compose test won't call code inside an effect. Check on it later")
    fun itShouldReverseADeletedNoteFromSnackBar(){
        //Arrange
        val addNoteButtonMatcher =
            hasContentDescription(composeRule.activity.getString(R.string.msg_add_note)) and hasClickAction()
        val note1 = NoteCreator(
            title = "Note Title4",
            body = "Note body4"
        )

        //Act
        assertNoteExistence(noteAvailable = false, note1)

        //Open and type a new note
        composeRule.onNode(addNoteButtonMatcher).performClick()
        insertNoteDetails(note1)

        //Press the delete action bar button
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.delete_note)).performClick()

        //Check for empty dashboard
        assertNoteExistence(noteAvailable = false, note1)

        //Check for the snack bar and press the action button
        composeRule.waitUntil {
            composeRule.onAllNodesWithText(
                composeRule.activity.getString(
                    R.string.msg_delete_note,
                    note1.title
                )
            ).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.msg_restore_note)).performClick()

        //Check if note was regenerated
        assertNoteExistence(noteAvailable = true, note1)
    }

    @Test
    fun itShouldOrderCreatedNotesCorrectly(){
        //Arrange
        val addNoteButtonMatcher =
            hasContentDescription(composeRule.activity.getString(R.string.msg_add_note)) and hasClickAction()
        val notes = arrayOf(
            NoteCreator(
            title = "title1",
            body = "body3"
        ),
            NoteCreator(
            title = "title2",
            body = "body1"
        ),
            NoteCreator(
            title = "title3",
            body = "body2"
        )
        )

        //Act & Assert
        //Make sure no note is available
        assertNoteExistence(noteAvailable = false, *notes)

        //Create the notes
        notes.forEach { note ->
            composeRule.onNode(addNoteButtonMatcher).performClick()
            insertNoteDetails(note)
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        //Open order dialog
        composeRule.onNode(hasContentDescription(composeRule.activity.getString(R.string.msg_sort_notes)) and hasClickAction()).performClick()

        //Select order by title ascending
        composeRule.onNode(hasTestTag(composeRule.activity.getString(R.string.title))).performClick()
        composeRule.onNode(hasTestTag(composeRule.activity.getString(R.string.ascending))).performClick()

        val titleArrangementAscending = composeRule.onAllNodesWithTag(TestTags.HOME_NOTE_INSTANCE_ITEM)

        titleArrangementAscending[0].assertTextContains(notes[0].title)
        titleArrangementAscending[1].assertTextContains(notes[1].title)
        titleArrangementAscending[2].assertTextContains(notes[2].title)

        composeRule.onNode(hasTestTag(composeRule.activity.getString(R.string.descending))).performClick()

        val titleArrangementDescending = composeRule.onAllNodesWithTag(TestTags.HOME_NOTE_INSTANCE_ITEM)

        titleArrangementDescending[0].assertTextContains(notes[2].title)
        titleArrangementDescending[1].assertTextContains(notes[1].title)
        titleArrangementDescending[2].assertTextContains(notes[0].title)

        composeRule.onNode(hasTestTag(composeRule.activity.getString(R.string.body))).performClick()
        composeRule.onNode(hasTestTag(composeRule.activity.getString(R.string.ascending))).performClick()

        val bodyArrangementAscending = composeRule.onAllNodesWithTag(TestTags.HOME_NOTE_INSTANCE_ITEM)

        bodyArrangementAscending[0].assertTextContains(notes[1].body)
        bodyArrangementAscending[1].assertTextContains(notes[2].body)
        bodyArrangementAscending[2].assertTextContains(notes[0].body)

        composeRule.onNode(hasTestTag(composeRule.activity.getString(R.string.descending))).performClick()

        val bodyArrangementDescending = composeRule.onAllNodesWithTag(TestTags.HOME_NOTE_INSTANCE_ITEM)

        bodyArrangementDescending[0].assertTextContains(notes[0].body)
        bodyArrangementDescending[1].assertTextContains(notes[2].body)
        bodyArrangementDescending[2].assertTextContains(notes[1].body)
    }

    private fun insertNoteDetails(noteCreator: NoteCreator){
        composeRule.onNode(hasTestTag(TestTags.NOTE_TITLE_TEXT_FIELD)).performTextInput(noteCreator.title)
        composeRule.onNode(hasTestTag(TestTags.NOTE_BODY_TEXT_FIELD)).performTextInput(noteCreator.body)

        composeRule.onNodeWithText(noteCreator.title).assertExists()
        composeRule.onNodeWithText(noteCreator.body).assertExists()
    }

    private fun assertNoteExistence(noteAvailable: Boolean, vararg notes: NoteCreator){
        val emptyDashboardTextMatcher = hasText(composeRule.activity.getString(R.string.msg_no_notes_found))
        composeRule.onNode(emptyDashboardTextMatcher).let {
            if (noteAvailable){
                it.assertDoesNotExist()
            }else{
                it.assertExists()
            }
        }
        notes.forEach { eachCreatedNote ->
            composeRule.onNode(
                hasText(eachCreatedNote.title) and hasClickAction()
            ).let {
                if (noteAvailable){
                    it.assertExists()
                }else{
                    it.assertDoesNotExist()
                }
            }
            composeRule.onNode(
                hasText(eachCreatedNote.body) and hasClickAction()
            ).let {
                if (noteAvailable){
                    it.assertExists()
                }else{
                    it.assertDoesNotExist()
                }
            }
        }
    }

    private data class NoteCreator(
        val title: String,
        val body: String
    )
}