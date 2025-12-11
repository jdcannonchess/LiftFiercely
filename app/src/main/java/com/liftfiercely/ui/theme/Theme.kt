package com.liftfiercely.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.liftfiercely.data.model.ExerciseCategory

// =============================================================================
// LIGHT COLOR SCHEME (Warm, Welcoming, Sunrise Energy)
// =============================================================================
private val LightColorScheme = lightColorScheme(
    // Primary - Coral
    primary = CoralPrimary,
    onPrimary = TextOnColorLight,
    primaryContainer = CoralPrimaryLight,
    onPrimaryContainer = TextPrimaryDark,
    
    // Secondary - Aqua
    secondary = AquaAccent,
    onSecondary = TextOnColorLight,
    secondaryContainer = AquaAccentLight,
    onSecondaryContainer = TextPrimaryDark,
    
    // Tertiary - Warning Amber
    tertiary = WarningAmber,
    onTertiary = TextPrimaryDark,
    tertiaryContainer = Color(0xFFFFEBCC),
    onTertiaryContainer = TextPrimaryDark,
    
    // Background & Surface
    background = LightBackground,
    onBackground = TextPrimaryDark,
    surface = LightSurface,
    onSurface = TextSecondaryDark,
    surfaceVariant = LightSurfaceAlt,
    onSurfaceVariant = TextPrimaryDark,
    
    // Error
    error = DangerRed,
    onError = TextOnColorLight,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF8C1D18),
    
    // Outline & Inverse
    outline = TextMutedDark,
    outlineVariant = StrokeLight,
    inverseSurface = TextPrimaryDark,
    inverseOnSurface = LightBackground,
    inversePrimary = CoralPrimaryLight,
    
    // Scrim & Surface Tint
    scrim = Color.Black.copy(alpha = 0.32f),
    surfaceTint = CoralPrimary
)

// =============================================================================
// DARK COLOR SCHEME (Warm Dark, Fitness-Forward)
// =============================================================================
private val DarkColorScheme = darkColorScheme(
    // Primary - Coral (lighter for dark backgrounds)
    primary = CoralPrimaryLight,
    onPrimary = TextOnColorLight,
    primaryContainer = DarkCoralContainer,
    onPrimaryContainer = TextPrimaryLight,
    
    // Secondary - Aqua
    secondary = AquaAccentLight,
    onSecondary = DarkAquaOnSecondary,
    secondaryContainer = AquaAccentDark,
    onSecondaryContainer = TextPrimaryLight,
    
    // Tertiary - Warning Amber
    tertiary = WarningAmber,
    onTertiary = TextPrimaryLight,
    tertiaryContainer = Color(0xFF5C4300),
    onTertiaryContainer = Color(0xFFFFEBCC),
    
    // Background & Surface
    background = DarkBackground,
    onBackground = TextPrimaryLight,
    surface = DarkSurface,
    onSurface = TextSecondaryLight,
    surfaceVariant = DarkSurfaceAlt,
    onSurfaceVariant = TextPrimaryLight,
    
    // Error
    error = DangerRed,
    onError = TextOnColorLight,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    // Outline & Inverse
    outline = TextMutedLight,
    outlineVariant = Color(0xFF4A4845),
    inverseSurface = TextPrimaryLight,
    inverseOnSurface = DarkBackground,
    inversePrimary = CoralPrimaryDark,
    
    // Scrim & Surface Tint
    scrim = Color.Black.copy(alpha = 0.5f),
    surfaceTint = CoralPrimaryLight
)

// =============================================================================
// THEME COMPOSABLE
// =============================================================================
@Composable
fun LiftFiercelyTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FierceTypography,
        content = content
    )
}

// =============================================================================
// CATEGORY COLOR HELPERS
// =============================================================================

/**
 * Returns the primary color for an exercise category
 */
@Composable
fun getCategoryColor(category: ExerciseCategory): Color {
    return when (category) {
        ExerciseCategory.PUSH -> CoralPrimary    // Warm coral
        ExerciseCategory.PULL -> AquaAccent      // Soft aqua
        ExerciseCategory.OTHER -> ChartPlum      // Bold plum
    }
}

/**
 * Returns the container/background color for an exercise category
 */
@Composable
fun getCategoryContainerColor(category: ExerciseCategory): Color {
    return when (category) {
        ExerciseCategory.PUSH -> PushContainer
        ExerciseCategory.PULL -> PullContainer
        ExerciseCategory.OTHER -> OtherContainer
    }
}

/**
 * Returns the darker variant of a category color
 */
@Composable
fun getCategoryColorDark(category: ExerciseCategory): Color {
    return when (category) {
        ExerciseCategory.PUSH -> CoralPrimaryDark
        ExerciseCategory.PULL -> AquaAccentDark
        ExerciseCategory.OTHER -> OtherColorDark
    }
}

// =============================================================================
// SEMANTIC COLOR EXTENSIONS
// =============================================================================

/**
 * Custom semantic colors that can be accessed throughout the app
 */
object SemanticColors {
    val success = SuccessGreen
    val warning = WarningAmber
    val error = DangerRed
    val info = InfoBlue
}
