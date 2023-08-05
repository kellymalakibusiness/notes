package com.malakiapps.notes.feature_note.presentation.notes_home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.malakiapps.notes.ui.theme.appColors
import com.malakiapps.notes.ui.theme.spacing

@Composable
fun RadioButtonSelector(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
        ){
        RadioButton(
            selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(
            selectedColor = MaterialTheme.colorScheme.secondary,
            unselectedColor = MaterialTheme.appColors.date
        ),
            modifier = Modifier.testTag(text)
        )
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.appColors.textColor)
    }
}