package com.malakiapps.notes.domain

import com.google.common.truth.Truth
import com.malakiapps.notes.feature_note.presentation.util.getDefaultDateInString
import com.malakiapps.notes.feature_note.presentation.util.getFullDateInString
import com.malakiapps.notes.feature_note.presentation.util.getThisWeekDateInString
import com.malakiapps.notes.feature_note.presentation.util.getThisYearDateInString
import com.malakiapps.notes.feature_note.presentation.util.getTodayDateInString
import org.junit.Test
import java.util.Date

class DateTests {

    @Test
    fun `it should format the date correctly`(){
        //Arrange
        val epochDay = Date(0)

        //Act
        val expectedFullDateString = "01 January 1970"
        val fullDateString = epochDay.getFullDateInString()

        val expectedDefaultDateInString = "January 1 3:00"
        val defaultDateInString = epochDay.getDefaultDateInString()

        val expectedTodayDateInString = "3:00"
        val todayDateInString = epochDay.getTodayDateInString()

        val expectedThisWeekDateInString = "Thursday 3:00"
        val thisWeekDateInString = epochDay.getThisWeekDateInString()

        val expectedThisYearDateInString = "January 1"
        val thisYearDateInString = epochDay.getThisYearDateInString()

        //Assert
        Truth.assertThat(expectedFullDateString).isEqualTo(fullDateString)
        Truth.assertThat(expectedDefaultDateInString).isEqualTo(defaultDateInString)
        Truth.assertThat(expectedTodayDateInString).isEqualTo(todayDateInString)
        Truth.assertThat(expectedThisWeekDateInString).isEqualTo(thisWeekDateInString)
        Truth.assertThat(expectedThisYearDateInString).isEqualTo(thisYearDateInString)
    }
}