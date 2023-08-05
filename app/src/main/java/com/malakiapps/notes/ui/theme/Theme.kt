package com.malakiapps.notes.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = appSignatureGold,
    secondary = darkActionBarYellow,
    tertiary = lightDate,
    background = darkScreenBackground,
)

private val LightColorScheme = lightColorScheme(
    primary = appSignatureGold,
    secondary = lightActionBarYellow,
    tertiary = darkDate,
    background = lightScreenBackground,
)

private val lightModeColors = AppsColors()

private val darkModeColors = AppsColors(
    screenBackground = lightScreenBackground,
            cardBackground = lightCardBackground,
            unselectedTab = lightUnselectedTab,
            buttonIcon = lightButtonIcon,
            selectedSecondaryTabBackground = lightSelectedSecondaryTabBackground,
            selectedSecondaryTabText = lightSelectedSecondaryTabText,
            textColor = lightTitle,
            secondaryText = lightSecondaryText,
            date = lightDate,
            titleLabel = lightTitleLabel,
            dateLabel = lightDateLabel,
            startTypingLabel = lightStartTypingLabel,
            semiMenuTitles = lightSemiMenuTitles,
            rowTitle = lightRowTitle,
            rowContent = lightRowContent,
)

@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        if(!darkTheme) {
            LocalColors provides darkModeColors
        }else{
            LocalColors provides lightModeColors
        }
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}