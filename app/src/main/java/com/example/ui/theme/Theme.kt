package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    primaryContainer = DeepPurple,
    onPrimaryContainer = Color(0xFFE9D5FF),
    secondary = NeonCyan,
    onSecondary = Color(0xFF032830),
    secondaryContainer = Color(0xFF0E3A4A),
    onSecondaryContainer = Color(0xFFA5F3FC),
    tertiary = HotPink,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkOutline,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // App enforces premium dark UI with purple and blue styling as specified in design requirements
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
