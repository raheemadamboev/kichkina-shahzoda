package xyz.teamgravity.kichkinashahzoda.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PinkGrey30,
    onPrimary = White,
    primaryContainer = Pink30,
    onPrimaryContainer = White700,
    inversePrimary = Pink40,
    secondary = DeepPink80,
    onSecondary = DeepPink20,
    secondaryContainer = PinkGrey30,
    onSecondaryContainer = DeepPink90,
    tertiary = Violet80,
    onTertiary = Violet20,
    tertiaryContainer = Violet30,
    onTertiaryContainer = Violet90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGray,
    onBackground = White700,
    surface = DarkGray200,
    onSurface = White700,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = DarkGray200,
    onSurfaceVariant = White700,
    outline = PinkGrey80
)

private val LightColorScheme = lightColorScheme(
    primary = Pink40,
    onPrimary = White,
    primaryContainer = Pink90,
    onPrimaryContainer = Pink10,
    inversePrimary = Pink80,
    secondary = DeepPink40,
    onSecondary = White,
    secondaryContainer = DeepPink90,
    onSecondaryContainer = DeepPink10,
    tertiary = Violet40,
    onTertiary = White,
    tertiaryContainer = Violet90,
    onTertiaryContainer = Violet10,
    error = Red40,
    onError = White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = PinkGrey90,
    onSurface = PinkGrey30,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = PinkGrey90,
    onSurfaceVariant = PinkGrey30,
    outline = PinkGrey50
)

@Composable
fun KichkinaShahzodaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}