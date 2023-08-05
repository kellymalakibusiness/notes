package com.malakiapps.notes.feature_note.presentation.notes_home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.malakiapps.notes.R
import com.malakiapps.notes.feature_note.domain.util.OrderMode
import com.malakiapps.notes.feature_note.domain.util.OrderType
import com.malakiapps.notes.ui.theme.spacing

@Composable
fun OrderModeSection(
    orderType: OrderType,
    onOrderModeChange: (OrderType) -> Unit
) {
    Row {
        RadioButtonSelector(text = stringResource(id = R.string.ascending), selected = orderType.orderMode == OrderMode.Ascending, onClick = { onOrderModeChange(orderType.copy(OrderMode.Ascending)) })
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        RadioButtonSelector(text = stringResource(id = R.string.descending), selected = orderType.orderMode == OrderMode.Descending, onClick = { onOrderModeChange(orderType.copy(OrderMode.Descending)) })
    }
}