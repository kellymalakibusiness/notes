package com.malakiapps.notes.feature_note.presentation.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

fun Date.getDefaultDateInString(): String{
    return SimpleDateFormat("MMMM d k:mm", Locale.getDefault()).format(this)
}

fun Date.getTodayDateInString(): String{
    return SimpleDateFormat("k:mm", Locale.getDefault()).format(this)
}

fun Date.getThisWeekDateInString(): String{
    return SimpleDateFormat("EEEE k:mm", Locale.getDefault()).format(this)
}

fun Date.getThisYearDateInString(): String{
    return SimpleDateFormat("MMMM d", Locale.getDefault()).format(this)
}

fun Date.getFullDateInString(): String{
    return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(this)
}