package com.example.flashfun.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1A6B4A),
    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFB7F0D4),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF002115),
    secondary = androidx.compose.ui.graphics.Color(0xFF4F6354),
    onSecondary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFD2E8D6),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF0D1F13),
    tertiary = androidx.compose.ui.graphics.Color(0xFF3B6680),
    onTertiary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFFBEE3FF),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF001E30),
    background = androidx.compose.ui.graphics.Color(0xFFF6FBF4),
    onBackground = androidx.compose.ui.graphics.Color(0xFF181D19),
    surface = androidx.compose.ui.graphics.Color(0xFFF6FBF4),
    onSurface = androidx.compose.ui.graphics.Color(0xFF181D19),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFDBE5DC),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF404942),
    surfaceContainerLow = androidx.compose.ui.graphics.Color(0xFFEEF5ED),
    surfaceContainerHigh = androidx.compose.ui.graphics.Color(0xFFE3EAE2),
    surfaceContainerHighest = androidx.compose.ui.graphics.Color(0xFFDDE4DC),
    outline = androidx.compose.ui.graphics.Color(0xFF70796F),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFFBFC8BF),
    error = androidx.compose.ui.graphics.Color(0xFFBA1A1A),
    errorContainer = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410002),
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF9BD4B8),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF003826),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF005237),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFB7F0D4),
    secondary = androidx.compose.ui.graphics.Color(0xFFB7CCBA),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF223527),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF384B3D),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFD2E8D6),
    tertiary = androidx.compose.ui.graphics.Color(0xFFA2C9E5),
    onTertiary = androidx.compose.ui.graphics.Color(0xFF003450),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFF1F4D66),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFFBEE3FF),
    background = androidx.compose.ui.graphics.Color(0xFF101510),
    onBackground = androidx.compose.ui.graphics.Color(0xFFE1E7E0),
    surface = androidx.compose.ui.graphics.Color(0xFF101510),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE1E7E0),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF404942),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFBFC8BF),
    surfaceContainerLow = androidx.compose.ui.graphics.Color(0xFF1C211C),
    surfaceContainerHigh = androidx.compose.ui.graphics.Color(0xFF272D27),
    surfaceContainerHighest = androidx.compose.ui.graphics.Color(0xFF323832),
    outline = androidx.compose.ui.graphics.Color(0xFF899389),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFF404942),
    error = androidx.compose.ui.graphics.Color(0xFFFFB4AB),
    errorContainer = androidx.compose.ui.graphics.Color(0xFF93000A),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
)

@Composable
fun FlashFunTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}