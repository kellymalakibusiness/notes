package com.malakiapps.notes.presentation.notes_home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.filters.LargeTest
import com.malakiapps.notes.R
import com.malakiapps.notes.di.AppModule
import com.malakiapps.notes.feature_note.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesHomeIntegrationTest {
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
    fun itShouldToggleTheSortOrderDialog_On() {
        //Arrange
        val sortNotesContentDescription = composeRule.activity.getString(R.string.msg_sort_notes)
        val orderDialogButtonMatcher = hasContentDescription(sortNotesContentDescription) and hasClickAction()
        val dateText = composeRule.activity.getString(R.string.date)
        val dateDialogBoxMatcher = (
                hasText(dateText)
                )

        //Act
        composeRule.onNode(orderDialogButtonMatcher).performClick()

        //Assert
        composeRule.onNode(dateDialogBoxMatcher).assertExists()
    }

    @Test
    fun itShouldToggleTheSortOrderDialog_Off() {
        //Arrange
        val sortNotesContentDescription = composeRule.activity.getString(R.string.msg_sort_notes)
        val orderDialogButtonMatcher = hasContentDescription(sortNotesContentDescription) and hasClickAction()
        val dateText = composeRule.activity.getString(R.string.date)
        val dateDialogBoxMatcher = (
                hasText(dateText)
                )

        //Act
        composeRule.onNode(orderDialogButtonMatcher).performClick()
        composeRule.onNode(dateDialogBoxMatcher).assertExists()
        //Now we close it
        composeRule.onNode(orderDialogButtonMatcher).performClick()

        //Assert
        composeRule.onNode(dateDialogBoxMatcher).assertDoesNotExist()
    }

    @Test
    fun theFloatingActionButtonShouldOpenAnEmptyNoteInstance() {
        //Arrange
        val addNoteDescription = composeRule.activity.getString(R.string.msg_add_note)
        val titleHint = composeRule.activity.getString(R.string.title)
        val bodyHint = composeRule.activity.getString(R.string.msg_start_typing)
        val floatingActionButtonMatcher = hasContentDescription(addNoteDescription) and hasClickAction()
        val titleEditFieldMatcher = hasText(titleHint)
        val bodyEditFieldMatcher = hasText(bodyHint)

        //Act
        composeRule.onNode(floatingActionButtonMatcher).performClick()

        //Assert
        composeRule.onNode(titleEditFieldMatcher).assertExists()
        composeRule.onNode(bodyEditFieldMatcher).assertExists()
    }

    @Test
    fun whenOnNoteInstance_BackButtonShouldCloseTheInstanceAndNotSaveItIfItsEmpty(){
        //Arrange
        val emptyNoteDashboardText = composeRule.activity.getString(R.string.msg_no_notes_found)
        val addNoteDescription = composeRule.activity.getString(R.string.msg_add_note)
        val titleHint = composeRule.activity.getString(R.string.title)
        val bodyHint = composeRule.activity.getString(R.string.msg_start_typing)
        val floatingActionButtonMatcher = hasContentDescription(addNoteDescription) and hasClickAction()
        val titleEditFieldMatcher = hasText(titleHint)
        val bodyEditFieldMatcher = hasText(bodyHint)

        //Act
        composeRule.onNode(hasText(emptyNoteDashboardText)).assertExists()
        composeRule.onNode(floatingActionButtonMatcher).performClick()
        composeRule.onNode(titleEditFieldMatcher).assertExists()
        composeRule.onNode(bodyEditFieldMatcher).assertExists()
        composeRule.activity.onBackPressedDispatcher.onBackPressed()

        //Assert
        composeRule.onNode(floatingActionButtonMatcher).assertExists()
        composeRule.onNode(hasText(emptyNoteDashboardText)).assertExists()
    }
}