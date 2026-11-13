package dev.auroralaboratories.fivecutmethod.units

import kotlin.math.roundToInt

/**
 * Accepts "1.75", "1,75", "3/4", "1 3/4" and "1-3/4".
 * @return The number as a decimal or null if the input is invalid.
 */
fun String.toNumberOrNull(): Float? = trim().replace(',', '.').toFloatOrNull()

/** Accepts "1.75", "1,75", "3/4", "1 3/4" and "1-3/4". */
fun parseInches(input: String): Float? {
    val text = input.trim().replace(',', '.').replace('-', ' ')
    if (text.isEmpty()) return null
    text.toFloatOrNull()?.let { return it }

    val parts = text.split(Regex("\\s+"))
    return when (parts.size) {
        1 -> parseFraction(parts[0])
        2 -> {
            val whole = parts[0].toFloatOrNull() ?: return null
            val fraction = parseFraction(parts[1]) ?: return null
            whole + fraction
        }
        else -> null
    }
}


/**
 * Accepts "1/2" and "3/4".
 * @return The fraction as a decimal or null if the input is invalid.
 */
fun parseFraction(text: String): Float? {
    val parts = text.split('/')
    if (parts.size != 2) return null
    val numerator = parts[0].toFloatOrNull() ?: return null
    val denominator = parts[1].toFloatOrNull()?.takeIf { it != 0f } ?: return null
    return numerator / denominator
}

/**
 * Converts inches to a string of fractional inches.
 * @param inches The number of inches to convert.
 * @return The string representation of the fractional inches.
 */
fun toFractionalInches(inches: Float): String {
    val thirtySeconds = (inches * 32).roundToInt()
    val whole = thirtySeconds / 32
    var numerator = thirtySeconds % 32
    var denominator = 32
    while (numerator != 0 && numerator % 2 == 0) {
        numerator /= 2
        denominator /= 2
    }
    return when {
        numerator == 0 -> "$whole\""
        whole == 0 -> "$numerator/$denominator\""
        else -> "$whole $numerator/$denominator\""
    }
}

