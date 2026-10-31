package com.example.a5cutmethod.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import dev.auroralaboratories.fivecut.ui.theme.Burnt
import dev.auroralaboratories.fivecut.ui.theme.Charcoal
import dev.auroralaboratories.fivecut.ui.theme.Graphite
import dev.auroralaboratories.fivecut.ui.theme.Linen
import dev.auroralaboratories.fivecut.ui.theme.OakGlow
import dev.auroralaboratories.fivecut.ui.theme.OakLight
import dev.auroralaboratories.fivecut.ui.theme.Pencil
import dev.auroralaboratories.fivecut.ui.theme.Plywood
import dev.auroralaboratories.fivecut.ui.theme.Sawdust
import dev.auroralaboratories.fivecut.ui.theme.Shavings
import dev.auroralaboratories.fivecut.ui.theme.Soot
import dev.auroralaboratories.fivecut.ui.theme.Walnut
import dev.auroralaboratories.fivecut.ui.theme.WalnutDeep
import dev.auroralaboratories.fivecut.ui.theme.WalnutMid
import dev.auroralaboratories.fivecut.ui.theme.WalnutShadow

private val LightColorScheme = lightColorScheme(
    primary = Walnut,
    onPrimary = Sawdust,
    primaryContainer = OakLight,
    onPrimaryContainer = WalnutDeep,
    background = Sawdust,
    onBackground = Charcoal,
    surface = Sawdust,
    onSurface = Charcoal,
    surfaceContainerHighest = Plywood,
    onSurfaceVariant = Graphite,
    outline = Pencil
)

private val DarkColorScheme = darkColorScheme(
    primary = OakGlow,
    onPrimary = WalnutShadow,
    primaryContainer = WalnutMid,
    onPrimaryContainer = OakLight,
    background = Soot,
    onBackground = Shavings,
    surface = Soot,
    onSurface = Shavings,
    surfaceContainerHighest = Burnt,
    onSurfaceVariant = Linen,
    outline = Pencil
)

@Composable
fun FiveCutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}