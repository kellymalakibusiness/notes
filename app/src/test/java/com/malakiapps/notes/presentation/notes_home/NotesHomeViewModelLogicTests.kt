package com.malakiapps.notes.presentation.notes_home

import com.google.common.truth.Truth.assertThat
import com.malakiapps.notes.common.FakeNoteRepository
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.common.NotesPresentationTestsStructure
import com.malakiapps.notes.common.FakeNoteRepositoryDomainImpl
import com.malakiapps.notes.feature_note.domain.util.OrderMode
import com.malakiapps.notes.feature_note.domain.util.OrderType
import com.malakiapps.notes.feature_note.presentation.NoteDateInstances
import com.malakiapps.notes.feature_note.presentation.notes_home.NotesHomeEvent
import com.malakiapps.notes.feature_note.presentation.notes_home.NotesHomeViewModel
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.util.TimeZone

class NotesHomeViewModelLogicTests: NotesPresentationTestsStructure() {
    private lateinit var viewModel: NotesHomeViewModel

    override fun testSetup(){
        notesUseCases = getNoteUseCases()
        viewModel = NotesHomeViewModel(
            noteUseCases = notesUseCases
        )
    }

    override fun setUpFakeRepository(vararg initialNotes: Note): FakeNoteRepository {
        return FakeNoteRepositoryDomainImpl(*initialNotes)
    }

    @Test
    fun `it should set up the initial values correctly after the viewModel is initialized`(){
        //Nothing much to test here yet. When we add more features we would add more assertions.
        assertThat(viewModel.homeState.value.isOrderSelectorVisible).isEqualTo(false)
    }

    @Test
    fun `it should provide correct date instances for the day it was run`(){
        //Arrange
        val currentDate = LocalDate.now()
        val offsetSeconds = TimeZone.getDefault().rawOffset /1000
        val zoneOffset = ZoneOffset.ofTotalSeconds(offsetSeconds)
        val beginningOfDay = currentDate.atStartOfDay().toEpochSecond(zoneOffset) * 1000
        val beginningOfWeek = currentDate
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay()
            .toEpochSecond(zoneOffset) * 1000

        val beginningOfYear = currentDate.withDayOfYear(1)
            .atStartOfDay()
            .toEpochSecond(zoneOffset) * 1000

        //Act
        val todayDateInstances = NoteDateInstances(
            startOfYear = beginningOfYear,
            startOfWeek = beginningOfWeek,
            startOftoday = beginningOfDay,
        )

        //Assert
        //Since the code runs at different times, milliseconds might be different.
        //We compare the difference and allow for a 5 seconds difference to eliminate flaky tests as much as possible
        assertThat(todayDateInstances.startOftoday - viewModel.dateInstances.startOftoday).isLessThan(5000)
        assertThat(todayDateInstances.startOfWeek - viewModel.dateInstances.startOfWeek).isLessThan(5000)
        assertThat(todayDateInstances.startOfYear - viewModel.dateInstances.startOfYear).isLessThan(5000)
    }

    @Test
    fun `it should order the notes by the order type selected`(){
        //Arrange
        //We create three notes that we would use to sort
        val note1 = generateANote(
            id = 0,
            title = "a",
            body = "z",
            date = 2
        )

        val note2 = generateANote(
            id = 1,
            title = "b",
            body = "y",
            date = 3
        )

        val note3 = generateANote(
            id = 2,
            title = "c",
            body = "x",
            date = 1
        )

        notesUseCases = getNoteUseCases(
            fakeRepository = setUpFakeRepository(note1, note2, note3)
        )
        viewModel = NotesHomeViewModel(notesUseCases)

        //Act
        //Create all the orders we would need and save them on a custom map
        val allOrders = mutableMapOf<OrderType, List<Note>?>(
            OrderType.Date(OrderMode.Ascending) to null,
            OrderType.Date(OrderMode.Descending) to null,
            OrderType.Title(OrderMode.Ascending) to null,
            OrderType.Title(OrderMode.Descending) to null,
            OrderType.Body(OrderMode.Ascending) to null,
            OrderType.Body(OrderMode.Descending) to null
        )

        //Save the result on the value of each key
        allOrders.forEach {
            viewModel.onEvent(NotesHomeEvent.Order(it.key))
            val notes = viewModel.homeState.value.notes
            allOrders[it.key] = notes
        }

        //Assert
        allOrders.forEach { orderInstance ->
            var correctOrder: List<Note>
            when(orderInstance.key){
                is OrderType.Body -> {
                    correctOrder = listOf(note3, note2, note1)
                    //If it's in descending we flip the results
                    if(orderInstance.key.orderMode == OrderMode.Descending){
                        correctOrder = correctOrder.reversed()
                    }
                }
                is OrderType.Date -> {
                    correctOrder = listOf(note3, note1, note2)
                    //If it's in descending we flip the results
                    if(orderInstance.key.orderMode == OrderMode.Descending){
                        correctOrder = correctOrder.reversed()
                    }
                }
                is OrderType.Title -> {
                    correctOrder = listOf(note1, note2, note3)
                    //If it's in descending we flip the results
                    if(orderInstance.key.orderMode == OrderMode.Descending){
                        correctOrder = correctOrder.reversed()
                    }
                }
            }
            assertThat(orderInstance.value).isEqualTo(correctOrder)
        }
    }
}