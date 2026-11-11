package dev.auroralaboratories.fivecutmethod.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

private val Base = Typography()
private val Display = FontFamily.Serif

val Typography = Typography(
    displayMedium = Base.displayMedium.copy(fontFamily = Display),
    displaySmall = Base.displaySmall.copy(fontFamily = Display),
    headlineMedium = Base.headlineMedium.copy(fontFamily = Display),
    headlineSmall = Base.headlineSmall.copy(fontFamily = Display),
    titleLarge = Base.titleLarge.copy(fontFamily = Display)
)