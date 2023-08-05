package com.malakiapps.notes.feature_note.presentation.note_instance

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakiapps.notes.feature_note.domain.errors.InvalidNoteException
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.use_case.NoteUseCases
import com.malakiapps.notes.feature_note.presentation.util.getDefaultDateInString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NoteInstanceViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _titleState = mutableStateOf(NoteTextFieldState())
    val titleState: State<NoteTextFieldState> = _titleState

    private val _bodyState = mutableStateOf(NoteTextFieldState())
    val bodyState: State<NoteTextFieldState> = _bodyState

    private val _date = mutableStateOf(Calendar.getInstance().time)
    val date: State<String> = derivedStateOf {
        _date.value.getDefaultDateInString()
    }


    private val _eventFlow = MutableSharedFlow<NoteInstanceUIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var closingNoteInstance = false

    private var noteId: Int? = null

    init {
        savedStateHandle.get<Int>(NoteInstanceResources.noteId)?.let { noteId ->
            if(noteId != -1){
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        this@NoteInstanceViewModel.noteId = note.id
                        _titleState.value = NoteTextFieldState(
                            text = note.title,
                            isHintVisible = note.title.isEmpty()
                        )
                        _bodyState.value = NoteTextFieldState(
                            text = note.body,
                            isHintVisible = note.body.isEmpty()
                        )

                        _date.value = Date(note.date)
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteInstanceEvent){
        when(event){
            is NoteInstanceEvent.ChangeBodyFocus -> {
                _bodyState.value = _bodyState.value.copy(
                    isHintVisible = _bodyState.value.text.isEmpty()
                )
            }
            is NoteInstanceEvent.ChangeTitleFocus -> {
                _titleState.value = _titleState.value.copy(
                    isHintVisible = _titleState.value.text.isEmpty()
                )
            }
            is NoteInstanceEvent.InsertedBody -> {
                _bodyState.value = _bodyState.value.copy(
                    text = event.value,
                    isHintVisible = event.value.isEmpty()
                )
            }
            is NoteInstanceEvent.InsertedTitle -> {
                _titleState.value = _titleState.value.copy(
                    text = event.value,
                    isHintVisible = event.value.isEmpty()
                )
            }
            is NoteInstanceEvent.CloseNote -> {
                viewModelScope.launch {
                    noteId = saveNote()
                    val deleteNoteId = if (event.deleteThisNote){
                        //Delete button was pressed
                        noteId
                    }else{
                        //Save button was pressed
                        null
                    }
                    closingNoteInstance = true
                    _eventFlow.emit(NoteInstanceUIEvent.CloseNoteInstance(deleteNoteId))
                }
            }

            NoteInstanceEvent.OnPauseDisposableEffect -> {
                viewModelScope.launch {
                    if(!closingNoteInstance){
                        noteId = saveNote()
                    }
                }
            }
        }
    }

    private suspend fun saveNote(): Int?{
        return try {
            if (noteId == -1){
                noteId = null
            }
            noteUseCases.addNote(
                Note(
                    id = noteId,
                    title = _titleState.value.text,
                    body = _bodyState.value.text,
                    date = _date.value.time
                )
            )
        }catch (e: InvalidNoteException){
            Log.e("notesErrors", "Note instance is invalid")
            /*_eventFlow.emit(
                NoteInstanceUIEvent.ShowSnackBar(message = e.message)
            )*/
            null
        }
    }

    sealed class NoteInstanceUIEvent{
        data class ShowSnackBar(val message: String?): NoteInstanceUIEvent()
        data class CloseNoteInstance(val deleteNoteId: Int? = null): NoteInstanceUIEvent()
    }

}