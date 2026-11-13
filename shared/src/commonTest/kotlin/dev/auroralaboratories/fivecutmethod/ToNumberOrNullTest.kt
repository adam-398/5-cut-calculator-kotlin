package dev.auroralaboratories.fivecutmethod

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ToNumberOrNullTest {

    @Test
    fun returnsNullForEmptyInput() {
        val result = "".toNumberOrNull()
        assertNull(result)
    }

    @Test
    fun returnsNullForInvalidInput() {
        val result = "abc".toNumberOrNull()
        assertNull(result)
    }

    @Test
    fun returnsValidOutputForStandardInput() {
        val result = "123.4".toNumberOrNull()
        assertEquals(123.4f, result)
    }

    @Test
    fun returnsNumberForInteger() {
        val result = "123".toNumberOrNull()
        assertEquals(123f, result)
    }

    @Test
    fun returnsDecimalToReplaceComma() {
        val result = "123,3".toNumberOrNull()
        assertEquals(123.3f, result)
    }

    @Test
    fun returnsSpaceTrimmedInteger() {
        val result = " 123 ".toNumberOrNull()
        assertEquals(123f, result)
    }

    @Test
    fun returnsSpaceTrimmedIntegerWithComma() {
        val result = " 123,3 ".toNumberOrNull()
        assertEquals(123.3f, result)
    }

    @Test
    fun returnsNullForMixedDecimalSeparators() {
        val result = "123.4,5".toNumberOrNull()
        assertNull(result)
    }


}