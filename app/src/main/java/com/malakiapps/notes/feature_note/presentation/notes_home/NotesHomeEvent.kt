package com.malakiapps.notes.feature_note.presentation.notes_home

import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.util.OrderType

sealed class NotesHomeEvent{
    data class Order(val orderType: OrderType): NotesHomeEvent()
    data class DeleteNote(val note: Note): NotesHomeEvent()
    object RestoreDeletedNote: NotesHomeEvent()
    object ToggleOrderSelector: NotesHomeEvent()
    data class GetThenDeleteNote(val noteId: Int): NotesHomeEvent()
}
