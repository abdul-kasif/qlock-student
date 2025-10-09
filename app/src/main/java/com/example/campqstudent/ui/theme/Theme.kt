// ui/theme/Theme.kt
package com.example.campqstudent.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = PrimaryForeground,
    primaryContainer = SurfaceVariant,
    onPrimaryContainer = Foreground,

    secondary = SurfaceVariant,
    onSecondary = OnSurfaceVariant,
    secondaryContainer = Surface,
    onSecondaryContainer = Foreground,

    tertiary = SurfaceVariant,
    onTertiary = OnSurfaceVariant,
    tertiaryContainer = Surface,
    onTertiaryContainer = Foreground,

    background = Background,
    onBackground = Foreground,

    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    error = Destructive,
    onError = DestructiveForeground,
    errorContainer = Destructive.copy(alpha = 0.1f),
    onErrorContainer = Destructive,

    outline = Border,
    outlineVariant = Border,
    scrim = Color(0xFF000000)
)

@Composable
fun CampQAppTheme(
    content: @Composable () -> Unit
) {
    // ✅ Force light theme — no dark mode
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}