package io.arjuna.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BlueGrey40,
    secondary = Teal40,
    onSecondary = Lime900,
    tertiary = Amber40,
    background = BlueGrey40,
    surfaceContainer = Teal40,
    errorContainer = DeepOrange900
)

private val LightColorScheme = lightColorScheme(
    primary = BlueGrey80,
    secondary = Teal80,
    onSecondary = Lime300,
    tertiary = Amber80,
    errorContainer = DeepOrange700
)

@Composable
fun ArjunaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}