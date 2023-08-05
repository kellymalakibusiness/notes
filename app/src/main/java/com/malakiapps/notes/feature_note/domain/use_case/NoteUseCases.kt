package com.malakiapps.notes.feature_note.domain.use_case

data class NoteUseCases(
    val getSortedNotes: GetSortedNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote,
    val getInitialRunState: GetInitialRunState,
    val setInitialRunState: SetInitialRunState
)
