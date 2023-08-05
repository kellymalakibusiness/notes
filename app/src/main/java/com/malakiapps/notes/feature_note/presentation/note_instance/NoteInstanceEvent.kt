package com.malakiapps.notes.feature_note.presentation.note_instance

import androidx.compose.ui.focus.FocusState

sealed class NoteInstanceEvent{
    data class InsertedTitle(val value: String): NoteInstanceEvent()
    data class ChangeTitleFocus(val focusState: FocusState): NoteInstanceEvent()
    data class InsertedBody(val value: String): NoteInstanceEvent()
    data class ChangeBodyFocus(val focusState: FocusState): NoteInstanceEvent()
    data class CloseNote(val deleteThisNote: Boolean): NoteInstanceEvent()
    object OnPauseDisposableEffect: NoteInstanceEvent()
}
