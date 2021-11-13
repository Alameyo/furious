package com.alameyo.furious

import com.alameyo.furiouscinema.inputvalidation.DateValidator
import com.alameyo.furiouscinema.inputvalidation.InputValidationException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class DateValidatorTest {
    private val dateValidator = DateValidator()

    @Test
    fun `If input have invalid date - should throw InputValidationException`() {
        val invalidDateJson = """20-11-11""".trim()
        assertThrows(InputValidationException::class.java) { dateValidator.validate(invalidDateJson) }
    }

    @Test
    fun `If input have valid date - should not throw exception`() {
        val invalidDateJson = """2021-11-11""".trim()
        assertDoesNotThrow { dateValidator.validate(invalidDateJson) }
    }
}
