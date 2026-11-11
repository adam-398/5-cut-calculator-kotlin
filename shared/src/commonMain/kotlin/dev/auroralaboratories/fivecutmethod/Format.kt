package dev.auroralaboratories.fivecutmethod

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Formats a decimal value to a string with the specified number of decimal places.
 * @param value The decimal value to format.
 * @param decimals The number of decimal places to include in the result.
 * @return The formatted decimal value as a string.
 */
fun formatDecimal(value: Float, decimals: Int): String {
    val factor = 10.0.pow(decimals).toLong()
    val scaled = (abs(value.toDouble()) * factor).roundToLong()
    val sign = if (value < 0 && scaled != 0L) "-" else ""
    if (decimals == 0) return "$sign$scaled"
    val frac = (scaled % factor).toString().padStart(decimals, '0')
    return "$sign${scaled / factor}.$frac"
}