package com.malakiapps.notes.presentation.note_instance

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
import org.junit.Rule
import org.junit.Test

@LargeTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
class NoteInstanceIntegrationTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup(){
        hiltRule.inject()
        val addNoteButtonMatcher =
            hasContentDescription(composeRule.activity.getString(R.string.msg_add_note)) and hasClickAction()
        composeRule.onNode(addNoteButtonMatcher).performClick()
    }


    @Test
    fun itShouldUpdateTheNumberOfCharacters_Correctly(){
        //Arrange
        val bodyTextCounterMatcher = hasTestTag(TestTags.NOTE_INSTANCE_CHARACTER_COUNT)
        val bodyTextFieldMatcher = hasTestTag(TestTags.NOTE_BODY_TEXT_FIELD)

        //Initially it should be zero with no text
        composeRule.onNode(bodyTextFieldMatcher).assertTextContains("")
        composeRule.onNode(bodyTextCounterMatcher).assertTextContains("0")

        //Act
        composeRule.onNode(bodyTextFieldMatcher).performTextInput("123")
        composeRule.onNode(bodyTextCounterMatcher).assertTextContains("3")

        composeRule.onNode(bodyTextFieldMatcher).performTextInput(" 4 5")
        composeRule.onNode(bodyTextCounterMatcher).assertTextContains("5")
    }
}