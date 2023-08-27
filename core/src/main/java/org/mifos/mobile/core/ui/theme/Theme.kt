package org.mifos.mobile.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightThemeColors = lightColorScheme(
    primary = Blue600,
    onPrimary = Black2,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = BackgroundLight,
    onSurface = Black2,
)

private val DarkThemeColors = darkColorScheme(
    primary = Blue700,
    secondary = Black1,
    error = RedErrorLight,
    background = BackgroundDark,
    surface = Black1,
)

@Composable
fun MifosMobileTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colors = when {
        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            if (useDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkThemeColors
        else -> LightThemeColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}