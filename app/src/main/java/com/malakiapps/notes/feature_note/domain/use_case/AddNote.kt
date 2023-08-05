package com.malakiapps.notes.feature_note.domain.use_case

import com.malakiapps.notes.feature_note.domain.errors.InvalidNoteException
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.domain.repository.NoteRepository
import com.malakiapps.notes.feature_note.domain.util.FeatureNoteErrorMessages

class AddNote(
    private val noteRepository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note): Int{
        if(note.title.isBlank() && note.body.isBlank()){
            throw InvalidNoteException(FeatureNoteErrorMessages.emptyNoteError)
        }
        return noteRepository.addNote(note)
    }
}