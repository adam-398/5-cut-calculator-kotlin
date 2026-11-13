package dev.auroralaboratories.fivecutmethod


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Tests for the [calculateCut] function.
 */
class CalculateCutTest {

    @Test
    fun returnsNullForNullNearSide() {
        val result = calculateCut(null, 1f, 1f, 1f)
        assertNull(result)
    }

    @Test
    fun returnsNullForNullFarSide() {
        val result = calculateCut(1f, null, 1f, 1f)
        assertNull(result)
    }

    @Test
    fun returnsNullForNullStripLength() {
        val result = calculateCut(1f, 1f, null, 1f)
        assertNull(result)
    }

    @Test
    fun returnsNullForNullFenceLength() {
        val result = calculateCut(1f, 1f, 1f, null)
        assertNull(result)
    }

    @Test
    fun returnsNullForStripLengthEqualToZero() {
        val result = calculateCut(1f, 1f, 0f, 1f)
        assertNull(result)
    }

    @Test
    fun returnsNullForStripLengthLessThanZero() {
        val result = calculateCut(1f, 1f, -1f, 1f)
        assertNull(result)
    }

    @Test
    fun returnsValidValueForStripLengthGreaterThanZero() {
        val result = calculateCut(1f, 1f, 1f, 1f)
        assertNotNull(result)
    }

    @Test
    fun returnsCorrectValueForKnownValidInputs() {
        val result = calculateCut(30f, 33f, 300f, 700f)
        val value = assertNotNull(result)
        assertEquals(1.75f, result, 0.01f)
    }


}