package com.malakiapps.notes.feature_note.presentation.notes_home

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakiapps.notes.R
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.use_case.NoteUseCases
import com.malakiapps.notes.feature_note.domain.util.InitialNotes
import com.malakiapps.notes.feature_note.domain.util.OrderMode
import com.malakiapps.notes.feature_note.domain.util.OrderType
import com.malakiapps.notes.feature_note.presentation.NoteDateInstances
import com.malakiapps.notes.feature_note.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NotesHomeViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
): ViewModel(){
    private var isThisFirstTimeRunningApp: Boolean
    val dateInstances: NoteDateInstances

    private val _homeState = mutableStateOf(NotesHomeState())
    val homeState: State<NotesHomeState> = _homeState

    private val _eventFlow = MutableSharedFlow<HomeUIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        dateInstances = getTodayDateInstances()
        isThisFirstTimeRunningApp = checkFirstRun()
        getNotes(OrderType.Date(OrderMode.Descending))
    }
    fun onEvent(event: NotesHomeEvent){
        when(event){
            is NotesHomeEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note

                    val noteTitle = if(event.note.title.isBlank()){
                        UiText.StringResource(R.string.msg_empty_title)
                    }else{
                        UiText.DynamicString(event.note.title)
                    }
                    _eventFlow.emit(HomeUIEvent.ShowSnackBar(
                        message = UiText.StringResource(R.string.msg_delete_note, noteTitle),
                        actionLabel = UiText.StringResource(R.string.msg_restore_note),
                        snackBarAction = NotesHomeEvent.RestoreDeletedNote
                    )
                    )
                }
            }

            is NotesHomeEvent.Order -> {
                homeState.value.selectedOrder.run {
                    if(!(this::class == event.orderType::class && this.orderMode == event.orderType.orderMode)){
                        //It's a different order mode
                        getNotes(event.orderType)
                    }
                }
            }

            NotesHomeEvent.RestoreDeletedNote -> {
                //Save it on a local variable so that it can't change while the function
                val deletedNote = recentlyDeletedNote
                if(deletedNote != null){
                    //Make it null in case another event is launched
                    recentlyDeletedNote = null
                    viewModelScope.launch {
                        noteUseCases.addNote(deletedNote)
                    }
                }
            }

            NotesHomeEvent.ToggleOrderSelector -> {
                _homeState.value = _homeState.value.copy(
                    isOrderSelectorVisible = !_homeState.value.isOrderSelectorVisible
                )
            }

            is NotesHomeEvent.GetThenDeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.getNote(event.noteId)?.let {
                        onEvent(NotesHomeEvent.DeleteNote(it))
                    }
                }
            }
        }
    }

    private fun getNotes(orderType: OrderType){
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getSortedNotes(orderType).onEach {
                    if(isThisFirstTimeRunningApp){
                        if(it.isEmpty()){
                    handleFirstRun()
                }
                isThisFirstTimeRunningApp = false
            }

            _homeState.value = _homeState.value.copy(
                notes = it,
                selectedOrder = orderType
            )
        }.launchIn(viewModelScope)
    }

    private suspend fun handleFirstRun(){
        val currentTime = Calendar.getInstance().time.time
        noteUseCases.addNote(InitialNotes.aboutAppNote.copy(date = currentTime))
        noteUseCases.addNote(InitialNotes.welcomeNote.copy(date = currentTime + 1))
        noteUseCases.setInitialRunState(false)
    }

    private fun checkFirstRun(): Boolean{
        return runBlocking {
            noteUseCases.getInitialRunState()
        }
    }

    private fun getTodayDateInstances(): NoteDateInstances {
        val today = Calendar.getInstance().apply {
            set(this.get(Calendar.YEAR), this.get(Calendar.MONTH), this.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val thisWeek = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val thisYear = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return NoteDateInstances(
            startOftoday = today,
            startOfWeek = thisWeek,
            startOfYear = thisYear
        )
    }
}

sealed class HomeUIEvent{
    data class ShowSnackBar(val message: UiText, val actionLabel: UiText, val snackBarAction: NotesHomeEvent): HomeUIEvent()
}