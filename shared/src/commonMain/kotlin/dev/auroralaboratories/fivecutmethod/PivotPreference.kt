package dev.auroralaboratories.fivecutmethod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
expect fun rememberPivotRight(): MutableState<Boolean>