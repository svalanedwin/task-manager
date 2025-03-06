package com.example.taskmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun TaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    selectedTheme: String = "Blue",
    content: @Composable () -> Unit
) {
    val colorScheme = when (selectedTheme) {
        "Blue" -> if (darkTheme) DarkColorScheme else LightColorScheme
        "Red" -> if (darkTheme) darkColorScheme(primary = Color.Red) else lightColorScheme(primary = Color.Red)
        "Green" -> if (darkTheme) darkColorScheme(primary = Color.Green) else lightColorScheme(primary = Color.Green)
        "Purple" -> if (darkTheme) darkColorScheme(primary = Color.Magenta) else lightColorScheme(primary = Color.Magenta)
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}