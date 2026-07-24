package com.jrprofessor.mindolist.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.jrprofessor.mindolist.utils.StatusBarDarkMode
import com.jrprofessor.mindolist.utils.StatusBarLightMode

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

@Immutable
data class MindoListColors(
    val background: Color,
    val cardBg: Color,
    val cardChildBg: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val mint: Color,
    val error: Color = Color(0xFFF87171),
    val success: Color = Color(0xFF4ADE80),
    val inputBg: Color
)

val LocalMindoListColors = staticCompositionLocalOf {
    MindoListColors(
        background = MindoListBgDark,
        cardBg = MindoListCardBgDark,
        cardChildBg = MindoListCardChildBgDark,
        textPrimary = MindoListTextPrimaryDark,
        textSecondary = MindoListTextSecondaryDark,
        accent = MindoListAccentDark,
        mint = MindoListMintDark,
        inputBg = MindoListInputBg
    )
}

object MindoListTheme {
    val colors: MindoListColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMindoListColors.current
}

private val DarkColorScheme = darkColorScheme(
    primary = MindoListAccentFixed,
    secondary = MindoListMintFixed,
    background = MindoListBgDark,
    surface = MindoListCardBgDark,
    onBackground = MindoListTextPrimaryDark,
    onSurface = MindoListTextPrimaryDark,
    error = Color(0xFFF87171)
)

private val LightColorScheme = lightColorScheme(
    primary = MindoListAccentLight,
    secondary = MindoListMintLight,
    background = MindoListBgLight,
    surface = MindoListCardBgLight,
    onBackground = MindoListTextPrimaryLight,
    onSurface = MindoListTextPrimaryLight,
    error = Color(0xFFDC2626)
)

@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.DARK, // Default to DARK for now, will be dynamic later
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val MindoListColors = if (darkTheme) {
        MindoListColors(
            background = MindoListBgDark,
            cardBg = MindoListCardBgDark,
            cardChildBg = MindoListCardChildBgDark,
            textPrimary = MindoListTextPrimaryDark,
            textSecondary = MindoListTextSecondaryDark,
            accent = MindoListAccentDark,
            mint = MindoListMintDark,
            error = Color(0xFFF87171),
            success = Color(0xFF4ADE80),
            inputBg = MindoListInputBg
        )
    } else {
        MindoListColors(
            background = MindoListBgLight,
            cardBg = MindoListCardBgLight,
            cardChildBg = MindoListCardChildBgLight,
            textPrimary = MindoListTextPrimaryLight,
            textSecondary = MindoListTextSecondaryLight,
            accent = MindoListAccentLight,
            mint = MindoListMintLight,
            error = Color(0xFFDC2626),
            success = Color(0xFF16A34A),
            inputBg = Color(0xFFF1F5F9) // Lighter input bg for light theme
        )
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    if (darkTheme) {
        StatusBarDarkMode()
    } else {
        StatusBarLightMode()
    }

    // Status bar logic can be added here if needed, or in the platform side.
    // Since this is commonMain, we use CompositionLocalProvider for colors.

    CompositionLocalProvider(
        LocalMindoListColors provides MindoListColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
