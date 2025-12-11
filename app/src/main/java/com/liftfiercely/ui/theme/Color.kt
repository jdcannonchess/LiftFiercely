package com.liftfiercely.ui.theme

import androidx.compose.ui.graphics.Color

// =============================================================================
// BRAND – CORAL
// =============================================================================
val CoralPrimary = Color(0xFFFF6B3D)
val CoralPrimaryDark = Color(0xFFD5522C)
val CoralPrimaryLight = Color(0xFFFFA37C)

// =============================================================================
// ACCENT – SOFT AQUA
// =============================================================================
val AquaAccent = Color(0xFF41C7D9)
val AquaAccentDark = Color(0xFF2B99A7)
val AquaAccentLight = Color(0xFF86E4EF)

// =============================================================================
// LIGHT MODE – BACKGROUNDS & SURFACES
// =============================================================================
val LightBackground = Color(0xFFF5F1EB)         // Main background (warm neutral)
val LightSurface = Color(0xFFE8E0D6)            // Default card surface
val LightSurfaceAlt = Color(0xFFFCE5D8)         // Subtle peach-tinted surface
val LightSurfaceHighlight = Color(0xFFFFF4EA)   // Highlight/selected surface

// =============================================================================
// LIGHT MODE – TEXT
// =============================================================================
val TextPrimaryDark = Color(0xFF222427)         // Main text on light bg
val TextSecondaryDark = Color(0xFF4A4D52)       // Body / secondary
val TextMutedDark = Color(0xFF7B7F86)           // Helper text
val TextOnColorLight = Color(0xFFFFFFFF)        // Text on coral/aqua

// =============================================================================
// DARK MODE – BACKGROUNDS & SURFACES
// =============================================================================
val DarkBackground = Color(0xFF141414)          // Main dark bg (slightly warm)
val DarkSurface = Color(0xFF1E1E1B)             // Cards
val DarkSurfaceAlt = Color(0xFF252521)          // Elevated cards
val DarkSurfaceHighlight = Color(0xFF2F2A24)    // Selected / focus surface

// =============================================================================
// DARK MODE – TEXT
// =============================================================================
val TextPrimaryLight = Color(0xFFF5F1EB)        // Main text on dark bg
val TextSecondaryLight = Color(0xFFD6D1C9)      // Secondary text
val TextMutedLight = Color(0xFF9A948A)          // Muted text

// =============================================================================
// SEMANTIC
// =============================================================================
val SuccessGreen = Color(0xFF35C87A)
val WarningAmber = Color(0xFFF6AE2D)
val DangerRed = Color(0xFFE63946)
val InfoBlue = Color(0xFF3A86FF)

// =============================================================================
// CHART COLORS
// =============================================================================
val ChartCoral = Color(0xFFFF6B3D)
val ChartAqua = Color(0xFF41C7D9)
val ChartGold = Color(0xFFF6AE2D)
val ChartPlum = Color(0xFF9B5DE5)
val ChartSteel = Color(0xFF7B7F86)

// =============================================================================
// DARK MODE CONTAINERS
// =============================================================================
val DarkCoralContainer = Color(0xFF5A2A1F)
val DarkAquaOnSecondary = Color(0xFF06252A)

// =============================================================================
// CATEGORY COLORS (Exercise Types)
// =============================================================================

// Push - Coral (Primary brand color)
val PushColor = CoralPrimary
val PushColorLight = CoralPrimaryLight
val PushColorDark = CoralPrimaryDark
val PushContainer = Color(0xFFFFE5DC)

// Pull - Aqua (Accent color)
val PullColor = AquaAccent
val PullColorLight = AquaAccentLight
val PullColorDark = AquaAccentDark
val PullContainer = Color(0xFFDDF7FA)

// Other - Plum (Chart accent)
val OtherColor = ChartPlum
val OtherColorLight = Color(0xFFB98AEF)
val OtherColorDark = Color(0xFF7A3FBF)
val OtherContainer = Color(0xFFF0E5FF)

// =============================================================================
// LEGACY ALIASES (Backward compatibility with existing UI code)
// =============================================================================

// Primary brand aliases
val FierceTeal = AquaAccent
val FierceTealDark = AquaAccentDark
val FierceTealLight = AquaAccentLight
val FierceOrange = CoralPrimary
val FierceOrangeDark = CoralPrimaryDark
val FierceOrangeLight = CoralPrimaryLight
val FlameOrange = CoralPrimary
val FlameOrangeDeep = CoralPrimaryDark
val FiercePeach = CoralPrimary
val FiercePeachDark = CoralPrimaryDark
val FiercePeachLight = CoralPrimaryLight
val FierceRed = DangerRed

// Background aliases
val MidnightGraphite = DarkBackground
val AppGlowWash = LightSurfaceHighlight
val IronGrey = DarkSurface
val SmokeGrey = DarkSurfaceAlt
val SurfaceHighlight = LightSurfaceHighlight
val WarmSand = LightBackground
val SoftStone = LightSurface
val PaleBlush = LightSurfaceAlt
val GlowLayer = LightSurfaceHighlight
val WarmWhite = LightSurfaceHighlight

// Text aliases
val TextPrimary = TextPrimaryDark
val TextSecondary = TextSecondaryDark
val TextTertiary = TextMutedDark
val TextStrong = TextPrimaryDark
val TextRegular = TextSecondaryDark
val TextMuted = TextMutedDark

// Stroke colors
val StrokeLight = Color(0xFFD4D0CC)
val StrokeHeavy = Color(0xFFB8B4B0)

// Status aliases
val ErrorRed = DangerRed
