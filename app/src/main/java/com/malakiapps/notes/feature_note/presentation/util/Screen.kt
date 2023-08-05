package com.malakiapps.notes.feature_note.presentation.util

sealed class Screen(val route: String){
    object NotesHome: Screen("notes_home")
    object NotesInstance: Screen("notes_instance")
}
