package com.malakiapps.notes.feature_note.presentation.notes_home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.malakiapps.notes.R
import com.malakiapps.notes.feature_note.domain.util.OrderType
import com.malakiapps.notes.ui.theme.spacing

@Composable
fun OrderTypeSection(
    orderType: OrderType,
    onOrderTypeChange: (OrderType) -> Unit
) {
    Row(horizontalArrangement = Arrangement.Center) {
        RadioButtonSelector(text = stringResource(id = R.string.date), selected = orderType is OrderType.Date, onClick = { onOrderTypeChange(OrderType.Date(orderType.orderMode))})
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        RadioButtonSelector(text = stringResource(id = R.string.title), selected = orderType is OrderType.Title, onClick = { onOrderTypeChange(OrderType.Title(orderType.orderMode)) })
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        RadioButtonSelector(text = stringResource(id = R.string.body), selected = orderType is OrderType.Body, onClick = { onOrderTypeChange(OrderType.Body(orderType.orderMode)) })
    }
}