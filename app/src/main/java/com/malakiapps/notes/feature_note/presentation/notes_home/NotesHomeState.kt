package com.malakiapps.notes.feature_note.presentation.notes_home

import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.util.OrderMode
import com.malakiapps.notes.feature_note.domain.util.OrderType

data class NotesHomeState(
    val notes: List<Note> = emptyList(),
    val selectedOrder: OrderType = OrderType.Date(OrderMode.Descending),
    val isOrderSelectorVisible: Boolean = false
)
