package com.malakiapps.notes.feature_note.presentation.notes_home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.malakiapps.notes.R
import com.malakiapps.notes.feature_note.domain.model.Note
import com.malakiapps.notes.feature_note.presentation.NoteDateInstances
import com.malakiapps.notes.feature_note.presentation.util.getFullDateInString
import com.malakiapps.notes.feature_note.presentation.util.getThisWeekDateInString
import com.malakiapps.notes.feature_note.presentation.util.getThisYearDateInString
import com.malakiapps.notes.feature_note.presentation.util.getTodayDateInString
import com.malakiapps.notes.ui.theme.Virgil
import com.malakiapps.notes.ui.theme.appColors
import com.malakiapps.notes.ui.theme.spacing
import com.malakiapps.notes.utils.TestTags
import java.util.Date

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    dateInstances: NoteDateInstances,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.medium)
            .testTag(TestTags.HOME_NOTE_INSTANCE_ITEM)
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.appColors.textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.smallMedium))
        Text(
            text = note.body,
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = Virgil),
            color = MaterialTheme.appColors.secondaryText,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.date.homeSimplifiedDate(dateInstances),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.appColors.date,
            )
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.msg_delete_note))
            }
        }
    }
}

private fun Long.homeSimplifiedDate(dates: NoteDateInstances): String {
    val date = Date(this)
    return when {
        this > dates.startOftoday -> {
            //Note from today
            date.getTodayDateInString()
        }

        this > dates.startOfWeek -> {
            //Note created this week
            date.getThisWeekDateInString()
        }

        this > dates.startOfYear -> {
            //Note created this year
            date.getThisYearDateInString()
        }

        else -> {
            //Note is from previous year
            date.getFullDateInString()
        }

    }
}