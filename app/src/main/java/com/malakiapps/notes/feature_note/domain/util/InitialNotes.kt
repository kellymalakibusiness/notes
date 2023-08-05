package com.malakiapps.notes.feature_note.domain.util

import com.malakiapps.notes.feature_note.domain.model.Note
import java.util.Calendar

object InitialNotes {
    val aboutAppNote = Note(
        title = "About the app",
        body = "The notes application was created from inspiration of the miui notes application, to help showcase my focus and skills in developing meticulous native android applications with a focus on:-\n1.Architectural design and proficiency in clean architecture.\n2.Dependency injection using Dagger Hilt.\n3.Local database implementation(using Room)\n4.MVVM.\n5.Unit, Integration & UI testing using JUnit 4.\n6.Efficient configuration changes and automatic support for both dark and light mode.\n7.Android resource handling and implementation.\n\nThe source code of the application can be accessed at\n\nhttps://github.com/kellymalakibusiness/notes\n\nCommitted to delivering high-quality code and building scalable, maintainable Android applications.",
        date = 0
    )

    val welcomeNote = Note(
        title = "Welcome to Malaki Notes!!",
        body = "Create and edit your important notes through the app. Your notes are saved automatically as you create and edit them. Create new notes by pressing the + button from the home screen.",
        date = 0
    )
}