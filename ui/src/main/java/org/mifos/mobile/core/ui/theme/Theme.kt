package org.mifos.mobile.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightThemeColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    error = RedErrorDark,
    background = BackgroundLight,
    onSurface = Black2,
    onSecondary = Color.Gray,
    outlineVariant = Color.Gray,
)

private val DarkThemeColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.White,
    secondary = Black1,
    error = RedErrorDark,
    background = BackgroundDark,
    surface = Black1,
    onSurface = Color.White,
    onSecondary = Color.White,
    outlineVariant = Color.White
)

@Composable
fun MifosMobileTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = when {
//        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
//            if (useDarkTheme) dynamicDarkColorScheme(context)
//            else dynamicLightColorScheme(context)
//        }
        useDarkTheme -> DarkThemeColors
        else -> LightThemeColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}