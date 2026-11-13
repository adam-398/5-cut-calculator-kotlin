package dev.auroralaboratories.fivecutmethod

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the [formatDecimal] function.
 */
class FormatDecimalTest {

    @Test
    fun zeroDecimalsRoundsToWholeNumber() {
        assertEquals("4", formatDecimal(3.7f, 0))
    }

    @Test
    fun zeroDecimalsRoundsHalfUp() {
        assertEquals("3", formatDecimal(2.5f, 0))
    }

    @Test
    fun padsFractionWithLeadingZero() {
        assertEquals("1.05", formatDecimal(1.05f, 2))
    }

    @Test
    fun wholeNumberShowsTrailingZeros() {
        assertEquals("2.00", formatDecimal(2f, 2))
    }

    @Test
    fun zeroShowsTrailingZeros() {
        assertEquals("0.00", formatDecimal(0f, 2))
    }

    @Test
    fun roundsUpAtRequestedDecimals() {
        assertEquals("1.3", formatDecimal(1.26f, 1))
    }

    @Test
    fun roundsDownAtRequestedDecimals() {
        assertEquals("1.2", formatDecimal(1.24f, 1))
    }

    @Test
    fun negativeNumberKeepsSign() {
        assertEquals("-1.5", formatDecimal(-1.5f, 1))
    }

    @Test
    fun negativeWholeNumberKeepsSign() {
        assertEquals("-3", formatDecimal(-2.5f, 0))
    }

    @Test
    fun negativeRoundingToZeroHasNoSign() {
        assertEquals("0.00", formatDecimal(-0.001f, 2))
    }

    @Test
    fun negativeRoundingToZeroWithNoDecimalsHasNoSign() {
        assertEquals("0", formatDecimal(-0.4f, 0))
    }

    @Test
    fun largeNumberKeepsAllDigits() {
        assertEquals("1234.5", formatDecimal(1234.5f, 1))
    }
}