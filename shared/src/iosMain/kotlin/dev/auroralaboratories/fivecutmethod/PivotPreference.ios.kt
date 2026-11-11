package dev.auroralaboratories.fivecutmethod

import androidx.compose.runtime.*
import platform.Foundation.NSUserDefaults

@Composable
actual fun rememberPivotRight(): MutableState<Boolean> {
    val defaults = NSUserDefaults.standardUserDefaults
    val state = remember {
        mutableStateOf(
            if (defaults.objectForKey("pivot_right") == null) true
            else defaults.boolForKey("pivot_right")
        )
    }
    LaunchedEffect(state.value) { defaults.setBool(state.value, forKey = "pivot_right") }
    return state
}