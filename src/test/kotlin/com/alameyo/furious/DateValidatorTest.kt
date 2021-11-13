package com.alameyo.furious

import com.alameyo.furiouscinema.inputvalidation.DateValidator
import com.alameyo.furiouscinema.inputvalidation.InputValidationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DateValidatorTest {
    private val dateValidator = DateValidator()

    @Test
    fun `If input have invalid date - should throw InputValidationException`() {
        val invalidDateJson = """20-11-11""".trim()
        Assertions.assertThrows(InputValidationException::class.java) { dateValidator.validate(invalidDateJson) }
    }

}