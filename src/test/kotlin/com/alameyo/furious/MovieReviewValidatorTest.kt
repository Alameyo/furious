package com.alameyo.furious

import com.alameyo.furiouscinema.inputvalidation.InputValidationException
import com.alameyo.furiouscinema.inputvalidation.MovieReviewValidator
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MovieReviewValidatorTest {
    private val movieReviewValidator = MovieReviewValidator()

    @Test
    fun `Valid review - should not throw exception`() {
        val validReviewBody = """
            {
                "rate": 5,
                "comment": "Great movie"
            }
            """.trim()
        assertDoesNotThrow { movieReviewValidator.validate(validReviewBody) }
    }

    @Test
    fun `Rate too high - should throw exception`() {
        val highRateInput = """
            {
                "rate": 6,
                "comment": "Great movie"
            }
            """.trim()
        assertThrows(InputValidationException::class.java) { movieReviewValidator.validate(highRateInput) }
    }

    @Test
    fun `Rate too low - should throw exception`() {
        val lowRateInput = """
            {
                "rate": 0,
                "comment": "Great movie"
            }
            """.trim()
        assertThrows(InputValidationException::class.java) { movieReviewValidator.validate(lowRateInput) }
    }

    @Test
    fun `Comment doesn't exist - should not throw exception`() {
        val lowRateInput = """
            {
                "rate": 5
            }
            """.trim()
        assertDoesNotThrow { movieReviewValidator.validate(lowRateInput) }
    }

    @Test
    fun `Json not formatted properly - should throw exception`() {
        val invalidJson = """
            {
                "rate": 2,,,,,,,:,,,,
                "comment": "Great movie"
            }
            """
        assertThrows(InputValidationException::class.java) { movieReviewValidator.validate(invalidJson) }
    }

    @Test
    @Suppress("MaxLineLength")
    fun `Too long comment - should throw exception`() {
        val invalidJson = """
            {
                "rate": 2,
                "comment": "Great movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movieGreat movie"
            }
            """
        assertThrows(InputValidationException::class.java) { movieReviewValidator.validate(invalidJson) }
    }
}
