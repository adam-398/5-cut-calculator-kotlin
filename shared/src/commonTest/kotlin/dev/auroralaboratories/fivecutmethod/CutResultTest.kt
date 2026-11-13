package dev.auroralaboratories.fivecutmethod

import kotlin.test.Test
import kotlin.test.assertEquals

class CutResultTest {

    @Test
    fun returnsIncompleteForNullCalculateCut() {
        val result = cutResult(1f, 1f, null, 1f, PivotSide.Left)
        assertEquals(CutResult.Incomplete, result)
    }
}