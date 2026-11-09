package dev.auroralaboratories.fivecutmethod

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberPivotRight(): MutableState<Boolean> {
    val prefs = LocalContext.current.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val state = remember { mutableStateOf(prefs.getBoolean("pivot_right", true)) }
    LaunchedEffect(state.value) {
        prefs.edit().putBoolean("pivot_right", state.value).apply()
    }
    return state
}
