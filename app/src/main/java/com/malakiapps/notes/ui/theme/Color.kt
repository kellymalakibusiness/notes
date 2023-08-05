package com.malakiapps.notes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val appSignatureGold = Color(0xFFf9b40f)

val lightActionBarYellow = Color(0xFFffbb00)
val darkActionBarYellow = Color(0xFFfaa401)

//Background
val lightScreenBackground = Color(0xFFf7f7f7)
val darkScreenBackground = Color(0xFF000000)

val lightCardBackground = Color.White
val darkCardBackground = Color(0xFF242424)


//Navigation tabs
val lightUnselectedTab = Color(0xFF949494)
val darkUnselectedTab = Color(0xFF666666)

val lightButtonIcon = Color(0xFF313131)
val darkButtonIcon = Color.LightGray

val lightSelectedSecondaryTabBackground = Color(0xFFeeeeee)
val darkSelectedSecondaryTabBackground = Color(0xFF3a3a3a)

val lightSelectedSecondaryTabText = Color(0xFF010101)
val darkSelectedSecondaryTabText = Color(0xFFececec)

//The cards
val lightTitle = Color(0xFF191919)
val darkTitle = Color(0xFFe9e9e9)

val lightSecondaryText = Color(0xFF666666)
val darkSecondaryText = Color(0xFFa7a7a7)

val lightDate = Color(0xFF808080)
val darkDate = Color(0xFF8d8d8d)

//Create Note Page
val lightTitleLabel = Color(0xFFcccccc)//Color(0xFF313131)
val darkTitleLabel = Color(0xFF333333)//Color.LightGray

val lightDateLabel = Color(0xFF999999)
val darkDateLabel = Color(0xFF666666)

val lightStartTypingLabel = lightTitleLabel
val darkStartTypingLabel = darkTitleLabel

//Settings page
val lightSemiMenuTitles = Color(0xFF8c93af)
val darkSemiMenuTitles = Color(0xFF808080)

val lightRowTitle = Color(0xFF000000)
val darkRowTitle = Color(0xFFe6e6e6)

val lightRowContent = Color(0xFF999999)
val darkRowContent = Color(0xFF666666)

data class AppsColors(
    val screenBackground: Color = darkScreenBackground,
    val cardBackground: Color = darkCardBackground,
    val unselectedTab: Color = darkUnselectedTab,
    val buttonIcon: Color = darkButtonIcon,
    val selectedSecondaryTabBackground: Color = darkSelectedSecondaryTabBackground,
    val selectedSecondaryTabText: Color = darkSelectedSecondaryTabText,
    val textColor: Color = darkTitle,
    val secondaryText: Color = darkSecondaryText,
    val date: Color = darkDate,
    val titleLabel: Color = darkTitleLabel,
    val dateLabel: Color = darkDateLabel,
    val startTypingLabel: Color = darkStartTypingLabel,
    val semiMenuTitles: Color = darkSemiMenuTitles,
    val rowTitle: Color = darkRowTitle,
    val rowContent: Color = darkRowContent
)

val LocalColors = compositionLocalOf { AppsColors() }

val MaterialTheme.appColors: AppsColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current