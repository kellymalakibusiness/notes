package com.malakiapps.notes.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.malakiapps.notes.feature_note.domain.model.Note
import org.junit.Before
import org.junit.Rule
import kotlin.random.Random

abstract class TestsStructure {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    abstract fun testSetup()

    fun generateANote(id: Int? = null, title: String = "Title".addRandomCharacters(), body: String = "Body".addRandomCharacters(), date: Long = 0): Note {
        return Note(
            id = id,
            title = title,
            body = body,
            date = date
        )
    }

    private fun String.addRandomCharacters(): String {
        val number = Random.nextInt(0, 1000)
        val asciiValue = Random.nextInt(33, 127)
        return "$this-$asciiValue$number"
    }

    fun logError(message: String){
        println(TestingLogTags.error + ":" + message)
    }

    fun logInfo(message: String){
        println(TestingLogTags.info + ":" + message)
    }

    fun logDebug(message: String){
        println(TestingLogTags.debug + ":" + message)
    }
}