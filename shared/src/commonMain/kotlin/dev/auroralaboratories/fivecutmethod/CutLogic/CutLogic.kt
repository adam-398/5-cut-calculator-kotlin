package dev.auroralaboratories.fivecutmethod

import kotlin.math.abs

internal val fiveCutSteps = listOf(
    "Take a square-ish panel and number its sides 1 to 4.",
    "Cut side 1. Turn the panel a quarter turn so the edge you just cut is against the fence, and cut side 2. Repeat for sides 3 and 4.",
    "Cut side 1 again, taking off a strip approximately 20 mm wide.",
    "Measure the strip's width at the end that was nearest you and at the far end, then enter everything below."
)

enum class PivotSide { Left, Right }

private const val SQUARE_TOLERANCE_MM = 0.05f

fun cutResult(
    near: Float?, far: Float?, strip: Float?, fence: Float?, pivot: PivotSide
): CutResult {
    val adjustment = calculateCut(near, far, strip, fence) ?: return CutResult.Incomplete
    if (abs(adjustment) < SQUARE_TOLERANCE_MM) return CutResult.Square
    return CutResult.Adjust(
        amount = abs(adjustment),
        forwards = (adjustment > 0) == (pivot == PivotSide.Left),
        difference = abs(far!! - near!!)
    )
}

fun calculateCut(
    nearSide: Float?,
    farSide: Float?,
    stripLength: Float?,
    fenceLength: Float?
): Float? {
    if (nearSide == null || farSide == null || stripLength == null || fenceLength == null) return null
    if (stripLength <= 0f) return null
    return ((farSide - nearSide) / 4f) * (fenceLength / stripLength)
}