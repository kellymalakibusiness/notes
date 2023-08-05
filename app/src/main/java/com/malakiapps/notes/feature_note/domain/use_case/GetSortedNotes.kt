package com.malakiapps.notes.feature_note.domain.use_case

import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.repository.NoteRepository
import com.malakiapps.notes.feature_note.domain.util.OrderMode
import com.malakiapps.notes.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSortedNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(orderType: OrderType = OrderType.Date(OrderMode.Descending)): Flow<List<Note>> {
        val orderMode = orderType.orderMode
        return noteRepository.queryAllNotes().map { noteList ->
            when(orderType){
                is OrderType.Body -> noteList.sortByOrderMode(orderMode){
                    it.body.lowercase()
                }
                is OrderType.Date -> noteList.sortByOrderMode(orderMode){
                    it.date
                }
                is OrderType.Title -> noteList.sortByOrderMode(orderMode) {
                    it.title.lowercase()
                }
            }
        }
    }

    private fun <Note, R : Comparable<R>> List<Note>.sortByOrderMode(orderMode: OrderMode, sortByOrderType: (Note) -> R?): List<Note>{
        return when(orderMode){
            OrderMode.Ascending -> this.sortedBy{
                sortByOrderType(it)
            }
            OrderMode.Descending -> this.sortedByDescending {
                sortByOrderType(it)
            }
        }
    }
}

