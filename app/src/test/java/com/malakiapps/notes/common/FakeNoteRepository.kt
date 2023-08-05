package com.malakiapps.notes.common

import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.repository.NoteRepository

abstract class FakeNoteRepository(vararg initialNotes: Note): NoteRepository {
    private val _notes: MutableMap<Int, Note?> = mutableMapOf()

    init {
       initialNotes.shuffle()
        initialNotes.forEach { note ->
            note.id?.let { noteId ->
                _notes[noteId] = note
            }
        }
    }

    protected fun coreAddNote(note: Note, afterAddingNote: () -> Unit = {}): Int {
        var checkingForId = true
        var id = 0
        while (checkingForId){
            if(_notes[id] == null){
                checkingForId = false
            }else{
                id++
            }
        }
        _notes[id] = note.copy(id = id)
        afterAddingNote()
        return id
    }

    protected fun coreDeleteNote(note: Note) {
        note.id?.let {
            _notes[it] = null
        }
    }

    protected fun coreUpdateNote(note: Note) {
        note.id?.let {
            _notes[it] = note
        }
    }

    override suspend fun fetchNote(id: Int): Note? {
        return _notes[id]
    }

    protected fun getCurrentNotes(): List<Note> {
        return buildList {
            _notes.forEach { map ->
                map.value?.let { note ->
                    add(note)
                }
            }
        }
    }

}