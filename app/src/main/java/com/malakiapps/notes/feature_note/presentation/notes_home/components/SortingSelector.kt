package com.malakiapps.notes.feature_note.presentation.notes_home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import com.malakiapps.notes.feature_note.domain.util.OrderType
import com.malakiapps.notes.ui.theme.appColors
import com.malakiapps.notes.ui.theme.spacing

@Composable
fun SortingSelector(
    orderType: OrderType,
    onOrderChange: (OrderType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        OrderTypeSection(orderType = orderType, onOrderTypeChange = onOrderChange)
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        OrderModeSection(orderType = orderType, onOrderModeChange = onOrderChange)
    }
}