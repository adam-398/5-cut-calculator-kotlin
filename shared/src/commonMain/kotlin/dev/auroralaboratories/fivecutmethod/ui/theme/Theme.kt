package dev.auroralaboratories.fivecutmethod.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Navy,
    onPrimary = Card,
    secondary = NavyMuted,
    onSecondary = Card,
    tertiary = Ember,
    onTertiary = EmberInk,
    tertiaryContainer = Sage,
    onTertiaryContainer = SageInk,
    primaryContainer = EmberMist,
    onPrimaryContainer = EmberInk,
    background = Mist,
    onBackground = Navy,
    surface = Card,
    onSurface = Navy,
    onSurfaceVariant = NavyMuted,
    surfaceContainerHigh = MistDeep,
    surfaceContainerHighest = MistDeeper,
    outline = Pebble,
    outlineVariant = Line,
            secondaryContainer = Navy,
    onSecondaryContainer = Card,
)

private val DarkColors = darkColorScheme(
    primary = Chalk,
    onPrimary = NavyInk,
    secondary = ChalkMuted,
    onSecondary = Night,
    tertiary = EmberGlow,
    onTertiary = EmberPale,
    tertiaryContainer = SageDusk,
    onTertiaryContainer = SagePale,
    primaryContainer = EmberDusk,
    onPrimaryContainer = EmberPale,
    background = Night,
    onBackground = Chalk,
    surface = NightRaised,
    onSurface = Chalk,
    onSurfaceVariant = ChalkMuted,
    surfaceContainerHigh = NightHigh,
    surfaceContainerHighest = NightHigher,
    outline = NightPebble,
    outlineVariant = NightLine,
    secondaryContainer = Chalk,
    onSecondaryContainer = NavyInk,
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun FiveCutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}