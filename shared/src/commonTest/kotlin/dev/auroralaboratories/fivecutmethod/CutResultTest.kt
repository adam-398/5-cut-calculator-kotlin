package dev.auroralaboratories.fivecutmethod

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CutResultTest {

    @Test
    fun returnsIncompleteForNullCalculateCut() {
        val result = cutResult(1f, 1f, null, 1f, PivotSide.Left)
        assertEquals(CutResult.Incomplete, result)
    }

    @Test
    fun returnsSquareForEqualNearAndFar() {
        val result = cutResult(30f, 30f, 300f, 700f, PivotSide.Left)
        assertEquals(CutResult.Square, result)
    }

    @Test
    fun returnsSquareForUnderToleranceGreaterNearSide() {
        val result = cutResult(30.08f, 30f, 300f, 700f, PivotSide.Left)
        assertEquals(CutResult.Square, result)
    }

    @Test
    fun returnsSquareForUnderToleranceGreaterFarSide() {
        val result = cutResult(30f, 30.08f, 300f, 700f, PivotSide.Right)
        assertEquals(CutResult.Square, result)
    }

    @Test
    fun returnsAdjustForOverToleranceGreaterNearSide() {
        val result = cutResult(30.09f, 30f, 300f, 700f, PivotSide.Left)
        assertIs<CutResult.Adjust>(result)
    }

    @Test
    fun returnsAdjustForOverToleranceGreaterFarSide() {
        val result = cutResult(30f, 30.09f, 300f, 700f, PivotSide.Right)
        assertIs<CutResult.Adjust>(result)
    }

    @Test
    fun returnsCorrectAdjustForRightSideOfBladePivotAndGreaterFarSide() {
        val result = cutResult(30f, 33f, 300f, 700f, PivotSide.Right)
        assertEquals(CutResult.Adjust(1.75f, false, 3f), result)
    }

    @Test
    fun returnsCorrectAdjustForRightSideOfBladePivotAndGreaterNearSide() {
        val result = cutResult(33f, 30f, 300f, 700f, PivotSide.Right)
        assertEquals(CutResult.Adjust(1.75f, true, 3f), result)
    }

    @Test
    fun returnsCorrectAdjustForLeftSideOfBladePivotAndGreaterFarSide() {
        val result = cutResult(30f, 33f, 300f, 700f, PivotSide.Left)
        assertEquals(CutResult.Adjust(1.75f, true, 3f), result)
    }

    @Test
    fun returnsCorrectAdjustForLeftSideOfBladePivotAndGreaterNearSide() {
        val result = cutResult(33f, 30f, 300f, 700f, PivotSide.Left)
        assertEquals(CutResult.Adjust(1.75f, false, 3f), result)
    }

}